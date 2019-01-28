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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Control.BasketAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.BasketItem;
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


public class shoppingCaretActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , BasketAdapter.onClickListener{

    Menu menu;

    int numbers = 0  , qty = 0 ;
    NavigationView navigationView;

    public static  boolean log  ;

    String firstname = "", lastname = "", password = "", confirmpassword = "",
            email = "", gender = "", language = "", interest = "",
            personoalmobile = "", persontype = "", companyInterest = "", city = "", country = "";
    TextView nav_firstname, nav_lastname, nav_email , totalCost ;
    CircleImageView nav_profileimage, profileImage;

    String firstName , lastName  ,userProfileImage  ;
    boolean login ;
    int userID;
    List<BasketItem> Basket1 = new ArrayList<>();

    List<Integer> amonts = new ArrayList<>();
     RecyclerView recyclerView ;

     BasketAdapter mAdapter ;

     BasketItem item ;
     double totalPrice ;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_navigation_menu);
        setTitle(getString(R.string.shooping_cart));

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
        Log.w("login##" , ""+login);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        totalCost = findViewById(R.id.total_cost);



        recyclerView = (RecyclerView) findViewById(R.id.basket_products);
        mAdapter = new BasketAdapter( shoppingCaretActivity.this,Basket1 , this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        if (userID== 0){

            Toast.makeText(getBaseContext() , R.string.login_first , Toast.LENGTH_LONG).show();
            Intent intent = new Intent(shoppingCaretActivity.this , NavigationMenu.class);
            startActivity(intent);
            finish();


        }else {
            getdata();
        }
        //Log.e("appUtils" , Basket.get(0).getAr_title());
    }
      public void addProductsButton(View view) {
        if (login){
          if (Basket1.size() == 0) {

              Toast.makeText(getBaseContext(), "you must have at least one Item ", Toast.LENGTH_LONG).show();
          } else {
              String productsId ="" ,type = "" , amunts ="" , prices ="", titles=""  ;
              int restDayes =1 ;
              Intent intent = new Intent(view.getContext(), PaymentScreen.class);


              for (int i =0 ; i < Basket1.size() ; i++){
                  restDayes = 1 ;
                  if (Basket1.get(i).getType() == 1){
                      restDayes = 7 - Basket1.get(i).getDays_remain();
                      Log.e("restDayes##" , "  : " +restDayes);
                  }
                  if (restDayes > 0) {
                      if (i==Basket1.size()-1){

                          productsId +=  String.valueOf(Basket1.get(i).getId()) ;
                          type +=  String.valueOf(Basket1.get(i).getType());
                          amunts +=  String.valueOf(amonts.get(i));

                          prices +=  String.valueOf(Basket1.get(i).getPrice());
                          titles +=  Basket1.get(i).getEn_title();
                      }else {
                          productsId += String.valueOf(Basket1.get(i).getId()) + ",";
                          type += String.valueOf(Basket1.get(i).getType()) + ",";
                          amunts += String.valueOf(amonts.get(i)) + ",";

                          prices += String.valueOf(Basket1.get(i).getPrice()) + ",";
                          titles += Basket1.get(i).getEn_title() + ",";
                      }
                  }
              }
              Log.w("amunts  :  " , ""+amunts);
              Log.e("productsId  : " , ""+productsId);
              Log.e("types  :  " , ""+type);
              intent.putExtra("kind", "product"); // kind may be product or coin
              intent.putExtra("price", totalPrice);
              intent.putExtra("productId", productsId);
              intent.putExtra("type",type);
              intent.putExtra("amunts",amunts);
              intent.putExtra("prices",prices);
              intent.putExtra("offer", 1);
              intent.putExtra("titles" , titles);
              view.getContext().startActivity(intent);
          }
        }else {
            Toast.makeText(getBaseContext() , "you must login first " , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(shoppingCaretActivity.this , LoginScreen.class);
            startActivity(intent);
            finish();
        }
      }
    public void getdata(){

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
                  if (msg.what == 0){
                      totalCost.setText(R.string.total_cost1);
                      totalCost.append(" "+totalPrice);

                  }
              }
          };

        new Thread(){
            public void run()
            {
                Log.e("appUTILS " ,"in " );
                 Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);
                final Call<ResultModel> getBasketItems =
                        userApi.getbasketProduct(userID);
                getBasketItems.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        List<BasketItem>  Basket = response.body().getBasketItems();
                        totalPrice =Double.valueOf( response.body().getData_1());

                        Log.e("appUTILS " ,"" +totalPrice);

                        for(int i =0 ; i< Basket.size(); i++)
                        {
                            int     o_id=Basket.get(i).getId();
                            int     o_userid=Basket.get(i).getShopper_id();
                            int     o_item_id = Basket.get(i).getItem_id();
                            String  o_price=Basket.get(i).getPrice();
                            String  o_description=Basket.get(i).getDescription_en();
                            String  o_image=Basket.get(i).getImage();
                            int     o_companyId = Basket.get(i).getCompany_id();
                            String  o_companyName = Basket.get(i).getCompany_name();
                            String  o_ar_companyName = Basket.get(i).getCompany_name_ar();
                            String  o_ar_Description = Basket.get(i).getDescription_ar();
                            String  o_ar_title = Basket.get(i).getAr_title() ;
                            String  o_en_title = Basket.get(i).getEn_title() ;
                            int     o_type = Basket.get(i).getType();
                            int     day_remain = Basket.get(i).getDays_remain();
                      /*int*/       numbers = Basket.get(i).getNumber();
                      /*int*/       qty = Basket.get(i).getItem_qty();


                            Log.e("apputils" , "" +o_ar_Description);
                            Log.e("apputils" , "" +o_description);

                            item = new BasketItem(o_id ,o_companyId
                                    ,o_item_id,
                                    o_userid ,
                                    o_type ,
                                    o_en_title,
                                    o_ar_title,
                                    o_companyName,
                                    o_ar_companyName,
                                    o_price,
                                    o_image ,
                                    o_description,
                                    o_ar_Description ,
                                    day_remain,
                                    numbers,
                                    qty);


                            Basket1.add(item);
                            amonts.add(1);
                            Log.e("apputils" , "basket " +Basket.get(i).getDescription_en());
                             mAdapter.notifyDataSetChanged();


                /*    // firebase
                         String sId = String.valueOf(o_id);
                      DatabaseReference mDatabase;
                      mDatabase = FirebaseDatabase.getInstance().getReference();
                      mDatabase.child("offers").child(sId).setValue(offer);

                         //firebase*/
                            if (Basket.isEmpty()){
                                Log.e("appUTILS" ,"basket is empty ");
                            }


                            //  mAdapter.notifyDataSetChanged();
                        }// or loop





                        handler.sendEmptyMessage(0);

                        if (progress != null && progress.isShowing()) {
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(shoppingCaretActivity.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                  getdata();
                                }
                            });
                        }
                    }
                });

            }

        }.start();

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

        SearchManager searchManager = (SearchManager) shoppingCaretActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(shoppingCaretActivity.this.getComponentName()));
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

       final Menu m = nv.getMenu();


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

                       // AppUtills.notificationBadge.setNotifecationBadge(mBadge,0);
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
    public void getTotalPrice (){
        double price = 0 ;
        int restDayes  ;
        for (int i = 0 ; i < Basket1.size() ; i++){
            restDayes = 1 ;
            if (Basket1.get(i).getType()==1){
                restDayes=7-Basket1.get(i).getDays_remain();
            }
            if (restDayes > 0) {
                price = price + (Double.valueOf(Basket1.get(i).getPrice()) * Double.valueOf(amonts.get(i)));
            }
        }
        totalPrice = price ;
        totalCost.setText(R.string.total_cost1);
        totalCost.append(" "+ totalPrice);

    }

    @Override
    public void onClick(int position) {
        AppUtills.notificationBadge.decreaseNumberOfItemsBuy(this,null ,Basket1.get(position).getId(),userID);
        Basket1.remove(position);

        amonts.remove(position);
        mAdapter.notifyDataSetChanged();
        getTotalPrice();
    }

    @Override
    public void onSpinnerItemSelected(int position ,int selectedNum) {
       amonts.set(position , selectedNum) ;
       getTotalPrice();
    }
}
