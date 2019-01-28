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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
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

public class PaymentScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    NotificationBadge mBadge ;
    String kind ="" , paymentMethod ="" , shipping_name="" , shipping_address="" , shipping_phone="" , shipping_price;
    String   productId = "" , type = "" , amunts ="" , Prices = "" ,titles ="";

    int advertise = 0 ;
    double price ;
    RadioGroup radioGroupPaymentMethods ;
    RadioButton radioButtonCashOnDelivery ;
    LinearLayout layoutCash , layoutShipping ;
    int coinOrProduct = 0 ;

    private Handler handler;
    private ProgressDialog progress;

    TextView txt_price , txt_shipping_name  , txt_shipping_phone ,totalCostPayments ;

    Dialog purchaseDialog , dilalogConfirm ;
    Spinner address ;
    // menu
    String firstName , lastName , email="" , interest ,userProfileImage;
    boolean login ;
    int userID ;
    TextView nav_firstname , nav_lastname , nav_email , shippingInfo , shipping_cost , dialogType ,dialogTotalPrice , dialogpaymentmethod ;
    CircleImageView nav_profileimage ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;
    int coinsQuantity = 0 ;
    ArrayAdapter<String> mAdapter ;
    int maxId = 0 ;
    int cashOnDeliveryCheck ;
    String billType ="" , offerIds = "" , refNum ="";


    int ShopperTrackType  ;

    List<String> country =new ArrayList<>(),prices = new ArrayList<>();
    //menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_screen_navigation_menu);
        setTitle(R.string.payment);
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


        layoutCash = (LinearLayout) findViewById(R.id.cash_on_delivery);
        radioButtonCashOnDelivery = (RadioButton) findViewById(R.id.radio_cash);
        layoutShipping =(LinearLayout) findViewById(R.id.shiping_info);
        txt_shipping_name = (TextView) findViewById(R.id.shipping_name);
        txt_shipping_phone = (TextView) findViewById(R.id.shipping_mobile);
        totalCostPayments=findViewById(R.id.total_cost_payments);
        //txt_shipping_address = (TextView) findViewById(R.id.shiping_address);
        shippingInfo=(TextView) findViewById(R.id.shipping_info);
        address = findViewById(R.id.address);
        shipping_cost = findViewById(R.id.shipping_price);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();


        if(b!=null)
        {
            if (b.containsKey("kind"))
            {kind=b.get("kind").toString();}

            price=Double.valueOf( b.get("price").toString());
            Log.e("payment##","payment : "+price);

            if (b.containsKey("productId"))
            {
              productId=(String) b.get("productId");
              type=(String)b.get("type") ;
              amunts=(String)b.get("amunts") ;
              Prices =(String)b.get("prices");
              titles = (String)b.get("titles");
              billType=getString(R.string.bill_type_product) ;
                Log.e("payment##","productIds : "+productId);
                Log.e("payment##","types : "+type);
                Log.e("payment##","amunts : "+amunts);
            }
            if (b.containsKey("quntity"))
            { coinsQuantity=(int) b.get("quntity");
                billType=getString(R.string.coins) ;
            }
            if (b.containsKey("advertise"))
            {
                advertise = (int)b.get("advertise");
                billType=getString(R.string.advertisment);
                offerIds= (String)b.get("offerIds");
                refNum=(String)b.get("refNumber");
            }
        }

        if(kind.equals("product"))
        {
            layoutCash.setVisibility(View.VISIBLE);
            radioButtonCashOnDelivery.setVisibility(View.VISIBLE);
            layoutShipping.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
            shipping_cost.setVisibility(View.VISIBLE);
            coinOrProduct = 0 ;
            getCountiresAndPrices();

        } // if kind product show cashOnDelivery
        else
        {
            layoutCash.setVisibility(View.GONE);
            radioButtonCashOnDelivery.setVisibility(View.GONE);
            txt_shipping_name .setVisibility(View.GONE);
            txt_shipping_phone .setVisibility(View.GONE);
          // txt_shipping_address.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            shipping_cost.setVisibility(View.GONE);
            shipping_price= "0";
            shippingInfo.setVisibility(View.GONE);
            coinOrProduct=1;


        } // if kind product disappear cashOnDelivery

        radioGroupPaymentMethods = (RadioGroup) findViewById(R.id.rGPaymentMethods);

        radioGroupPaymentMethods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i)
                {

                    case R.id.radio_cash :
                        paymentMethod = "cash on delivery";
                        break;
                    case R.id.rbbanktransport :
                        paymentMethod = "Bank Transport";
                        break;
                    case R.id.rbfawry :
                        paymentMethod = "fawry";
                        break;

                }
            }
        }); //radioGroupPaymentMethod


        purchaseDialog = new Dialog(this);
        purchaseDialog.setContentView(R.layout.dialog_continue_buy);

        dilalogConfirm = new Dialog(this);
        dilalogConfirm = new Dialog(this);
        dilalogConfirm.setContentView(R.layout.dialog_confirm_order);

        dialogType = dilalogConfirm.findViewById(R.id.confirm_dialog_type);
        dialogpaymentmethod = dilalogConfirm.findViewById(R.id.confirm_dialog_wiht);
        dialogTotalPrice=dilalogConfirm.findViewById(R.id.confirm_dialog_cost);

        txt_price = (TextView) findViewById(R.id.pricePayment);
        txt_price.setText(price+" EGP");

              mAdapter =new ArrayAdapter<String>(
        this, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        address.setAdapter(mAdapter);
        mAdapter.add(getString(R.string.country_choose));
             totalCostPayments.append(" "+price);

          address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  if (i == 0 ){
                      shipping_address = "";
                      shipping_price = "0";

                  }else{
                      shipping_address = country.get(i-1);
                      shipping_price = prices.get(i-1);

                      Log.e("shipping" , "address : " + shipping_address);
                      Log.e("shipping" , "price : "+ shipping_price);


                      shipping_cost.setText("+ "+shipping_price+" EGP");
                      totalCostPayments.setText(getString(R.string.total_cost)+price+" + "+shipping_price);
                  }
              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {
                  shipping_address = "";
                  shipping_price = "0";
              }
          });






    } // onCreate function

    private void getCountiresAndPrices(){
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        final Handler handler ;

        if (!((Activity) this).isFinishing()) {
            //show dialog
            progress.show();
        }

        handler =    new Handler( ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        new Thread(){
            public void run()
            {
                Log.e("appUTILS " ,"in " );
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                final ApiConfig userApi = retrofit.create(ApiConfig.class);
                final Call<ResultModel> getCounters =
                        userApi.getCountriesAndPrices();

                getCounters.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        country = response.body().getCoutries();
                        prices = response.body().getPrices();
                        Log.e("country##" , ""+country.size());
                        Log.e("country##" , ""+prices.size());

                        for (int i = 0 ; i < country.size() ; i++){
                            Log.e("country##" , "countery  :  "+country.get(i)+ "price :  "+prices.get(i) );
                            mAdapter.add(country.get(i));
                            mAdapter.notifyDataSetChanged();
                        }

                            //show dialog
                            progress.dismiss();


                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(PaymentScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getCountiresAndPrices();
                                }
                            });
                        }
                    }
                });




            }

        }.start();


    }



    public void getMaxOrderId(){

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        final Handler handler ;

        if (!((Activity) this).isFinishing()) {
            //show dialog
            progress.show();
        }

        handler =    new Handler( ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };


        new Thread(){
            public void run()
            {

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/" , getApplicationContext());


                final ApiConfig userApi = retrofit.create(ApiConfig.class);
                final Call<ResultModel> getMaxId =
                        userApi.getMaxOrderId();

                getMaxId.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try{

                            Log.e("maxId##" , ""+response.body().getMaxOrdersId());
                            maxId = response.body().getMaxOrdersId() + 1;
                            progress.dismiss();
                            refNum ="P"+(maxId)+userID ;

                        }catch(Exception e) {
                            Toast.makeText(getBaseContext() , "there is some server problems ..?" , Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(PaymentScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getMaxOrderId();
                                }
                            });
                        }
                    }
                });
            }
        }.start();

    }
    public void shoppingCart(View view ){

        //TODO shopping cart action here
        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (PaymentScreen.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }

    public void confirmOrder(View view ){
        dilalogConfirm.dismiss();
        Intent intent = new Intent (PaymentScreen.this , fawry.class);
        intent.putExtra("customerName" , shipping_name);
        intent.putExtra("customerPhone" , shipping_phone);
        intent.putExtra("customerAddress" , shipping_address);
        intent.putExtra("price" , price);
        if (productId != "" )
        {intent.putExtra("productId" , productId);
            intent.putExtra("type",type);
            intent.putExtra("amunts",amunts);
            intent.putExtra("prices" , Prices);
            intent.putExtra("shippingCoast" ,Double.valueOf( shipping_price));
            intent.putExtra("titles" , titles);
        }
        if (coinsQuantity != 0 )
        {intent.putExtra("coinsQuantity" ,coinsQuantity);}
        if (advertise != 0 )
        {intent.putExtra("advertise" ,1);
          intent.putExtra("offerIds" , offerIds);
          intent.putExtra("refNumber" ,refNum);
        }

        startActivity(intent);

    }

    public void cancelOrder(View view){

        dilalogConfirm.cancel();
    }
    public void paymentPlaceOrder(View view)
    {
        shipping_name = txt_shipping_name.getText().toString();
        shipping_phone = txt_shipping_phone.getText().toString();

        if((!shipping_name.equals("") && !shipping_phone.equals("") && !shipping_address.equals(""))||coinOrProduct==1) {
            if (paymentMethod.equals("Bank Transport")) {
                Intent intent = new Intent(getBaseContext(), BankTransport.class);
                intent.putExtra("type*" , billType);
                intent.putExtra("coast*" , price+" + "+shipping_price);
                intent.putExtra("customerName" , shipping_name);
                intent.putExtra("customerPhone" , shipping_phone);
                intent.putExtra("customerAddress" , shipping_address);
                intent.putExtra("price" , price);
                if (productId != "" )
                {intent.putExtra("productId" , productId);
                    intent.putExtra("type",type);
                    intent.putExtra("amunts",amunts);
                    intent.putExtra("prices" , Prices);
                    intent.putExtra("shippingCoast" ,Double.valueOf( shipping_price));
                    intent.putExtra("titles" , titles);
                }
                if (coinsQuantity != 0 )
                {intent.putExtra("coinsQuantity" ,coinsQuantity);}
                if (advertise != 0 )
                {
                    intent.putExtra("advertise" ,1);
                    intent.putExtra("offerIds" , offerIds);
                    intent.putExtra("refNumber" ,refNum);
                }
                startActivity(intent);
            } // id user choose to buy with card

            else if (paymentMethod.equals("cash on delivery")) {
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.cash_not_avaliable);




                final Button btn = dialog.findViewById(R.id.ok);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });


                 final DatabaseReference cashOnDelivery = FirebaseDatabase.getInstance().getReference("payment") ;
                   cashOnDelivery.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           cashOnDeliveryCheck= dataSnapshot.child("cashOnDelivery").getValue(Integer.class);
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

                   if (cashOnDeliveryCheck == 1 ){
                       getMaxOrderId();
                       purchaseDialog.show();

                   }else {
                       dialog.show();
                   }


            }
            else  if (paymentMethod.equalsIgnoreCase("fawry")){

                if (!((Activity) this).isFinishing()) {
                    //show dialog
                    dilalogConfirm.show();

                }

                dialogTotalPrice.setText(price+" + "+shipping_price);
                dialogpaymentmethod.setText(getResources().getString(R.string.fawry));
                dialogType.setText(billType);


            }
        } // if


        else
        {
            Toast.makeText(getBaseContext(),"Complete Fields",Toast.LENGTH_LONG).show();
        } // else

    } // button paymentPlaceOrder

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

        SearchManager searchManager = (SearchManager) PaymentScreen.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(PaymentScreen.this.getComponentName()));
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


            if (m.findItem(R.id.nav_logout).getTitle().toString().equals("Logout"))  {
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

    public void continuePurchase(View view)
    {


        continueProchase();


    } // continue Button

    private void continueProchase(){
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
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> cashOnDeliveryconnection =
                        userApi.orderCashOnDelivery(productId, userID,Prices, type,amunts,Double.valueOf(shipping_price) ,shipping_name, shipping_phone, shipping_address , refNum);
                cashOnDeliveryconnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {

                            Toast.makeText(getBaseContext(), "Done", Toast.LENGTH_LONG).show();
                            progress.dismiss();
                            Intent intent = new Intent(getBaseContext(), NavigationMenu.class);
                            startActivity(intent);

                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Connection Error", Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(PaymentScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    continueProchase();
                                }
                            });
                        }

                    }
                });
                //Retrofit


            }

        }.start();


    }


    public void cancelPurchase(View view)
    {
        purchaseDialog.cancel();
    } // cancel button



} // class of PaymentScreen
