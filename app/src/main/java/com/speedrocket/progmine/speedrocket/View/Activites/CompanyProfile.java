package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Activity;
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
import android.support.design.widget.TabLayout;
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
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Control.CompanyTabAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.Company;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class CompanyProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    String companName , companyLogo , companyEmail , companyAddress ;
    float companyRate ;
    List<Company> company ;
    int id  , userID , copmpanyId;
    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.qabout,
            R.drawable.qnews,
            R.drawable.qproduct
    };

    private ProgressDialog progress;
    private Handler handler;

    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage , profileImage ;

    String firstName , lastName , email ;

    TextView pro_companyName , pro_companyEmail , pro_companyAddress ;

    CircleImageView company_profile_image ;
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F); // make animation on button

    Dialog  ratingDialog  , sendMessage;
    Button rate , cancelRate ;
    // menu
    String interest ,userProfileImage  ;
    boolean login ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;
    NotificationBadge mBadge ;
    RatingBar rating , dialogRatBar ;

    EditText dilaogTitle , dialogMessage ;
    //menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyprofile_menu);

        //menu
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

        // menu

        company_profile_image = (CircleImageView) findViewById(R.id.company_profile_image);
        mBadge = findViewById(R.id.badge);
        AppUtills.notificationBadge.setNotifecationBadgeByShared(mBadge , this);


        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        firstName = prefs1.getString("firstName", "");//"No name defined" is the default value.
        lastName = prefs1.getString("lastName", "");//"No name defined" is the default value.
        email = prefs1.getString("email", "");//"No name defined" is the default value.
        userID=prefs1.getInt("id",0);

        pro_companyName=(TextView)findViewById(R.id.pro_companyName);
        pro_companyEmail=(TextView)findViewById(R.id.pro_companyEmail);
        pro_companyAddress=(TextView)findViewById(R.id.pro_companyAddress);

        ratingDialog = new Dialog(this ) ;
        ratingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ratingDialog.setContentView(R.layout.rate_company_dialog);


        sendMessage = new Dialog(this);
        sendMessage.setContentView(R.layout.message_to_company);

        dilaogTitle = sendMessage.findViewById(R.id.contact_title);
        dialogMessage = sendMessage.findViewById(R.id.contact_message);



        rate = ratingDialog.findViewById(R.id.rate);
        cancelRate = ratingDialog.findViewById(R.id.cancel_rate);
        dialogRatBar = ratingDialog.findViewById(R.id.rating);


       rating = findViewById(R.id.rating);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogRatBar.getRating() == 0 ){
                    Toast.makeText(getBaseContext() , " please set your rating " , Toast.LENGTH_SHORT).show();
                }else {
                    if (! (userID==0)){
                        AppUtills.Rate.setRate(copmpanyId , userID , dialogRatBar.getRating() , CompanyProfile.this);
                        ratingDialog.cancel();
                    }else {
                        ratingDialog.cancel();
                        Toast.makeText(getBaseContext() , " please login first " , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CompanyProfile.this , LoginScreen.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });

        cancelRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingDialog.cancel();
            }
        });


       rating.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {
             if (motionEvent.getAction() == MotionEvent.ACTION_UP){

                 ratingDialog.show();
             }


               return true ;

           }
       });

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            copmpanyId=(int)b.get("companyId");
        }

       /* Toast.makeText(getBaseContext(),"userID : "+userID,
                Toast.LENGTH_LONG).show();*/




        getProfielData();

       /* RatingBar ratingBar = (RatingBar) findViewById(R.id.CompanymyRatingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ffc356"), PorterDuff.Mode.SRC_ATOP);*/

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_company);
        if (getSharedPreferences("MyPref" , 0).getString("langa" , "").equalsIgnoreCase("ar")){
            tabLayout.addTab(tabLayout.newTab().setText("عن الشركه"));
            tabLayout.addTab(tabLayout.newTab().setText("اخر العروض"));
            tabLayout.addTab(tabLayout.newTab().setText("المنتجات"));
        }else{
            tabLayout.addTab(tabLayout.newTab().setText("About"));
            tabLayout.addTab(tabLayout.newTab().setText("Latest Offer"));
            tabLayout.addTab(tabLayout.newTab().setText("Product"));

        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_company);
        final CompanyTabAdapter adapter = new CompanyTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void send(View v){

        sendMessage.show();
    }

            public void sendMessage(View v ){

              sendMessage();
            }
            public void sendMessage(){

                final ProgressDialog progress;
                progress = new ProgressDialog(this);
                progress.setTitle("Please Wait");
                progress.setMessage("Loading..");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                if (!((Activity) this).isFinishing()) {
                    //show dialog
                    progress.show();
                }
                new Thread() {
                    @Override
                    public void run() {

                        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());
                        UserApi api = retrofit.create(UserApi.class);
                        Call<ResultModel> setProgress = api.companyMessage(dilaogTitle.getText().toString() ,copmpanyId , userID , dialogMessage.getText().toString() );

                        setProgress.enqueue(new Callback<ResultModel>() {
                            @Override
                            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                                progress.dismiss();
                                sendMessage.cancel();
                                Toast.makeText(getBaseContext(), "your message sent successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResultModel> call, Throwable t) {
                                progress.dismiss();
                                if (t instanceof IOException){
                                    final Dialog   noInternet = AppConfig.InternetFaild(CompanyProfile.this);
                                    final Button btn = noInternet.findViewById(R.id.Retry);
                                    noInternet.show();
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            noInternet.cancel();
                                            sendMessage();
                                        }
                                    });
                                }
                            }
                        });


                    }
                }.start();


            }


            public void cancelContactUsDialog(View v){
              sendMessage.cancel();

            }

   public NotificationBadge getBadge(){

        return  mBadge ;
   }
    public void shoppingCart(View view ){

        //TODO shopping cart action here
        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (CompanyProfile.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(CompanyProfile.this , NavigationMenu.class);
        startActivity(intent);
        this.finish();
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
        setTitle(R.string.profile);
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

        nav_profileimage = (CircleImageView) findViewById(R.id.nav_profileimage);

        final ImageView nav_msgBadge = (ImageView) findViewById(R.id.msg_badge);

        if (getSharedPreferences("MyPrefsFile",0).getBoolean("msg",false)) nav_msgBadge.setVisibility(View.VISIBLE);
        else nav_msgBadge.setVisibility(View.INVISIBLE);

        nav_firstname.setText(firstName);
        nav_lastname.setText(lastName);
        nav_email.setText(email);


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

        SearchManager searchManager = (SearchManager) CompanyProfile.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(CompanyProfile.this.getComponentName()));
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

      final  Menu m=nv.getMenu();




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


    public  void getProfielData()
    {
        //Retrofit
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        handler = new Handler()
        {

            @Override
            public void handleMessage(Message msg)
            {
                progress.dismiss();
                super.handleMessage(msg);
            }

        };

        progress.show();


        new Thread()
        {
            public void run()
            {
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());

        final UserApi userApi = retrofit.create(UserApi.class);
        Call<ResultModel> getProfileConnection =
                userApi.getCompanyAccount(copmpanyId);

        getProfileConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                try {
                    company = response.body().getCompany();
                    if (  getSharedPreferences("MyPref" , MODE_PRIVATE).getString("langa" ,"").equalsIgnoreCase("ar")){

                        companName=company.get(0).getAr_name();
                        pro_companyName.setTextDirection(View.TEXT_DIRECTION_RTL);
                        pro_companyAddress.setTextDirection(View.TEXT_DIRECTION_RTL);
                        pro_companyEmail.setTextDirection(View.TEXT_DIRECTION_RTL);
                    }else{
                        companName=company.get(0).getEn_name();
                    }
                    companyLogo = company.get(0).getLogo();
                    companyEmail = company.get(0).getEmail();
                    companyAddress = company.get(0).getCity();
                    companyRate = company.get(0).getRate();

                    Picasso.with(getBaseContext()).load("https://speed-rocket.com/upload/logo/"
                            +companyLogo).
                            fit().centerCrop().into(company_profile_image);



                    pro_companyName.setText(companName);
                    pro_companyEmail.setText(companyEmail);
                    pro_companyAddress.setText(companyAddress);
                    rating.setRating(companyRate);

                    progress.dismiss();



                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Connection Success\n" +
                                    "Exception"+e.toString()
                            ,Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                progress.dismiss();
                if (t instanceof IOException){
                    final Dialog   noInternet = AppConfig.InternetFaild(CompanyProfile.this);
                    final Button btn = noInternet.findViewById(R.id.Retry);
                    noInternet.show();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noInternet.cancel();
                            getProfielData();
                        }
                    });
                }


            }
        });
        //Retrofit

            }

        }.start();

    } // getProfileData Function

}
