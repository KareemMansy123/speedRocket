package com.speedrocket.progmine.speedrocket.View.Activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class RegisterAsCompany extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F); // make animation on button
    boolean log = true ;

    String firstname = "", lastname = "", password = "", confirmpassword = "",
            email = "", gender = "",
            language = "", interest = "", personoalmobile = "",
            persontype = "" , companyInterest="" , city = "" , country="";
    Button backregister , submit ;


    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage , profileImage ;

    String firstName1 , lastName1 , email1 ;

    TextView pro_companyName ;

    int userID1;


    EditText ed_companyname , ed_companycity , ed_companymobile , ed_companybankaccount
             , ed_companyswift , ed_companybankaddress;
    Spinner spinner;

    String companyName , companyCity , companyMobile , companyBankAccount
            , companySwift , companyBabkAddress , companyCountry ;
    NotificationBadge mBadge ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyregister_navigation_menu);
        setTitle(R.string.companyRegistration);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mBadge = findViewById(R.id.badge);
        AppUtills.notificationBadge.setNotifecationBadgeByShared(mBadge , this);


        firstName1 = prefs.getString("firstName", "");//"No name defined" is the default value.
        lastName1 = prefs.getString("lastName", "");//"No name defined" is the default value.
        email1 = prefs.getString("email", "");//"No name defined" is the default value.
        userID1=prefs.getInt("id",0);

        ed_companyname=(EditText) findViewById(R.id.ed_companuname);
        ed_companybankaccount=(EditText) findViewById(R.id.ed_compnaybankaccount);
        ed_companybankaddress=(EditText) findViewById(R.id.ed_companybankaddress);
        ed_companycity=(EditText) findViewById(R.id.ed_companycity);
        ed_companymobile=(EditText) findViewById(R.id.ed_companymobile);
        ed_companyswift=(EditText) findViewById(R.id.ed_companyswift);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        submit = (Button) findViewById(R.id.Bt_registerCompany);
         spinner = (Spinner) findViewById(R.id.spinner_country);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.country, R.layout.spinner_item);
        spinner.setAdapter(adapter1);
    }

    public void shoppingCart(View view ){

        //TODO shopping cart action here
        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (RegisterAsCompany.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }

    public void bt_RegisterCompany(View view)
    {
       /* Toast.makeText(getBaseContext() , "Company Submitted" ,
                Toast.LENGTH_SHORT).show();


        int c_companyname = 0  , c_companycity = 0 , c_companymobile = 0 , c_companycountry =0;

        companyName = ed_companyname.getText().toString();
        companyCity = ed_companycity.getText().toString();
        companyMobile = ed_companymobile.getText().toString();
        companyCountry=spinner.getSelectedItem().toString();

        if(companyName.equals(""))
            c_companyname=a1;
        if(companyCity.equals(""))
            c_companycity=a1;
        if(companyCountry.equals(""))
            c_companycountry=a1;
        if(companyMobile.equals(""))
            c_companymobile=a1;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

             firstname = (String) bundle.get("firstName");
            lastname = (String) bundle.get("lastName");
            email = (String) bundle.get("email");
            password = (String) bundle.get("password");
            gender = (String) bundle.get("gender");
            language = (String) bundle.get("language");
            interest = (String) bundle.get("interest");
            personoalmobile=(String) bundle.get("mobile");
            persontype=(String) bundle.get("type");
            companyInterest=(String) bundle.get("companyInterest");
            city=(String) bundle.get("city");
            country=(String) bundle.get("country");

        }

        if(c_companyname == 0 && c_companycity==0
                && c_companycountry==0 && c_companymobile==0) {
            //Retrofit
            //Retrofit
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://speed-rocket.com/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();

            UserApi userApi = retrofit.create(UserApi.class);
            Call<ResultModel> adduserconnection =
                    userApi.adduser(firstname, lastname, email, password,
                            gender, language, interest, personoalmobile, persontype,

                            companyName,
                            companyCity, companyMobile, companyCountry,companyInterest,city,country);
            adduserconnection.enqueue(new Callback<ResultModel>() {
                @Override
                public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                    try {

                *//*    Toast.makeText(getBaseContext(), "Connection Success"
                            +"first : "+firstname+" last : "+lastname+
                                    " email : "+email+" pass : "+password+
                                    " mob : "+personoalmobile+
                                    " type : "+persontype+
                                    " interest : "+interest+
                                    " language : "+language+
                                    " companuN : "+companyName+
                                    " companCity : "+companyCity+
                                    " companCountry : "+companyCountry+
                                    " companyMobile : "+companyMobile
                            , Toast.LENGTH_LONG).show();*//*


                        Toast.makeText(getBaseContext(), "Connection sucess" +
                                " Company Submitted", Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(getBaseContext(), LoginScreen.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);


                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "exception" + (e.toString()), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<ResultModel> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Connection faild", Toast.LENGTH_LONG).show();

                }
            });
            //Retrofit

        } // if
        else
            {
                if(c_companyname == a1)
                    Toast.makeText(getBaseContext(), "CompanyName Required",
                            Toast.LENGTH_LONG).show();
                else if(c_companycity == a1)
                    Toast.makeText(getBaseContext(), "Adress Required",
                            Toast.LENGTH_LONG).show();

                else if(c_companymobile == a1)
                    Toast.makeText(getBaseContext(), "Mobile Required",
                            Toast.LENGTH_LONG).show();
                else if(c_companycountry == a1)
                    Toast.makeText(getBaseContext(), "Country Required",
                            Toast.LENGTH_LONG).show();
            }// else
*/

    }


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
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        nav_firstname = (TextView) findViewById(R.id.menu_firstname);
        nav_lastname = (TextView) findViewById(R.id.menu_lastname);
        nav_email = (TextView) findViewById(R.id.menu_email);

        nav_profileimage = (CircleImageView) findViewById(R.id.nav_profileimage);


        final ImageView nav_msgBadge = (ImageView) findViewById(R.id.msg_badge);

        if (getSharedPreferences("MyPrefsFile",0).getBoolean("msg",false)) nav_msgBadge.setVisibility(View.VISIBLE);
        else nav_msgBadge.setVisibility(View.INVISIBLE);

        nav_firstname.setText(firstName1);
        nav_lastname.setText(lastName1);
        if (!isProbablyArabic(firstName1) && !isProbablyArabic(lastName1) &&
                getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa","").equalsIgnoreCase("ar")) {
            nav_firstname.setText(lastName1);
            nav_lastname.setText(firstName1);
        }else if (isProbablyArabic(firstName1) &&isProbablyArabic(lastName1) &&
                getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa","").equalsIgnoreCase("en"))
        {
            nav_firstname.setText(lastName1);
            nav_lastname.setText(firstName1);

        }


        nav_email.setText(email1);


        nav_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ProfileAccount.class);
                intent.putExtra("userID",userID1);
                startActivity(intent);
            }
        });
        return true;
    }

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

        Menu m=nv.getMenu();

        int id = item.getItemId();

        if (id == R.id.nav_offers) {
            // Handle the camera action

            return true;

        } else if (id == R.id.nav_country) {
            return true;

        } else if (id == R.id.nav_language) {
            Intent intent = new Intent(getBaseContext(),ChangeLanguage.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_logout) {
            if(log == true)
            {
                m.findItem(R.id.nav_logout).setTitle("Login");

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.remove("firstName");
                editor.remove("lastName");
                editor.remove("email");
                editor.remove("ItemsNumber");
                editor.apply();

                nav_firstname.setText("");
                nav_lastname.setText("");
                nav_email.setText("");
                log = false ;

            }
            else
            {
                m.findItem(R.id.nav_logout).setTitle("Logout");
                log = true ;
                Intent intent = new Intent(getBaseContext() , LoginScreen.class );
                startActivity(intent);


            }
        }
        else if (id == R.id.nav_home) {
            Intent intent = new Intent(getBaseContext() , NavigationMenu.class );
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
