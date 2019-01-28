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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Control.MyProductAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.ProductsWinner;
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

public class MyWinnerProducts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    int userID , offerId , srCoin , days , states;
    double price ;


    List<ProductsWinner> productList ;

    String productTitle , productImage , productcompanyName , productDescription ;
    ProductsWinner product ;
    private List<ProductsWinner> productWinnerList = new ArrayList<>();
    private List<ProductsWinner> productWinnerFilterList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyProductAdapter mAdapter;

    private ProgressDialog progress;
    private Handler handler;


    // menu
    String firstName , lastName , email="" , interest ,userProfileImage  ;
    boolean login  ;
    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;
    int coins ;
    NotificationBadge mBadge ;


    //menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_winner_products_navigation_view);
        setTitle(R.string.products);
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
        coins = prefs.getInt("coins" , 0);
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
         mBadge = findViewById(R.id.badge);
        // menu


        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userID=prefs1.getInt("id",0);

        getProductsWinnerByUser();

        recyclerView = (RecyclerView) findViewById(R.id.myProductList);
        mAdapter = new MyProductAdapter(productWinnerList ,MyWinnerProducts.this,mBadge );
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    } // onCreate function
    public void shoppingCart(View view ){


        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (MyWinnerProducts.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }
    public  void addOfferToWinners()
    {
        //Retrofit
        new Thread(){
            @Override
            public void run() {
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());


                UserApi userApi = retrofit.create(UserApi.class);

                Call<ResultModel> addToWinnerConnection =
                        userApi.addProductOnWinners(getIntent().getExtras().getInt("userID"),getIntent().getExtras().getInt("offerID"),getIntent().getExtras().getInt("winnerCoins"));

                addToWinnerConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {
                    /*Toast.makeText(getBaseContext(), "Done Winner"
                            , Toast.LENGTH_LONG).show();*/


                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Connection error", Toast.LENGTH_LONG).show();
                        }


                    }
                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        Toast.makeText(getBaseContext(), "Connection faild\n" +
                                "" + t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                //Retrofit
            }
        }.start();


    }
public void updateUserCoin(){

    SharedPreferences.Editor editor =
            getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

    editor.putInt("coins",coins - getIntent().getExtras().getInt("winnerCoins"));
    editor.apply();
}

   public void activeOffer(View view){
       productWinnerFilterList.clear();
    mAdapter = new MyProductAdapter(productWinnerFilterList , MyWinnerProducts.this , mBadge);
    mAdapter.notifyDataSetChanged();
       recyclerView.setAdapter(mAdapter);
   for (int i = 0 ; i <productWinnerList.size() ; i++){
       product = productWinnerList.get(i);
       int rest_Days = 7 - product.getDays();
       if (product.getStates() == 0 && rest_Days >= 0) {
           productWinnerFilterList.add(product);
           mAdapter.notifyDataSetChanged();
       }
   }

   }

   public void soldOffer(View view){
       productWinnerFilterList.clear();
       mAdapter = new MyProductAdapter(productWinnerFilterList , MyWinnerProducts.this , mBadge);
       mAdapter.notifyDataSetChanged();
       recyclerView.setAdapter(mAdapter);
       for (int i = 0 ; i <productWinnerList.size() ; i++){
           product = productWinnerList.get(i);
           if (product.getStates() == 1 ) {
               productWinnerFilterList.add(product);
               mAdapter.notifyDataSetChanged();
           }
       }

   }

   public void expiredOffer(View view ){
       productWinnerFilterList.clear();

       mAdapter = new MyProductAdapter(productWinnerFilterList , MyWinnerProducts.this , mBadge);
       mAdapter.notifyDataSetChanged();
       recyclerView.setAdapter(mAdapter);

       for (int i = 0 ; i <productWinnerList.size() ; i++){
           product = productWinnerList.get(i);
           int rest_Days = 7 - product.getDays();
           if (product.getStates() == 0 && rest_Days < 0) {
               productWinnerFilterList.add(product);
               mAdapter.notifyDataSetChanged();
           }
       }
   }

   public void allOffers(View view){
       mAdapter = new MyProductAdapter(productWinnerList , MyWinnerProducts.this , mBadge);
       mAdapter.notifyDataSetChanged();
       recyclerView.setAdapter(mAdapter);
   }

    public  void getProductsWinnerByUser()
    {


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
          Intent intent = getIntent() ;
          if (intent != null) {
              if (intent.getExtras() != null) {
                  if (intent.getExtras().containsKey("fromPostDetails") && intent.getExtras().getInt("fromPostDetails") == 2224) {
                      updateUserCoin();
                      //addOfferToWinners();
                  }
              }
          }
        new Thread() {
            public void run() {


                //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

        final UserApi userApi = retrofit.create(UserApi.class);
        Call<ResultModel> winnerProductConnection =
                userApi.getProductWinnersByUser(userID);

        winnerProductConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                try {

                    productList = response.body().getWinners();

                    for(int i = 0 ; i<productList.size() ; i++)
                    {
                        offerId = productList.get(i).getOfferId();
                        srCoin = productList.get(i).getSrCoin();
                        //   created_at = productList.get(i).getCreated_at();
                        productTitle = productList.get(i).getTitle();
                        days = productList.get(i).getDays();
                        productImage = productList.get(i).getImage();
                        productcompanyName = productList.get(i).getCompanyName();
                        price = productList.get(i).getPrice();
                        productDescription = productList.get(i).getEn_description();
                        String ar_title = productList.get(i).getAr_title();
                        String ar_desc = productList.get(i).getAr_description();
                        String ar_companyName = productList.get(i).getAr_companyName();
                        states = productList.get(i).getStates();
                        int qty = productList.get(i).getQty();




                        ProductsWinner p = new ProductsWinner(userID,offerId,
                                srCoin,productTitle,ar_title,days,productcompanyName,ar_companyName,
                                productImage,price,productDescription,ar_desc,states,qty);

                        productWinnerList.add(p);
                        mAdapter.notifyDataSetChanged();

                    } // for Loop

                    /*Toast.makeText(getActivity(), "Connection Success\n"
                            ,Toast.LENGTH_LONG).show();*/

                    Log.i("QP","Done");
                    progress.dismiss();




                } catch (Exception e) {
                   /* Toast.makeText(getBaseContext(), "Connection Success\n" +
                                    "Exception"+e.toString()
                            ,Toast.LENGTH_LONG).show();*/
                    progress.dismiss();
                }


            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                progress.dismiss();
                if (t instanceof IOException){
                    final Dialog noInternet = AppConfig.InternetFaild(MyWinnerProducts.this);

                    final Button btn = noInternet.findViewById(R.id.Retry);
                    noInternet.show();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noInternet.cancel();
                            getProductsWinnerByUser();
                        }
                    });
                }



            }
        });

            }

        }.start();
        //Retrofit
    } // function getProductsWinnerByUser

    @Override
    public void onBackPressed() {
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
      Intent intent = new Intent(getBaseContext(),NavigationMenu.class);
      startActivity(intent);
      finish();
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

        SearchManager searchManager = (SearchManager) MyWinnerProducts.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MyWinnerProducts.this.getComponentName()));
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
} // class MyWinnerProducts
