package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Control.MyCompanyAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.Company;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class MyCompanyProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    String firstName1 , lastName1 , email1 ,userProfileImage ,companyArName;
    int id  , userID , userID1 , companyId;

    TextView fName ;
    CircleImageView myProfileImage ;

    Dialog contactUsDialog ;

    EditText ed_contactName , ed_contactEmail , ed_contactMessage ;

    String contactName="" , contactEmail="" , contactMessage="",companyName , companyLogo ;

    // menu
    String firstName , lastName , email="" , interest   ;
    boolean login ;
    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;
    NotificationBadge mBadge ;
    //menu


    private RecyclerView recyclerView;
    private MyCompanyAdapter mAdapter;
    Company company;
    private List<Company> companyList1 = new ArrayList<>();

    private ProgressDialog progress;
    private Handler handler;
    List<Company> CList = new ArrayList<>();

    Dialog companyListDialog;
    ImageView notify ;
    CardView cardProfile , cardAddOffer , cardAddCompany , cardAddProduct , cardFinance , cardContactUs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_company_profile_navigation_menu);
      setTitle(R.string.myPanal);

        //menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBadge = findViewById(R.id.badge);
        AppUtills.notificationBadge.setNotifecationBadgeByShared(mBadge , this);

        menu();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        firstName = prefs.getString("firstName", "");//"No name defined" is the default value.
        lastName = prefs.getString("lastName", "");//"No name defined" is the default value.
        email = prefs.getString("email", "");//"No name defined" is the default value.
        userID = prefs.getInt("id", 0);
        interest = prefs.getString("interest", "");
        userProfileImage = prefs.getString("profileImage", "");
        login = prefs.getBoolean("login", false);

        if (login == false){
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Login");
            navigationView.getMenu().findItem(R.id.nav_buycoins).setVisible(false);
        }

        else if (login == true){
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Logout");
            navigationView.getMenu().findItem(R.id.nav_buycoins).setVisible(true);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // menu

        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        firstName1 = prefs1.getString("firstName", "");//"No name defined" is the default value.
        lastName1 = prefs1.getString("lastName", "");//"No name defined" is the default value.
        email1 = prefs1.getString("email", "");//"No name defined" is the default value.
        userID1=prefs1.getInt("id",0);
        userProfileImage = prefs1.getString("profileImage","");



        companyListDialog = new Dialog(this);
        companyListDialog.setContentView(R.layout.dialogue_with_my_company);



        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            if (b.containsKey("userID"))
            userID=(int)b.get("userID");
        }


        fName = (TextView) findViewById(R.id.myName);
        myProfileImage = (CircleImageView) findViewById(R.id.my_profile_image);

        fName.setText(firstName1);
        Picasso.with(getBaseContext()).load("https://speed-rocket.com/upload/users/"
                +userProfileImage).
                fit().centerCrop().into(myProfileImage);


        contactUsDialog = new Dialog(this); // Context, this, etc.
        contactUsDialog.setContentView(R.layout.dialogu_contactus);



        ed_contactName = (EditText) contactUsDialog.findViewById(R.id.contactName);
        ed_contactEmail = (EditText) contactUsDialog.findViewById(R.id.contactEmail);
        ed_contactMessage = (EditText) contactUsDialog.findViewById(R.id.contactMessage);

        recyclerView = (RecyclerView) companyListDialog.findViewById(R.id.recycleViewCompany);

        mAdapter = new MyCompanyAdapter(companyList1, MyCompanyProfile.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(llm);

        cardProfile = (CardView) findViewById(R.id.cardViewMyProfile);
        cardAddCompany = (CardView) findViewById(R.id.cardViewAddCompany);
        cardAddOffer = (CardView) findViewById(R.id.cardViewAddOffer);
        cardAddProduct = (CardView) findViewById(R.id.cardViewAddProduct);
        cardFinance = (CardView) findViewById(R.id.cardViewFinance);
        cardContactUs = (CardView) findViewById(R.id.cardViewContactUs);

        notify = findViewById(R.id.notify_badge);
        if (getSharedPreferences("MyPrefsFile",0).getBoolean("msg",false)) notify.setVisibility(View.VISIBLE);
        else notify.setVisibility(View.INVISIBLE);

        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ProfileAccount.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        }); // cardProfile

        cardAddCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(),CompanyScreen.class);
                startActivity(intent);
            }
        }); // cardAddCompany

        cardAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompanies(1);
            }
        }); // cardAddOffer

        cardAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompanies(0);
            }
        }); // cardAddProduct

        cardFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),MyCashScreen.class);
                startActivity(intent);
            }
        }); // cardFinance

        cardContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactUsDialog.show();
            }
        }); // cardContactUs



    } // onCreate function
    public void shoppingCart(View view ){

        //TODO shopping cart action here
        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (MyCompanyProfile.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }
    public  void  getCompanies(final int check)
    {

        companyList1.clear();
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                super.handleMessage(msg);
            }

        };
        progress.show();
        new Thread() {
            public void run() {
                //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModel> getCompanyConnection = userApi.getCompany(userID1);

                getCompanyConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try
                        {

                            Log.i("QP",userID1+"");
                            CList = response.body().getCompanies();

                            for(int i = 0 ; i <CList.size() ; i++)
                            {
                                companyId = CList.get(i).getId();
                                companyName = CList.get(i).getEn_name();
                                companyLogo = CList.get(i).getLogo();
                                companyArName = CList.get(i).getAr_name();
                                int categoryId = CList.get(i).getCategoryId();

                                company = new Company(companyId,categoryId ,companyName,companyArName,companyLogo,check);

                                companyList1.add(company);
                                mAdapter.notifyDataSetChanged();

                            }
                            companyListDialog.show();
                            progress.dismiss();
                        } // try
                        catch (Exception e)
                        {
                   /* Toast.makeText(getBaseContext(),"Connection Success\n" +
                                    "Exception Navigation menu\n"+e.toString(),
                            Toast.LENGTH_LONG).show();*/progress.dismiss();

                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(MyCompanyProfile.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getCompanies(check);
                                }
                            });
                        }

                    } // on Failure
                });


            }

        }.start();

// Retrofit
    } // finction get Companies

/*
    public void cardViewMyProfile(View view)
    {
        Intent intent = new Intent(getBaseContext(),ProfileAccount.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
    } // cardViewMyProfile Function

    public void cardViewAddProduct(View view)
    {
        getCompanies(0);

    } // cardViewAddProduct Function

    public void cardViewAddOffer(View view)
    {
       getCompanies(a1);

     *//*   Intent intent = new Intent(getBaseContext(),AddOffer.class);
        startActivity(intent);*//*
    } // cardViewAddOffer Function


    public void cardViewAddCompany(View view)
    {

        Intent intent = new Intent(getBaseContext(),CompanyScreen.class);
        startActivity(intent);
    } // function cardViewAddCompany

    public void cardViewFinance(View view)
    {
             Intent intent = new Intent(getBaseContext(),MyCashScreen.class);
             startActivity(intent);
    } // function cardViewFinance

    public void cardViewContactUs(View view)
    {
        contactUsDialog.show();
    }// function cardViewContactUs*/

    public void cancelContactUsDialog(View view)
    {
        contactUsDialog.cancel();
    }// function cancelContactUsDialog

    public void sendMessage(View view)
    {
        contactName = ed_contactName.getText().toString();
        contactEmail = ed_contactEmail.getText().toString();
        contactMessage = ed_contactMessage.getText().toString();

        if(contactName.equals("") || contactEmail.equals("") || contactMessage.equals(""))
            Toast.makeText(getBaseContext(),"Fields Empty",Toast.LENGTH_LONG).show();


        else {

          sendMesage();
        }//else
        //Retrofit

    }// function cancelContactUsDialog

    private void sendMesage(){

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                super.handleMessage(msg);
            }

        };
        progress.show();
        new Thread() {
            public void run() {

                //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> contactUsConnection =
                        userApi.contactUs(contactName, contactEmail, contactMessage);
                contactUsConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {
                   /* Toast.makeText(getBaseContext(), "Done"
                        , Toast.LENGTH_LONG).show(); */
                            Log.i("QP", "Done");
                            progress.dismiss();
                            contactUsDialog.cancel();

                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "exception" + (e.toString()), Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(MyCompanyProfile.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    sendMesage();
                                }
                            });
                        }
                    }
                });

            }
        }.start();


    }

    public void cancelMyCompanyList(View view)
    {
        companyListDialog.cancel();
    } // functtion cancelMyCompanyList


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        if(userID != 0)
        {
            menu.findItem(R.id.nav_myProducts).setVisible(true);
            menu.findItem(R.id.nav_cPanal).setVisible(true);
            menu.findItem(R.id.yourOrders).setVisible(true);
        } // if


        nav_firstname = (TextView) findViewById(R.id.menu_firstname);
        nav_lastname = (TextView) findViewById(R.id.menu_lastname);
        nav_email = (TextView) findViewById(R.id.menu_email);
        nav_firstname.setText(firstName);
        nav_lastname.setText(lastName);
        nav_profileimage = (CircleImageView) findViewById(R.id.nav_profileimage);



        nav_email.setText(email);

        final ImageView nav_msgBadge = (ImageView) findViewById(R.id.msg_badge);

        if (getSharedPreferences("MyPrefsFile",0).getBoolean("msg",false)) nav_msgBadge.setVisibility(View.VISIBLE);
        else nav_msgBadge.setVisibility(View.INVISIBLE);

        Picasso.with(getBaseContext()).load("https://speed-rocket.com/upload/users/"
                +userProfileImage).
                fit().centerCrop().into(nav_profileimage);
        nav_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),MyCompanyProfile.class);
                intent.putExtra("userID",userID);
                startActivity(intent);


            }
        });
        MenuItem searchItem = menu.findItem(R.id.action_settings);

        SearchManager searchManager = (SearchManager) MyCompanyProfile.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MyCompanyProfile.this.getComponentName()));
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


        NavigationView nv= (NavigationView) findViewById(R.id.nav_view);

       final Menu m=nv.getMenu();




        int id = item.getItemId();

        //  for(int i =a1 ; i<= numOfCategoryItems ; i++) {



        if (id == R.id.nav_category) {


            Intent intent = new Intent(getBaseContext(),ProductMenuList.class);
            startActivity(intent);


        }
        else if (id == R.id.nav_myProducts)
        {
            Intent intent = new Intent(getBaseContext(), MyWinnerProducts.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_offers)
        {
            Intent intent = new Intent(getBaseContext(),OfferMenuList.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_country) {

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

                        AppUtills.notificationBadge.setNotifecationBadge(mBadge,0);
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

        }  else if (id == R.id.nav_buycoins) {
            Intent intent = new Intent(getBaseContext(), BuyCoins.class);
            startActivity(intent);
            return true;

        }
        else if (id == R.id.nav_cPanal) {
            if(userID == 0)
            {
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

    public  void menu()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        menu = navigationView.getMenu();



        navigationView.setNavigationItemSelectedListener(this);

    }
}
