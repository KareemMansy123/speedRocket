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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Control.LatestOfferAdapter;
import com.speedrocket.progmine.speedrocket.Control.OffersAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.CurrentOffers;
import com.speedrocket.progmine.speedrocket.Model.FakePeople;
import com.speedrocket.progmine.speedrocket.Model.Image;
import com.speedrocket.progmine.speedrocket.Model.Offer;
import com.speedrocket.progmine.speedrocket.Model.OfferWinner;
import com.speedrocket.progmine.speedrocket.Model.PersonalUser;
import com.speedrocket.progmine.speedrocket.Model.Post;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.Model.UserInOffer;
import com.speedrocket.progmine.speedrocket.Model.UserOnline;
import com.speedrocket.progmine.speedrocket.Model.offerId;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;


public class PostDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

  /*void  restLoader(){
      final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
      DatabaseReference ref = database.child("UserInOffer");
      ref.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              LoaderManager loaderManager = getLoaderManager();
              loaderManager.restartLoader(1, null,PostDetails.this );
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

    }*/

    @Override
    protected void onPause() {
//        media.stop();
        // media.release();
        try {
            offerList1.clear();
            mAdapter1.notifyDataSetChanged();

            userList.clear();
            SEC = Tseconds.getText().toString() ;
            MIN = Tminutes.getText().toString();
            offerQuery.removeEventListener(userDatalistener);
            //maxCoinRefrenace.removeEventListener(maxCoinListener);
            updateWinnerReferance.removeEventListener(updateWinnerLisener);
            if (timerFunction != null) {
                timerFunction.cancel();
                timerFunction = null;
            }
            super.onPause();
            ActivityVisable = false;
        }catch (Exception e){
            Toast.makeText(this,"please try agian",Toast.LENGTH_LONG).show();
        }
        super.onPause();

    }
    final double DOT_ZERO = 0.5 ;
    int intPrice , realPrice ;
    double doublePrice ;
    String SEC ,MIN ;
    boolean ActivityVisable;
    String timerKey;
    int timerValue = 1; // its an imagenary value it has no use but you cant remove it :)
    String keyAdd, keychange;
    String[] FireBaseOfferName;
    int OFFFERID;
    int idddd;
    List<Integer> offerNames = new ArrayList<>();
    //List<String[]> offerNames = new ArrayList<>();

    public void getAllUserinOffer() {
        // final int country_im2 = (R.drawable.redicon);
        final int country_im3 = (R.drawable.greenicon);
        final int country_im4 = (R.drawable.orangeicon);

        Log.e("getleast##", "" + offerID);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyAdd = dataSnapshot.getKey();

                Log.e("getleast##", "key : " + keyAdd);
                if (keyAdd.contains("_")){
                    FireBaseOfferName = keyAdd.split("_");

                if (FireBaseOfferName.length == 3) {
                    boolean b = offersId.contains(FireBaseOfferName[2]) ? true : false;
                    Log.e("getleast##", "b : " + b);
                    if (offersId.contains(FireBaseOfferName[2])) {

                        database.child(keyAdd).orderByChild("position").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.e("getleast##", "second");
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    UserInOffer user = data.getValue(UserInOffer.class);
                                    Log.e("getleast##", "userId : " + user.getUserId());

                                    int userid = user.getUserId();
                                    if (userid == userID) {
                                        if (user.getId() != offerID) {
                                            Log.e("getleast##", "true");
                                            CurrentOffers offer = new CurrentOffers(country_im3, user.getOfferTitle(), user.getId(), 0);
                                            offerList1.add(offer);
                                            for (int i = 0; i < offerList1.size(); i++) {
                                                Log.e("removeList##", "offerId win : " + offerList1.get(i).getOfferid());
                                                offerNames.add(offerList1.get(i).getOfferid());
                                            }
                                            mAdapter1.notifyDataSetChanged();
                                        }
                                    } else {
                                        if (user.getId() != offerID) {
                                            Log.e("getleast##", "false");
                                            CurrentOffers offer = new CurrentOffers(country_im4, user.getOfferTitle(), user.getId(), 0);
                                            offerList1.add(offer);
                                            for (int i = 0; i < offerList1.size(); i++) {
                                                Log.e("removeList##", "offerId lose : " + offerList1.get(i).getOfferid());
                                                offerNames.add(offerList1.get(i).getOfferid());
                                            }
                                            mAdapter1.notifyDataSetChanged();
                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                keychange = dataSnapshot.getKey();
                if (keychange.contains("_")) {


                    Log.e("getleastchanged##", "key  :  " + keychange);
                    if (keychange.length() == 3){
                        if (offersId.contains(keychange.split("_")[2])) {
                            Log.e("getleastchanged##", "true  :  " + keychange.split("_")[2]);
                            database.child(keychange).orderByChild("position").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        UserInOffer user = data.getValue(UserInOffer.class);
                                        Log.e("getleastchanged##", "userId  :  " + user.getUserId());
                                        if (user.getUserId() == userID) {
                                            CurrentOffers offer = new CurrentOffers(country_im4, user.getOfferTitle(), user.getId(), 0);
                                            Log.e("getleastchanged##", "win  :  " + user.getPosition());
                                            if (offerList1.contains(offer)) {
                                                Log.e("getleastchanged##", "winiiin  :  " + user.getPosition());
                                                offerList1.get(offerList1.indexOf(offer)).setImage(country_im3);
                                                mAdapter1.notifyDataSetChanged();

                                            }
                                        } else {
                                            Log.e("getleastchanged##", "lose");
                                            if (offerNames.contains(user.getId())) {
                                                Log.e("getleastchanged##", "loseeeee");
                                                CurrentOffers offerwin1 = new CurrentOffers(country_im4, user.getOfferTitle(), user.getId(), 0);
                                                offerList1.set(offerNames.indexOf(user.getId()), offerwin1);
                                                mAdapter1.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Log.e("removeList##" , dataSnapshot.getKey().split("_")[1]);
                if (dataSnapshot.getKey().split("-")[0].equalsIgnoreCase("UserInOffer")) {
                    if (offersId.contains(dataSnapshot.getKey().split("_")[1])) {
                        //  Log.e("removeList##" , "here 1 ");
                        if (Integer.valueOf(dataSnapshot.getKey().split("_")[1]) != offerID) {
                            //Log.e("removeList##" , "here 2 ");
                            if (offerNames.contains(Integer.valueOf(dataSnapshot.getKey().split("_")[1]))) {
                                //Log.e("removeList##" , "here 3 ");
                                int removedId = offerNames.indexOf(Integer.valueOf(dataSnapshot.getKey().split("_")[1]));
                                offerNames.remove(removedId);
                                offerList1.remove(removedId);
                                mAdapter1.notifyDataSetChanged();
                            }
                        }
                    }
                }


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    final int[] progressColor = new int[]{R.drawable.progress_blue, R.drawable.progress_green, R.drawable.progress_red, R.drawable.timer_progerss};

    private void startBrogressCountdown(final long countdownTime, final ProgressBar bar, int check, CountDownTimer timer) {
        if (check == 1) bar.setMax(60000);
        else bar.setMax((int) countdownTime);


        bar.setProgress((int) countdownTime);
        timer = new CountDownTimer(countdownTime, 1) {
            // The millisUntilFinished thing is for a "pausing" function I implemented.
            public void onTick(long millisUntilFinished) {

                bar.setProgress((int) millisUntilFinished);
            }

            public void onFinish() {
                // For styling purposes because of the "inaccuracy" of the timer
                // so the progress will be shown as "full"
                bar.setProgress((int) msecondsToRun);
            }
        }.start();
    }

    int getUserDataCheck = 1, getUserDataChek2 = 0;
    long timerProgress, msecondsToRun;
    ArrayList<Integer> idOfUsers1 = new ArrayList<Integer>();
    ArrayList<Integer> srCoinsOfUsers1 = new ArrayList<Integer>();
    List<PersonalUser> user;
    List<Offer> offer;
    List<offerId> offersID;
    int OfferID = 0;
    List<Image> images;
    private ProgressDialog progress;
    private Handler handler;
    String o_firstName, o_lastName, o_description, o_image, o_title, myFirstNameAccount, myLastNameAccount, o_created_at, congratFname = "", congratLname = "", userProfileImage, o_logo;

    Query offerQuery, maxCoinRefrenace, updateWinnerReferance;
    int myUserIDAccount;
    int o_srcoin, o_view, o_userid, o_id, o_hours, o_minutes;
    double o_price;
    PersonalUser p;
    Button getOffer;

    boolean isIAcceptedCoinsOffer = false ;

    Dialog dialog, dialog1, dialog2, dialog3, dialog4 , ratingDialog;

    int viewInhome, viewInhome1, checkSrCoin = 0;
    TextView t_title, t_price, t_srcoin, t_dialog_description, myfirstname, mylastname, t_companyname, t_createdat, t_myCoins, congratFirstName, congratLastName, congratFirstName1, congratLastName2;
    int srcoin = 0;
    CircleImageView myProfileImage;
    int offerID;
    int lastTenSec = 0;
    ProgressBar bar_min, bar_sec;
 String winnerFname  , winnerLname ;

    String winnerKey;
    String randomN;
    TextView Thours, Tminutes, Tseconds;
    private List<Post> offerList = new ArrayList<>();
    private List<CurrentOffers> offerList1 = new ArrayList<CurrentOffers>();
    public List<Integer> idOfUsers;
    private List<Integer> srCoinsOfUsers = new ArrayList<>();
    private List<PersonalUser> userList = new ArrayList<PersonalUser>();
    ;
    private RecyclerView recyclerView, recyclerView_Latest_Offer;
    private OffersAdapter mAdapter;
    private LatestOfferAdapter mAdapter1;
    int addOnCoins = 0, winnerId = 0, winnerCoins = 0;
    private final int item_imgs[] = {R.drawable.product,
            R.drawable.samsung1,
            R.drawable.samsung2,
            R.drawable.samsung3,
            R.drawable.samsung4,
            R.drawable.samsung5,
    };

    String fakeFirstNames[] = {"Ali", "Ahmed", "Muhammed",
            "Hassan", "Amr", "Tamer", "Mahmoud", "Seif", "abdo"};

    String fakeLastNames[] = {"Ali", "Ahmed", "mohamed",
            "Hassan", "Amr", "Tamer", "Mahmoud", "Seif", "abdo"};

    String fakeFname, fakeLname;
    int counterLatest = 0;

    Button cancel;
    int id, userID, userCoin, counterFake = 0, fakeid = 1234, addOffer = 0;

    TextView nav_firstname, nav_lastname, nav_email, amountofcoinsrecvived, nameofsender, nameofsender2;
    CircleImageView nav_profileimage, profileImage;
    int sr, counter = 0, users_counter = 0, fid, srCoin = 1;
    String firstName, lastName, email, fname, lname, pImage;


    boolean finish = false;
    CircleImageView company_image;
    private Retrofit retrofit;

    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F); // make animation on button

    Button Backdetails;


    int f = 0;
    ArrayList<String> imagesUrl = new ArrayList<String>();


    UserOnline userOnline;

    String acceptOffer, newsFeedTitle;
    String Key; // key to make position of the user 1 when he click offer
    UserInOffer userInOffer;

    FakePeople fakePeople;
    private String fake_firstName, fake_lastName, fake_Image, fake_email;
    private int fake_id;

    private int o_companyId;
    private float o_rate ;
    // menu
    MediaPlayer media;
    String interest;
    boolean login;
    public static boolean log;
    Menu menu;
    NavigationView navigationView;
    //menu

    CountDownTimer timerFunction, progressTimerSec, progressTimerMin;
    int seconds, minutes, hours;
    RatingBar ratingBar , dialogRatBar ;

    Button rate , cancelRate ;
    int maxCoin;
    NotificationBadge mBadge;
    List<String> offersId = new ArrayList<>();

    private DatabaseReference mRestaurantReference;


    public void updateUi(List<PersonalUser> personalUsers) {

        mAdapter = new OffersAdapter(personalUsers, PostDetails.this, userID,
                offerID, firstName, lastName);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());       // mLayoutManager.setmediaLayout(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);
    }

    ImageView loading ;
    AnimationDrawable animation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.offerDetails);
        SharedPreferences share = getBaseContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String lang = share.getString("langa", "noFile");
        Log.e("langInPost##", lang);

        //TODO damm
        if (lang.equalsIgnoreCase("ar")) {
            setContentView(R.layout.postdetails_login_navigation_menu_ar);
            company_image = (CircleImageView) findViewById(R.id.destails_companyimage_ar);
            ratingBar = (RatingBar) findViewById(R.id.myRatingBar_ar);
            t_companyname = (TextView) findViewById(R.id.offerdetails_companname_ar);
            t_myCoins = (TextView) findViewById(R.id.offerdetails_mycoins);
        } else {
            setContentView(R.layout.postdetails_login_navigation_menu);
            company_image = (CircleImageView) findViewById(R.id.destails_companyimage);
            ratingBar = (RatingBar) findViewById(R.id.myRatingBar);
            t_companyname = (TextView) findViewById(R.id.offerdetails_companname);
            t_myCoins = (TextView) findViewById(R.id.offerdetails_mycoins);
        }



        mBadge = findViewById(R.id.badge);
        AppUtills.notificationBadge.setNotifecationBadgeByShared(mBadge, this);
        FirebaseApp.initializeApp(this);

        Log.e("loadingusers##", "oncreat************************************************");
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
        } else if (login == true) {
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

        firstName = prefs1.getString("firstName", "");//"No name defined" is the default value.
        lastName = prefs1.getString("lastName", "");//"No name defined" is the default value.
        email = prefs1.getString("email", "");//"No name defined" is the default value.
        userID = prefs1.getInt("id", 0);
        userCoin = prefs1.getInt("coins", 300);
        userProfileImage = prefs1.getString("profileImage", "");

        bar_min = (ProgressBar) findViewById(R.id.progressBarTodayMin);
        bar_sec = (ProgressBar) findViewById(R.id.progressBarTodaySec);

        dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.activity_dialog_more_details);
        dialog.setTitle(R.string.dialog_title);

        dialog1 = new Dialog(this); // Context, this, etc.
        dialog1.setContentView(R.layout.havenotenoughcoins);
        dialog1.setTitle("havenotenoughcoins");


        dialog2 = new Dialog(this); // Context, this, etc.
        dialog2.setContentView(R.layout.congratulation);
        dialog2.setTitle("congratulation");
        dialog2.setCancelable(false);
        dialog2.setCanceledOnTouchOutside(false);

        dialog3 = new Dialog(this); // Context, this, etc.
        dialog3.setContentView(R.layout.recieveofferfromhim);
        dialog3.setTitle("recieveofferfromhim");
        dialog3.setCancelable(false);
        dialog3.setCanceledOnTouchOutside(false);

        dialog4 = new Dialog(this); // Context, this, etc.
        dialog4.setContentView(R.layout.goodlucktryanotheroffer);
        dialog4.setTitle("goodlucktryanotheroffer");
        dialog4.setCancelable(false);
        dialog4.setCanceledOnTouchOutside(false);

        ratingDialog = new Dialog(this ) ;
        ratingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ratingDialog.setContentView(R.layout.rate_company_dialog);


        rate = ratingDialog.findViewById(R.id.rate);
        cancelRate = ratingDialog.findViewById(R.id.cancel_rate);
        dialogRatBar = ratingDialog.findViewById(R.id.rating);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogRatBar.getRating() == 0 ){
                    Toast.makeText(getBaseContext() , " please set your rating " , Toast.LENGTH_SHORT).show();
                }else {
                    if (! (userID==0)){
                     AppUtills.Rate.setRate(o_companyId , userID , dialogRatBar.getRating() , PostDetails.this);
                     ratingDialog.cancel();
                    }else {
                        ratingDialog.cancel();
                        Toast.makeText(getBaseContext() , " please login first " , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostDetails.this , LoginScreen.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });

        cancelRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingDialog.cancel();
            }
        });

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                   ratingDialog.show();

                }
                return true;
            }
        });

        t_title = (TextView) findViewById(R.id.offerdetails_title);
        t_price = (TextView) findViewById(R.id.offerdetails_price);
        // t_srcoin=(TextView) findViewById(R.id.offerdetails_srcoin);
        t_dialog_description = (TextView) dialog.findViewById(R.id.offerdetails_description);

        t_createdat = (TextView) findViewById(R.id.offerdetails_createdat);
        t_myCoins = (TextView) findViewById(R.id.offerdetails_mycoins);

        congratFirstName = (TextView) dialog2.findViewById(R.id.congrat_firstname);
        congratLastName = (TextView) dialog2.findViewById(R.id.congrat_secondname);

        congratFirstName1 = (TextView) dialog4.findViewById(R.id.congrat_firstname1);
        congratLastName2 = (TextView) dialog4.findViewById(R.id.congrat_secondname1);

        amountofcoinsrecvived = (TextView) dialog3.findViewById(R.id.amountofcoinRecived);
        nameofsender = (TextView) dialog3.findViewById(R.id.nameofsender);
        nameofsender2 = (TextView) dialog3.findViewById(R.id.nameofsender2);
        company_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                Intent intent = new Intent(view.getContext(), CompanyProfile.class);
                intent.putExtra("companyId", o_companyId);
                view.getContext().startActivity(intent);

            }
        });

        t_myCoins.setText(userCoin + "");


        LinearLayout lUp, lDown;

        lUp = (LinearLayout) findViewById(R.id.linearUserUp);
        lDown = (LinearLayout) findViewById(R.id.linearUserDowun);

        if (userID != 0) {
            lUp.setVisibility(View.VISIBLE);
            lDown.setVisibility(View.VISIBLE);
        } // if user not login

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Thours = (TextView) findViewById(R.id.txt_hours);
        Tminutes = (TextView) findViewById(R.id.txt_minutes);
        Tseconds = (TextView) findViewById(R.id.txt_seconds);


        myfirstname = (TextView) findViewById(R.id.myfirstname_offerdetails);
        mylastname = (TextView) findViewById(R.id.mylastname_offerdetails);

        myProfileImage = (CircleImageView) findViewById(R.id.myProfileImage);
        Picasso.with(getBaseContext()).load("https://speed-rocket.com/upload/users/"
                + userProfileImage).
                fit().centerCrop().into(myProfileImage);

        SharedPreferences pref1s = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        myFirstNameAccount = pref1s.getString("firstName", "");//"No name defined" is the default value.
        myLastNameAccount = pref1s.getString("lastName", "");//"No name defined" is the default value.
        myUserIDAccount = pref1s.getInt("id", 0);

        myfirstname.setText(myFirstNameAccount);
        mylastname.setText(myLastNameAccount);

        myProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Intent intent = new Intent(getBaseContext(), MyCompanyProfile.class);
                intent.putExtra("userID", myUserIDAccount);
                startActivity(intent);
            }
        });

        getOffer = (Button) findViewById(R.id.getOffer);
        cancel = (Button) findViewById(R.id.bt_cancel_dialog_moredetails);
        // start recycle view code of item post_list
        recyclerView = (RecyclerView) findViewById(R.id.item_offerlist);
        recyclerView_Latest_Offer = (RecyclerView) findViewById(R.id.list_latest_offer);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();


        if (b != null) {
            offerID = (int) b.get("offerID");
            Log.e("id###", "" + offerID);
        }

        loading = findViewById(R.id.loading_ainm);

        animation = (AnimationDrawable)loading.getBackground() ;



        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {

                Random r = new Random();
                int i = r.nextInt(fakeFirstNames.length) + 0; // 0 min . f.lenght max
                int j = r.nextInt(fakeLastNames.length) + 0; // 0 min . f.lenght max

                fakeFname = fakeFirstNames[i];
                fakeLname = fakeLastNames[j];

                if (finish == false && srCoin < o_srcoin && userID == 7)
                    //  fakePeople();
                    Log.i("FakeC", "hello");

            }
        }, 60 * 1000, 120 * 1000);

        getOfferData();


        updateUi(userList);

        mAdapter1 = new LatestOfferAdapter(offerList1, PostDetails.this);
        RecyclerView.LayoutManager m1LayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView_Latest_Offer.setLayoutManager(m1LayoutManager);
        recyclerView_Latest_Offer.setItemAnimator(new DefaultItemAnimator());
        recyclerView_Latest_Offer.setAdapter(mAdapter1);


//TODO

        //preparePostData2();


        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#E65100"), PorterDuff.Mode.SRC_ATOP);
        // end recycle view code of item post_list


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("online");

        //Query offerQuery = ref.orderByChild(phoneNo).equalTo("+923336091371");


        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                            String k = singleSnapshot.getKey();
                            userOnline = singleSnapshot.getValue(UserOnline.class);

                            Log.e("online", k + "\n" + userOnline.getOfferid());


                            String userid = String.valueOf(userID);


                            if (offerID == userOnline.getOfferid()) {
                                userOnline = singleSnapshot.getValue(UserOnline.class);


                                String userid2 = String.valueOf(userOnline.getUserid2());
                                String userid1 = String.valueOf(userOnline.getUserid1());
                                acceptOffer = String.valueOf(userOnline.getAccept());
                                if (userid.equals(userid2)) {


                                    amountofcoinsrecvived.setText(String.valueOf(userOnline.getAmount()));
                                    nameofsender.setText(userOnline.getFirstName());
                                    nameofsender2.setText(userOnline.getLastName());
                                /*Toast.makeText(getBaseContext(), "HIin\n" +
                                                userid2 + " " + userid
                                                + "\n"
                                        , Toast.LENGTH_LONG).show();*/


                                    if (!isFinishing() && !acceptOffer.equals("1")) {
                                        dialog3.show();
                                    }

                                    if (acceptOffer.equals("1")) {
                                        getOffer.setVisibility(View.INVISIBLE);
                                        isIAcceptedCoinsOffer =true ;

                                    }
                                } // if

                                else if (userid.equals(userid1)) {
                                    if (acceptOffer.equals("1")) {
                                        t_myCoins.setText(userCoin - userOnline.getAmount() + "");
                                        SharedPreferences settings = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor prefEditor = settings.edit();
                                        prefEditor.putInt("coins", userCoin - userOnline.getAmount());
                                        prefEditor.apply();

                                        //Retrofit

                                        Retrofit retrofit =AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());


                                        UserApi userApi = retrofit.create(UserApi.class);
                                        Call<ResultModel> updateUserCoinConnection =
                                                userApi.updateUserCoin(userID, userCoin - userOnline.getAmount());
                                        updateUserCoinConnection.enqueue(new Callback<ResultModel>() {
                                            @Override
                                            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                                                try {
                                                    /* Toast.makeText(getBaseContext(), "Done"
                                                     , Toast.LENGTH_LONG).show(); */


                                                } catch (Exception e) {
                                                    Toast.makeText(getBaseContext(), "exception" + (e.toString()), Toast.LENGTH_LONG).show();

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResultModel> call, Throwable t) {
                                                Toast.makeText(getBaseContext(), "Connection faild\n", Toast.LENGTH_LONG).show();

                                            }
                                        });
                                        //Retrofit
                                    }
                                }
                       /*     Toast.makeText(getBaseContext(), "HIout\n" +
                                    userid2 + " " + userid, Toast.LENGTH_LONG).show();*/


                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }); // offerhim
        } // if condition


        //   mRestaurantReference = FirebaseDatabase.getInstance().getReference("UserInOffer");
        //   setUpFirebaseAdapter();


    }



    @Override
    protected void onResume() {
        super.onResume();
        ActivityVisable = true;
        updateWinnerFromfireBase();
         getusers();

        // onOfferChanged();
        //getMaxCoins();
    }

    public void getusers(){


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserInOffer_Data_"+offerID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int  i =0 ;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    UserInOffer user = data.getValue(UserInOffer.class);

                    PersonalUser p = new PersonalUser(
                            user.getSrCoin(),user.getFirstName(),user.getLastName(),user.getId(),user.getProfileImage()
                    );
                     maxCoin = user.getSrCoin() ;
                    userList.add(0,p); // list to show in recycleView
                    mAdapter.notifyDataSetChanged();

                          if (i == dataSnapshot.getChildrenCount()-1){
                              if (user.getUserId() == userID)
                              {
                                  getOffer.setVisibility(View.GONE);
                                  animation.start();
                                  loading.setVisibility(View.VISIBLE);
                              }
                          }
                              i++ ;
                }
                getUserData();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void shoppingCart(View view) {

        //TODO shopping cart action here
        Log.e("shopping Caret", "ssssssssss");

        Intent intent = new Intent(PostDetails.this, shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mFirebaseAdapter.cleanup();
    }



    @Override
    protected void onStart() {
        OfferID = 0;
        Log.e("onStart##", "" + OfferID);
        super.onStart();
    }

    @Override
    protected void onStop() {

        OfferID = 0;
        Log.e("onStop##", "" + OfferID);
        // media.reset();

        super.onStop();
    }

    @Override
    protected void onRestart() {

        offerList.clear();
        //getUserData();
       // Timer(Double.valueOf(SEC) , Double.valueOf(MIN));
        getOfferData();
        super.onRestart();
    }

    private void preparePostData2()

    {


        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        //Query offerQuery = ref.orderByChild(phoneNo).equalTo("+923336091371");


        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                new LoadingLeastOfferList().execute(dataSnapshot);

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
        });


    } // function Latest

    ChildEventListener userDatalistener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            if (userList.isEmpty()) {
                UserInOffer value = dataSnapshot.getValue(UserInOffer.class);
                //Key = dataSnapshot.getKey().toString() ;
                Log.e("loadingusers##", "onChileAdd *************************** ");
                //   new   LoadingUsers().execute(value);
                getUserDataCheck++;
                maxCoin = value.getSrCoin();
                Log.e("getmaxCoin##", "" + maxCoin);

                PersonalUser person;
                person = new PersonalUser(value.getSrCoin(),
                        value.getFirstName(),
                        value.getLastName(),
                        value.getUserId(),
                        value.getProfileImage()); // object of bojo class

                userList.add(0, person); // list to show in recycleView
                mAdapter.notifyDataSetChanged(); // recycleView custom Adapter

                Log.e("loadingusers##", "fname  :" + value.getFirstName());
                Log.e("loadingusers##", "lname  :" + value.getLastName());
                winnerFname = value.getFirstName();
                winnerLname = value.getLastName();

                if (value.getUserId() != userID) {

                    loading.setVisibility(View.INVISIBLE);
                    animation.stop();
                    if (!isIAcceptedCoinsOffer)
                        getOffer.setVisibility(View.VISIBLE);


                } else {
                    loading.setVisibility(View.VISIBLE);
                    animation.start();
                    getOffer.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            UserInOffer value = dataSnapshot.getValue(UserInOffer.class);
            //Key = dataSnapshot.getKey().toString() ;
            Log.e("loadingusers##", "onChileAdd *************************** ");
            //   new   LoadingUsers().execute(value);
            getUserDataCheck++;
            maxCoin = value.getSrCoin();
            Log.e("getmaxCoin##", "" + maxCoin);

            PersonalUser person;
            person = new PersonalUser(value.getSrCoin(),
                    value.getFirstName(),
                    value.getLastName(),
                    value.getUserId(),
                    value.getProfileImage()); // object of bojo class

            userList.add(0, person); // list to show in recycleView
            mAdapter.notifyDataSetChanged(); // recycleView custom Adapter

            Log.e("loadingusers##", "fname  :" +value.getFirstName());
            Log.e("loadingusers##", "lname  :" +value.getLastName());
            winnerFname = value.getFirstName();
            winnerLname = value.getLastName() ;

            if (value.getUserId() != userID){

                loading.setVisibility(View.INVISIBLE);
                animation.stop();
                if (!isIAcceptedCoinsOffer)
                getOffer.setVisibility(View.VISIBLE);


            }else{
                loading.setVisibility(View.VISIBLE);
                animation.start();
                getOffer.setVisibility(View.GONE);
            }

//            if (value.getUserId() != userID ){
//                getOffer.setVisibility(View.VISIBLE);
//            }else{
//                getOffer.setVisibility(View.INVISIBLE);
//            }





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


    public void getUserData() {
        Log.e("loadingusers##", "getUserData");
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("UserInOffer_" + offerID);
        offerQuery = ref.orderByValue() ;

        if (ref != null) {
            offerQuery.addChildEventListener(userDatalistener);
        } // if check if firebase null
    } // get user data



    private void onOfferChanged() {

        Log.w("offerKEY##", "mmh");
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        int profile_im = (R.drawable.profile_image);
        int post_im = (R.drawable.post_image);
        final int country_im = (R.drawable.f1);

        int profile_im2 = (R.drawable.p1);
        int profile_im3 = (R.drawable.p2);
        int profile_im4 = (R.drawable.p3);

        final int country_im2 = (R.drawable.redicon);
        final int country_im3 = (R.drawable.greenicon);
        final int country_im4 = (R.drawable.orangeicon);


        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    int offerId = Integer.valueOf(dataSnapshot.getKey().toString().split("_")[1]);
                    Log.e("onofferChanged##", "offeridFirst  :" + offerId);

                    if (offerId != offerID) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            UserInOffer value = data.getValue(UserInOffer.class);

                            int offerid, userid, position, view;
                            offerid = value.getId();
                            userid = value.getUserId();
                            position = value.getPosition();
                            view = value.getView();
                            f = value.getFinish();
                            Log.e("onofferChanged##", "offerid  :" + offerid);
                            Log.e("onofferChanged##", "userid  :" + userid);
                            Log.e("onofferChanged##", "position  :" + position);
                            Log.e("onofferChanged##", "view  :" + view);
                            Log.e("onofferChanged##", "finish  :" + f);

                            newsFeedTitle = value.getOfferTitle();


                            if (position == 0 && userid == userID && view == 1 && f == 0) {
                                CurrentOffers currentOffers = new CurrentOffers(country_im4, newsFeedTitle, offerid, f);
                                offerList1.add(currentOffers);
                                mAdapter1.notifyDataSetChanged();

                            } else if (position == 1 && userid == userID && view == 1 && f == 0) {
                                CurrentOffers currentOffers = new CurrentOffers(country_im3, newsFeedTitle, offerid, f);
                                offerList1.add(currentOffers);
                                mAdapter1.notifyDataSetChanged();
                            } else if (position == 0 && userid == userID && view == 1 && f == 1) {
                                CurrentOffers currentOffers = new CurrentOffers(country_im2, newsFeedTitle, offerid, f);
                                offerList1.remove(currentOffers);
                                mAdapter1.notifyDataSetChanged();
                            } else if (position == 1 && userid == userID && view == 1 && f == 1) {
                                CurrentOffers currentOffers = new CurrentOffers(country_im2, newsFeedTitle, offerid, f);
                                offerList1.remove(currentOffers);
                                mAdapter1.notifyDataSetChanged();
                            }

                            OfferID = offerid;


                        }

                    }
                } catch (Exception e) {

                }
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
        });


    }


    public void button_MoreDetails(View view) {

        dialog.show();

    } // function for open dialogue display more details about product

    public void CancelDialogueMoreDetails(View view) {
        dialog.cancel();
    }  // function of CancelDialogueMoreD


    public void Timer(final double seconds1, double minutes1) {


        double l1 = seconds1 * 1000;
        double l2 = minutes1 * 60 * 1000;
        double l = l1 + l2;

        //  startBrogressCountdown((long)seconds1*1000,bar_min , 1 , progressTimerMin);

        timerFunction = new CountDownTimer((long) l, 1000) {
            int progressSec = 10;
            int progressMin = (int) seconds1;
            int check = (int) seconds1;


            @Override
            public void onTick(long l) {


                // Log.e("time##", "second" + l);
                seconds = (int) (l / 1000);     // where l = 7,200,000
                minutes = seconds / 60;
                hours = minutes / 60;
                seconds = seconds % 60;
             /*
             if(minutes >= 60) {
                 startBrogressCountdown(60000,bar_min);
             }*/

                Tseconds.setText("" + seconds);
                Tminutes.setText("" + minutes);
//                  if (minutes==0 && seconds<4){
//                      getOffer.setBackgroundResource(R.drawable.button_offer_shape_deactive);
//                      getOffer.setEnabled(false);
//                  }
                //Thours.setText("" + hours);


            }


            @Override
            public void onFinish() {


                //show dialog

                onOfferFinish();
            }
        }.start();

    } // function of Timer

    public void onOfferFinish() {


//             offerQuery.removeEventListener(userDatalistener);
            // maxCoinRefrenace.removeEventListener(maxCoinListener);
             //updateWinnerReferance.removeEventListener(updateWinnerLisener);

             progress = new ProgressDialog(PostDetails.this);
             progress.setTitle("Please Wait");
             progress.setMessage("Loading..");
             progress.setCancelable(false);
             progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             if (!((Activity) PostDetails.this).isFinishing()) {
                 //show dialog
                 progress.show();
             }
        if (login) {
             finish = true;
             final Handler handler = new Handler();
             handler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     // Do something after 5s = 5000ms
                     progress.dismiss();
                     if (!isFinishing()) {


                         if (userID == winnerId) {
                             if (!isProbablyArabic(congratFname) && !isProbablyArabic(congratLname)) {
                                 if (getBaseContext().
                                         getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa", "")
                                         .equalsIgnoreCase("ar")) {
                                     congratFirstName.setText(congratLname);
                                     congratLastName.setText(congratFname);
                                     congratFirstName1.setText(congratLname);
                                     congratLastName2.setText(congratFname);
                                 } else {
                                     congratFirstName.setText(congratFname);
                                     congratLastName.setText(congratLname);
                                     congratFirstName1.setText(congratFname);
                                     congratLastName2.setText(congratLname);
                                 }

                             } else {

                                 if (getBaseContext().
                                         getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa", "")
                                         .equalsIgnoreCase("ar")) {
                                     congratFirstName.setText(congratFname);
                                     congratLastName.setText(congratLname);
                                     congratFirstName1.setText(congratFname);
                                     congratLastName2.setText(congratLname);
                                 } else {
                                     congratFirstName.setText(congratLname);
                                     congratLastName.setText(congratFname);
                                     congratFirstName1.setText(congratLname);
                                     congratLastName2.setText(congratFname);
                                 }
                             }
                             if (!((Activity) PostDetails.this).isFinishing()) {
                                 //show dialog
                                 dialog2.show();
                             }

                             Log.e("update", "you are the winner ");
                             if (ActivityVisable) {
                                 media = MediaPlayer.create(getBaseContext(), R.raw.congratiolation);
                                 media.start();
                                 media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                     @Override
                                     public void onCompletion(MediaPlayer mediaPlayer) {
                                         media.stop();
                                         media.release();
                                     }
                                 });
                             }
//                        userList.clear();
//
//                        recyclerView.setAdapter(null);
                         } else {
                             if (congratFname != null || congratFname.isEmpty() || congratFname.equalsIgnoreCase("")
                                     || congratLname != null || congratLname.isEmpty() || congratLname.equalsIgnoreCase("")) {
                                 if (!isProbablyArabic(congratFname) && !isProbablyArabic(congratLname)) {
                                     if (getBaseContext().
                                             getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa", "")
                                             .equalsIgnoreCase("ar")) {
                                         congratFirstName.setText(congratLname);
                                         congratLastName.setText(congratFname);
                                         congratFirstName1.setText(congratLname);
                                         congratLastName2.setText(congratFname);
                                     } else {
                                         congratFirstName.setText(congratFname);
                                         congratLastName.setText(congratLname);
                                         congratFirstName1.setText(congratFname);
                                         congratLastName2.setText(congratLname);
                                     }

                                 } else {

                                     if (getBaseContext().
                                             getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa", "")
                                             .equalsIgnoreCase("ar")) {
                                         congratFirstName.setText(congratFname);
                                         congratLastName.setText(congratLname);
                                         congratFirstName1.setText(congratFname);
                                         congratLastName2.setText(congratLname);
                                     } else {
                                         congratFirstName.setText(congratLname);
                                         congratLastName.setText(congratFname);
                                         congratFirstName1.setText(congratLname);
                                         congratLastName2.setText(congratFname);
                                     }
                                 }
                             }
                             if (!((Activity) PostDetails.this).isFinishing()) {
                                 //show dialog
                                 congratFirstName1.setText(winnerFname);
                                 congratLastName2.setText(winnerLname);
                                 dialog4.show();
                             }

                             Log.e("update", "you are  lost ");
                             if (ActivityVisable) {
                                 media = MediaPlayer.create(getBaseContext(), R.raw.lose);
                                 media.start();
                                 media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                     @Override
                                     public void onCompletion(MediaPlayer mediaPlayer) {
//                                media.reset();
                                         media.release();

                                     }
                                 });
                             }
//                        userList.clear();
//
//                        recyclerView.setAdapter(null);
                         }
                         // PostDetails.this.finish();
                     }
                 }
             }, 5000);
         }else {

     handler.postDelayed(new Runnable() {
         @Override
         public void run() {
             progress.dismiss();
             Toast.makeText(getBaseContext() , "sorry you must login first " , Toast.LENGTH_LONG).show();
             Intent intent = new Intent(PostDetails.this , NavigationMenu.class);
             startActivity(intent);
             finish();
         }
     }, 5000);

         }

    }

    ChildEventListener setTimeFromDataBase = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            timerKey = dataSnapshot.getKey();
            Log.e("update##", timerKey);
//             int seconds =  dataSnapshot.child("sec").getValue(Integer.class);
//             int min = seconds / 60 ;
//             int sec = seconds %60;
//                Tseconds.setText("" + sec);
//                Tminutes.setText("" + min);


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if (timerFunction != null) {
                timerFunction.cancel();
                timerFunction = null;
            }
            int seconds = dataSnapshot.child("sec").getValue(Integer.class);
            int min = seconds / 60;
            int sec = seconds % 60;
            Tseconds.setText("" + sec);
            Tminutes.setText("" + min);


            if (seconds == 0) {

                onOfferFinish();

            }

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

    private void setTimerFromFireBase() {
        Log.e("update##", "0");
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("timer").child("" + offerID);
        ref.addChildEventListener(setTimeFromDataBase);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }

        recyclerView.setAdapter(null);
        Intent intent = new Intent(PostDetails.this, NavigationMenu.class);
        startActivity(intent);
        finish();
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

        SearchManager searchManager = (SearchManager) PostDetails.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(PostDetails.this.getComponentName()));
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
            Intent intent = new Intent(getBaseContext(), ChangeLanguage.class);
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

        }
        else if (id == R.id.yourOrders){
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
        if (navigationView.getMenu() == null) {
            Log.e("in get menu", "dam me again");
        } else {
            menu = navigationView.getMenu();
        }

        navigationView.setNavigationItemSelectedListener(this);
    }


    public void getOfferData() {
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
                Call<ResultModel> getOfferConnection =  userApi.getOfferDetails(offerID, userID);

                Log.e("messageeeeeee","you areeeeee hereeeeeeeeeeeeeeeeee");
//TODO
                getOfferConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {

                            offer = response.body().getOffers();
                            offersID = response.body().getOffersId();
                            String date = response.body().getTodayDate();
                            o_price = offer.get(0).getPrice();
                            o_srcoin = offer.get(0).getSrcoin();
                            o_userid = offer.get(0).getUserId();
                            o_hours = offer.get(0).getHours();
                            o_minutes = offer.get(0).getMinutes();
                            o_created_at = offer.get(0).getCreated_at();
                            o_logo = offer.get(0).getCompanyLogo();
                            o_companyId = offer.get(0).getCompanyId();
                            o_rate = offer.get(0).getRate();

                            images = response.body().getImages();

                            for (int i = 0; i < images.size(); i++) {
                                imagesUrl.add(images.get(i).getImage());
                            }

                            imageSlider(imagesUrl);

                            Picasso.with(getBaseContext()).load("https://speed-rocket.com/upload/logo/"
                                    + o_logo).
                                    fit().centerCrop().into(company_image);
                            String time = response.body().getTime();


                            double timeN = Double.parseDouble(time); // 94.5

                            int n = (int) timeN; //5

                            double hours = 0, minutes = 0, seconds = 0;

                            minutes = n;
                            seconds = (timeN - n) * 60;



                            Timer(seconds, minutes);

                         ratingBar.setRating(o_rate);


                            String companyName;

                            if (getBaseContext().getSharedPreferences("MyPref", 0).getString("langa", "").equalsIgnoreCase("ar")) {
                                companyName = offer.get(0).getAr_companyName();
                                o_title = offer.get(0).getAr_title();
                                o_description = offer.get(0).getAr_description();
                            } else {
                                companyName = offer.get(0).getCompanyName();
                                o_title = offer.get(0).getEn_title();
                                o_description = offer.get(0).getEn_description();
                            }


                            //TODO company 1
                            t_companyname.setText(companyName);

                            t_title.setText(o_title);
                            t_createdat.setText(date);
                            doublePrice = o_price ;
                            intPrice = (int)doublePrice ;
                            if ((doublePrice - intPrice) > DOT_ZERO)
                                realPrice = intPrice+1 ;
                            else
                             realPrice = intPrice ;
                            t_price.setText(realPrice + "");
                            //  t_srcoin.setText(o_srcoin+"");

                            //int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                            //  String thisMonth , thisDay , thisYear;
                            //int thisDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

//                    Calendar rightNow = Calendar.getInstance();
//                    SimpleDateFormat df1 = new SimpleDateFormat("dd");
//                    SimpleDateFormat df2 = new SimpleDateFormat("MM");
//                    SimpleDateFormat df3 = new SimpleDateFormat("YYYY");
//                    thisDay = df1.format(rightNow.getTime());
//                    thisMonth = df2.format(rightNow.getTime());
//                    thisYear = df3.format(rightNow.getTime());


                            // t_createdat.setText(thisDay+"/"+thisMonth+"/"+thisYear);
                            try {
                                t_dialog_description.setText(o_description);
                            } catch (Exception e) {
                                Log.e("error###", "firstCatch");
                     /*Toast.makeText(getBaseContext(),"hi\n"
                     +e.toString(),Toast.LENGTH_LONG).show();*/
                            }


                            Log.e("offerId###", "" + offersID.size());

                            for (int i = 0; i < offersID.size(); i++) {
                                Log.e("offerId###", "" + offersID.get(i).getOffersId());
                                offersId.add(String.valueOf(offersID.get(i).getOffersId()));
                            }
                            setTimerFromFireBase();
                            getAllUserinOffer();

                        } catch (Exception e) {
                   /* Toast.makeText(getBaseContext(), "Connection Success\n" +
                                    "Exception"+e.toString()
                            ,Toast.LENGTH_LONG).show();*/

                            Log.e("error###", e.toString());
                        }

                        progress.dismiss();

                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(PostDetails.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getOfferData();
                                }
                            });
                        }
                    }
                });
                //Retrofit

            }

        }.start();


    } // getProfileData Function

    public void getCompanyNameOnline(final int id) {
        //Retrofit
        //Retrofit
        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

        final UserApi userApi = retrofit.create(UserApi.class);
        Call<ResultModel> getProfileConnection =
                userApi.getProfileAccount(id);

        getProfileConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                try {
                    user = response.body().getUser();
                    String companyName = user.get(0).getCompanyName();

                    t_companyname.setText(companyName);
                   /* Toast.makeText(getBaseContext(), "Connection Success\n"
                            +"Company Name : "+companyName+" id : "+id
                            ,Toast.LENGTH_LONG).show();*/


                } catch (Exception e) {
                  /*  Toast.makeText(getBaseContext(), "Connection Success\n" +
                                    "Exception"+e.toString()
                            ,Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Connection faild\n" +
                                "Exception" + t.toString()
                        , Toast.LENGTH_LONG).show();


            }
        });
        //Retrofit
    } // getProfileData Function

    public void getOffer(View view) {


        getOffer.setVisibility(View.GONE);
        animation.start();
        loading.setVisibility(View.VISIBLE);

        if (ActivityVisable) {
            media = MediaPlayer.create(getBaseContext(), R.raw.audio_btn);
            media.start();
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.reset();
                    mediaPlayer.release();

                }
            });
        }


        view.startAnimation(buttonClick);
        countViews();



        if (userID == 0) {
            Toast.makeText(getBaseContext(), "you have to log in first :)", Toast.LENGTH_LONG).show();
            Intent login = new Intent(PostDetails.this, LoginScreen.class);
            startActivity(login);
        } // if user not login*/

        else {
            if (userCoin <= maxCoin) {

                Toast.makeText(getBaseContext(), "you have to by rockets :)", Toast.LENGTH_LONG).show();
                Intent buyCoins = new Intent(PostDetails.this, BuyCoins.class);
                startActivity(buyCoins);
            } else {

                //  insertInFireBase();
//TODO any thing just a sign
//                getOffer.setVisibility(View.INVISIBLE);

                final Handler offerButtonHundler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        if (msg.what == 1) {
                            if (getUserDataCheck == getUserDataChek2) {
                                offerList.clear();
                                offerQuery.removeEventListener(userDatalistener);

                                getUserData();
                                getUserDataCheck = 1;
                                getUserDataChek2 = 0;
                            }

                            getOffer.setEnabled(true);
                            if (ActivityVisable) {
                                media = MediaPlayer.create(getBaseContext(), R.raw.reverse_btn);
                                media.start();
                                media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        mediaPlayer.reset();
                                        mediaPlayer.release();

                                    }
                                });
                            }
                            //  setWinner();
                            Log.e("loadingusers##", "check  :  " + getUserDataCheck);
                            Log.e("loadingusers##", "check  :  " + getUserDataChek2 + "\n");
                            getUserDataChek2 = getUserDataCheck;
                        }
                    }
                };


                Log.e("timer##", "min  " + Integer.valueOf(Tminutes.getText().toString()));
                Log.e("timer##", "sec  " + Integer.valueOf(Tseconds.getText().toString()));
                if (Integer.valueOf(Tminutes.getText().toString()) == 0 && Integer.valueOf(Tseconds.getText().toString()) < 10) {
                    final int min = 20;
                    final int max = 80;
                    timerValue = new Random().nextInt((max - min) + 1) + min;
                    lastTenSec = 1;
                    //  FirebaseDatabase.getInstance().getReference().child("timer").child(""+offerID).child(timerKey).child("sec").setValue(timerValue);
                } else {
                    lastTenSec = 0;
                }
                setPostionToUsersFireBase();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", PostDetails.this);

                        final UserApi userApi = retrofit.create(UserApi.class);
                        Log.w("usetId", "" + userID);
                        Log.w("offerId", "" + offerID);
                        Call<ResultModel> setOfferCall = userApi.setUserOfferTest(userID, offerID, lastTenSec);
                        setOfferCall.enqueue(new Callback<ResultModel>() {
                            @Override
                            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                                Log.e("masseage##", response.body().getMessage().toString());
                                if (response.body().getMessage().toString().equalsIgnoreCase("true")) {
                                    Log.e("masseage##", "i am here");

                                    offerButtonHundler.sendEmptyMessage(1);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultModel> call, Throwable t) {
                                progress.dismiss();
                                if (t instanceof IOException){
                                    final Dialog   noInternet = AppConfig.InternetFaild(PostDetails.this);
                                    final Button btn = noInternet.findViewById(R.id.Retry);
                                    noInternet.show();
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            noInternet.cancel();

                                            loading.setVisibility(View.INVISIBLE);
                                            animation.stop();
                                            if (!isIAcceptedCoinsOffer)
                                            getOffer.setVisibility(View.VISIBLE);

                                        }
                                    });
                                }
                            }
                        });


      /*     addOffer = 1;

            Random rand = new Random();

            int  n = rand.nextInt(3000) + 1;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("QP","hello");
                    getLastSrCoin();                }
            }, n);

                    */
                    }
                }.start();

            }//second else


        } //first else


    } //get offer function button


    public void cancelDialogHaveNotEnoughCoins(View view) {
        dialog1.cancel();
    } // cancel dialog haven't enough coins

    public void DialogRecharege(View view) {

        Intent intent = new Intent(getBaseContext(), LoginScreen.class);
        startActivity(intent);
    }

    private void setPostionToUsersFireBase(){
        final DatabaseReference positionControl = FirebaseDatabase.getInstance().getReference().child("UserInOffer__data_"+offerID) ;
        positionControl.orderByChild("position").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    positionControl.child(data.getKey()).child("position").setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void CancelReciveOfferFromHimDialogue(View view) {

        String offerid = String.valueOf(offerID);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("online");

        //Query offerQuery = ref.orderByChild(phoneNo).equalTo("+923336091371");


        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        String key = singleSnapshot.getKey();
                        userOnline = singleSnapshot.getValue(UserOnline.class);

                        int offerid, userid;
                        offerid = userOnline.getOfferid();
                        userid = userOnline.getUserid2();
                        Log.i("QP", offerid + " : " + userid);

                        if (userID == userid && offerID == offerid) {
                            ref.child(key).setValue(null);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        DatabaseReference dbNode =
                FirebaseDatabase.getInstance().getReference().getRoot().child("online")
                        .child(offerid);
        dbNode.setValue(null);
        dialog3.cancel();
    }  // cancel dialog CancelReciveOfferFromHimDialogue


    public void fakePeople() {


        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("fakePeople");
        //Query offerQuery = ref.orderByChild("srCoin");

        Random rand = new Random();

        int randomNumber = rand.nextInt(53) + 1;
        randomN = String.valueOf(randomNumber);

        if (ref != null) {


            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                        String num = snapshot.getKey().toString();
                        if (randomN.equals(num)) {
                            FakePeople value = snapshot.getValue(FakePeople.class);
                            fake_firstName = value.getFirstName();
                            fake_lastName = value.getLastName();
                            fake_Image = value.getProfileImage();
                            fake_email = value.getEmail();
                            fake_id = value.getId();


                            Log.i("fakePeople", num + " : " +
                                    fake_firstName + " : " + fake_lastName + " : "
                                    + fake_Image + " : " + randomN + " : "
                                    + fake_id);
                            if (!srCoinsOfUsers.isEmpty()) {

                                addOnCoins++;
                                srCoin = srCoinsOfUsers.get(0);
                                srCoin = 1 + srCoin;
                            } else {

                                srCoin = 1 + srCoin;
                            }

                            userInOffer =
                                    new UserInOffer(fake_id, fake_id, offerID, srCoin, 0, fake_firstName
                                            , fake_lastName, fake_email, 1, 0, fake_Image, o_title, 0);
      /*  Toast.makeText(getBaseContext(),"Sr : "
        +srCoin,Toast.LENGTH_LONG).show();
         userInOfferList.add(userInOffer);*/

                            congratFname = firstName;
                            congratLname = lastName;
                            winnerId = userID;
                            winnerCoins = srCoin;
                            congratFirstName.setText(congratFname);
                            congratLastName.setText(congratLname);
                            congratFirstName1.setText(congratFname);
                            congratLastName2.setText(congratLname);

                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            final String key = mDatabase.push().getKey();
                            mDatabase.child("UserInOffer").child(key).setValue(userInOffer);


                            mDatabase.child("UserInOffer").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                                        String userid = String.valueOf(fake_id);
                                        String offerid = String.valueOf(offerID);

                                        String userIdFire = data.child("id").getValue().toString();
                                        String offerIdFire = data.child("offerId").getValue().toString();


                                        if (userid.equals(userIdFire) && offerid.equals(offerIdFire)) {
                                            data.getRef().child("position").setValue(1);
                                            break;

                                        } else if (!userid.equals(userIdFire) && offerid.equals(offerIdFire))
                                            data.getRef().child("position").setValue(0);

                 /*  Toast.makeText(getBaseContext(),"Update : \n"
                    +"child id : "+data.child("id").getValue() + ":user id : "+userid +"\n"
                                   +"child offer : "+data.child("offerId").getValue()
                                   + " offerid : "+offerid
                    , Toast.LENGTH_LONG).show();*/
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } // if


        /////////////////////////////////// make offer


    }


    public void AcceptReciveOfferFromHimDialogue(View view) {


        DatabaseReference mDatabase;
        String sId = String.valueOf(offerID);

        final int userid1 = (userOnline.getUserid1());
        final int userid2 = (userOnline.getUserid2());
        final int coinSend = (userOnline.getAmount());


        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref1 = database1.child("online");

        //Query offerQuery = ref.orderByChild(phoneNo).equalTo("+923336091371");


        if (ref1 != null) {
            ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                            String k = singleSnapshot.getKey();
                            userOnline = singleSnapshot.getValue(UserOnline.class);

                            Log.e("onlineAccept", k + "\n" + userOnline.getOfferid());


                            if (offerID == userOnline.getOfferid() &&
                                    userid1 == userOnline.getUserid1() &&
                                    userid2 == userOnline.getUserid2()) {
                                userOnline = new UserOnline(userid1, userid2, coinSend, 1, firstName, lastName, offerID);
                                ref1.child(k).setValue(userOnline);
                                SharedPreferences.Editor editor =
                                        getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

                                editor.putInt("coins", userCoin + coinSend);
                                editor.apply();
                                Log.e("onlineAcceptIf", ref1.child(k) + "\n" + userOnline.getOfferid());
                            } // if
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }); // offerhim
        } // if condition


        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("UserInOffer_" + offerID);
        //Query offerQuery = ref.orderByChild("srCoin");


        if (ref != null) {


            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInOffer value = snapshot.getValue(UserInOffer.class);

                        int usId = value.getUserId();


                        if (usId == userID) {
                            snapshot.getRef().child("expired").setValue(1);
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } // if


        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

        UserApi userApi = retrofit.create(UserApi.class);
        Call<ResultModel> updateUserCoinConnection =
                userApi.updateUserCoin(userID, userCoin + userOnline.getAmount());
        updateUserCoinConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                try {
                   /* Toast.makeText(getBaseContext(), "Done"
                        , Toast.LENGTH_LONG).show();*/


                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "exception" + (e.toString()), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Connection faild\n" +
                        "" + t.toString(), Toast.LENGTH_LONG).show();

            }
        });
        //Retrofit

        t_myCoins.setText(userCoin + userOnline.getAmount() + "");


        SharedPreferences settings = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putInt("coins", userCoin + userOnline.getAmount());
        prefEditor.apply();
        // getOffer.setVisibility(View.INVISIBLE);
        dialog3.cancel();

    } // cancel dialog CancelReciveOfferFromHimDialogue


    public void continueDialogCongratulations(View view) {


        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Log.e("remove## ", "" + offerID);
        Log.e("remove## ", "" + winnerKey);
        final DatabaseReference ref = database.child("Winner_" + offerID);
        ref.child(winnerKey).removeValue();
        if (userID == winnerId) {
            Toast.makeText(getBaseContext(), "Congratulations",
                    Toast.LENGTH_LONG).show();
            // winnerCoins minus from userCoin on DB
            Intent intent = new Intent(getBaseContext(), MyWinnerProducts.class);
            intent.putExtra("userID", userID);
            intent.putExtra("offerID", offerID);
            intent.putExtra("winnerCoins", winnerCoins);
            intent.putExtra("userCoin", userCoin);
            intent.putExtra("fromPostDetails", 2224);

            if (dialog2 != null && dialog2.isShowing()) {
                dialog2.dismiss();
            }
            startActivity(intent);

            recyclerView.setAdapter(null);
            finish();
        } else {
            recyclerView.setAdapter(null);
            Intent intent = new Intent(getBaseContext(), NavigationMenu.class);
            startActivity(intent);
            finish();
        }

    } //  continueDialogCongratulations function


    public void addOfferToWinners() {
        //Retrofit
        new Thread() {
            @Override
            public void run() {
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());
                UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> addToWinnerConnection =
                        userApi.addProductOnWinners(userID, offerID, winnerCoins);
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


    public void countViews() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("UserInOffer_" + offerID);
        //Query offerQuery = ref.orderByChild("srCoin");


        if (ref != null) {


            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInOffer value = snapshot.getValue(UserInOffer.class);

                        int usId = value.getUserId();
                        int vi = value.getView();

                        if (usId == userID && vi == 1) {
                            viewInhome++;
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } // if

    }


    public void tryAnotherOfferGoodLuckDialogue(View view) {
        if (dialog4 != null && dialog4.isShowing()) {
            dialog4.dismiss();
        }
        Intent intent = new Intent(getBaseContext(), NavigationMenu.class);
        startActivity(intent);
        finish();
    } // tryAnotherOfferGoodLuckDialogue Button


    public void imageSlider(ArrayList<String> urls) {

        SliderLayout sliderShow = (SliderLayout) findViewById(R.id.slider);


        for (int i = 0; i < urls.size(); i++) {

            Log.e("imageUrlss" + i, "");
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.image("https://speed-rocket.com/upload/products/" + urls.get(i));
            sliderShow.addSlider(textSliderView);
            Log.e("IMAGE", urls.get(i));
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Intent intent = new Intent(getBaseContext(), image_item.class);
                    intent.putExtra("imageList", imagesUrl);
                    startActivity(intent);
                }
            });
        }


    } // imageSlider function


    public void getLastSrCoin() {

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String key = mDatabase.push().getKey();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


            mDatabase.child("UserInOffer").runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {


                    for (MutableData data : mutableData.getChildren()) {


                        String offerid = String.valueOf(offerID);
                        Log.i("QP", "offerId : " + offerid);

                        String offerIdFire = data.child("offerId").getValue().toString();


                        if (offerid.equals(offerIdFire)) {

                            if (data.getValue() == null) {
                                srCoin = Integer.parseInt(data.child("srCoin").getValue().toString());

                            } else {
                                srCoin = Integer.parseInt(data.child("srCoin").getValue().toString()) + 1;

                                //  checkSrCoin = Integer.parseInt(data.child("srCoin").getValue().toString());
                            }
                        }

                    }
                    //Log.i("QP","srcoin : "+srCoin);


                    //insertInFireBase(5 , 5);


                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                }
            });

        /*    mDatabase.child("UserInOffer").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {


                        String offerid = String.valueOf(offerID);

                        String offerIdFire = data.child("offerId").getValue().toString();

                        if (offerid.equals(offerIdFire)) {

                            srCoin = Integer.parseInt(data.child("srCoin").getValue().toString())+1;
                          //  checkSrCoin = Integer.parseInt(data.child("srCoin").getValue().toString());

                        }

                    }

                    Log.i("QP","srcoin : "+srCoin);

                    insertInFireBase();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/

        } else {
            Toast.makeText(getBaseContext(), "Connection Faild"
                    , Toast.LENGTH_LONG).show();
        }
    } // get max srcoin in offer from firebase

    ChildEventListener maxCoinListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            UserInOffer value = dataSnapshot.getValue(UserInOffer.class);



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

    public void getMaxCoins() {

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String key = mDatabase.push().getKey();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            maxCoinRefrenace = mDatabase.child("UserInOffer_" + offerID);
            maxCoinRefrenace.addChildEventListener(maxCoinListener);

        }


    }

    public void insertInFireBase() {

        //   Log.i("QP1", "srCoin : " + srCoin);


//            if (srCoin > userCoin) {
//                Intent intent = new Intent(getBaseContext(), BuyCoins.class);
//                startActivity(intent);
//            } // check when press button offer if your coins less than srcions to recharge his coins
//
//            else {
//
//
//               if (viewInhome > 0) {
//                    userInOffer =
//                            new UserInOffer(userID, userID, offerID, srCoin, userCoin, firstName
//                                    , lastName, email, 0, 0, userProfileImage, o_title, 0);
//                } else if (viewInhome == 0) {
//                    userInOffer =
//                            new UserInOffer(userID, userID, offerID, srCoin, userCoin, firstName
//                                    , lastName, email, 1, 0, userProfileImage, o_title, 0);
//                }


        // congratFname = firstName;
        // congratLname = lastName;
        // // winnerId = userID;
        // winnerCoins = srCoin;
        //  congratFirstName.setText(congratFname);
        //  congratLastName.setText(congratLname);
        // congratFirstName1.setText(congratFname);
        // congratLastName2.setText(congratLname);


        // user already login


        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String key = mDatabase.push().getKey();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            try {


//                    mDatabase.child("UserInOffer_"+offerID).runTransaction(new Transaction.Handler() {
//                        @Override
//                        public Transaction.Result doTransaction(MutableData mutableData) {
//
//                         /* for (MutableData data : mutableData.getChildren()){
//
//                              String userid = String.valueOf(userID);
//                              String offerid = String.valueOf(offerID);
//
//                              String offerIdFire = data.child("offerId").getValue().toString();
//                              String userIdFire = data.child("userId").getValue().toString();
//
//                              if (userid.equals(userIdFire) && offerid.equals(offerIdFire)) {
//                                  data.child("position").setValue(1);
//                                  break;
//
//                              } else if (!userid.equals(userIdFire) && offerid.equals(offerIdFire))
//                                  data.child("position").setValue(0);
//
//                          }*/
//
//                        //   mDatabase.child("UserInOffer").child(key).setValue(userInOffer);
//
//                            return Transaction.success(mutableData);
//                        }
//
//                        @Override
//                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                           if(dataSnapshot != null) {
//                                for (DataSnapshot data : dataSnapshot.getChildren()) {
//
//                                    String userid = String.valueOf(userId);
//                                    String offerid = String.valueOf(offerID);
//                                    String srCoin  = String.valueOf(SrCoin) ;
//
//                                    String userIdFire = data.child("userId").getValue().toString();
//                                    String offerIdFire = data.child("id").getValue().toString();
//                                   int coinsFire =Integer.valueOf( data.child("srCoin").getValue().toString());
//
//                                    Log.e("offerid# insertF#" , offerid);
//                                    Log.e("offeridFire#insertF#" , offerIdFire);
//                                   // Log.e("userid##" , userid);
//                                  //  Log.e("useridFirebase##" , userIdFire);
//                                    Log.e("seCoin#insertF#" , srCoin);
//                                    Log.e("seCoinFire#insertF#" , ""+coinsFire+"\n\n");
//
//                                   if (offerid.equalsIgnoreCase(offerIdFire)&& SrCoin == coinsFire )
//                                    {
//                                        data.getRef().child("position").setValue(1);
//                                    }
//
//                                }
//                            }//if datasanpshot != null
//                        }
//                    }
//
//                    );

                mDatabase.child("UserInOffer_" + offerID).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            data.child("position").getRef().setValue(0);
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
                });


            } // try
            catch (Exception e) {
                Log.i("QPError : ", e.toString());
            }// catch

             /*   mDatabase.child("UserInOffer").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                            String userid = String.valueOf(userID);
                            String offerid = String.valueOf(offerID);

                            String userIdFire = data.child("id").getValue().toString();
                            String offerIdFire = data.child("offerId").getValue().toString();


                            if (userid.equals(userIdFire) && offerid.equals(offerIdFire)) {
                                data.getRef().child("position").setValue(1);
                                break;

                            } else if (!userid.equals(userIdFire) && offerid.equals(offerIdFire))
                                data.getRef().child("position").setValue(0);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

        } else {
            Toast.makeText(getBaseContext(), "Connection Faild"
                    , Toast.LENGTH_LONG).show();
        }
        // if serCoin != srcoin.get(0)
    }

    public void test() {
        //Retrofit
        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());
        UserApi userApi = retrofit.create(UserApi.class);
        Call<ResultModel> updateUserCoinConnection =
                userApi.test(offerID);
        updateUserCoinConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                try {
                      /*  Toast.makeText(getBaseContext(), "Done"
                                , Toast.LENGTH_LONG).show();*/

                    Log.i("QP", "done");

                } catch (Exception e) {
                    Log.i("QP", "Exception" + e.toString());
                }


            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                Log.i("QP", "Connection" + t.toString());
            }
        });
        //Retrofit
    }

    private void setWinner() {
        final DatabaseReference mDataBaseRef = FirebaseDatabase.getInstance().getReference();
        ConnectivityManager connecet = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connecet.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connecet.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            mDataBaseRef.child("UserInOffer_" + offerID).runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            data.getRef().child("position").setValue(0);
                        }


                        mDataBaseRef.child("UserInOffer_" + offerID).child(Key).child("position").setValue(1);
                    } catch (Exception e) {

                    }
                }
            });

        }

    }

    private class LoadingUsers extends AsyncTask<UserInOffer, Void, List<PersonalUser>> {

        @Override
        protected List<PersonalUser> doInBackground(UserInOffer... value) {

            //  Log.e("loadingusers##" , ""+getUserDataCheck);
            // String key = dataSnapshot.getKey();
            //setWinner();
            int sr = value[0].getSrCoin();

            String fname = value[0].getFirstName();
            String lname = value[0].getLastName();
            int fid = value[0].getUserId();
            String pImage = value[0].getProfileImage();
            PersonalUser p;

            Log.e("loadingusers##", "fname: " + fname + " " + lname);


            //  Log.e("sr##", ""+dataSnapshot.getKey());
            /*Log.e("sr##", ""+sr);

            Log.e("fname##", fname);
            Log.e("lname##", ""+lname);
            Log.e("fid##", ""+fid);
            Log.e("expired##", ""+value[0].getExpired());
            Log.e("coines##", ""+value[0].getCoins());
            Log.e("offerTitle##", ""+value[0].getOfferTitle());
            Log.e("finish##", ""+value[0].getFinish());
            Log.e("idd##", ""+value[0].getId());
            Log.e("email##", ""+value[0].getEmail());
            Log.e("view##", ""+value[0].getView());
            Log.e("position##", ""+value[0].getPosition());*/

            int offId = value[0].getId();
            // Toast.makeText(getBaseContext() , ""+offId , Toast.LENGTH_LONG).show();
            // Log.e("offerId##" , ""+value[0].getId());

            if (offerID == offId) {
                p = new PersonalUser(sr, fname, lname, fid, pImage);

                //  Log.e("p##" ,"!! null");
                userList.add(0, p);

            }

            if (userList.size() > 0) {
                congratFname = userList.get(0).getFirstName();
                congratLname = userList.get(0).getLastName();
                winnerCoins = userList.get(0).getSrCoin();
                winnerId = userList.get(0).getId();
//                Log.e("loadingusers##" , "fname: " + congratFname);
//                Log.e("loadingusers##" , "lname: " + congratLname);
//                Log.e("loadingusers##" , "coins: " + winnerCoins);
//                Log.e("loadingusers##" , "id: " + winnerId);


            }
            return userList;
        }

        @Override
        protected void onPostExecute(List<PersonalUser> personalUsers) {

            // updateUi(personalUsers);
            mAdapter.notifyDataSetChanged();
            congratFirstName.setText(congratFname);
            congratLastName.setText(congratLname);
            congratFirstName1.setText(congratFname);
            congratLastName2.setText(congratLname);

        }


    }

    private class LoadingLeastOfferList extends AsyncTask<DataSnapshot, Void, List<CurrentOffers>> {


        @Override
        protected List<CurrentOffers> doInBackground(DataSnapshot... dataSnapshot) {


            return offerList1;
        }

        @Override
        protected void onPostExecute(List<CurrentOffers> currentOffers) {


        }

    }

    ChildEventListener updateWinnerLisener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {


            OfferWinner mWinner = dataSnapshot.getValue(OfferWinner.class);
            if (mWinner.getOffer() == 1) {
                congratFname = mWinner.getFirstName();
                congratLname = mWinner.getLastName();
            }

            if (mWinner.getUserid() == userID && mWinner.getOffer() == 1) {

                winnerKey = dataSnapshot.getKey();
                winnerCoins = mWinner.getCoins();
                winnerId = mWinner.getUserid();
            }
            Log.e("updateWinner##", "fname: " + congratFname);
            Log.e("updateWinner##", "kwy: " + winnerKey);
            Log.e("updateWinner##", "lname: " + congratLname);
            Log.e("updateWinner##", "coins: " + winnerCoins);
            Log.e("updateWinner##", "id: " + winnerId);

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

    public void updateWinnerFromfireBase() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Log.e("updateWinner##", "fname: " + offerID);
        DatabaseReference ref = database.child("Winner_" + offerID);
        updateWinnerReferance = ref.orderByValue();
        if (ref != null) {
            updateWinnerReferance.addChildEventListener(updateWinnerLisener);
        }

    }

} // postdetails class

