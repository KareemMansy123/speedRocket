

package com.speedrocket.progmine.speedrocket.View.Activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Control.SpeedBanksAdapter;
import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.ServerResponse;
import com.speedrocket.progmine.speedrocket.Model.SpeedRocketBanks;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

/**
 * Created by Ibrahim on 8/29/2018.
 */

public class BankTransport extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
    File Image ;
    Menu menu;
    String customerName = "" , customerPhone = "" , customerAddress = "" ,userEmail="" ;
    double  price =0 ;
    int coinsQuantity = 0 ;
    TextView  dialogType , dialogTotalPrice ;
    int maxId = 0 ;
    EditText paymentDate , amount ;

    String datee = "", refnum = "";
    double aamount = 0;
    int bankID = 0 ;

    int numbers = 0 ;
    NavigationView navigationView;
    String imagePath ;
    DatePickerDialog.OnDateSetListener date ;
    Calendar myCalendar ;
    public static final int GET_FROM_GALLERY = 10;
    private static final int REQUEST_CAMERA = 1888;
    public static  boolean log  ;
    String productID = "" , TYPE = "" , Amount = "" ;
    String firstname = "", lastname = "", password = "", confirmpassword = "",
            email = "", gender = "", language = "", interest = "",
            personoalmobile = "", persontype = "", companyInterest = "", city = "", country = "";
    TextView nav_firstname, nav_lastname, nav_email , totalCost ;
    CircleImageView nav_profileimage, profileImage;
    String marchantRefNumber = "" , type_letter = "";
    String firstName , lastName  ,userProfileImage ,type , cost ;
    boolean login ;
    int userID = 0;
    boolean saveTempraryorder = false ;
    private Double shipping_cost = 0.0;
    Spinner speedRocketBanks ;
    RecyclerView dialogSpeedBanks ;
    Button dialogOk ;

    SpeedBanksAdapter mAdapter1;

    Dialog speedRocketBanksDialog ;

    List<SpeedRocketBanks> banks1 = new ArrayList<>();
    List<SpeedRocketBanks> realBanks = new ArrayList<>();
    ArrayAdapter<String> banks ;
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_transfer_navigation_view);
        setTitle(getString(R.string.bank_transport));

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
        Log.w("login##", "" + login);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        speedRocketBanksDialog = new Dialog(this); // Context, this, etc.
        speedRocketBanksDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        speedRocketBanksDialog.setContentView(R.layout.dialog_speed_banks);
        speedRocketBanksDialog.setCancelable(false);
        speedRocketBanksDialog.setCanceledOnTouchOutside(false);

        dialogSpeedBanks = speedRocketBanksDialog.findViewById(R.id.speed_banks);

        Intent inn = getIntent() ;
        Bundle b = inn.getExtras() ;
        if (b!= null){
            if (b.containsKey("type*"))
                type =(String)b.get("type*");
            if (b.containsKey("coast*"))
              cost = (String)b.get("coast*");


            if (b.containsKey("coinsQuantity")){
                type_letter="C";
                coinsQuantity = (int)b.get("coinsQuantity") ;
                price = Double.valueOf( b.get("price").toString());


                saveTempraryorder = true ;
            }else if (b.containsKey("advertise")){
                type_letter="A";
                marchantRefNumber = (String)b.get("refNumber");
                price = Double.valueOf( b.get("price").toString());
                Log.e("perice##","fawry offer : "+price);
                // initFawrySdk(false);
                saveTempraryorder = false ;
            }else if (b.containsKey("productId")){
                type_letter="P";
                productID =(String)b.get("productId") ;
                TYPE = (String)b.get("type") ;
                Amount =(String)b.get("amunts");
                price = Double.valueOf( b.get("price").toString());
                shipping_cost = (Double)b.get("shippingCoast");


                saveTempraryorder = true ;
            }

        }
        getMaxOrderId();
        dialogType = findViewById(R.id.confirm_dialog_type);
        dialogTotalPrice=findViewById(R.id.confirm_dialog_cost);

        dialogType.setText(type);
        dialogTotalPrice.setText(cost);

        paymentDate = findViewById(R.id.payment_date);
        amount = findViewById(R.id.amount);
        myCalendar = Calendar.getInstance();


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

       paymentDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               new DatePickerDialog(BankTransport.this, date, myCalendar
                       .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                       myCalendar.get(Calendar.DAY_OF_MONTH)).show();
           }
       });

       speedRocketBanks = findViewById(R.id.speed_rocket_banks);
       banks = new ArrayAdapter<String>(this , R.layout.spinner_item);
       banks . add("Select Bank");
       speedRocketBanks.setAdapter(banks);

       speedRocketBanks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if (realBanks.size()>0){
               bankID = realBanks.get(i-1).getId() ;
               Log.e("speedRocketBanks##",""+bankID);
               }else{
                   bankID = 0 ;
               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {
            bankID =0 ;
           }
       });




        mAdapter1 = new SpeedBanksAdapter(realBanks, BankTransport.this);
        RecyclerView.LayoutManager m1LayoutManager = new LinearLayoutManager(getBaseContext());
        dialogSpeedBanks.setLayoutManager(m1LayoutManager);
        dialogSpeedBanks.setItemAnimator(new DefaultItemAnimator());
        dialogSpeedBanks.setAdapter(mAdapter1);

        speedRocketBanksDialog.show();

        getSpeedRocketBanks();
    }

    public void ok(View v){

        speedRocketBanksDialog.cancel();
    }

    private void getSpeedRocketBanks(){

       final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

      Handler  handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                super.handleMessage(msg);
            }

        };
        progress.show();

        new Thread() {

            @Override
            public void run() {


                final Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());
                final UserApi api = retrofit.create(UserApi.class);
                Call<ResultModel> speedBanks = api.getAllSpeedRocketBanks() ;
                speedBanks.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                           banks1 = response.body().getSpeedBanks() ;
                        Log.e("banks##" ,""+ banks1.size());
                           for (int i =0 ; i < banks1.size() ; i++){
                               realBanks.add(new SpeedRocketBanks(banks1.get(i).getId() , banks1.get(i).getName(),banks1.get(i).getBankAccount() , banks1.get(i).getBankAddress()
                               ,banks1.get(i).getSwift(),
                                       banks1.get(i).getAccountName()
                               ));

                               mAdapter1.notifyDataSetChanged();
                              Log.e("banks##" , banks1.get(i).getName());
                               banks.add(banks1.get(i).getName());
                               banks.notifyDataSetChanged();
                           }

                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(BankTransport.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getSpeedRocketBanks();
                                }
                            });
                        }
                    }
                });


            }
        }.start();
    }

    private void updateLabel() {
        String myFormat = "yyyy-M-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        paymentDate.setText(sdf.format(myCalendar.getTime()));


    }// updateLabel function

    public void choseImage(View v){
        String[] items = new String[]{getString(R.string.gallary) , getString(R.string.pic_photo) , getString(R.string.cancel)};
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.add_photo);
       final int PERMISSION_REQUEST_CODE = 1;
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0 :
                        if (Build.VERSION.SDK_INT >= 23) {
                            //do your check here
                            ActivityCompat.requestPermissions(BankTransport.this, new String[]
                                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                            // ask user for permission to access storage // allow - deni

                        }
                            startActivityForResult(new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                    , GET_FROM_GALLERY);
                                    break;
                    case 1 :
                        if (ActivityCompat.checkSelfPermission(BankTransport.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(BankTransport.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(BankTransport.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }
                            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            BankTransport.this.startActivityForResult(camera, REQUEST_CAMERA);

                        break;
                    case 2 :
                        dialogInterface.cancel();
                        break;
                }

            }
        });
     dialog.show();
    }


    private void saveTempraryorder(final String productId , final String type ,final int userID ,final int coinsqty , final String refNumber ,
                                   final String type_letter , final String  itemQty ,final double shipping_cost , final double price,
                                   final String shopper_address , final String shopper_phone , final String shopper_name
    )


    {
        if (saveTempraryorder) {

            new Thread() {
                @Override
                public void run() {

                    final Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/",getApplicationContext());


                    final UserApi userApi = retrofit.create(UserApi.class);
                    final Call<ResultModel> saveProducts =
                            userApi.setShoppertrack(productId, type, userID, coinsqty, price, refNumber, type_letter, itemQty,shipping_cost, shopper_address, shopper_name, shopper_phone);
                    Log.e("saveTemprary##", "product id  : " + productId);
                    Log.e("saveTemprary##", "type  : " + type);
                    Log.e("saveTemprary##", "userId  : " + userID);
                    Log.e("saveTemprary##", "coins :  " + coinsqty);
                    Log.e("saveTemprary##", "refNum  :  " + refNumber);
                    Log.e("saveTemprary##", "type lettere : " + type_letter);
                    Log.e("saveTemprary##", "quty  : " + itemQty);

                    saveProducts.enqueue(new Callback<ResultModel>() {
                        @Override
                        public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                            try {

                                    Log.e("BankTransport##" , response.body().getMessage());
                            } catch (Exception e) {
                                Toast.makeText(getBaseContext(), "there is some server problems .. 401", Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResultModel> call, Throwable t) {

                            if (t instanceof IOException){
                                final Dialog   noInternet = AppConfig.InternetFaild(BankTransport.this);
                                final Button btn = noInternet.findViewById(R.id.Retry);
                                noInternet.show();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        noInternet.cancel();
                                        saveTempraryorder(productId , type ,userID,coinsqty,refNumber,type_letter,itemQty,shipping_cost,price,shopper_address,shopper_phone,shopper_name);
                                    }
                                });
                            }
                        }
                    });


                }
            }.start();
        }

    }


    @SuppressLint("HandlerLeak")
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

        handler =    new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };


        new Thread(){
            public void run()
            {

                final Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/",getApplicationContext());

                final ApiConfig userApi = retrofit.create(ApiConfig.class);
                final Call<ResultModel> getMaxId =
                        userApi.getMaxOrderId();

                getMaxId.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try{

                            Log.e("maxId##" , ""+response.body().getMaxOrdersId());
                            maxId = response.body().getMaxOrdersId() + 1;
                            Log.e("maxId##" , ""+maxId);
                            progress.dismiss();
                            Log.e("maxId##" , ""+type_letter);
                            Log.e("maxId##" , ""+maxId);
                            Log.e("maxId##" , ""+userID);
                            marchantRefNumber =type_letter+(maxId)+userID ;
                            Log.e("maxId##" , ""+marchantRefNumber);
                            saveTempraryorder(productID ,TYPE,userID,coinsQuantity ,marchantRefNumber , type_letter ,Amount ,shipping_cost , price , customerAddress,customerPhone,customerName);

                            //initFawrySdk(true);
                        }catch(Exception e) {
                            Log.e("error###" , e.toString());
                            Toast.makeText(getBaseContext() , "there is some server problems ..?" , Toast.LENGTH_LONG).show();
                        }



                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(BankTransport.this);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK) {

                if (requestCode == GET_FROM_GALLERY) {
                    onSelectFromGalleryResult(data);
                } else if (requestCode == REQUEST_CAMERA) {
                    onCaptureImageResult(data);
                }

                Log.i("QP", imagePath);
            }
            if (imagePath != null) {
                Image = new File(imagePath);
                if (Image.exists()) {
                    Log.e("file  :", "there is file ");
                }
            }
        }
    }

    public void onCaptureImageResult(Intent data)
    {
        if (data !=null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedimage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            Uri imageUri = getImageUri(getBaseContext() , bitmap);

            imagePath = getRealPathFromURI(imageUri);
        }


        Log.i("QP",imagePath);
    }

    public void supmit(View v){

        saveBankTransport();
    }
private void saveBankTransport (){
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

     refnum = marchantRefNumber ;
     datee = paymentDate.getText().toString() ;
     aamount = Double.valueOf( amount.getText().toString()) ;


    handler =    new Handler( ){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress.dismiss();
        }
    };
    if (!datee.equalsIgnoreCase("")&& aamount != 0 && imagePath != null && bankID!=0) {
        new Thread() {
            public void run() {


                    if (Image.exists()) {
                        RequestBody request = RequestBody.create(MediaType.parse("*/*"), Image);

                        MultipartBody.Part image = MultipartBody.Part.createFormData("receipt_Image", Image.getName(), request);
                        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), Image.getName());


                        final ApiConfig api = AppConfig.getRetrofit(getApplicationContext()).create(ApiConfig.class);
                        final Call<ServerResponse> saveTransporrt = api.uploadBncTransport(userID, name, image, bankID, datee, aamount, marchantRefNumber , 0);

                        saveTransporrt.enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                Log.e("bankTransfer##", response.body().getMessage());
                                progress.dismiss();
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                Log.e("bankTransfer##", t.toString());
                                progress.dismiss();
                            }
                        });
                    }


            }

        }.start();


}   else {

    Toast.makeText(getBaseContext(), "please complete all data ", Toast.LENGTH_LONG).show();
}
}
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

                Log.i("QP","imagePath : "+imagePath+"\n");
            }
            // upload image
        } // if

    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getBaseContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void shoppingCart(View view) {


        Log.e("shopping Caret", "ssssssssss");

        Intent intent = new Intent(BankTransport.this, shoppingCaretActivity.class);
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

        SearchManager searchManager = (SearchManager) BankTransport.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(BankTransport.this.getComponentName()));
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

}
