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
import com.speedrocket.progmine.speedrocket.Control.BankAccountsAdapter;
import com.speedrocket.progmine.speedrocket.Control.MyCompanyAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.BankAccount;
import com.speedrocket.progmine.speedrocket.Model.Company;
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

public class MyCashScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,
        BankAccountsAdapter.bankInterface {



    //menu
    NotificationBadge mBadge ;
    String firstName , lastName , email="" , interest ,userProfileImage  ;
    boolean login ;
    int userID;
    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;

    int BankId =0 ;

    int netCash=0 , pendingCash=0 , totalCash=0 ;
    //menu
    Dialog companyListDialog;
    String companyName="" , companyLogo ="" ,companyArName ="" ;
     Dialog  myBankAccountsDialog , confirmCompany ;
    int id   , userID1 , companyId;
    private ProgressDialog progress;
    private Handler handler;
    Company company;
    private List<Company> companyList1 = new ArrayList<>();
    List<Company> CList = new ArrayList<>();
    List<BankAccount> bankList;

    BankAccountsAdapter mAdapter ;
    RecyclerView recyclerView;
    List <BankAccount> bankAccountList = new ArrayList<>();
    BankAccount bankAccount;

    private RecyclerView recyclerViewDialog;
    private MyCompanyAdapter mAdapterDialog;
    TextView txt_totalCash , txt_netCash , txt_pendingCash , txt_amountCash;

    int swift , bankId ;
    String bankName , bankAddress  , bankAcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cash_navigation_menu);
        setTitle(R.string.myCash);
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


        myBankAccountsDialog = new Dialog(this);
        myBankAccountsDialog.setContentView(R.layout.dialog_with_my_bank_accounts);

        txt_amountCash = (TextView) myBankAccountsDialog.findViewById(R.id.txt_amountOfCash);


        recyclerView = (RecyclerView) myBankAccountsDialog.findViewById(R.id.recycleViewBankAccounts);

        confirmCompany = new Dialog(this);
        confirmCompany.setContentView(R.layout.dialog_confirm_company);


        mAdapter = new BankAccountsAdapter(getBaseContext() , bankAccountList ,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(llm);


        txt_totalCash = (TextView) findViewById(R.id.totalCash);
        txt_netCash = (TextView) findViewById(R.id.netCash);
        txt_pendingCash = (TextView) findViewById(R.id.pendingCash);

        myCashAll();

        companyListDialog = new Dialog(this);
        companyListDialog.setContentView(R.layout.dialogue_with_my_company);


        recyclerViewDialog = (RecyclerView) companyListDialog.findViewById(R.id.recycleViewCompany);

        mAdapterDialog = new MyCompanyAdapter(companyList1, MyCashScreen.this);
        RecyclerView.LayoutManager mLayoutManagerDialog = new LinearLayoutManager(getBaseContext());
        //llm.setAutoMeasureEnabled(false);
        recyclerViewDialog.setLayoutManager(mLayoutManagerDialog);
        recyclerViewDialog.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDialog.setAdapter(mAdapterDialog);
        LinearLayoutManager lm = new LinearLayoutManager(getBaseContext());

    } // onCreate function
        public void orders(View view ){
            getCompanies(2);
         }

          public void confirmActivity(View v){
              getCompanies(3);
            }


    public  void  getCompanies(final int check)
    {

        companyList1.clear();
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
                //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModel> getCompanyConnection = userApi.getCompany(userID);

                getCompanyConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try
                        {

                            Log.i("QP",userID1+"");
                            CList = response.body().getCompanies();

                            for(int i = 0 ; i <CList.size() ; i++)
                            {
                                companyId = CList.get(i).getId();
                                companyName = CList.get(i).getEn_name();
                                companyLogo = CList.get(i).getLogo();
                                companyArName = CList.get(i).getAr_name();
                                int categoryId = CList.get(i).getCategoryId();

                                company = new Company(companyId,categoryId ,companyName,companyArName,companyLogo,check);

                                companyList1.add(company);
                                mAdapterDialog.notifyDataSetChanged();

                            }
                            companyListDialog.show();
                            progress.dismiss();
                        } // try
                        catch (Exception e)
                        {
                   /* Toast.makeText(getBaseContext(),"Connection Success\n" +
                                    "Exception Navigation menu\n"+e.toString(),
                            Toast.LENGTH_LONG).show();*/progress.dismiss();
                            Log.e("error" , e.toString());

                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(MyCashScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getCompanies(check);
                                }
                            });
                        }

                    } // on Failure
                });


            }

        }.start();

// Retrofit
    }

    public void shoppingCart(View view ){

        //TODO shopping cart action here
        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (MyCashScreen.this , shoppingCaretActivity.class);
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

        SearchManager searchManager = (SearchManager) MyCashScreen.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MyCashScreen.this.getComponentName()));
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

    public void addBankAccountButton(View view)
    {
       Intent intent = new Intent(getBaseContext(),AddBankAccount.class);
       startActivity(intent);
    } // addBankAccountButton function
    public void sendRequestForCash(View view)
    {
        int amount = 0 ;
         try{
         amount =Integer.parseInt(txt_amountCash.getText().toString());
         }catch (Exception e){
             Toast.makeText(getBaseContext() , "please enter just numbers " , Toast.LENGTH_LONG).show();
         }
         final int amount2 = amount ;
        Log.i("QP","amount : "+amount +"\n net : "+ netCash);
        if(netCash < amount)
    {
        Toast.makeText(getBaseContext(),"max amount can withdrawn : "+netCash,
                Toast.LENGTH_LONG).show();
    }else {
            sendCashRequest(amount2);
        }
    } // send on dialog

    public void sendCashRequest(final int amount2){

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
        new Thread() {
            @Override
            public void run() {

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());
                final UserApi userApi = retrofit.create(UserApi.class);
                final Call<ResultModel> saveRequest = userApi.saveRequest(userID, BankId, amount2);

                saveRequest.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        Toast.makeText(getBaseContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(MyCashScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    sendCashRequest(amount2);
                                }
                            });
                        }
                    }
                });


            }
        }.start();

    }


    public void cancelMyCompanyList(View view)
    {
        myBankAccountsDialog.cancel();
        companyListDialog.cancel();
    } // cancel dialog

    public void requestCashButton(View view)
    {

        isConfirmed();

    } // requestCashButton function
         private void isConfirmed (){
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
               new Thread(){

                   @Override
                   public void run() {

                       Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());
                       final UserApi userApi = retrofit.create(UserApi.class);

                       Call<ResultModel> isConfirm = userApi.isConfirmed(userID);
                       isConfirm.enqueue(new Callback<ResultModel>() {
                           @Override
                           public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                               if (response.body().isConfirmed()){
                                   progress.dismiss();
                                   myBankAccountsDialog.show();
                                   getBankAccounts();
                               }else {
                                   progress.dismiss();
                                  confirmCompany.show();
                               }

                           }

                           @Override
                           public void onFailure(Call<ResultModel> call, Throwable t) {
                               if (t instanceof IOException){
                                   final Dialog   noInternet = AppConfig.InternetFaild(MyCashScreen.this);
                                   final Button btn = noInternet.findViewById(R.id.Retry);
                                   noInternet.show();
                                   btn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           noInternet.cancel();
                                           isConfirmed();
                                       }
                                   });
                               }
                           }
                       });

                   }
               }.start();

          }

    public  void getBankAccounts ()
    {
        bankAccountList.clear();
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
                //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModel> getBankAccountsWithUserIdConnection = userApi.getBankAccountWithUserId(userID);

                getBankAccountsWithUserIdConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try {

                            bankList = response.body().getBanks();

                            for(int i =0 ; i < bankList.size() ; i++)
                            {
                                swift = bankList.get(i).getSwift();
                                bankId = bankList.get(i).getId();
                                bankAddress = bankList.get(i).getBankAddress();
                                bankName = bankList.get(i).getName();
                                bankAcount = bankList.get(i).getBankAccount();
                                 Log.e("bankAccount#" , "name  : " + bankName);
                                Log.e("bankAccount#" , "bankID  : " + bankId);
                                Log.e("bankAccount#" , "bankAddress  : " + bankAddress);
                                Log.e("bankAccount#" , "bankAccount  : " + bankAccount);
                                Log.e("bankAccount#" , "swift  : " + swift);
                                bankAccount = new BankAccount(swift,bankId,bankAcount,bankAddress,bankName ,0);

                                bankAccountList.add(bankAccount);
                                mAdapter.notifyDataSetChanged();

                            }

                            progress.dismiss();
                        } // try

                        catch (Exception e) {
                   /* Toast.makeText(getBaseContext(),"Connection Success\n" +
                                    "Exception Navigation menu\n"+e.toString(),
                            Toast.LENGTH_LONG).show();*/
                            progress.dismiss();

                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(MyCashScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getBankAccounts();
                                }
                            });
                        }

                    } // on Failure
                });


            } }.start();

// Retrofit
    } // function of getBankAccounts


    public  void myCashAll()
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
        new Thread() {
            public void run() {
        //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

        final UserApi userApi = retrofit.create(UserApi.class);
        Call<ResultModel> getMyCashConnection =
                userApi.getMyCash(userID);

                getMyCashConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                try {

                    netCash = response.body().getNet();
                    pendingCash = response.body().getPending();

                    totalCash = netCash + pendingCash;
                    txt_totalCash.setText(totalCash+" L.E");
                    txt_netCash.setText(netCash+" L.E");
                    txt_pendingCash.setText(pendingCash+" L.E");
                    progress.dismiss();

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Connection Error\n"+e.toString()
                            ,Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                if (t instanceof IOException){
                    final Dialog   noInternet = AppConfig.InternetFaild(MyCashScreen.this);
                    final Button btn = noInternet.findViewById(R.id.Retry);
                    noInternet.show();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noInternet.cancel();
                            myCashAll();
                        }
                    });
                }

            }
        });

            } }.start();
        //Retrofit
    }

    public  void myCashDay()
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
        new Thread() {
            public void run() {
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> mycashConnection =
                        userApi.getMyCashDay(userID);

                mycashConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {

                            netCash = response.body().getNet();
                            pendingCash = response.body().getPending();

                            totalCash = netCash + pendingCash;
                            txt_totalCash.setText(totalCash+" L.E");
                            txt_netCash.setText(netCash+" L.E");
                            txt_pendingCash.setText(pendingCash+" L.E");
                            progress.dismiss();

                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Connection Error\n"+e.toString()
                                    ,Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(MyCashScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    myCashDay();
                                }
                            });
                        }

                    }
                });

            } }.start();
        //Retrofit
    }
    public  void myCashMonth()
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
        new Thread() {
            public void run() {
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> mycashConnection =
                        userApi.getMyCashMonth(userID);

                mycashConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {

                            netCash = response.body().getNet();
                            pendingCash = response.body().getPending();

                            totalCash = netCash + pendingCash;
                            txt_totalCash.setText(totalCash+" L.E");
                            txt_netCash.setText(netCash+" L.E");
                            txt_pendingCash.setText(pendingCash+" L.E");
                            progress.dismiss();

                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Connection Error\n"+e.toString()
                                    ,Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(MyCashScreen.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    myCashMonth();
                                }
                            });
                        }





                    }
                });

            } }.start();
        //Retrofit
    }
    public void dayReport(View view)
    {
         myCashDay();
    } // button day report

    public void monthReport(View view)
    {
          myCashMonth();
    } // button month report

    public void allReport(View view)
    {
        myCashAll();
    } //  button all report


    @Override
    public void checkRadioButton(int id, int itemPosition) {
        for (int i =0 ; i < bankAccountList.size() ; i++){
            if (bankAccountList.get(i).getChecked()==1){
                bankAccountList.get(i).setChecked(0);
            }
        }
        bankAccountList.get(itemPosition).setChecked(1);
        mAdapter= new BankAccountsAdapter(getBaseContext() , bankAccountList , this);
        recyclerView.setAdapter(mAdapter);

        BankId = id ;
        Log.e("bankId##" , ""+BankId);
    }
} // class of MyCashScreen
