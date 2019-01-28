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

import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Control.ProductAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.Product;
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

public class ProductsScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;

    int companyId;

    List<Product> productList ;

    String enTitle , enDescription ,arTitle,arDiscription, productImage , companyName;
    int productId ;
    double price ;
    int  productQty ;

    Product Product;
    private List<Product> productList1 = new ArrayList<>();
    Product product ;
    private List<Product> productListFilter = new ArrayList<>();
    private ProgressDialog progress;
    private Handler handler;

    int categoryId=0 , ProductId= 0;

    // menu
    String firstName , lastName , email="" , interest ,userProfileImage  ;
    boolean login ;
    int userID;
    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;

    //menu
    NotificationBadge mBadge ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_screen_navigation_menu);
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


        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            if (b.containsKey("productId")){
                ProductId = (int)b.get("productId");
                Log.e("produtScreen" , "productId : " + ProductId);
            }else{
            categoryId=(int)b.get("categoryId");
            Log.e("produtScreen##" , "categoryId : "+categoryId );
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.postlist_product);

        mAdapter = new ProductAdapter(productList1, ProductsScreen.this,mBadge);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (categoryId != 0)
        {
            getProductsByCategoryId(categoryId);

        }

        else if (ProductId != 0){
          getJustProduct(ProductId);
        }else
        getProducts();

    } // onCreate function

    public  void getJustProduct(final int ProductId){

        Log.e("produtScreen##" , "getJustProduct");
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

        new Thread() {
            public void run() {
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModel> getJustoneProduct = userApi.getproductById(ProductId);

                getJustoneProduct.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try
                        {


                            productList = response.body().getProducts();
                            for(int i =0 ; i< productList.size(); i++)
                            {

                                enTitle = productList.get(i).getEn_title();
                                enDescription = productList.get(i).getEn_discription();
                                arTitle = productList.get(i).getAr_title();
                                arDiscription = productList.get(i).getAr_discription();
                                productImage = productList.get(i).getImage();
                                productId = productList.get(i).getId();
                                companyName = productList.get(i).getCompanyName();
                                price = productList.get(i).getPrice();
                                productQty = productList.get(i).getProductQty();
                                product = new Product(productId,
                                        enTitle,
                                        enDescription,
                                        productImage,
                                        companyName,
                                        price ,
                                        arTitle,
                                        arDiscription,
                                        productQty
                                        );


                                productList1.add(product);
                                mAdapter.notifyDataSetChanged();

                            }// or loop


                            progress.dismiss();

                        } // try
                        catch (Exception e)
                        {
                            Toast.makeText(getBaseContext(),"Connection Success\n" +
                                            "Exception Home Page\n"+e.toString(),
                                    Toast.LENGTH_LONG).show();
                            progress.dismiss();

                        } // catch



                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(ProductsScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getJustProduct(ProductId);
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

        Intent intent = new Intent (ProductsScreen.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }
    public  void getProducts ()
    {
        Log.e("produtScreen##" , "getProducts");
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
                //Retrofit
                //Retrofit
                Retrofit retrofit =AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModel> getProductConnection = userApi.getProducts();

                getProductConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try
                        {


                            productList = response.body().getProducts();
                            for(int i =0 ; i< productList.size(); i++)
                            {

                                enTitle = productList.get(i).getEn_title();
                                enDescription = productList.get(i).getEn_discription();

                                arTitle = productList.get(i).getAr_title();
                                arDiscription = productList.get(i).getAr_discription();

                                productImage = productList.get(i).getImage();
                                productId = productList.get(i).getId();
                                price = productList.get(i).getPrice();
                                productQty = productList.get(i).getProductQty();
                                SharedPreferences shared = getSharedPreferences("MyPref" , Context.MODE_PRIVATE);
                                if (shared.getString("langa","nothing").equalsIgnoreCase("ar")){
                                    product = new Product(arTitle,arDiscription,productImage,productId,price,productQty);
                                }else {
                                    product = new Product(enTitle, enDescription, productImage, productId, price,productQty);
                                }

                                productList1.add(product);
                                mAdapter.notifyDataSetChanged();
                            }// or loop


                            progress.dismiss();

                        } // try
                        catch (Exception e)
                        {
                            Toast.makeText(getBaseContext(),"Connection Success\n" +
                                            "Exception Home Page\n"+e.toString(),
                                    Toast.LENGTH_LONG).show();
                            progress.dismiss();

                        } // catch



                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

               /* Toast.makeText(getBaseContext(),"Connection Faild",
                        Toast.LENGTH_LONG).show();*/
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(ProductsScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getProducts();
                                }
                            });
                        }

                    } // on Failure
                });

// Retrofit

            }

        }.start();


    } // getProducts function


    public void getProductsByCategoryId(final int categoryId)
    {
        Log.e("produtScreen##" , "getProductsByCategory");
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
                //Retrofit
                //Retrofit
                Retrofit retrofit =AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModel> getProductConnection = userApi.chooseCategory(categoryId);

                getProductConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try
                        {

                           Log.e("produtScreen##" , "categoryId : " + categoryId);
                            productList = response.body().getProducts();
                            for(int i =0 ; i< productList.size(); i++)
                            {

                                enTitle = productList.get(i).getEn_title();
                                enDescription = productList.get(i).getEn_discription();
                                arTitle = productList.get(i).getAr_title();
                                arDiscription = productList.get(i).getAr_discription();
                                productImage = productList.get(i).getImage();
                                productId = productList.get(i).getId();
                                companyName = productList.get(i).getCompanyName();
                                price = productList.get(i).getPrice();
                                productQty = productList.get(i).getProductQty();
                                product = new Product(productId,
                                        enTitle,
                                        enDescription,
                                        productImage,
                                        companyName,
                                        price,
                                        arTitle,
                                        arDiscription,
                                        productQty
                                        );


                                productList1.add(product);
                                mAdapter.notifyDataSetChanged();
                            }// or loop


                            progress.dismiss();

                        } // try
                        catch (Exception e)
                        {
                            Toast.makeText(getBaseContext(),"Connection Success\n" +
                                            "Exception Home Page\n"+e.toString(),
                                    Toast.LENGTH_LONG).show();
                            progress.dismiss();

                        } // catch



                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

               /* Toast.makeText(getBaseContext(),"Connection Faild",
                        Toast.LENGTH_LONG).show();*/

                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(ProductsScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getProductsByCategoryId(categoryId);
                                }
                            });
                        }

                    } // on Failure
                });

// Retrofit

            }

        }.start();
    } // function of getProductsByCategoryId

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
         Menu menue = menu ;
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
        MenuItem searchItem = menue.findItem(R.id.action_settings);

        SearchManager searchManager = (SearchManager) ProductsScreen.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.isEmpty()){
                        mAdapter = new ProductAdapter(productList1 , ProductsScreen.this , mBadge);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);

                    }else {
                        productListFilter.clear();
                        for (int i = 0; i < productList1.size(); i++) {
                            Product = productList1.get(i);
                            if (Product.getEn_title().contains(newText)) {
                                productListFilter.add(Product);
                            } else {
                                if (Product.getAr_title().contains(newText)) {
                                    productListFilter.add(Product);
                                }
                            }
                            mAdapter = new ProductAdapter(productListFilter, ProductsScreen.this, mBadge);
                            mAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(mAdapter);

                        }
                    }
                    return true;
                }
            });
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ProductsScreen.this.getComponentName()));
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


} // class of ProductsScreen
