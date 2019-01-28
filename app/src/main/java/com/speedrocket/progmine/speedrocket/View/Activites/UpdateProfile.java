package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;
import com.thomashaertel.widget.MultiSpinner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UpdateProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {


    // set initial selection
    NotificationBadge mBadge ;
    EditText ed_firstName , ed_lastName , ed_Email , ed_MobileNumber ;

    RadioGroup radioGroupGender ;

    RadioButton radioButtonMale , radioButtonFemale ;

    Spinner spinnerCity , spinnerCountry , spinnerLanguage;

    MultiSpinner spinnerInterest;
    ArrayAdapter<String> adapter;

    List<String> interstList ;

    StringBuilder chosenInterestList= new StringBuilder();

    String firstName="" , lastName="" , gender="" , mobileNumber="" , language=""
            , email="" , city="" , country="" , interest="" ;

    String firstName1="" , lastName1="" , gender1="" , mobileNumber1="" , language1=""
            , email1="" , city1="" , country1="" , interest1="" ;

    String emailError ;

    int  userID;
    String[] interestChosenBefore ;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    // menu
    String userProfileImage  ;
    boolean login ;
    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;
    //menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_navigation_menu);
        setTitle(R.string.update);

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

        firstName1 = prefs1.getString("firstName", "");
        lastName1 = prefs1.getString("lastName", "");
        email1 = prefs1.getString("email","");
        gender1 = prefs1.getString("gender","");
        interest1 = prefs1.getString("interest","");
        language1 = prefs1.getString("language","");
        mobileNumber1 = prefs1.getString("mobile","");
        city1 = prefs1.getString("city","");
        country1 = prefs1.getString("country","");
        interest1 = prefs1.getString("interest","");




        userID=prefs1.getInt("id",0);


        ed_firstName = (EditText) findViewById(R.id.update_firstname);
        ed_lastName = (EditText) findViewById(R.id.update_lastname);
        ed_Email = (EditText) findViewById(R.id.update_email);
        ed_MobileNumber = (EditText) findViewById(R.id.update_personalmobile);

        radioGroupGender = (RadioGroup) findViewById(R.id.update_gender);
        radioButtonMale = (RadioButton) findViewById(R.id.update_male);
        radioButtonMale = (RadioButton) findViewById(R.id.update_female);

        spinnerCity = (Spinner) findViewById(R.id.update_spinner_city);
        spinnerCountry = (Spinner) findViewById(R.id.update_spinner_country);
        spinnerLanguage = (Spinner) findViewById(R.id.update_spinner_language);

        spinnerInterest = (MultiSpinner) findViewById(R.id.update_spinnerMulti_interest);


        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.Language,
                R.layout.spinner_item);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.cityEgypt,
                R.layout.spinner_item);
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.country,
                R.layout.spinner_item);




        spinnerLanguage.setAdapter(adapter1);
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


       interestChosenBefore = interest1.split(",");
        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://speed-rocket.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final UserApi userApi = retrofit.create(UserApi.class);

        final Call<ResultModel> getInterestConnection = userApi.getUsersInterest(getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa",""));

        getInterestConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                try
                {
                    interstList = response.body().getType();

                    for(int i = 0 ; i< interstList.size();i++)
                    {
                        adapter.add(interstList.get(i));

                        spinnerInterest.setAdapter(adapter, false, onSelectedListener);
                    } // for loop
                    boolean[] selectedItems = new boolean[adapter.getCount()];
                    for (int i = 0 ; i<interstList.size() ; i++)
                    {

                        if(Arrays.asList(interestChosenBefore).contains(interstList.get(i)))
                        {

                            selectedItems[i] = true; // select second item
                            spinnerInterest.setSelected(selectedItems);

                            Log.i("QP","Done");
                        } // if

                        // set initial selection
                    }
                    /*Toast.makeText(getBaseContext(),"Connection Success\n",
                            Toast.LENGTH_LONG).show();*/
                } // try
                catch (Exception e)
                {
                    Toast.makeText(getBaseContext(),"Connection Success\n" +
                                    "Exception\n"+e.toString(),
                            Toast.LENGTH_LONG).show();
                } // catch
            } // onResponse

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

                Toast.makeText(getBaseContext(),"Connection Faild",
                        Toast.LENGTH_LONG).show();
            } // on Failure
        }); // select interests from backend
// Retrofit


        gender = gender1;

        if(gender1.equals("male"))
            radioGroupGender.check(R.id.update_male);
        else
            radioGroupGender.check(R.id.update_female);

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.update_male:
                        // do operations specific to this selection
                       /* Toast.makeText(getBaseContext(), "male",
                                Toast.LENGTH_SHORT).show();*/
                        gender = "male";
                        break;
                    case R.id.update_female:
                        // do operations specific to this selection
                       /* Toast.makeText(getBaseContext(), "female",
                                Toast.LENGTH_SHORT).show();*/
                        gender = "female";
                        break;

                }
            }
        });




        ed_firstName.setText(firstName1);
        ed_lastName.setText(lastName1);
        ed_Email.setText(email1);
        ed_MobileNumber.setText(mobileNumber1);


    } // onCreate function

    public void shoppingCart(View view ){

        //TODO shopping cart action here
        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (UpdateProfile.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }
    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items

            for (int i = 0; i < selected.length; i++)
            {
                if (selected[i])
                {
                    chosenInterestList.append(adapter.getItem(i)).append(",");
                }
            }

        }
    }; // on SelectedListner To Choose Interest


    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    } // validation of email




    public void Update(View view)
    {


        try {
            int c_firstname = 0, c_lastname = 0, c_email = 0,
                    c_mobile = 0, c_emailValidation = 0, c_gender = 0, c_mobileContain = 0;

            firstName = ed_firstName.getText().toString();
            lastName = ed_lastName.getText().toString();
            email = ed_Email.getText().toString();
            mobileNumber = ed_MobileNumber.getText().toString();

            language = spinnerLanguage.getSelectedItem().toString();


            interest = chosenInterestList.toString();

            city = spinnerCity.getSelectedItem().toString();
            country = spinnerCountry.getSelectedItem().toString();


            /////// Validation

            if (firstName.equals(""))
                c_firstname = 1;
            if (lastName.equals(""))
                c_lastname = 1;
            if (mobileNumber.equals(""))
                c_mobile = 1;
            if (email.equals(""))
                c_email = 1;
            if (!isEmailValid(email))
            {
                if (email.length() != 11){
                    c_emailValidation = 1;
                }
            }

            if (gender.equals(""))
                c_gender = 1;
            if (mobileNumber.length() != 11)
                c_mobileContain = 1;




            if (c_firstname == 0 && c_lastname == 0
                    && c_email == 0 && c_mobile == 0 && c_mobileContain == 0 && c_emailValidation == 0
                    && c_gender == 0) {


                // make Retrofit connection to update

                //Retrofit
                 Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());
                UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> updateUserConnection =
                        userApi.updateUser(userID,firstName,lastName,email,gender,language,interest
                        ,mobileNumber,city,country);
                updateUserConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {

                            List<String> emailErrors = response.body().getEmail();

                            String message = response.body().getMessage();
                            Log.i("QP",message+"");
                            if(message.equals("true"))
                            {
                                Log.i("QP","true");

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("firstName",firstName );
                                editor.putString("lastName",lastName );
                                editor.putString("email",email );
                                editor.putString("gender",gender );
                                editor.putString("language",language );
                                editor.putString("interest",interest );
                                editor.putString("mobile",mobileNumber );
                                editor.putString("city",city );
                                editor.putString("country",country );
                                editor.apply();
                                Intent intent = new Intent(getBaseContext(),NavigationMenu.class);
                                startActivity(intent);
                            }
                            if (emailErrors != null) {
                                emailError = emailErrors.get(0);
                            } else {
                                emailError = "";
                            }


                            if (emailError.equals("")) {


                            }// check if email already register

                            else {
                                Toast.makeText(getBaseContext(), "Error\n" +
                                        emailError, Toast.LENGTH_LONG).show();

                            } // check if email already register

                            Log.i("QP",emailErrors+"");

                        } catch (Exception e) {
                            Log.i("QP",e.toString()+"");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(UpdateProfile.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
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

        }
        catch (Exception e)
        {
            Log.i("QP : ",e.toString()+"");
        }
            /////// Validation

    } // Update Button

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

        SearchManager searchManager = (SearchManager) UpdateProfile.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(UpdateProfile.this.getComponentName()));
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

        //  for(int i =1 ; i<= numOfCategoryItems ; i++) {



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



} // class of UpdateProfile
