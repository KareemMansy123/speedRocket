package com.speedrocket.progmine.speedrocket.View.Activites;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.BuildConfig;
import com.speedrocket.progmine.speedrocket.ChatActivity;
import com.speedrocket.progmine.speedrocket.Control.PostsAdapter;
import com.speedrocket.progmine.speedrocket.MainChatLayout;
import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.Category;
import com.speedrocket.progmine.speedrocket.Model.Offer;
import com.speedrocket.progmine.speedrocket.Model.OfferWinner;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.Model.UserInOffer;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.services.AnalyticsService;
import com.speedrocket.progmine.speedrocket.users_View_List;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class NavigationMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private long BackPress;

    DatabaseReference database;
    DatabaseReference mDatabaseReference;
    private ProgressDialog progress;
    private Handler handler;
    RelativeLayout mNoInterntLayout;
    Menu menu;
    NavigationView navigationView;
    private List<Offer> offerList1 = new ArrayList<>();
    private List<Offer> offerListFilter = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostsAdapter mAdapter;
    public static int numOfCategoryItems;
    boolean flag = true;
    int userID;
    int profile_im = (R.drawable.profile_image);
    int post_im = (R.drawable.post_image);
    int country_im = (R.drawable.f1);


    SwipeRefreshLayout mSwipeRefreshLayout;
    int idFromLogin;
    public static boolean log;
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F); // make animation on button

   private CircleImageView nav_profileimage, profileImage ;
    private ImageView  nav_msgBadge ;

    Button start;


    TextView nav_firstname, nav_lastname, nav_email;

   private Offer offer;
   private NotificationBadge mBadge;

   private String firstName = "", lastName = "", email = "", gender = "", language = "", interest = "", userProfileImage = "";

   private String  o_description, o_image, o_createdat, o_companyName, o_logo, o_time, o_ar_companyName;

   private String o_ar_Description;
   private int o_srcoin, o_view, o_userid, o_id, o_companyId , pageNumber;
    double o_price;
private List<Offer> offerList;

    private String halfname = "";

    private List<UserInOffer> userInOfferList;

    private  Category category;
    private List<Category> categoryList;
    private MediaPlayer media;
    private int categoryId;
    private String categoryTitle;
    private String companyName = "", offerTitle = "", congratFname = "", congratlname = "", offerImage = "", companyLogo = "", winnerKey = "";
    private int offerID = 0, winnerCoins = 0, userCoin = 0;
    private TextView dCompanyName, dOfferTitle, dCongratefName, dCongratlName;
    private ImageView dOfferImage;
    private CircleImageView dCompanyLogo;
    private String chosenInterest = "";
    public TextView mobileNumberActivation ;
    private boolean login, check = true;
    private FloatingActionButton fab;
    private Dialog congratolitaionDialog , noInternet;
    private boolean ActivityVisable;


Intent ServiceIntent ;
    @Override
    //language
    protected void onDestroy() {
        //test();
        Log.e("onDestroy", "yes");
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        editor.putString("langa", "en");
        Log.e("onDestroy", "yes");
        editor.commit();


        super.onDestroy();
    }


    @Override
    //notifications
    protected void onRestart() {
        super.onRestart();
        AppUtills.notificationBadge.setNotifecationBadgeByShared(mBadge, this);
    }

    View rootLayout;

    private int revealX;
    private int revealY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //language
        Log.e("lang##", "" + getSharedPreferences("MyPref", 0).getString("langa", "").toString());
        setTitle(R.string.home);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        if (!Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
            final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            final SharedPreferences.Editor editor = pref.edit();
            editor.putString("langa", "ar");
            editor.commit();
            String languageToLoad = "ar";
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        } else {

            final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            final SharedPreferences.Editor editor = pref.edit();
            editor.putString("langa", "en");
            editor.commit();
            String languageToLoad = "en";
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }


        setContentView(R.layout.activity_navigation_menu);
        mBadge = findViewById(R.id.badge);
        AppUtills.notificationBadge.setNotifecationBadgeByShared(mBadge, this);


        //menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        mobileNumberActivation = findViewById(R.id.accountActive);

        menu();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        // check if user complete all his data
        firstName = prefs.getString("firstName", "");
        lastName = prefs.getString("lastName", "");
        email = prefs.getString("email", "");
        userID = prefs.getInt("id", 0);
        interest = prefs.getString("interest", "");
        userProfileImage = prefs.getString("profileImage", "");
        login = prefs.getBoolean("login", false);
        userCoin = prefs.getInt("coins", 300);
        Log.e("userOffersInfo", "userCoins  :  " + userCoin );
        if (login == false) {
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Login");
            navigationView.getMenu().findItem(R.id.nav_buycoins).setVisible(false);
            fab.setVisibility(View.VISIBLE);
        } else if (login == true) {
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Logout");
            fab.setVisibility(View.INVISIBLE);
            navigationView.getMenu().findItem(R.id.nav_buycoins).setVisible(true);
            test();
            // checkIftheuserWinOffer();
        }

        // item go to login screen
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(i);
            }
        });
        // layout id
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // action bar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // menu

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        //when open this activity from navigation menu (offers)

        if (b != null) {
            chosenInterest = !(((String) b.get("chosenInterest")) == null) ? (String) b.get("chosenInterest") : "";
            if (!chosenInterest.equalsIgnoreCase("")) {
                setTitle(chosenInterest);
            }
        }

        //contnu regtertion
        if (!getSharedPreferences("RegistrationNumber", 0).getString("number", "").isEmpty() &&
                getSharedPreferences("RegistrationNumber", 0).getInt("active", 0) == 0) {
            mobileNumberActivation.setVisibility(View.VISIBLE);
        } else {
            mobileNumberActivation.setVisibility(View.GONE);
        }

        // if registertion not complet click moilenumberaction and comtun your registertion
        mobileNumberActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationMenu.this, RegistrationCustom.class);
                intent.putExtra("step", 2);
                intent.putExtra("mobileNumber", getSharedPreferences("RegistrationNumber", 0).getString("number", ""));
                startActivity(intent);
                finish();
            }
        });

        // start recycle view code of item post_list
        recyclerView = (RecyclerView) findViewById(R.id.item_postlist);
        // show offers in navigation layout
        mAdapter = new PostsAdapter(offerList1, NavigationMenu.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        // class get value from Firebase
        upgradeDialog();
        final Intent intent = getIntent();

        rootLayout = (FrameLayout) findViewById(R.id.root_layout);

        // ------------------------------------------------------------------------------------------
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra("x") &&
                intent.hasExtra("y")) {

            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra("x", 0);
            revealY = intent.getIntExtra("y", 0);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {

                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);

                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }


        } else {
            rootLayout.setVisibility(View.VISIBLE);

            if (interest.equals(""))
                preparePostData();

            else if (!chosenInterest.equals(""))
                // get the offer about his choose interest list
                getOfferWithOfferChosen(chosenInterest);
            else
                preparePostDataToUserInterest();
        }


        Log.e("interest##", interest);
        // --------------------------------------------------------------------------------------------------

        // end recycle view code of item post_list
       /* DatabaseReference dbNode =
                FirebaseDatabase.getInstance().getReference().getRoot().child("UserInOffer");
        dbNode.setValue(null);*/
        // --------------------------internet connection error ------------------
        // if not internet connection
        mNoInterntLayout = (RelativeLayout) findViewById(R.id.no_internet_layout);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItems();
            }
        });

        // ---------------------------dialog information show item_post && congratulation  layouts ---------------------------
        congratolitaionDialog = new Dialog(this); // Context, this, etc.
        congratolitaionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        congratolitaionDialog.setContentView(R.layout.congratulation_main);
        congratolitaionDialog.setCancelable(false);
        congratolitaionDialog.setCanceledOnTouchOutside(false);

        dCompanyName = congratolitaionDialog.findViewById(R.id.companName);
        dOfferTitle = congratolitaionDialog.findViewById(R.id.offer_title);
        dCompanyLogo = congratolitaionDialog.findViewById(R.id.profile_image);
        dOfferImage = congratolitaionDialog.findViewById(R.id.post_image);
        dCongratefName = congratolitaionDialog.findViewById(R.id.congrat_firstname);
        dCongratlName = congratolitaionDialog.findViewById(R.id.congrat_secondname);

        AppUtills.onLineAdvertisment.getAdvertiseFromFireBase(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("notificationForAll").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "subscribed success";
                if (!task.isSuccessful()) {
                    msg = "subscribed fail";
                }
                Log.d("topics ", msg);
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        //registerToken(instanceIdResult.getToken());
                    }
                });
                //Toast.makeText(NavigationMenu.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        //getAllUserinOffer();


    }

             // dont know
    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(100);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (interest.equals(""))
                        preparePostData();

                    else if (!chosenInterest.equals(""))
                        getOfferWithOfferChosen(chosenInterest);
                    else
                        preparePostDataToUserInterest();
                }
            });

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);

            circularReveal.start();
        } else {
            //test that thing
            finish();
        }
    }
        // ------------------------------------------------------------------------------------------------------------------

    int upgradeDialogCheck = 0 ,versionCode = 0;
    //home page load
private void upgradeDialog(){

    FirebaseApp.initializeApp(getApplicationContext());
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("update");
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            upgradeDialogCheck = dataSnapshot.child("dialog").getValue(Integer.class);
            versionCode = dataSnapshot.child("version").getValue(Integer.class);
            Log.e("upgradeCheck##" , "**********************************  :  "+upgradeDialogCheck);

            if (upgradeDialogCheck == 1 && BuildConfig.VERSION_CODE < versionCode){
                AppUtills.upgradeClass.showUpgradeDialoog(NavigationMenu.this);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}

    @Override
    protected void onResume() {
        checkIftheuserWinOffer();
        ActivityVisable = true;
        if (login) AppUtills.getUserCoins(NavigationMenu.this);
        super.onResume();
    }

    @Override
    protected void onPause() {

        ActivityVisable = false;
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        database.removeEventListener(userWinnerLisener);

        //startService();
        super.onStop();
    }




    public void shoppingCart(View view) {

        //TODO shopping cart action here
        Log.e("shopping Caret", "ssssssssss");

        Intent intent = new Intent(NavigationMenu.this, shoppingCaretActivity.class);
        startActivity(intent);

    }

    public void continueDialogCongratulations(View view) {

        FirebaseApp.initializeApp(this);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("Winner_" + offerID);
        Log.e("userOffersInfo##", "offerID" + offerID);
        Log.e("userOffersInfo##", "winnerKey" + winnerKey);
        ref.child(winnerKey).removeValue();
        Toast.makeText(getApplicationContext(), "Congratulations",
                Toast.LENGTH_LONG).show();
        // winnerCoins minus from userCoin on DB
        Intent intent = new Intent(getApplicationContext(), MyWinnerProducts.class);
        intent.putExtra("userID", userID);
        intent.putExtra("offerID", offerID);
        intent.putExtra("winnerCoins", winnerCoins);
        intent.putExtra("userCoin", userCoin);
        intent.putExtra("fromPostDetails", 2224);

        if (congratolitaionDialog != null && congratolitaionDialog.isShowing()) {
            congratolitaionDialog.dismiss();
        }
        startActivity(intent);
        finish();
    }

    private void startcongratolitaionDialog() {
        if (!isProbablyArabic(congratFname) && !isProbablyArabic(congratlname)) {
            if (getApplicationContext().
                    getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa", "")
                    .equalsIgnoreCase("ar")) {
                dCongratefName.setText(congratlname);
                dCongratlName.setText(congratFname);

            } else {
                dCongratefName.setText(congratFname);
                dCongratlName.setText(congratlname);
            }

        } else {

            if (getApplicationContext().
                    getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa", "")
                    .equalsIgnoreCase("ar")) {
                dCongratefName.setText(congratFname);
                dCongratlName.setText(congratlname);
            } else {
                dCongratefName.setText(congratlname);
                dCongratlName.setText(congratFname);

            }
        }
        dCompanyName.setText(companyName);
        dOfferTitle.setText(offerTitle);


        Picasso.with(this).load("https://speed-rocket.com/upload/offers/"
                + offerImage).
                fit().centerCrop().into(dOfferImage);


        Picasso.with(this).load("https://speed-rocket.com/upload/logo/"
                + companyLogo).
                fit().centerCrop().into(dCompanyLogo);
        if (ActivityVisable) {
            media = MediaPlayer.create(getApplicationContext(), R.raw.congratiolation);
            media.start();
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    media.stop();
                    media.release();
                }
            });
        }
        if (!((Activity) NavigationMenu.this).isFinishing()) {
            //show dialog
            congratolitaionDialog.show();
        }

    }

    public void clear() {
        int size = this.offerList1.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.offerList1.remove(0);
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    void refreshItems() {
        // Load items
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            if (interest.equals(""))
                preparePostData();

            else if (!chosenInterest.equals(""))
                getOfferWithOfferChosen(chosenInterest);

            else
                preparePostDataToUserInterest();
        } // check if conection found
        else {
                    //if connection not fond
                noInternet = AppConfig.InternetFaild(NavigationMenu.this);
                final Button btn = noInternet.findViewById(R.id.Retry);
                noInternet.show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noInternet.cancel();
                        preparePostData();
                    }
                });


        }

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("postAdabter", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        editor.putInt("refresh", 1);
        editor.commit();
        onItemsLoadComplete();

        if (login) AppUtills.getUserCoins(NavigationMenu.this);
        if (getSharedPreferences("MyPrefsFile",0).getBoolean("msg",false)) nav_msgBadge.setVisibility(View.VISIBLE);
        else nav_msgBadge.setVisibility(View.INVISIBLE);
        AppUtills.notificationBadge.setNotifecationBadgeByShared(mBadge, NavigationMenu.this);
        // Load complete

    }


    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void preparePostData() {

        clear();
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait here");
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
                //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModel> getOfferConnection = userApi.getOffers(userID);
                Log.e("userAPIID",getOfferConnection.toString());

                getOfferConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try {


                            offerList = response.body().getOffers();
                            for (int i = 0; i < offerList.size(); i++) {

                                o_id = offerList.get(i).getId();
                                o_userid = offerList.get(i).getUserId();
                                o_price = offerList.get(i).getPrice();
                                o_view = offerList.get(i).getView();
                                o_srcoin = offerList.get(i).getSrcoin();
                               // pageNumber = offerList.get(i).getPageNumber();
                                o_description = offerList.get(i).getEn_description();
                                o_image = offerList.get(i).getImage();
                                o_logo = offerList.get(i).getCompanyLogo();
                                o_time = offerList.get(i).getTime();
                                o_companyId = offerList.get(i).getCompanyId();
                                o_companyName = offerList.get(i).getCompanyName();
                                o_ar_companyName = offerList.get(i).getAr_companyName();
                                o_ar_Description = offerList.get(i).getAr_description();
                                String o_artitle = offerList.get(i).getAr_title();
                                String o_enTitle = offerList.get(i).getEn_title();


                                offer = new Offer(o_id, 0, o_userid, 0, 0, o_srcoin,
                                        o_view, o_price, o_artitle, o_enTitle, o_ar_Description, o_description
                                        , "", "", post_im, o_image, o_createdat, o_companyName, o_logo,
                                        o_time, o_companyId, o_ar_companyName);


                                offerList1.add(offer);

                                mAdapter.notifyDataSetChanged();

                            }// or loop
                            progress.dismiss();

                        } // try
                        catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Connection Success\n" +
                                            "Exception Home Page\n" + e.toString(),
                                    Toast.LENGTH_LONG).show();
                            progress.dismiss();

                        } // catch

                        if (response.isSuccessful()) {
                            //   getUsersInOffer();
                        }

                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

               /* Toast.makeText(getApplicationContext(),"Connection Faild",
                        Toast.LENGTH_LONG).show();*/
                             progress.dismiss();
                         if (t instanceof IOException){
                            noInternet = AppConfig.InternetFaild(NavigationMenu.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                              noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    preparePostData();
                                }
                            });
                         }



                    } // on Failure
                });

// Retrofit

            }

        }.start();


    } // function preparepostdata




    public void test()
    {
       new Thread(){
           @Override
           public void run() {
               Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",NavigationMenu.this);

               UserApi  updateUser = retrofit.create(UserApi.class);

           Call<ResultModel> call =updateUser.teest();
               call.enqueue(new Callback<ResultModel>() {
                   @Override
                   public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                   }

                   @Override
                   public void onFailure(Call<ResultModel> call, Throwable t) {

                   }
               });
           }
       }.start();
    }

    ChildEventListener userWinnerLisener = new ChildEventListener() {
        @Override
        //-----------------check for this user take any offer and get all data -------------------------------
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {
                halfname = dataSnapshot.getKey().toString().split("_")[0];
                Log.e("userOffersInfo##", "halfName  : " + halfname);
                if (halfname.equals("Winner")) {
                    //offerID= Integer.valueOf(dataSnapshot.getKey().toString().split("_")[1]);
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        OfferWinner winner = data.getValue(OfferWinner.class);
                        if (winner.getUserid() == userID) {
                            if (check) {
                                Log.e("userOffersInfo##", "in :  *************************");
                                Log.e("userOffersInfo##", "Key :  " + dataSnapshot.getKey().toString());

                                congratFname = winner.getFirstName();
                                congratlname = winner.getLastName();
                                offerTitle = winner.getOfferEnTitle();
                                companyName = winner.getCompanyEnTitle();
                                companyLogo = winner.getCompanyLogo();
                                offerImage = winner.getOfferImage();
                                winnerKey = data.getKey();
                                offerID = winner.getOffer_id();
//                                          offerID = Integer.valueOf(data.getRef().getKey().split("_")[1]);
                                startcongratolitaionDialog();
                                Log.e("userOffersInfo##", "Winner Key :  " + winnerKey);
                                Log.e("userOffersInfo##", "offerID :  " + offerID);

                                check = false;
                            }

                        }

                    }
                } else {

                }
            } catch (Exception e) {

            }


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
            // check if user have any offer or not and get all data from onChildAdded()
    private void checkIftheuserWinOffer() {
        Log.e("userOffersInfo##", "     first  ***************************          ");
        FirebaseApp.initializeApp(this);

        database = FirebaseDatabase.getInstance().getReference();
        database.addChildEventListener(userWinnerLisener);


    }

    //--------------------------------------------------------- get data to home page------------------------------------------------------------
    private void preparePostDataToUserInterest() {
        clear();
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
                //<K.M>
                //<K.M>
                //<K.M>
                //<K.M>
                final Call<ResultModel> getOfferConnection = userApi.getOffersToUserInterest(interest,0 , userID );
                getOfferConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try {

                            offerList = response.body().getOffers();
                            for (int i = 0; i < offerList.size(); i++) {
                                o_id = offerList.get(i).getId();
                                o_userid = offerList.get(i).getUserId();
                                o_price = offerList.get(i).getPrice();
                                o_view = offerList.get(i).getView();
                                o_srcoin = offerList.get(i).getSrcoin();
                                o_description = offerList.get(i).getEn_description();
                                o_image = offerList.get(i).getImage();
                                o_logo = offerList.get(i).getCompanyLogo();
                                o_time = offerList.get(i).getTime();
                                o_companyId = offerList.get(i).getCompanyId();
                                String o_artitle = offerList.get(i).getAr_title();
                                String o_enTitle = offerList.get(i).getEn_title();
                                String o_arDescription = offerList.get(i).getAr_description();


                                o_companyName = offerList.get(i).getCompanyName();


                                offer = new Offer(o_id, 0, o_userid, 0, 0, o_srcoin,
                                        o_view, o_price, o_artitle, o_enTitle, o_arDescription, o_description
                                        , "", "", post_im, o_image, o_createdat, o_companyName, o_logo,
                                        o_time, o_companyId);


                                offerList1.add(offer);

                                if (!offerList1.isEmpty()) {
                                    mNoInterntLayout.setVisibility(View.GONE);
                                }
                                mAdapter.notifyDataSetChanged();
                            }// or loop
                            progress.dismiss();

                        } // try
                        catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Connection Success\n" +
                                            "Exception Home Page\n" + e.toString(),
                                    Toast.LENGTH_LONG).show();
                            progress.dismiss();

                        } // catch

                        if (response.isSuccessful()) {
                            //   getUsersInOffer();
                        }

                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

               /* Toast.makeText(getApplicationContext(),"Connection Faild",
                        Toast.LENGTH_LONG).show();*/

                        progress.dismiss();
                        if (t instanceof IOException){
                            noInternet = AppConfig.InternetFaild(NavigationMenu.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    preparePostDataToUserInterest();
                                }
                            });
                        }

                    } // on Failure
                });

// Retrofit

            }

        }.start();


    } // function preparePostDataToUserInterest

    @Override
    //back button and exit app <K.M>
    public void onBackPressed() {

        //K.M
        if (BackPress +2000 >System.currentTimeMillis()){
            mDatabaseReference.child(userID+"").child("online").setValue(ServerValue.TIMESTAMP);
            finishAffinity();
            return;
        }else {
            Toast.makeText(getBaseContext(),"Press back agian to exit",Toast.LENGTH_LONG).show();
        }
        BackPress = System.currentTimeMillis();


    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
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


        if (userID != 0) {
            menu.findItem(R.id.nav_myProducts).setVisible(true);
            menu.findItem(R.id.nav_cPanal).setVisible(true);
            menu.findItem(R.id.yourOrders).setVisible(true);
        } // if

        nav_firstname = (TextView) findViewById(R.id.menu_firstname);
        nav_lastname = (TextView) findViewById(R.id.menu_lastname);
        nav_email = (TextView) findViewById(R.id.menu_email);

        nav_profileimage = (CircleImageView) findViewById(R.id.nav_profileimage);
        nav_msgBadge = (ImageView) findViewById(R.id.msg_badge);

        if (getSharedPreferences("MyPrefsFile",0).getBoolean("msg",false)) nav_msgBadge.setVisibility(View.VISIBLE);
        else nav_msgBadge.setVisibility(View.INVISIBLE);

        nav_firstname.setText(firstName);
        nav_lastname.setText(lastName);
        nav_email.setText(email);


        Picasso.with(getApplicationContext()).load("https://speed-rocket.com/upload/users/"
                + userProfileImage).
                fit().centerCrop().into(nav_profileimage);

        nav_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyCompanyProfile.class);
                intent.putExtra("userID", userID);
                startActivity(intent);


            }
        });
        MenuItem searchItem = menue.findItem(R.id.action_settings);

        SearchManager searchManager = (SearchManager) NavigationMenu.this.getSystemService(Context.SEARCH_SERVICE);

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
                    Log.e("searchItem##" , newText);
                    if (newText.isEmpty()){
                        mAdapter = new PostsAdapter(offerList1 , NavigationMenu.this);
                        mAdapter.notifyDataSetChanged();

                        recyclerView.setAdapter(mAdapter);
                    }else {

                        offerListFilter.clear();
                        for (int i = 0; i < offerList1.size(); i++) {
                            Offer offer = offerList1.get(i);
                            if (offer.getEn_title().contains(newText)) {
                                offerListFilter.add(offer);
                            } else {
                                if (offer.getAr_title().contains(newText)) {
                                    offerListFilter.add(offer);
                                }
                            }
                            mAdapter = new PostsAdapter(offerListFilter, NavigationMenu.this);
                            mAdapter.notifyDataSetChanged();

                            recyclerView.setAdapter(mAdapter);

                        }
                    }
                    return true;
                }
            });
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(NavigationMenu.this.getComponentName()));

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

        //  for(int i =a1 ; i<= numOfCategoryItems ; i++) {


        if (id == R.id.nav_category) {


            Intent intent = new Intent(getApplicationContext(), ProductMenuList.class);
            startActivity(intent);


        } else if (id == R.id.nav_myProducts) {
            Intent intent = new Intent(getApplicationContext(), MyWinnerProducts.class);
            startActivity(intent);
        } else if (id == R.id.nav_offers) {
            Intent intent = new Intent(getApplicationContext(), OfferMenuList.class);
            startActivity(intent);
        } else if (id == R.id.nav_country) {

            return true;

        } else if (id == R.id.nav_language) {
            Intent intent = new Intent(getApplicationContext(), ChangeLanguage.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_logout) {


            if (m.findItem(R.id.nav_logout).getTitle().toString().equals("Logout")) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(R.string.LogOut);
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
                        fab.setVisibility(View.VISIBLE);
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

        } else if (id == R.id.nav_buycoins) {
            Intent intent = new Intent(getApplicationContext(), BuyCoins.class);
            startActivity(intent);
            return true;

        }// K.M
        else if(id == R.id.nav_users){
           Intent intent = new Intent(getApplicationContext(),MainChatLayout.class);
           startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_cPanal) {
            if (userID == 0) {
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
            } // check if login 0 ---> user not login
            else {
                Intent intent = new Intent(getApplicationContext(), MyCompanyProfile.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
            return true;

        }else if (id == R.id.yourOrders){
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

    public void menu() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        menu = navigationView.getMenu();


        navigationView.setNavigationItemSelectedListener(this);

    } // function fill gategory on menu

    private void getUsersInOffer() {

        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());


        final UserApi userApi = retrofit.create(UserApi.class);

        final Call<ResultModel> getOfferInConnection = userApi.getUsersByOffers();

        getOfferInConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                try {

                    userInOfferList = response.body().getUsers();
                    for (int i = 0; i < userInOfferList.size(); i++) {
                        int id, userId, offersId, srCoin, coins;
                        String firstName, lastName, email;

                        id = userInOfferList.get(i).getId();
                        userId = userInOfferList.get(i).getUserId();
                        offersId = userInOfferList.get(i).getOffersId();
                        srCoin = userInOfferList.get(i).getSrCoin();
                        coins = userInOfferList.get(i).getCoins();
                        firstName = userInOfferList.get(i).getFirstName();
                        lastName = userInOfferList.get(i).getLastName();
                        email = userInOfferList.get(i).getEmail();


                        UserInOffer userInOffer =
                                new UserInOffer(id, userId, offersId, srCoin, coins, firstName
                                        , lastName, email, 0, 0, "", "", 0);

                        // userInOfferList.add(userInOffer);


                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        String key = mDatabase.push().getKey();
                        mDatabase.child("UserInOffer").child(key).setValue(userInOffer);


                        //  if(response.isSuccessful() && i==userInOfferList.size()-a1)break;
                         /*Toast.makeText(getApplicationContext(),"Connection Success\n" +
                                        "\nUserInOffer"+userInOfferList.size(),
                                Toast.LENGTH_LONG).show();*/


                    }// for loop


                } // try
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Connection Success\n" +
                                    "Exception UserInOffer\n" + e.toString(),
                            Toast.LENGTH_LONG).show();

                } // catch
            } // onResponse

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Connection Faild",
                        Toast.LENGTH_LONG).show();

            } // on Failure
        });


// Retrofit
    }// function getUsersInOffer

    private void getOfferWithOfferChosen(final String chosenInterest ) {

        //Retrofit
        clear();
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
                Call<ResultModel> chooseInterestConnection =
                        //<K.M>
                        //<K.M>
                        //<K.M>
                        //<K.M>
                        //<K.M>
                        userApi.getOffersToUserInterest(chosenInterest,1 , userID );
                chooseInterestConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {
                            offerList = response.body().getOffers();
                            for (int i = 0; i < offerList.size(); i++) {
                                o_id = offerList.get(i).getId();
                                o_userid = offerList.get(i).getUserId();
                                o_price = offerList.get(i).getPrice();
                                o_view = offerList.get(i).getView();
                                o_srcoin = offerList.get(i).getSrcoin();
                                o_description = offerList.get(i).getEn_description();
                                o_image = offerList.get(i).getImage();
                                o_logo = offerList.get(i).getCompanyLogo();
                                o_time = offerList.get(i).getTime();
                                o_companyId = offerList.get(i).getCompanyId();
                                String o_artitle = offerList.get(i).getAr_title();
                                String o_enTitle = offerList.get(i).getEn_title();
                                String o_arDescription = offerList.get(i).getAr_description();

                                o_companyName = offerList.get(i).getCompanyName();


                                offer = new Offer(o_id, 0, o_userid, 0, 0, o_srcoin,
                                        o_view, o_price, o_artitle, o_enTitle, o_arDescription, o_description
                                        , "", "", post_im, o_image, o_createdat, o_companyName, o_logo,
                                        o_time, o_companyId );


                                offerList1.add(offer);

                                // firebase
                                String sId = String.valueOf(o_id);
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("offers").child(sId).setValue(offer);
                                //firebase

                                if (!offerList1.isEmpty()) {
                                    mNoInterntLayout.setVisibility(View.GONE);
                                }


                                mAdapter.notifyDataSetChanged();
                            }// or loop


                            progress.dismiss();

                        } // try
                        catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Connection Success\n" +
                                            "Exception Home Page\n" + e.toString(),
                                    Toast.LENGTH_LONG).show();
                            progress.dismiss();

                        } // catch
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            noInternet = AppConfig.InternetFaild(NavigationMenu.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getOfferWithOfferChosen(chosenInterest);
                                }
                            });
                        }

                    }
                });

            }

        }.start();


    }




}
