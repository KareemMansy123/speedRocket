package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.BasketItem;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

/**
 * Created by Ibrahim on 9/2/2018.
 */

public class ConfirmCompany extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Menu menu;


    NavigationView navigationView;

    public static  boolean log  ;
    private ArrayList<String> mResultsTax = new ArrayList<>();
    private ArrayList<String> mResultsCommecial = new ArrayList<>();
    private ArrayList<String> mResultsIdentidication = new ArrayList<>();
    String firstname = "", lastname = "",
            email = "", language = "", interest = "",
             country = "";
    TextView nav_firstname, nav_lastname, nav_email  ;
    CircleImageView nav_profileimage, profileImage;

    String firstName , lastName  ,userProfileImage  ;
    boolean login ;
    int userID;
    int companyId = 0 ;
    RecyclerView recyclerView ;
    BasketItem item ;
    ImageView commerceImage , identificationImage , taxImage ;
    final int TAX_REQUEST = 2224 ;
    final int COMMERCIAL_REQUEST = 555 ;
    final int IDENTIFICATION_REQUEST= 4455 ;
    LinearLayout verifiedlayout ;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_company_navigation_view);
        setTitle(getString(R.string.confirm_company));
        Fresco.initialize(getBaseContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menu();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        firstName = prefs.getString("firstName", "");//"No name defined" is the default value.
        lastName = prefs.getString("lastName", "");//"No name defined" is the default value.
        email = prefs.getString("email", "");//"No name defined" is the default value.
        userID = prefs.getInt("id", 0);
        interest = prefs.getString("interest", "");
        userProfileImage = prefs.getString("profileImage", "");
        login = prefs.getBoolean("login", false);
        Log.w("login##", "" + login);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Intent in = getIntent();
        Bundle b =in.getExtras() ;

        if (b!= null ){
            if (b.containsKey("companyId"))
                companyId= (int)b.get("companyId");
        }

        identificationImage = findViewById(R.id.identification_mark);
        commerceImage = findViewById(R.id.commerce_mark);
        taxImage = findViewById(R.id.tax_mark);

        verifiedlayout = findViewById(R.id.completedVerificationLayout);

    }

    public void shoppingCart(View view) {


        Log.e("shopping Caret", "ssssssssss");

        Intent intent = new Intent(ConfirmCompany.this, shoppingCaretActivity.class);
        startActivity(intent);

    }



    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation_menu, menu);

        menu = navigationView.getMenu();

        if (userID != 0) {
            menu.findItem(R.id.nav_myProducts).setVisible(true);
            menu.findItem(R.id.nav_cPanal).setVisible(true);
            menu.findItem(R.id.yourOrders).setVisible(true);

        } // if


        nav_firstname = (TextView) findViewById(R.id.menu_firstname);
        nav_lastname = (TextView) findViewById(R.id.menu_lastname);
        nav_email = (TextView) findViewById(R.id.menu_email);

        nav_profileimage = (CircleImageView) findViewById(R.id.nav_profileimage);

        final ImageView nav_msgBadge = (ImageView) findViewById(R.id.msg_badge);

        if (getSharedPreferences("MyPrefsFile",0).getBoolean("msg",false)) nav_msgBadge.setVisibility(View.VISIBLE);
        else nav_msgBadge.setVisibility(View.INVISIBLE);


        nav_firstname.setText(firstName);
        nav_lastname.setText(lastName);



        nav_email.setText(email);


        Picasso.with(getBaseContext()).load("https://speed-rocket.com/upload/users/"
                + userProfileImage).
                fit().centerCrop().into(nav_profileimage);
        nav_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyCompanyProfile.class);
                intent.putExtra("userID", userID);
                startActivity(intent);


            }
        });
        MenuItem searchItem = menu.findItem(R.id.action_settings);

        SearchManager searchManager = (SearchManager) ConfirmCompany.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ConfirmCompany.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);

    }  // control search icon on toolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);

        final Menu m = nv.getMenu();


        int id = item.getItemId();

        //  for(int i =1 ; i<= numOfCategoryItems ; i++) {


        if (id == R.id.nav_category) {


            Intent intent = new Intent(getBaseContext(), ProductMenuList.class);
            startActivity(intent);


        } else if (id == R.id.nav_myProducts) {
            Intent intent = new Intent(getBaseContext(), MyWinnerProducts.class);
            startActivity(intent);
        } else if (id == R.id.nav_offers) {
            Intent intent = new Intent(getBaseContext(), OfferMenuList.class);
            startActivity(intent);
        } else if (id == R.id.nav_country) {

            return true;

        } else if (id == R.id.nav_language) {
            Intent intent = new Intent(getBaseContext(),ChangeLanguage.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_logout) {


            if (m.findItem(R.id.nav_logout).getTitle().toString().equals("Logout")) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(getString(R.string.LogOut));
                dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m.findItem(R.id.nav_logout).setTitle("Login");

                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.remove("firstName");
                        editor.remove("lastName");
                        editor.remove("email");
                        editor.remove("id");
                        editor.remove("coin");
                        editor.remove("profileImage");
                        editor.remove("interest");
                        editor.putBoolean("login", false);
                        editor.remove("ItemsNumber");
                        editor.apply();

                        menu.findItem(R.id.nav_myProducts).setVisible(false);
                        menu.findItem(R.id.nav_cPanal).setVisible(false);

                        nav_profileimage.setVisibility(View.INVISIBLE);
                        nav_firstname.setText("");
                        nav_lastname.setText("");
                        nav_email.setText("");
                        log = false;
                        // make the log in fab button in visable

                        // AppUtills.notificationBadge.setNotifecationBadge(mBadge,0);
                        dialogInterface.dismiss();
                    }
                });
                dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();

            } else {
                Intent intent = new Intent(getBaseContext(), LoginScreen.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(getBaseContext(), NavigationMenu.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_buycoins) {
            Intent intent = new Intent(getBaseContext(), BuyCoins.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_cPanal) {
            if (userID == 0) {
                Intent intent = new Intent(getBaseContext(), LoginScreen.class);
                startActivity(intent);
            } // check if login 0 ---> user not login
            else {
                Intent intent = new Intent(getBaseContext(), MyCompanyProfile.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
            return true;

        }else if (id == R.id.yourOrders){
            Intent intent = new Intent(getBaseContext(),ConfirmCompany.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.nav_about_us){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://speed-rocket.com/selling-agreement"));
            startActivity(browserIntent);
        }else if (id == R.id.nav_advertise_with_us){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://speed-rocket.com/usage-policy"));
            startActivity(browserIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        //  }
        return true;
    }

    public void menu() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        menu = navigationView.getMenu();


        navigationView.setNavigationItemSelectedListener(this);

    }
         public void goToHome(View v){
             Intent intent = new Intent(ConfirmCompany.this , NavigationMenu.class);
             startActivity(intent);
             finish();
           }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){
            if (requestCode == TAX_REQUEST)
            {
                mResultsTax = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResultsTax != null;

              final ProgressDialog  progress = new ProgressDialog(this);
                progress.setTitle("Please Wait");
                progress.setMessage("Loading..");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

              Handler  handler = new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        progress.dismiss();
                        super.handleMessage(msg);
                    }

                };
                progress.show();

                new Thread() {
                    public void run() {
                        File file2 = null ;

                        MultipartBody.Part fileToUpload2 =null ;
                        RequestBody filename2 = null ;
                        if (mResultsTax.size() >=0) {
                            File file1 = new File(mResultsTax.get(0));
                            if (mResultsTax.size()==2){
                             file2 = new File(mResultsTax.get(1));
                            }

                            RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file1);

                            MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);
                            RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), file1.getName());

                            if (file2 != null){
                            RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), file2);

                             fileToUpload2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestBody2);
                             filename2 = RequestBody.create(MediaType.parse("text/plain"), file2.getName());
                            }
                            UserApi getResponse = AppConfig.getRetrofit(getApplicationContext()).create(UserApi.class);
                            Call call = getResponse.saveImages(fileToUpload1, filename1, fileToUpload2, filename2, companyId, userID);

                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    Log.e("sucsess", " sucsess");
                                    taxImage.setVisibility(View.VISIBLE);
                                    if (identificationImage.getVisibility() == View.VISIBLE
                                            && taxImage.getVisibility() == View.VISIBLE
                                            && commerceImage.getVisibility() == View.VISIBLE
                                            ){
                                        verifiedlayout.setVisibility(View.VISIBLE);
                                    }
                                    progress.dismiss();
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Log.e("error ", t.toString());
                                    progress.dismiss();
                                }
                            });

                        }
                    }
                }.start();

            }
            else if (requestCode == COMMERCIAL_REQUEST)
            {
                mResultsCommecial = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResultsCommecial != null;

                if (mResultsCommecial.size() >= 0) {
                    final ProgressDialog progress = new ProgressDialog(this);
                    progress.setTitle("Please Wait");
                    progress.setMessage("Loading..");
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    Handler handler = new Handler() {

                        @Override
                        public void handleMessage(Message msg) {
                            progress.dismiss();
                            super.handleMessage(msg);
                        }

                    };
                    progress.show();

                    new Thread() {
                        public void run() {
                            File file2 = null ;
                            MultipartBody.Part fileToUpload2 =null ;
                            RequestBody filename2 = null ;
                            File file1 = new File(mResultsCommecial.get(0));
                            if (mResultsCommecial.size()==2){
                             file2 = new File(mResultsCommecial.get(1));
                            }

                            RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file1);

                            MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);
                            RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), file1.getName());

                            if (file2 != null){
                            RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), file2);
                             fileToUpload2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestBody2);
                             filename2 = RequestBody.create(MediaType.parse("text/plain"), file2.getName());
                            }
                            UserApi getResponse = AppConfig.getRetrofit(getApplicationContext()).create(UserApi.class);
                            Call call = getResponse.saveImages(fileToUpload1, filename1, fileToUpload2, filename2, companyId, userID);

                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    Log.e("sucsess", " sucsess");
                                    commerceImage.setVisibility(View.VISIBLE);
                                    if (identificationImage.getVisibility() == View.VISIBLE
                                            && taxImage.getVisibility() == View.VISIBLE
                                            && commerceImage.getVisibility() == View.VISIBLE
                                            ){
                                        verifiedlayout.setVisibility(View.VISIBLE);
                                    }
                                    progress.dismiss();
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Log.e("error ", t.toString());
                                    progress.dismiss();
                                }
                            });

                        }

                    }.start();
                }
            }
            else if (requestCode == IDENTIFICATION_REQUEST) {
                mResultsIdentidication = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResultsIdentidication != null;


                if (mResultsIdentidication.size() >= 0) {
                    final ProgressDialog progress = new ProgressDialog(this);
                    progress.setTitle("Please Wait");
                    progress.setMessage("Loading..");
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    Handler handler = new Handler() {

                        @Override
                        public void handleMessage(Message msg) {
                            progress.dismiss();
                            super.handleMessage(msg);
                        }

                    };
                    progress.show();

                    new Thread() {
                        public void run() {
                            File file2 = null ;
                            MultipartBody.Part fileToUpload2 =null ;
                            RequestBody filename2 = null ;
                            File file1 = new File(mResultsIdentidication.get(0));
                            if (mResultsIdentidication.size() == 2){
                             file2 = new File(mResultsIdentidication.get(1));
                            }

                            RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file1);

                            MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);
                            RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), file1.getName());
                             if (file2 != null) {
                                 RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), file2);

                                 fileToUpload2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestBody2);
                                 filename2 = RequestBody.create(MediaType.parse("text/plain"), file2.getName());
                             }
                            UserApi getResponse = AppConfig.getRetrofit(getApplicationContext()).create(UserApi.class);
                            Call call = getResponse.saveImages(fileToUpload1, filename1, fileToUpload2, filename2, companyId, userID);

                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    Log.e("sucsess", " sucsess");
                                    identificationImage.setVisibility(View.VISIBLE);
                                    if (identificationImage.getVisibility() == View.VISIBLE
                                            && taxImage.getVisibility() == View.VISIBLE
                                            && commerceImage.getVisibility() == View.VISIBLE
                                            ){
                                        verifiedlayout.setVisibility(View.VISIBLE);
                                    }

                                    progress.dismiss();
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Log.e("error ", t.toString());
                                    progress.dismiss();
                                }
                            });

                        }
                    }.start();

                }
            }
        }

    }
    public void uploadTax(View view)
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // start multiple photos selector
        Intent intent = new Intent(getBaseContext(), ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResultsTax);
// start the selector
        startActivityForResult(intent,TAX_REQUEST );
    } // function of uploadOfferImageButton

    public void uploadCommercial(View view)
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // start multiple photos selector
        Intent intent = new Intent(getBaseContext(), ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 2);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResultsCommecial);
// start the selector
        startActivityForResult(intent,COMMERCIAL_REQUEST );
    }

    public void uploadIdentidfication(View view)
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // start multiple photos selector
        Intent intent = new Intent(getBaseContext(), ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 2);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResultsIdentidication);
// start the selector
        startActivityForResult(intent,IDENTIFICATION_REQUEST );

    }

    }
