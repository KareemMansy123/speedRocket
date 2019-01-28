package com.speedrocket.progmine.speedrocket.View.Activites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Control.RegistrationTapAdapter;
import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.ServerResponse;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Fragment.RegistrationFirstTap;
import com.speedrocket.progmine.speedrocket.View.Fragment.RegistrationVirevifyTap;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;
import static com.speedrocket.progmine.speedrocket.View.Activites.ProfileAccount.GET_FROM_GALLERY;

public class RegistrationCustom extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,
        RegistrationFirstTap.onCallBack , RegistrationVirevifyTap.OnCallBack2 {

   public static CircularProgressButton next ;

    ViewPager pager ;

    public  String mobileNumber = "" ;
    private int position = 0 ;

    TextView nav_firstname, nav_lastname, nav_email;
    CircleImageView nav_profileimage, profileImage;
    public static  boolean log  ;
    private static final int REQUEST_CAMERA = 1888;
    Menu menu;

    int userID;
     int code = 0 ;
    String firstName = "", lastName = "", 
            email = "",  language = "", interest = "";
    public static   String userProfileImage  ;

    String imagePath = "" ;
    sendImageUrl callBack ;
    NavigationView navigationView;
     private int userId = 0 ;
    boolean login ;

    public static int check =0;

    public static String secImagePath="" , secName ="" ,secEmail = "" , secPassword="",secIntersts = "" ;
    public static boolean secAccptTerms = false ;
    public static String rMobileNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_navigation_menu);
        setTitle(R.string.register);


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

        int PERMISSION_REQUEST_CODE = 1;

        if (Build.VERSION.SDK_INT >= 23) {
            //do your check here
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            // ask user for permission to access storage // allow - deni
        }

        if (login == false) {
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Login");
            navigationView.getMenu().findItem(R.id.nav_buycoins).setVisible(false);
        }

        else if (login == true) {
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Logout");
            navigationView.getMenu().findItem(R.id.nav_buycoins).setVisible(true);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        next = findViewById(R.id.nextWizerd);
        pager = findViewById(R.id.Registration_view_pager);

        final RegistrationTapAdapter adapter = new RegistrationTapAdapter(getSupportFragmentManager(), 3);

        pager.setAdapter(adapter);

        Bundle b = getIntent().getExtras();
        if (b != null){
            if (b.containsKey("step")){
                rMobileNumber = (String)b.get("mobileNumber");
                pager.setCurrentItem(1);
                userId = getSharedPreferences("RegistrationNumber",0).getInt("userId",0);

            }
        }

//        pager.setCurrentItem(2);
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        PushDownAnim.setPushDownAnimTo(next).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                next.startAnimation();
                if (check ==0){
                registerByNumber();
                }else{
                 registerTheRestOFData();
                }

            }
        });

    }
    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }
    private void registerTheRestOFData(){

        if (secName.equalsIgnoreCase("")){
            Toast.makeText(this , "please add your name ",Toast.LENGTH_LONG).show();
            next.revertAnimation();
        }else if (secPassword.equalsIgnoreCase("") ){
            Toast.makeText(this , "please add your password ",Toast.LENGTH_LONG).show();
            next.revertAnimation();
        }else if (!secAccptTerms){
            Toast.makeText(this , "please Read and check police terms  ",Toast.LENGTH_LONG).show();
            next.revertAnimation();
        }else {
          if (secEmail.isEmpty()){
              if (rMobileNumber.isEmpty())
              secEmail =mobileNumber ;
              else
                  secEmail=rMobileNumber ;
          }
            Call<ServerResponse> registerFullData  ;
            if (!secImagePath.equalsIgnoreCase("")) {
                Retrofit retrofit = AppConfig.getRetrofit(this);

                ApiConfig api = retrofit.create(ApiConfig.class);

                File file = new File(secImagePath);

                // RequestBody userIdRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userID));

                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);

                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                registerFullData = api.registerRestOFData(filename , fileToUpload, userId ,secName , secEmail , secPassword , secIntersts);
                    }
             else{
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",this);

                ApiConfig api = retrofit.create(ApiConfig.class);

                registerFullData = api.registerRestOFData( userId ,secName , secEmail , secPassword , secIntersts);

            }

                Log.e("data" , ""+userId +" " + secName +" " + secEmail +" "+ secPassword+ " " + secIntersts);

               if (registerFullData != null) {
                   registerFullData.enqueue(new Callback<ServerResponse>() {
                       @Override
                       public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                           code = response.body().getCode();

                           switch (code) {
                               case 1:
                                   Toast.makeText(getApplicationContext(), "the email is already taken ", Toast.LENGTH_LONG).show();
                                   next.revertAnimation();
                                   break;
                               case 2:
                                   Toast.makeText(getApplicationContext(), " user not found  ", Toast.LENGTH_LONG).show();
                                   next.revertAnimation();
                                   break;
                               case 3:
                                   Toast.makeText(getApplicationContext(), "please try again ", Toast.LENGTH_LONG).show();
                                   next.revertAnimation();
                                   break;
                               case 4:
                                   Intent intent = new Intent(getBaseContext(), LoginScreen.class);
                                   startActivity(intent);
                                   finish();
                                Log.e("loginScreenError##" , secEmail) ;
                                Log.e("loginScreenError##" ,secPassword );
                                   getSharedPreferences("Auth", 0).edit().putString("email", secEmail).apply();
                                   getSharedPreferences("Auth", 0).edit().putString("pass", secPassword).apply();
                                   break;

                           }
                       }

                       @Override
                       public void onFailure(Call<ServerResponse> call, Throwable t) {
                           next.revertAnimation();
                           if (t instanceof IOException) {
                               final Dialog noInternet = AppConfig.InternetFaild(RegistrationCustom.this);
                               final Button btn = noInternet.findViewById(R.id.Retry);
                               noInternet.show();
                               btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       noInternet.cancel();
                                       registerTheRestOFData();
                                       next.startAnimation();
                                   }
                               });
                           }
                       }
                   });


               }

        }


    }

    private void registerByNumber(){

        if (mobileNumber.length() != 11)
        {
            Toast.makeText(getApplicationContext(), "please enter vaild mobile number" , Toast.LENGTH_LONG).show();
            next.revertAnimation();
        }else {

            new Thread() {

                @Override
                public void run() {
                    Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", RegistrationCustom.this);
                    ApiConfig api = retrofit.create(ApiConfig.class);


                    Call<ResultModel> needVerification = api.registrationNew(mobileNumber);
                    needVerification.enqueue(new Callback<ResultModel>() {
                        @Override
                        public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                            code = response.body().getCode();
                            switch (code) {

                                case 0:
                                    Toast.makeText(getApplicationContext(), "the mobiel is already registerd ", Toast.LENGTH_LONG).show();
                                    next.revertAnimation();
                                    break;
                                case 5:
                                    pager.setCurrentItem(1);
                                    next.setVisibility(View.INVISIBLE);
                                    userId = response.body().getUserId();
                                    getApplicationContext().getSharedPreferences("RegistrationNumber" , 0 ).edit().putString("number",mobileNumber).apply();
                                    getApplicationContext().getSharedPreferences("RegistrationNumber" , 0 ).edit().putInt("active",0).apply();
                                    getApplicationContext().getSharedPreferences("RegistrationNumber" , 0 ).edit().putInt("userId",userId).apply();
                                    break;
                                case 3:
                                    Toast.makeText(getApplicationContext(), "server busy please try again ", Toast.LENGTH_LONG).show();
                                    next.revertAnimation();
                                    break;
                            }

                        }

                        @Override
                        public void onFailure(Call<ResultModel> call, Throwable t) {
                            next.revertAnimation();
                            if (t instanceof IOException) {
                                final Dialog noInternet = AppConfig.InternetFaild(RegistrationCustom.this);
                                final Button btn = noInternet.findViewById(R.id.Retry);
                                noInternet.show();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        noInternet.cancel();
                                        registerByNumber();
                                        next.startAnimation();
                                    }
                                });
                            }
                        }
                    });

                }
            }.start();
        }
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

        SearchManager searchManager = (SearchManager) RegistrationCustom.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(RegistrationCustom.this.getComponentName()));
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

        final  Menu m = nv.getMenu();


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

                        //AppUtills.notificationBadge.setNotifecationBadge(mBadge,0);
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
                finish();
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
                finish();
            } // check if login 0 ---> user not login
            else {
                Intent intent = new Intent(getBaseContext(), MyCompanyProfile.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
            return true;

        }else if (id == R.id.yourOrders){
            Intent intent = new Intent(getBaseContext(),yourOrder.class);
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


    @Override
    public void RegisterNumber(String number) {
        mobileNumber = number ;
        rMobileNumber = number;
        next.setVisibility(View.VISIBLE);
        Log.e("ActivityMoileNumber##" , mobileNumber);
    }

    @Override
    public void stepThreeTreger() {
        pager.setCurrentItem(2);
        next.setVisibility(View.VISIBLE);
        next.setText("Finish");

    }

    public static void setAnimation(boolean anim)
    {
        if (anim){
            next.startAnimation();
        }else{

            next.revertAnimation();
        }
    }

    public static void buttonVisibalty(boolean visable){
        if (visable){
            next.setVisibility(View.VISIBLE);
        }else{
           next.setVisibility(View.INVISIBLE);
        }

    }

    public interface sendImageUrl{
        void imageUrl(Uri uri, String imagePath);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == RESULT_OK) {

            if (requestCode == GET_FROM_GALLERY) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }


            /*CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image_account);
            imageView.setImageBitmap(bitmap);*/


           // uploadProfileImageFast();
        }
    } // on Activity result


    public void onSelectFromGalleryResult(Intent data)
    {
        if (data != null) {

              /*  bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedimage = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

            Uri imageUri = data.getData();
            //profileImage.setImageURI(imageUri);

            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = getRealPathFromURI(imageUri);
            }

            callBack.imageUrl(imageUri , imagePath);

            // upload image
        }
    } // onSelectFromGalleryResult


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getBaseContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void onCaptureImageResult(Intent data)
    {
        if (data !=null) {

           /* bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedimage = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

            Uri imageUri = data.getData();
            //profileImage.setImageURI(imageUri);
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.ACTION_IMAGE_CAPTURE};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = getRealPathFromURI(imageUri);
            }
            callBack.imageUrl(imageUri , imagePath);

            Log.i("QP",imagePath);
        }
    } // onCaptureImageResult
}
