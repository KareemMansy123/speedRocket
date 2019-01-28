package com.speedrocket.progmine.speedrocket.View.Activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Control.ChooseProductAdapter;
import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.Company;
import com.speedrocket.progmine.speedrocket.Model.Offer;
import com.speedrocket.progmine.speedrocket.Model.Product;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.ServerResponse;
import com.speedrocket.progmine.speedrocket.Model.TraderPages;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class AddOffer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Calendar myCalendar , myCalendar1 ;
    EditText ed_startTime , ed_endTime , ed_enTitle , ed_arTitle , ed_enDescription , ed_arDescription , ed_price
            ,ed_discount , ed_offerImage , ed_minutes , ed_count , ed_srcoin;

    private RecyclerView recyclerView;
    private ChooseProductAdapter mAdapter;

    NotificationBadge mBadge ;
    String productIds="";
    RadioGroup radioGroup_packages ;
    RadioButton radioButton_firstPackage , radioButton_secondPackage ,
            radioButton_thirdPackage ,radioButton_fourthPackage ;

    String enTitle="" ,arTitle = "" , startTime ="" , endTime = "", mainImage="" , discount="";


    private ProgressDialog progress;
    private Handler handler;

    List<TraderPages> traderPAges  ;
    List <Product> productList ;
    List <Offer> offer ;
    int counter = 0 ;
    int MaxID = 0 ;
    Date dateStart , dateEnd ;
    ArrayList qty = new ArrayList() ;
    ArrayList<String> arrArTitle=new ArrayList<>() , arrEnTitle  = new ArrayList<>();
    String dispaly = "" ;
    int companyPosition , interestPosition , userId , companyId ,m , offerId , productId ,
            positionOfChosenProduct , packageChosen=0 , productIdChosen=0;

    ArrayAdapter<String> spinnerArrayAdapter , spinnerArrayAdapter1;
    private List<Company> companyList = new ArrayList<>();
    List<Company> CList;
    Company company;

    List<String> interstList ;

    Dialog chooseProductDialog ;

    public static final int GET_FROM_GALLERY = 10;
    private static final int REQUEST_CAMERA = 1888;

    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();

    String imagePath;

    List <String> imagePathList ;

    DatePickerDialog.OnDateSetListener date , date2;

    Button addOffer ;
    private List<Product> productList1 = new ArrayList<Product>();
    Product product;

    TextView txt_chooseProduct ;
    // menu
    String firstName , lastName , email="" , interest ,userProfileImage  ;
    boolean login ;
    int userID;
    String refNum ="" ;
    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;
    //menu

    int diffDaysInteger , diffMonthesInteger , diffYearsInteger= 0 ;
    TextView txt_rate , txt_rate1 , txt_cost , txt_totalCost;
        int traderPageId =-1 ;
    Button addOfferButton ;
    TextView [] mTextViewArr ;
    TextView firstTraderPage , secTraderPage , thirdTraderPage ;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_offer_navigation_menu);
        setTitle(R.string.addOffer);
        Fresco.initialize(getBaseContext());


        //menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mBadge = findViewById(R.id.badge);
        AppUtills.notificationBadge.setNotifecationBadgeByShared(mBadge , this);

        menu();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        firstName = prefs.getString("firstName", "");
        lastName = prefs.getString("lastName", "");
        email = prefs.getString("email", "");
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

        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userId=prefs1.getInt("id",0);
         myCalendar = Calendar.getInstance();
        myCalendar1 = Calendar.getInstance();

        ed_startTime= (EditText) findViewById(R.id.oStartTime);
        ed_endTime = (EditText) findViewById(R.id.oEndTime);

        firstTraderPage = findViewById(R.id.firstPage);
        secTraderPage = findViewById(R.id.secPage);
        thirdTraderPage = findViewById(R.id.thirdPage);


        // ed_discount = (EditText) findViewById(R.id.oDiscount);
             ed_offerImage = (EditText) findViewById(R.id.oOfferImage);

        final SeekBar seekBar = findViewById(R.id.seekBar);

  /*      radioGroup_packages = (RadioGroup) findViewById(R.id.rGPackage);
        radioGroup_packages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.firstPackage:

                        packageChosen=a1;
                        break;
                    case R.id.secondPackage:

                        packageChosen=2;
                        break;
                    case R.id.thirdPackage:

                        packageChosen=3;
                        break;
                    case R.id.fourthPackage:

                        packageChosen=4;
                        break;

                }
            }
        });
*/

        mTextViewArr = new TextView[]{firstTraderPage ,secTraderPage , thirdTraderPage};
        getMaxShopperTrackId();
        addOffer = (Button) findViewById(R.id.oOffer);


        spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            if(b.containsKey("companyId")){
            companyId=(int)b.get("companyId");
            getProducts();
            }

        }
        else {
            getProducts();
        }



        Log.i("QP","companyId"+companyId);


       // date picker
       date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

                diffDaysInteger = ((myCalendar1.get(Calendar.DAY_OF_MONTH)) - (myCalendar.get(Calendar.DAY_OF_MONTH))) ;
                diffMonthesInteger =  ((myCalendar1.get(Calendar.MONTH)) - (myCalendar.get(Calendar.MONTH))) ;
                diffYearsInteger = ((myCalendar1.get(Calendar.YEAR)) - (myCalendar.get(Calendar.YEAR))) ;

                if(diffMonthesInteger > 0)
                {
                    diffDaysInteger = diffDaysInteger + diffMonthesInteger*30 ;
                }

                txt_totalCost.setText(((seekBar.getProgress() + 5) * 5) * diffDaysInteger +"");

            }

        };
        date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();

                 diffDaysInteger = ((myCalendar1.get(Calendar.DAY_OF_MONTH)) - (myCalendar.get(Calendar.DAY_OF_MONTH))) ;
                 diffMonthesInteger =  ((myCalendar1.get(Calendar.MONTH)) - (myCalendar.get(Calendar.MONTH))) ;
                 diffYearsInteger = ((myCalendar1.get(Calendar.YEAR)) - (myCalendar.get(Calendar.YEAR))) ;

                if(diffMonthesInteger > 0)
                {
                    diffDaysInteger = diffDaysInteger + diffMonthesInteger*30 ;
                }

                txt_totalCost.setText(((seekBar.getProgress() + 5) * 5) * diffDaysInteger +"");
            }

        };




        ed_startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddOffer.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ed_endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddOffer.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // date picker


        chooseProductDialog = new Dialog(this); // Context, this, etc.
        chooseProductDialog.setContentView(R.layout.dialog_choose_product);

        recyclerView = (RecyclerView) chooseProductDialog.findViewById(R.id.listChooseProduct);

        mAdapter = new ChooseProductAdapter(productList1 ,AddOffer.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(llm);


        txt_chooseProduct = (TextView) findViewById(R.id.oProduct);

        txt_chooseProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               chooseProductDialog.show();
            }
        }); // click on textview to choose product

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));



        // set a change listener on the SeekBar

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();
        txt_rate = (TextView) findViewById(R.id.textViewRate);
        txt_rate1 = (TextView) findViewById(R.id.textViewRate1);
        txt_totalCost=(TextView) findViewById(R.id.textViewTotalCost);


        if(diffDaysInteger != 0) {


            if (progress < 15) {

                txt_rate1.setText((progress + 5)+"");
                packageChosen = progress;
                txt_cost = findViewById(R.id.textViewCost);
                txt_cost.setText((progress + 5) * 5 +"");
                txt_totalCost.setText(((progress + 5) * 5) * diffDaysInteger  +"");
            } else {

                txt_rate1.setText( (progress + 5) + "");
                packageChosen = progress;
                txt_cost = findViewById(R.id.textViewCost);
                txt_cost.setText((progress + 5) * 5 + "");
                txt_totalCost.setText(((progress + 5) * 5) * diffDaysInteger +"");

            }
        } // if

        else
        {
            if (progress < 15) {

                txt_rate1.setText( (progress + 5)+"");
                packageChosen = progress;
                txt_cost = findViewById(R.id.textViewCost);
                txt_cost.setText(((progress + 5) * 5)+"");
                txt_totalCost.setText(((progress + 5) * 5)+"");
            } else {

                txt_rate1.setText((progress + 5) + "");
                packageChosen = progress;
                txt_cost = findViewById(R.id.textViewCost);
                txt_cost.setText(((progress + 5) * 5)+"");
                txt_totalCost.setText(((progress + 5) * 5)+"");

            }
        } // else

        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutAddOffer);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });


        addOfferButton = (Button) findViewById(R.id.oOffer);
        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOfferButton ();
            }
        }); // button add offer



        getTraderPages();
    } // onCreate function

//http://speed-rocket.com/trader/{id}


    public void getTraderPages(){

        new Thread(){
            @Override
            public void run() {

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/" , AddOffer.this);

                ApiConfig api = retrofit.create(ApiConfig.class);

                final Call<ResultModel> traderPages = api.getTraderPages();

                traderPages.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        traderPAges = response.body().getTraders();
                        for(int i = 0 ; i < traderPAges.size() ; i++){
                            SpannableString content = null ;
                            if (i < mTextViewArr.length) {
                              if ( getSharedPreferences("MyPref" , MODE_PRIVATE).getString("langa","en").equalsIgnoreCase("ar")){
                               content = new SpannableString(traderPAges.get(i).getTitle_ar());
                              }else{
                                  content = new SpannableString(traderPAges.get(i).getTitle_en());
                              }
                                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                mTextViewArr[i].setText(content);
                                traderPageId = traderPAges.get(i).getId();
                                mTextViewArr[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://speed-rocket.com/trader/" + traderPageId));
                                        startActivity(browserIntent);
                                    }
                                });
                            }

                        }

//
//
//                        SpannableString content1 = new SpannableString(traderPAges.get(1).getTitle_ar());
//                        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
//                        secTraderPage.setText(content1);
//                        secTraderPage.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://speed-rocket.com/trader/"+traderPAges.get(1).getId()));
//                                startActivity(browserIntent);
//                            }
//                        });
//
//                        SpannableString content2 = new SpannableString(traderPAges.get(2).getTitle_ar());
//                        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
//                        thirdTraderPage.setText(content2);
//                        thirdTraderPage.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://speed-rocket.com/trader/"+traderPAges.get(2).getId()));
//                                startActivity(browserIntent);
//                            }
//                        });
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                    }
                });
            }
        }.start();

    }

    public void shoppingCart(View view ){

        //TODO shopping cart action here
        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (AddOffer.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            if(diffDaysInteger != 0) {
                if (progress < 15) {

                    txt_rate1.setText((progress + 5)+"");
                    packageChosen = progress;
                    txt_cost = findViewById(R.id.textViewCost);
                    txt_cost.setText((progress + 5) * 5 +"");
                    txt_totalCost.setText(((progress + 5) * 5) * diffDaysInteger  +"");
                } else {

                    txt_rate1.setText( (progress + 5) + "");
                    packageChosen = progress;
                    txt_cost = findViewById(R.id.textViewCost);
                    txt_cost.setText((progress + 5) * 5 + "");
                    txt_totalCost.setText(((progress + 5) * 5) * diffDaysInteger +"");

                }
            } // if

            else
            {
                if (progress < 15) {

                    txt_rate1.setText( (progress + 5)+"");
                    packageChosen = progress;
                    txt_cost = findViewById(R.id.textViewCost);
                    txt_cost.setText(((progress + 5) * 5)+"");
                    txt_totalCost.setText(((progress + 5) * 5)+"");
                } else {
                    ;
                    txt_rate1.setText((progress + 5) + "");
                    packageChosen = progress;
                    txt_cost = findViewById(R.id.textViewCost);
                    txt_cost.setText(((progress + 5) * 5)+"");
                    txt_totalCost.setText(((progress + 5) * 5)+"");


                }
            } // else
            packageChosen = progress;






        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            dispaly = "" ;
            qty = intent.getIntegerArrayListExtra("quantity");
            arrArTitle = intent.getStringArrayListExtra("arTitle");
            arrEnTitle = intent.getStringArrayListExtra("enTitle");
            if (getBaseContext().
                    getSharedPreferences("MyPref", MODE_PRIVATE).
                    getString("langa","").
                    equalsIgnoreCase("ar")){
                 for (String product : arrArTitle) {
                     dispaly= dispaly + " , "+product ;
                 }

            }else {

                for (String product : arrEnTitle) {
                    dispaly= dispaly +product + " , ";
                }
            }
        }
    };
    private void updateLabel() {
        String myFormat = "yyyy-M-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ed_startTime.setText(sdf.format(myCalendar.getTime()));


    }// updateLabel function

    private void updateLabel2() {
        String myFormat = "yyyy-M-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ed_endTime.setText(sdf.format(myCalendar1.getTime()));
    }// updateLabel function

    public void uploadOfferImageButton(View view)
    {
       /* int PERMISSION_REQUEST_CODE = a1;

        if (Build.VERSION.SDK_INT >= 23) {
            //do your check here
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

            // ask user for permission to access storage // allow - deni
        }

        startActivityForResult(new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                , GET_FROM_GALLERY);*/

        // handle crash on camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // start multiple photos selector
        Intent intent = new Intent(getBaseContext(), ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
        startActivityForResult(intent, REQUEST_CODE);


    } // function of uploadOfferImageButton

    // SelectImage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        // get selected images from selector
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;

                // show results in textview
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
                for(String result : mResults) {
                    sb.append(result).append("\n");

                }
                ed_offerImage.setText(sb.toString());

                /*for(int i = 0 ; i < mResults.size() ; i++) {
                    Log.i("QP", "Images : " + mResults.get(i).toString());
                }*/
            }
        }
       super.onActivityResult(requestCode, resultCode, data);

    /*    if (resultCode == RESULT_OK) {

            if (requestCode == GET_FROM_GALLERY) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }

            Log.i("QP", imagePath);
            ed_offerImage.setText(imagePath.toString());


        }*/
    } // on Activity result

    public void onSelectFromGalleryResult(Intent data)
    {
        if (data != null) {
            Uri imageUri = data.getData();
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = getRealPathFromURI(imageUri);

                imagePathList.add(imagePath);

                Log.i("QP","imagePath : "+imagePath+"\n"+"imagePathList : "+imagePathList.size());
            }
            // upload image
        } // if

    } // onSelectFromGalleryResult

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getBaseContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    } // getRealPathFromURI

    public void onCaptureImageResult(Intent data)
    {
        if (data !=null) {

           /* bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedimage = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

            Uri imageUri = data.getData();
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.ACTION_IMAGE_CAPTURE};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = getRealPathFromURI(imageUri);
            }


            Log.i("QP",imagePath);
        }
    } // onCaptureImageResult

    public void addOfferButton()
    {
            Log.e("addOffer##" , ""+packageChosen);
        if(!productList1.isEmpty()) {


            startTime = ed_startTime.getText().toString();
            endTime = ed_endTime.getText().toString();
            //  discount = ed_discount.getText().toString();

            if (startTime.equals("") || endTime.equals("")  || txt_chooseProduct.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Complete Fields", Toast.LENGTH_LONG).show();
                } // if condition
                else {
                    Log.i("QP", "Offer Data \n Image " + mainImage + "\n Pack" + packageChosen + "\n Product" +
                            productIds + "\n CompanyId" + companyId);
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
                    refNum = "A"+MaxID+userID ;
                    progress.show();
                    new Thread() {
                        public void run() {



                            discount = txt_rate1.getText().toString();
                            ApiConfig getResponse = AppConfig.getRetrofit(getApplicationContext()).create(ApiConfig.class);
                            Call call = getResponse.uploadFileAndOfferData( startTime, endTime, ""+packageChosen
                                    , productIds, packageChosen, companyId , refNum , Double.valueOf(txt_totalCost.getText().toString())); //qty array of ids of products chosen by user , you want make offer for these products
                            call.enqueue(new Callback() {

                                @Override
                                public void onResponse(Call call, Response response) {
                                    ServerResponse serverResponse = (ServerResponse) response.body();

                                    try {


                                        Log.i("QP1", ((ServerResponse) response.body()).getMessage1()  + "");



                                        progress.dismiss();

                                        Intent intent = new Intent(getBaseContext(), PaymentScreen.class);

                                        intent.putExtra("price" ,txt_totalCost.getText().toString());
                                        intent.putExtra("advertise" ,1);
                                        intent.putExtra("offerIds" , productIds);
                                        intent.putExtra("refNumber" , refNum);
                                        startActivity(intent);
                                        finish();
                                        //sendImagesWithOffer();
                                    } catch (Exception e) {
                                        Log.i("QP2", "errorE" + e.toString());
                                        progress.dismiss();
                                    }

                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {

                                    Toast.makeText(getBaseContext(), "Retry Connection Faild", Toast.LENGTH_LONG).show();
                                    Log.i("QP3", "fail\n" + t.toString());
                                    progress.dismiss();
                                }


                            });

                        }

                    }.start();
                } // else
            } // if productlist not empty

            else {
                Toast.makeText(getBaseContext(), "You should choose one product at least",
                        Toast.LENGTH_LONG).show();
            } // if productlist empty


    } // function of addOfferButton

    public  void getProducts ()
    {
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
                Retrofit retrofit  = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());


                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModel> getProductConnection = userApi.getProductsToCompanyProfile(companyId);

                getProductConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try
                        {


                            productList = response.body().getProducts();
                            for(int i =0 ; i< productList.size(); i++)
                            {
                                enTitle = productList.get(i).getEn_title();
                                productId = productList.get(i).getId();
                                arTitle = productList.get(i).getAr_title();


                                product = new Product(productId,enTitle,arTitle);

                                productList1.add(product);
                                mAdapter.notifyDataSetChanged();
                            }// or loop


                            progress.dismiss();

                        } // try
                        catch (Exception e)
                        {

                            progress.dismiss();

                        } // catch



                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

               /* Toast.makeText(getBaseContext(),"Connection Faild",
                        Toast.LENGTH_LONG).show();*/

                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(AddOffer.this);
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



// Retrofit
    }

       private void getMaxShopperTrackId(){
        new Thread(){
            @Override
            public void run() {



                final Retrofit retrofit =AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());
                final ApiConfig userApi = retrofit.create(ApiConfig.class);
                final Call<ResultModel> maxId = userApi.getMaxAdvertiseOrderId();
                maxId.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                       MaxID = response.body().getMaxOrdersId() + 1;
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(AddOffer.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getMaxShopperTrackId();
                                }
                            });
                        }

                    }
                });

            }
        }.start();



}

    public  void sendImagesWithOffer()
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


                for (int i = 0; i < mResults.size(); i++) {


                    ///////
                    File file = new File(mResults.get(i).toString());

                    // RequestBody userIdRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userID));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);

                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                    Log.i("QP22", "fileName : " + filename + "\n file : " + fileToUpload+
                            "  id :"+mResults.get(i).toString());


                    Retrofit retrofit =AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());

                    ApiConfig getResponse =retrofit.create(ApiConfig.class);
                    Call call = getResponse.uploadImagesWithOffer(filename, fileToUpload);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            ServerResponse serverResponse = (ServerResponse) response.body();

                            try {


                                counter++;
                                Log.i("QP1", ((ServerResponse) response.body()).getMessage() + " : Done" );

                                if(counter == mResults.size()) {

                                  /*  Toast.makeText(getBaseContext(),"successfully added",
                                            Toast.LENGTH_LONG).show();*/
                                    progress.dismiss();

                                    Intent intent = new Intent(getBaseContext(), PaymentScreen.class);

                                    intent.putExtra("price" ,txt_totalCost.getText().toString());
                                    intent.putExtra("advertise" ,1);
                                    intent.putExtra("refNumber" , refNum);
                                    startActivity(intent);
                                    finish();
                                }// if condition


                            } catch (Exception e) {
                                Log.i("QP2", "errorE" + e.toString());

                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                            progress.dismiss();
                            if (t instanceof IOException){
                                final Dialog   noInternet = AppConfig.InternetFaild(AddOffer.this);
                                final Button btn = noInternet.findViewById(R.id.Retry);
                                noInternet.show();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        noInternet.cancel();
                                       sendImagesWithOffer();
                                    }
                                });
                            }

                        }


                    });

                }// for loop


            }
        }.start();
    } // function of sendImagesWithOffer

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(R.string.areYouSure);
            dialog.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    dialogInterface.dismiss();
                }
            });
            dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
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

        SearchManager searchManager = (SearchManager) AddOffer.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(AddOffer.this.getComponentName()));
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

    public  void menu()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        menu = navigationView.getMenu();



        navigationView.setNavigationItemSelectedListener(this);

    } // function fill gategory on menu

    public void okChooseProduct(View view)
    {
        if(!qty.isEmpty()) {
            for (int i = 0; i < qty.size(); i++) {
                Log.i("QP", "qty" + qty.get(i));
            } // for
        } // if

         productIds = qty.get(0).toString();
        for (int i = 1 ; i<qty.size();i++)
        {
            productIds +=  ","+qty.get(i).toString();
        }

        if(!qty.isEmpty())
        txt_chooseProduct.setText(dispaly);
        else if (qty.isEmpty())
            txt_chooseProduct.setText("Choose Product");

        chooseProductDialog.cancel();
    } // choose product

    public void cancelDailogChooseProduct(View view)
    {
        chooseProductDialog.cancel();
    } // cancel dialog choose product
} // class add offer
