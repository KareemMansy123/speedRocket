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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;
import com.thomashaertel.widget.MultiSpinner;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class Registration extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener

{


    String firstname = "", lastname = "", password = "", confirmpassword = "",
            email = "", gender = "", language = "", interest = "",
            personoalmobile = "", persontype = "", companyInterest = "", city = "", country = "";
    List<String> interstList;
    StringBuilder chosenInterestList = new StringBuilder();

   private int code ;
    TextView nav_firstname, nav_lastname, nav_email;
    CircleImageView nav_profileimage, profileImage;

    String firstName1, lastName1, email1;


    int userID1;


    String interestUser;
    String emailError;
    EditText ed_firstname, ed_lastname, ed_email, ed_password, ed_confirmpassword, ed_personalmobile;
    RadioGroup radiogroup_kindRegister, radiogroup_genderRegister;
    ArrayAdapter<String> spinnerArrayAdapter;
    private MultiSpinner spinner;
    private ArrayAdapter<String> adapter;

    Spinner spinner1, spinner2, spinnerCity, spinnerCountry;


    TextView policyTextView ;
    Button register_bt ;
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F); // make animation on button


    // menu
    String firstName , lastName  ,userProfileImage  ;
    boolean login ;
    int userID;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;
    //menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalregist_navigation_menu);
        setTitle(R.string.registertion);
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

        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        firstName1 = prefs1.getString("firstName", "");//"No name defined" is the default value.
        lastName1 = prefs1.getString("lastName", "");//"No name defined" is the default value.
        email1 = prefs1.getString("email", "");//"No name defined" is the default value.
        userID1 = prefs1.getInt("id", 0);



        ed_firstname = (EditText) findViewById(R.id.reg_firstname);
        ed_lastname = (EditText) findViewById(R.id.reg_lastname);
        ed_email = (EditText) findViewById(R.id.reg_email);

        ed_password = (EditText) findViewById(R.id.reg_password);
        ed_confirmpassword = (EditText) findViewById(R.id.reg_confirmpassword);
        ed_personalmobile = (EditText) findViewById(R.id.reg_personalmobile);

        spinner1 = (Spinner) findViewById(R.id.spinner_language);
        //spinner2 = (Spinner) findViewById(R.id.spinner_interest);
        spinnerCity = (Spinner) findViewById(R.id.spinner_city);
        spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
        spinner = (MultiSpinner) findViewById(R.id.spinnerMulti);

        spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item);

        final LinearLayout l = (LinearLayout) findViewById(R.id.interest_layout);
        final LinearLayout l1 = (LinearLayout) findViewById(R.id.interestCompany_layout);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);


//Retrofit
        Retrofit retrofit =AppConfig.getRetrofit2("https://speed-rocket.com/api/" , Registration.this);
        final UserApi userApi = retrofit.create(UserApi.class);

        final Call<ResultModel> getInterestConnection = userApi.getUsersInterest(getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa",""));

        getInterestConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                try {
                    interstList = response.body().getType();
                    for (int i = 0; i < interstList.size(); i++) {
                        adapter.add(interstList.get(i));
                        spinner.setAdapter(adapter, false, onSelectedListener);
                        spinnerArrayAdapter.add(interstList.get(i));
                        spinnerArrayAdapter.notifyDataSetChanged();

                        Log.i("QP", interstList.get(i));


                        // set initial selection
                        boolean[] selectedItems = new boolean[adapter.getCount()];
                        selectedItems[i] = true; // select second item
                        spinner.setSelected(selectedItems);

                    } // for loop
//                    Toast.makeText(getBaseContext(), "Connection Success\n",
//                            Toast.LENGTH_LONG).show();

                } // try
                catch (Exception e) {
//                    Toast.makeText(getBaseContext(), "Connection Success\n" +
//                                    "Exception\n" + e.toString(),
//                            Toast.LENGTH_LONG).show();
                } // catch
            } // onResponse

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

                Toast.makeText(getBaseContext(), "Connection Faild",
                        Toast.LENGTH_LONG).show();
            } // on Failure
        });
// Retrofit


        register_bt = (Button) findViewById(R.id.Bt_register);


        //   radiogroup_kindRegister = (RadioGroup) findViewById(R.id.radioGroup_kindregister);
/*
        radiogroup_kindRegister.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radiobt_personal:
                        // do operations specific to this selection
 Toast.makeText(getBaseContext(), "personal",
                                Toast.LENGTH_SHORT).show();

                        register_bt.setText("Register");
                        persontype = "0";
                        l.setVisibility(View.VISIBLE);
                        l1.setVisibility(View.GONE);
                        break;
                    case R.id.radiobt_company:
                        // do operations specific to this selection
Toast.makeText(getBaseContext(), "company",
                                Toast.LENGTH_SHORT).show();

                        register_bt.setText("Continue");
                        persontype = "1";
                        l.setVisibility(View.GONE);
                        l1.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });
*/
        radiogroup_genderRegister = (RadioGroup) findViewById(R.id.reg_gender);

        radiogroup_genderRegister.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rd_male:

                        gender = "male";
                        break;
                    case R.id.rd_female:

                        gender = "female";
                        break;

                }
            }
        });

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.cityEgypt,
                R.layout.spinner_item);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.Language,
                R.layout.spinner_item);

        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.country,
                R.layout.spinner_item);
        spinner1.setAdapter(adapter1);
        //  spinner2.setAdapter(spinnerArrayAdapter);
        spinnerCity.setAdapter(adapter2);
        spinnerCountry.setAdapter(adapter3);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0 :
                        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(view.getContext(), R.array.cityEgypt,
                                R.layout.spinner_item);
                        spinnerCity.setAdapter(adapter2);
                        break;
                    case 1 :
                        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(view.getContext(), R.array.cityEmirat,
                                R.layout.spinner_item);
                        spinnerCity.setAdapter(adapter3);
                        break;
                    case 2 :
                        ArrayAdapter adapter4 = ArrayAdapter.createFromResource(view.getContext(), R.array.cityKuwait,
                                R.layout.spinner_item);
                        spinnerCity.setAdapter(adapter4);
                        break;
                    case 3 :
                        ArrayAdapter adapter5 = ArrayAdapter.createFromResource(view.getContext(), R.array.citysaudi,
                                R.layout.spinner_item);
                        spinnerCity.setAdapter(adapter5);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        policyTextView = findViewById(R.id.policyTerms);

        policyTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    //Button Pressed
                    policyTextView.setTextColor(getResources().getColor(R.color.link));
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    //finger was lifted
                    policyTextView.setTextColor(getResources().getColor(R.color.white));
                }
                return false;
            }
        });

        policyTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://speed-rocket.com/privacy-policy"));
                startActivity(browserIntent);
            }
        });


    }// on Create

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    chosenInterestList.append(adapter.getItem(i)).append(",");
                }
            }

            Toast.makeText(getBaseContext(), "Selected" +
                            chosenInterestList.toString()
                    , Toast.LENGTH_LONG).show();
        }
    };


    public void check_kind_register() {


    }  // function check_kind_register

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
    } // validation of email


    public void bt_Register(View view) {


        try {
            int c_firstname = 0, c_lastname = 0, c_email = 0,
                    c_mobile = 0, c_passEqual = 0, c_emailValidation = 0, c_password = 0, c_gender = 0, c_type = 0, c_findUser = 0, c_mobileContain = 0;

            String bt_text = register_bt.getText().toString();

            firstname = ed_firstname.getText().toString();
            lastname = ed_lastname.getText().toString();
            email = ed_email.getText().toString();
            password = ed_password.getText().toString();
            confirmpassword = ed_confirmpassword.getText().toString();
            personoalmobile = ed_personalmobile.getText().toString();

            language = spinner1.getSelectedItem().toString();

            if (spinner2 != null && spinner2.getSelectedItem() != null)
                companyInterest = spinner2.getSelectedItem().toString();

            interest = chosenInterestList.toString();

            city = spinnerCity.getSelectedItem().toString();
            country = spinnerCountry.getSelectedItem().toString();


            /////// Validation

            if (firstname.equals(""))
                c_firstname = 1;
            if (lastname.equals(""))
                c_lastname = 1;
            if (personoalmobile.equals(""))
                c_mobile = 1;
            if (email.equals(""))
                c_email = 1;
            if (!password.equals(confirmpassword))
                c_passEqual = 1;
            if (!isEmailValid(email))
                c_emailValidation = 1;
            if (gender.equals(""))
                c_gender = 1;
            if (password.equals(""))
                c_password = 1;
          /*  if (persontype.equals(""))
                c_type = 1;*/
            if (personoalmobile.length() != 11)
                c_mobileContain = 1;

            /////// Validation


            Toast.makeText(getBaseContext(), "registration in process\n"
                    , Toast.LENGTH_LONG).show();

  /*          if (bt_text.equals("Continue")) {


                if (password.equals(confirmpassword) && c_firstname == 0 && c_lastname == 0
                        && c_email == 0 && c_mobile == 0 && c_mobileContain == 0 && c_emailValidation == 0
                        && c_gender == 0  && c_password == 0) {
                    Intent intent = new Intent(getBaseContext(), RegisterAsCompany.class);
                    intent.putExtra("firstName", firstname);
                    intent.putExtra("lastName", lastname);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("gender", gender);
                    intent.putExtra("language", language);
                    intent.putExtra("interest", interest);
                    intent.putExtra("mobile", personoalmobile);
                    intent.putExtra("type", persontype);
                    intent.putExtra("companyInterest", companyInterest);
                    intent.putExtra("city", city);
                    intent.putExtra("country", country);
                    startActivity(intent);
                } else {
                    if (c_firstname == 1)
                        Toast.makeText(getBaseContext(), "FirstName Required",
                                Toast.LENGTH_LONG).show();

                    else if (c_lastname == 1)
                        Toast.makeText(getBaseContext(), "LastName Required",
                                Toast.LENGTH_LONG).show();

                    else if (c_email == 1)
                        Toast.makeText(getBaseContext(), "Email Required",
                                Toast.LENGTH_LONG).show();
                    else if (c_emailValidation == 1)
                        Toast.makeText(getBaseContext(), "Email Not Vaild",
                                Toast.LENGTH_LONG).show();
                    else if (c_password == 1)
                        Toast.makeText(getBaseContext(), "Password Required",
                                Toast.LENGTH_LONG).show();
                    else if (c_passEqual == 1)
                        Toast.makeText(getBaseContext(), "Confirm Password Wrong",
                                Toast.LENGTH_LONG).show();
                    else if (c_gender == 1)
                        Toast.makeText(getBaseContext(), "Gender Required",
                                Toast.LENGTH_LONG).show();
                    else if (c_mobile == 1)
                        Toast.makeText(getBaseContext(), "Mobile Required",
                                Toast.LENGTH_LONG).show();

                    else if (c_mobileContain == 1)
                        Toast.makeText(getBaseContext(), "Mobile Number Doesn't Vaild",
                                Toast.LENGTH_LONG).show();

                    else if (c_type == 1)
                        Toast.makeText(getBaseContext(), "User Type Required",
                                Toast.LENGTH_LONG).show();

                } // password and confirm not equal
            } // if user company*/

        /*    else {*/


            //Retrofit


            if (password.equals(confirmpassword) && c_firstname == 0 && c_lastname == 0
                    && c_email == 0 && c_mobile == 0 && c_mobileContain == 0 && c_emailValidation == 0
                    && c_gender == 0 && c_password == 0) {
                //Retrofit


                  /*  Toast.makeText(getBaseContext(), "Interest : " + interest,
                            Toast.LENGTH_LONG).show();*/

                //Retrofit

                final ProgressDialog progress;
                progress = new ProgressDialog(this);
                progress.setTitle("Please Wait");
                progress.setMessage("Loading..");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                if (!((Activity) Registration.this).isFinishing()) {
                    //show dialog
                    progress.show();
                }


                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/" , getApplicationContext());

                UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> adduserconnection =
                        userApi.adduser(firstname, lastname, email, password,
                                gender, language, interest, personoalmobile
                                , city, country);
                adduserconnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {

                            List<String> emailErrors = response.body().getEmail();

                            if (emailErrors != null) {
                                emailError = emailErrors.get(0);
                                progress.dismiss();
                            } else {
                                emailError = "";
                                progress.dismiss();

                            }

                            code = response.body().getCode();

                            switch (code){
                                case 0 :
                                    Toast.makeText(getBaseContext() , "email is already registerd " , Toast.LENGTH_LONG).show();
                                    break;
                                case 1 :
                                    Toast.makeText(getBaseContext() , "mobile is already registerd " , Toast.LENGTH_LONG).show();

                                    break;
                                case 2 :
                                    Toast.makeText(getBaseContext() , "first Name  is Requerid  " , Toast.LENGTH_LONG).show();

                                    break;
                                case 3 :
                                    Toast.makeText(getBaseContext() , "first Name  is Requerid  " , Toast.LENGTH_LONG).show();

                                    break;
                                case 4 :
                                    Toast.makeText(getBaseContext() , "password  is Requerid and min characters is 5  " , Toast.LENGTH_LONG).show();

                                    break;
                                case 5 :

                                    Intent intent = new Intent(getBaseContext(), VerficationCode.class);
                                    intent.putExtra("number", personoalmobile);
                                    startActivity(intent);
                                    finish();

                                    getSharedPreferences("Auth",0).edit().putString("email",email).apply();
                                    getSharedPreferences("Auth",0).edit().putString("pass",password).apply();
                                    break;
                            }
                            Log.i("QP", "error : " + emailError);
                            if (emailError.equals("")) {

//                                new Thread(){
//                                    @Override
//                                    public void run() {
//                                        try{
//                                            GMailSender sender = new GMailSender("info@speed-rocket.com", "speedrocket2224##");
//                                            sender.sendMail("congrats , "+firstname +" you're ready for offering ",
//                                                    App.getWelcomeMail(firstname)
//                                                    ,
//                                                    "MahmoOd@gmail.com",
//                                                    email);
//                                            Log.e("SendMail","email sent");
//                                        }catch (Exception e){
//
//                                            Log.e("SendMail", e.getMessage(), e);
//                                        }
//                                    }
//                                }.start();

                                progress.dismiss();


                            }// check if email already register
                            else {


//                                Toast.makeText(getBaseContext(), "Connection Success\n" +
//                                        "" + emailError, Toast.LENGTH_LONG).show();

                            } // check if email already register

                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "exception" + (e.toString()), Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(Registration.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    progress.dismiss();
                                    noInternet.cancel();

                                }
                            });
                        }

                    }
                });
                //Retrofit

            }
            // if check password and confirm are equal


            else {
                if (c_firstname == 1)
                    Toast.makeText(getBaseContext(), "FirstName Required",
                            Toast.LENGTH_LONG).show();
                else if (c_lastname == 1)
                    Toast.makeText(getBaseContext(), "LastName Required",
                            Toast.LENGTH_LONG).show();

                else if (c_email == 1)
                    Toast.makeText(getBaseContext(), "Email Required",
                            Toast.LENGTH_LONG).show();
                else if (c_emailValidation == 1)
                    Toast.makeText(getBaseContext(), "Email Not Vaild",
                            Toast.LENGTH_LONG).show();
                else if (c_password == 1)
                    Toast.makeText(getBaseContext(), "Password Required",
                            Toast.LENGTH_LONG).show();
                else if (c_passEqual == 1)
                    Toast.makeText(getBaseContext(), "Confirm Password Wrong",
                            Toast.LENGTH_LONG).show();
                else if (c_gender == 1)
                    Toast.makeText(getBaseContext(), "Gender Required",
                            Toast.LENGTH_LONG).show();
                else if (c_mobile == 1)
                    Toast.makeText(getBaseContext(), "Mobile Required",
                            Toast.LENGTH_LONG).show();


                else if (c_mobileContain == 1)
                    Toast.makeText(getBaseContext(), "Mobile Number Doesn't Vaild",
                            Toast.LENGTH_LONG).show();


            } // password and confirm not equal

          /*  }*/

            // if user personal
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Register Exception\n" +
                    e.toString() + "city : " + city + "\ncountry : " + country, Toast.LENGTH_LONG).show();
        }
    } // function of button_Register

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

        SearchManager searchManager = (SearchManager) Registration.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(Registration.this.getComponentName()));
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
}