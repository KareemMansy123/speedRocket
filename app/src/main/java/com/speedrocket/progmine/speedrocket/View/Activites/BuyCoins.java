package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class BuyCoins extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);


    // menu
    String firstName , lastName , email="" , interest ,userProfileImage  ;
    boolean login ;
    int userID;
    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;
    RadioGroup coinsPackegesGroup;
    int coinsPrice ;
    int coinsQuantity ;
    //int coinPriceFromAdmin ;
    double coinsPriceCustom ;
    int coinsQuantityCustom ;
    EditText freepackegeEditeTxt ;
    TextView freePackegetxt ;
    TextView packegs_50 ;
    TextView packegs_100 ;
    TextView packegs_200 ;
    TextView packegs_400 ;
    Toast tost ;
    int userCoin;
    TextView userCoinTxt;
    RadioButton rpackegs_50;
   RadioButton  rpackegs_100;
    RadioButton rpackegs_200;
    RadioButton  rpackegs_400;
    NotificationBadge mBadge ;
    //menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buycoins_navigation_menu);

      setTitle(R.string.buyCoins);
        //menu

        mBadge = findViewById(R.id.badge);
        AppUtills.notificationBadge.setNotifecationBadgeByShared(mBadge , this);
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
        userCoin=prefs.getInt("coins",300);


        userCoinTxt = (TextView)findViewById(R.id.user_coin_txt) ;
        packegs_50 = (TextView)findViewById(R.id.price_50_txt) ;
        packegs_100 = (TextView)findViewById(R.id.price_100_txt) ;
        packegs_200 = (TextView)findViewById(R.id.price_200_txt) ;
        packegs_400 = (TextView)findViewById(R.id.price_400_txt) ;
        userCoinTxt.setText(""+userCoin);
        rpackegs_50 =(RadioButton) findViewById(R.id.coins_50);
        rpackegs_100 =(RadioButton) findViewById(R.id.coins_100);
        rpackegs_200 =(RadioButton) findViewById(R.id.coins_200);
        rpackegs_400 =(RadioButton)findViewById(R.id.coins_400);

        if (login == false) {
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Login");
            navigationView.getMenu().findItem(R.id.nav_buycoins).setVisible(false);
        }

        else if (login == true) {
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Logout");
            navigationView.getMenu().findItem(R.id.nav_buycoins).setVisible(true);
        };


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // menu
        coinsPackegesGroup = (RadioGroup)findViewById(R.id.coins_radio_group);
        coinsPackegesGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.coins_50:
                        coinsPrice = 10 ;
                        coinsQuantity = 40 ;
                        break;
                    case R.id.coins_100:
                        coinsPrice = 20 ;
                        coinsQuantity = 100 ;
                        break;
                    case R.id.coins_200:
                        coinsPrice = 50 ;
                        coinsQuantity = 300 ;
                        break;
                    case R.id.coins_400:
                        coinsPrice = 100 ;
                        coinsQuantity = 700 ;
                        break;
                    default:
                        coinsQuantity = 0 ;
                        coinsPrice = 0 ;
                        break;
                }

            }
        });

        freepackegeEditeTxt = (EditText)findViewById(R.id.free_packege_edite_txt);
        freePackegetxt = (TextView)findViewById(R.id.free_packege_txt);


     freepackegeEditeTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
         @Override
         public void onFocusChange(View view, boolean b) {
             if (b){
                 ((EditText)view).addTextChangedListener(new TextWatcher() {
                     @Override
                     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                     }

                     @Override
                     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                         try {
                             if (freepackegeEditeTxt.getText().toString() != null && !freepackegeEditeTxt.getText().toString().equalsIgnoreCase("")) {

                             int Quantity = Integer.valueOf(charSequence.toString());
                             coinsQuantityCustom = Quantity;
                             coinsPriceCustom = Quantity * .25;
                             freePackegetxt.setText("" + coinsPriceCustom);
                         }else{
                                 freePackegetxt.setText("" );
                             }
                         }catch (NumberFormatException e){
                             if (tost!=null){
                                 tost.cancel();
                                 tost=null;
                             }else{
                                 tost = Toast.makeText(getApplicationContext() , "please enter just numbers" , Toast.LENGTH_SHORT);
                                 tost.show();
                             }
                             freepackegeEditeTxt.setText("");
                         }

                     }

                     @Override
                     public void afterTextChanged(Editable editable) {

                     }
                 });
             }

         }
     });
       // getCoinsbyAdmin();
    } // onCreate function
    public void shoppingCart(View view ){

        //TODO shopping cart action here
        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (BuyCoins.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }
    public void choosePackage(View view)
    {
        view.startAnimation(buttonClick);
        if (rpackegs_50.isChecked() ||rpackegs_100.isChecked()||rpackegs_200.isChecked() ||rpackegs_400.isChecked() ){

            Intent intent = new Intent(getApplicationContext(),PaymentScreen.class);
            intent.putExtra("kind","coin");
            intent.putExtra("price",coinsPrice);
            intent.putExtra("quntity",coinsQuantity);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), R.string.chose_packege_warning , Toast.LENGTH_LONG).show();
        }

    } // button choose package

    public void freePackage(View view)
    {
        view.startAnimation(buttonClick);
             if (freepackegeEditeTxt.getText()==null || freepackegeEditeTxt.getText().toString().equalsIgnoreCase("")){
          Toast.makeText(getApplicationContext(), R.string.free_packege_warning , Toast.LENGTH_LONG).show();
              }else{
        Intent intent = new Intent(getApplicationContext(),PaymentScreen.class);
        intent.putExtra("kind","coin");
        intent.putExtra("price",coinsPriceCustom);
        intent.putExtra("quntity",coinsQuantityCustom);
        startActivity(intent);
             }
    } // button free package





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


        Picasso.with(getApplicationContext()).load("https://speed-rocket.com/upload/users/"
                +userProfileImage).
                fit().centerCrop().into(nav_profileimage);
        nav_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyCompanyProfile.class);
                intent.putExtra("userID",userID);
                startActivity(intent);


            }
        });

        MenuItem searchItem = menu.findItem(R.id.action_settings);

        SearchManager searchManager = (SearchManager) BuyCoins.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(BuyCoins.this.getComponentName()));
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


            Intent intent = new Intent(getApplicationContext(),ProductMenuList.class);
            startActivity(intent);


        }
        else if (id == R.id.nav_myProducts)
        {
            Intent intent = new Intent(getApplicationContext(), MyWinnerProducts.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_offers)
        {
            Intent intent = new Intent(getApplicationContext(),OfferMenuList.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_country) {

            return true;

        } else if (id == R.id.nav_language) {
            Intent intent = new Intent(getApplicationContext(),ChangeLanguage.class);
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
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(), NavigationMenu.class);
            startActivity(intent);
            return true;

        }  else if (id == R.id.nav_buycoins) {
            Intent intent = new Intent(getApplicationContext(), BuyCoins.class);
            startActivity(intent);
            return true;

        }
        else if (id == R.id.nav_cPanal) {
            if(userID == 0)
            {
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
            } // check if login 0 ---> user not login
            else {
                Intent intent = new Intent(getApplicationContext(), MyCompanyProfile.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
            return true;

        }    else if (id == R.id.yourOrders){
            Intent intent = new Intent(getApplicationContext(),yourOrder.class);
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
