package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.PersonalUser;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , GoogleApiClient.OnConnectionFailedListener{


    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private Handler handler;
    private ProgressDialog progress;
    TextView forgetpassword;
    boolean log = true;
    TextView nav_firstname , nav_lastname , nav_email , welcomeName;
    CircleImageView nav_profileimage , profileImage ;

    int id  , userID;
    String firstName , lastName , menu_email ;

    String email = "", password = "";

    Button back_loginbar, registerPersonal, registerCompany, login;
    EditText ed_email, ed_password;

    int user_id , user_coins , basketItem;
    String user_firstName , user_lastName , user_email , user_password , user_interest
            ,user_language , user_mobile , user_gender , user_joinAt , user_type
            ,user_companyName , user_companyCity , user_companyCountry
            , user_companyMobile , user_Image , user_city , user_country;
      final int RC_SIGN_IN = 2224 ;
    int check = 1 ;
    Dialog dialog ;
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F); // make animation on button

    Button  twitter, googleplus;

    GoogleApiClient mGoogleApiClient ;
    GoogleSignInOptions mGoogleSignOption ;

    @Override
    protected void onStart() {
        super.onStart();

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if (account != null){
//        Log.e("google##" , ""+account.getEmail());
//        mGoogleSignInClient.signOut();
//        }
    }

  /*  public void setUp() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        // Go to the Google API Console, open your application's
        // credentials page, and copy the client ID and client secret.
        // Then paste them into the following code.
        String clientId = "988468023398-4h781rpbgsfrc76c82ltep6n5s2ru2kp.apps.googleusercontent.com";
        String clientSecret = "dAPvRrobb15t6FndrHmd-7Fg";

        // Or your redirect URL for web based applications.
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
        String scope = "https://www.googleapis.com/auth/contacts.readonly";

        // Step 1: Authorize -->
        String authorizationUrl =
                new GoogleBrowserClientRequestUrl(clientId, redirectUrl, Arrays.asList(scope)).build();

        // Point or redirect your user to the authorizationUrl.
        System.out.println("Go to the following link in your browser:");
        System.out.println(authorizationUrl);

        // Read the authorization code from the standard input stream.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What is the authorization code?");
        String code = in.readLine();
        // End of Step 1 <--

        // Step 2: Exchange -->
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl)
                        .execute();
        // End of Step 2 <--

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        PeopleService peopleService =
                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();
        com.google.api.services.people.v1.model.Person profile = peopleService.people().get("people/me")
                .execute();
        for (PhoneNumber Phone : profile.getPhoneNumbers()){
         Log.e("google#" , Phone.getValue() )  ;
        }

    }*/
  CircularProgressButton login_btn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_navigation_menu);
        setTitle(R.string.login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();


        //mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        firstName = prefs.getString("firstName", "");
        lastName = prefs.getString("lastName", "");
        menu_email = prefs.getString("email", "");
        userID=prefs.getInt("id",0);

        forgetpassword = (TextView) findViewById(R.id.txt_forgetpassword);

       // facebook = (LoginButton) findViewById(R.id.login_button);
       // twitter = (Button) findViewById(R.id.twitter);
      //  googleplus = (Button) findViewById(R.id.googleplus);
        login = (Button) findViewById(R.id.login_btn);

        login_btn = (CircularProgressButton) findViewById(R.id.btn_id);

        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        button_login(v);
                    }
                }
        );

        dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.welcomeloginscreen);
        dialog.setTitle(R.string.welcome);


        welcomeName = (TextView)dialog.findViewById(R.id.namaOfWelcomeMessage);
        //  back_loginbar = (Button) findViewById(R.id.back_loginbar);


        registerPersonal = (Button) findViewById(R.id.bt_registerAsPersonal);


        ed_email = (EditText) findViewById(R.id.log_email);
        ed_password = (EditText) findViewById(R.id.log_paswword);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        if (bundle != null) {
            email = (String) bundle.get("email");
            password = (String) bundle.get("password");
            ed_email.setText(email);
            ed_password.setText(password);
        }

        ed_email.setText(getSharedPreferences("Auth",0).getString("email",""));
        ed_password.setText(getSharedPreferences("Auth",0).getString("pass",""));

      forgetpassword.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.speed-rocket.com/en/password/reset"));
              startActivity(browserIntent);
          }
      });
      /*  Toast.makeText(getBaseContext(), "email : " + email + " pass : " + password,
                Toast.LENGTH_LONG).show();*/
//        facebook.setReadPermissions(Arrays.asList("public_profile" , "email","picture" ));
//         facebook.registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//
//
//                         String accsesToken = loginResult.getAccessToken().getToken();
//                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//
//                                getData(object);
//                                Log.w("facebook##",response.toString());
//                            }
//                        });
//
//                        Bundle b =new Bundle() ;
//                        request.executeAsync();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });

//          findViewById(R.id.sign_in_button_google).setOnClickListener(new View.OnClickListener() {
//              @Override
//              public void onClick(View view) {
//                  Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                  startActivityForResult(signInIntent, RC_SIGN_IN);
//              }
//          });



    }

    private void getData(JSONObject obj) {
        try{
        URL pictureURL = new URL("https://graph.facebook.com/"+obj.getString("id"));
        Log.e("faceBook##" , pictureURL.toString());
        Log.e("faceBook##" , obj.getString("email"));
        Log.e("faceBook##" , obj.getString("name"));

        }catch (Exception e){

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }





    public void twitterclick(View view) {
        view.startAnimation(buttonClick);
    } // twitter click function

    public void googleplusclick(View view) {
        view.startAnimation(buttonClick);
    } // googleplus click function

    public void facebookclick(View view) {
        view.startAnimation(buttonClick);
    }// facebook click function


    public void bt_RegisterASPersonal(View view) {
        Intent intent = new Intent(getBaseContext(), RegistrationCustom.class);
        startActivity(intent);
    } // function register as personal



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(),NavigationMenu.class);
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
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        nav_firstname = (TextView) findViewById(R.id.menu_firstname);
        nav_lastname = (TextView) findViewById(R.id.menu_lastname);
        nav_email = (TextView) findViewById(R.id.menu_email);

        nav_profileimage = (CircleImageView) findViewById(R.id.nav_profileimage);

        final ImageView nav_msgBadge = (ImageView) findViewById(R.id.msg_badge);

        if (getSharedPreferences("MyPrefsFile",0).getBoolean("msg",false)) nav_msgBadge.setVisibility(View.VISIBLE);
        else nav_msgBadge.setVisibility(View.INVISIBLE);


        nav_firstname.setText(firstName);
        nav_lastname.setText(lastName);

        nav_email.setText(menu_email);


        nav_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ProfileAccount.class);
                intent.putExtra("userID",userID);
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

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);

        Menu m = nv.getMenu();

        int id = item.getItemId();

        if (id == R.id.nav_offers) {
            // Handle the camera action

            return true;

        }

         else if (id == R.id.nav_language) {
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
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(getBaseContext(), NavigationMenu.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
    public void button_login(final View view) {

        login_btn.startAnimation();

        email = ed_email.getText().toString();
        password = ed_password.getText().toString();

        if(!isEmailValid(email)&& email.length() != 11) {

                Toast.makeText(getBaseContext(), "Email or mobile number Not Vaild",
                        Toast.LENGTH_LONG).show();

            login_btn.revertAnimation();
        }
       else if(!email.equals("") && !password.equals(""))
        {
            login_btn.revertAnimation();
            if (isEmailValid(email))
                check = 1;
            else
                check = 2 ;

            login(view);
        }

        else
        {
            Toast.makeText(getBaseContext(),"Enter Email & Password",Toast.LENGTH_LONG).show();
            login_btn.revertAnimation();
        }
    } // Button Login


    private void login(final View v){

        login_btn.startAnimation();

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


        //progress.show();
        new Thread()
        {
            public void run()
            {
                // Write Your Downloading logic here
                // at the end write this.
                //Retrofit
                //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/" , getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> loginuserconnection =
                        userApi.loginuser(email, password, check);

                loginuserconnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        int code =0 ;
                            try {
                                 code = response.body().getCode();
                            }catch (Exception e){

                            }
                            if (code == 8) {
                                Intent intent = new Intent(LoginScreen.this, RegistrationCustom.class);
                                intent.putExtra("step", 2);
                                intent.putExtra("mobileNumber", getSharedPreferences("RegistrationNumber", 0).getString("number", ""));
                                startActivity(intent);
                                finish();
                            } else {
                                try {
                                    List<PersonalUser> data = response.body().getData();
                                    user_id = data.get(0).getId();
                                    user_firstName = data.get(0).getFirstName();
                                    user_lastName = data.get(0).getLastName();
                                    user_email = data.get(0).getEmail();
                                    user_gender = data.get(0).getGender();
                                    user_password = data.get(0).getPassword();
                                    user_interest = data.get(0).getInterest();
                                    user_language = data.get(0).getLanguage();
                                    user_mobile = data.get(0).getMobile();
                                    user_joinAt = data.get(0).getCreated_at();
                                    user_type = data.get(0).getType();
                                    user_companyName = data.get(0).getCompanyName();
                                    user_companyCity = data.get(0).getCompanyCity();
                                    user_companyCountry = data.get(0).getCompanyCountry();
                                    user_companyMobile = data.get(0).getCompanyMobile();
                                    user_coins = data.get(0).getSrCoin();
                                    user_Image = data.get(0).getImage();
                                    user_city = data.get(0).getCity();
                                    user_country = data.get(0).getCountry();
                                    basketItem = response.body().getBasketItem();
                                    Log.e("basketItem", "" + basketItem);
                                    user_id = data.get(0).getId();
                               /* Toast.makeText(getBaseContext(), "Connection Success" +
                                        "  email : " + email + " password : " + password +" message :"+
                                        response.body().getMessage()+"data : "+user_joinAt+" "+user_firstName+" "+user_lastName,Toast.LENGTH_LONG).show();*/


                                    if (response.body().getMessage().equals("true")) {

                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString("firstName", user_firstName);
                                        editor.putString("lastName", user_lastName);
                                        editor.putString("email", user_email);
                                        editor.putString("gender", user_gender);
                                        editor.putString("language", user_language);
                                        editor.putString("interest", user_interest);
                                        editor.putString("join", user_joinAt);
                                        editor.putString("mobile", user_mobile);
                                        editor.putString("city", user_city);
                                        editor.putString("country", user_country);
                                        editor.putString("type", user_type);
                                        editor.putString("companyName", user_companyName);
                                        editor.putString("companyCity", user_companyCity);
                                        editor.putString("companyCountry", user_companyCountry);
                                        editor.putString("companyMobile", user_companyMobile);
                                        editor.putString("profileImage", user_Image);
                                        editor.putInt("id", user_id);
                                        editor.putInt("coins", user_coins);
                                        editor.putBoolean("login", true);
                                        editor.putInt("ItemsNumber", basketItem);
                                        editor.apply();

                                        getSharedPreferences("Auth", 0).edit().putString("email", user_email).apply();
                                        getSharedPreferences("Auth", 0).edit().putString("pass", password).apply();

                                        int SPLASH_DISPLAY_LENGTH = 3000;

                                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginScreen.this, v, "transition");
                                        int reavelX = (int) (v.getX() + v.getWidth() / 2);
                                        int reavelY = (int) (v.getY() + v.getHeight() / 2);
                                        Intent intent = new Intent(getBaseContext(), NavigationMenu.class);
                                        intent.putExtra("x", reavelX);
                                        intent.putExtra("y", reavelY);
                                        ActivityCompat.startActivity(LoginScreen.this, intent, options.toBundle());
                                        finish();

                                        welcomeName.setText(user_firstName);


                                        //login_btn.revertAnimation();


                                    }


                                } catch (Exception e) {
                                    Toast.makeText(getBaseContext(), "User Not Found", Toast.LENGTH_LONG).show();
                                    Log.e("errorLogin###", e.toString());
                                    login_btn.revertAnimation();
                                }
                            }

                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        if (t instanceof IOException){
                            login_btn.revertAnimation();
                            final Dialog   noInternet = AppConfig.InternetFaild(LoginScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    login(v);
                                }
                            });
                        }
                    }
                });
                //Retrofit
            }

        }.start();


    }


    public void welcomeLoginButton(View view)
    {
        dialog.cancel();
    } // cance welcome Login Secreen

    public void continueWelcomeLoginButton(View view)
    {
        Intent intent = new Intent(getBaseContext(),NavigationMenu.class);
        startActivity(intent);
        dialog.cancel();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
