package com.speedrocket.progmine.speedrocket.View.Activites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.Category;
import com.speedrocket.progmine.speedrocket.Model.Company;
import com.speedrocket.progmine.speedrocket.Model.Product;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.ServerResponse;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class AddProduct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    EditText ed_enTitle , ed_arTitle , ed_enDescription , ed_arDescription , ed_productImage , ed_Price ;
    Spinner sp_companies ;
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();
    private final int SELECT_PHOTO = 1;
    Category category  ;
    int counter = 0 ;

    public static final int GET_FROM_GALLERY = 10;
    private static final int REQUEST_CAMERA = 1888;
    String imagePath="";
    int categoryId ;

  String categoryTitle ;
    ArrayAdapter<String> spinnerArrayAdapter ;
    String enTitle="" , arTitle="" , enDescription="" , arDesription="" , productImage="" , companyName="";
    String price;
    int userId , subProductId;

    int companyPosition , productId ;

    private List<Company> companyList = new ArrayList<>();
    List<Category> CList= new ArrayList<>(); ;
    Company company;
    List<Product> product ;
    List<Category> categoryList = new ArrayList<Category>();
    NotificationBadge mBadge ;
    int CompanyId ;


    String penTitle , parTitle , penDes , parDes , pproductImage , pprice ;
    EditText qty  ;

    Button addProduct ;
    private ProgressDialog progress;
    private Handler handler;
    private  int productQty =0 ;

    // menu
    String firstName , lastName , email="" , interest ,userProfileImage  ;
    boolean login ;
    int userID;
    TextView nav_firstname , nav_lastname , nav_email;
    CircleImageView nav_profileimage ;
    public static  boolean log  ;
    Menu menu;
    NavigationView navigationView;
        ImageView image1 , image2 , image3 , image4 , image5 ;
        LinearLayout imageLayout ;
    //menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_navigation_menu);
        setTitle(R.string.addProduct);
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

        qty = findViewById(R.id.quantity);
        ed_enTitle = (EditText) findViewById(R.id.pEnName);
        ed_arTitle = (EditText) findViewById(R.id.pArName);
        ed_enDescription= (EditText) findViewById(R.id.pEndescription);
        ed_arDescription = (EditText) findViewById(R.id.pArDescription);
        ed_productImage = (EditText) findViewById(R.id.pProductImage);
        ed_Price = (EditText) findViewById(R.id.pPrice);

        addProduct = (Button) findViewById(R.id.addP);

        sp_companies = (Spinner) findViewById(R.id.pCompanies);


        spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item);





        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b.containsKey("productId"))
        {
            Log.e("countainKey" , "hereProduct");
            productId=(int)b.get("productId");
            addProduct.setText(getString(R.string.update));
            getProductData();
        }
        else if  (   b.containsKey("companyId")) {
            Log.e("countainKey" , "hereCompany");
             CompanyId=(int)b.get("companyId");
            Log.e("CombanyId##" , ""+CompanyId);
             getCompany(0);
        }



        sp_companies.setAdapter(spinnerArrayAdapter);


        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutAddProduct);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

    } // onCreate function

    public void shoppingCart(View view ){


        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (AddProduct.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public void uploadProductImage(View view)
    {
        int PERMISSION_REQUEST_CODE = 1;

        if (Build.VERSION.SDK_INT >= 23) {
            //do your check here
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            // ask user for permission to access storage // allow - deni

        }

        startActivityForResult(new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            , GET_FROM_GALLERY);


    } // function uploadProductImage

    public void addProductButton(View view)
    {
        if(addProduct.getText().equals(getString(R.string.update)))
        {
            Log.i("QPUpdate","hi");
             updateProductToServer();
        }
        else
        {
            Log.i("QPAdd","hi");
            addProductToServer();

        }

    } // function addProductButton

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
                ed_productImage.setText(sb.toString());

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

    public void uploadProductImageButton(View view)
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // start multiple photos selector
        Intent intent = new Intent(getBaseContext(), ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 4);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
        startActivityForResult(intent, REQUEST_CODE);
    } // function of uploadOfferImageButton

    public void deleteProductImage(final int productId){
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
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());

                ApiConfig getResponse = retrofit.create(ApiConfig.class);

                Call call = getResponse.deleteProductImage(productId);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.e("update##" ,((ResultModel) response.body()).getMessage());
                        progress.dismiss();
                        sendImagesWithProduct(productId);

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(AddProduct.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    deleteProductImage(productId);
                                }
                            });
                        }
                    }
                });

            }
        }.start();



    }
    public  void sendImagesWithProduct(final int productId ) {
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


                for (int i = 1; i < mResults.size(); i++) {


                    ///////

                    File file = new File(mResults.get(i).toString());

                    // RequestBody userIdRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userID));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);

                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                    Log.i("QP22", "fileName : " + filename + "\n file : " + fileToUpload +
                            "  id :" + mResults.get(i).toString());


                    Retrofit retrofit = AppConfig.getRetrofit(getApplicationContext());

                    ApiConfig getResponse = retrofit.create(ApiConfig.class);
                    Call call = getResponse.uploadImagewithProduct(filename, fileToUpload, productId);
                    call.enqueue(new Callback() {

                        @Override
                        public void onResponse(Call call, Response response) {
                            ServerResponse serverResponse = (ServerResponse) response.body();

                            try {


                                counter++;
                                Log.i("QP1", ((ServerResponse) response.body()).getMessage() + " : Done");

                                if (counter == mResults.size()-1) {

                                  /*  Toast.makeText(getBaseContext(),"successfully added",
                                            Toast.LENGTH_LONG).show();*/
                                    progress.dismiss();

                                    finish();
//                                    Intent intent = new Intent(getBaseContext(), PaymentScreen.class);
//
//                                    intent.putExtra("price" ,txt_totalCost.getText().toString());
//                                    intent.putExtra("advertise" ,1);
//                                    startActivity(intent);
//                                    finish();
                                }// if condition


                            } catch (Exception e) {
                                Log.i("QP2", "errorE" + e.toString());
                                progress.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                            progress.dismiss();
                            if (t instanceof IOException){
                                final Dialog   noInternet = AppConfig.InternetFaild(AddProduct.this);
                                final Button btn = noInternet.findViewById(R.id.Retry);
                                noInternet.show();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        noInternet.cancel();
                                       sendImagesWithProduct(productId);
                                    }
                                });
                            }

                        }


                    });


                }// for loop


            }
        }.start();


    } // function of sendImagesWithOffer

    public  void addProductToServer ()
    {
        enTitle = ed_enTitle.getText().toString();
        arTitle = ed_arTitle.getText().toString();
        enDescription = ed_enDescription.getText().toString();
        arDesription = ed_arDescription.getText().toString();
        productImage = mResults.get(0).toString();
        price =ed_Price.getText().toString();
        companyPosition = sp_companies.getSelectedItemPosition();
        productQty = Integer.valueOf(qty.getText().toString()) ;
        if (!CList.isEmpty())
        { categoryId =  CList.get(companyPosition).getId();}
        else
        {categoryId = (int)getIntent().getExtras().get("categoryId");}



        if(enTitle.equals("") || arTitle.equals("") || enDescription.equals("") || arDesription.equals("")
                || productImage.equals("") || price.equals("") || productQty == 0)

            Toast.makeText(getBaseContext(),"Complete Fields",Toast.LENGTH_LONG).show();


        else {
            if (isProbablyArabic(arTitle)&&  isProbablyArabic(arDesription)) {

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

                        ///////
                        File file = new File(productImage);
                        Log.e("QP3" , productImage);
                        // RequestBody userIdRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userID));
                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);

                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                        Log.i("QP22", "fileName : " + filename + "\n file : " + fileToUpload);
                        Log.i("CombanyId##", "companyId: : " + CompanyId );
                        Log.i("CombanyId##", "companyId: : " +categoryId );
                        ApiConfig getResponse = AppConfig.getRetrofit(getApplicationContext()).create(ApiConfig.class);
                        Call call = getResponse.uploadFileAndProductData(filename, fileToUpload, enTitle, arTitle, enDescription,
                                          arDesription, price,productQty,CompanyId ,categoryId);
                        call.enqueue(new Callback() {

                            @Override
                            public void onResponse(Call call, Response response) {
                                ServerResponse serverResponse = (ServerResponse) response.body();

                                    try {


                                        Log.i("QP1", serverResponse.getMessage() + "productId  : "+Integer.valueOf(serverResponse.getProduct_id()));


                                        progress.dismiss();
                                         sendImagesWithProduct(Integer.valueOf(serverResponse.getProduct_id()) );
                                    }catch(Exception e){
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
            }else {
                Toast.makeText(getBaseContext(), R.string.arabic_namee_warrning,Toast.LENGTH_LONG).show();
            }
        } // else





    } // add to server


    public  void  updateProductToServer()
    {
        enTitle = ed_enTitle.getText().toString();
        arTitle = ed_arTitle.getText().toString();
        enDescription = ed_enDescription.getText().toString();
        arDesription = ed_arDescription.getText().toString();
        productImage = !mResults.isEmpty()? mResults.get(0).toString():"";
        price =ed_Price.getText().toString();
        productQty = Integer.valueOf(qty.getText().toString()) ;
        companyPosition = sp_companies.getSelectedItemPosition();

        if (!CList.isEmpty())
        { categoryId =  CList.get(companyPosition).getId();}
        else
        {categoryId = (int)getIntent().getExtras().get("categoryId");}


        if(enTitle.equals("") || arTitle.equals("") || enDescription.equals("") || arDesription.equals("")
                || price.equals("") || productQty == 0)

            Toast.makeText(getBaseContext(),"Complete Fields",Toast.LENGTH_LONG).show();


        else {

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


                    if(productImage.equals(""))
                    {
                        Log.i("QPE1",  "Done");
                        ApiConfig getResponse = AppConfig.getRetrofit(getApplicationContext()).create(ApiConfig.class);
                        Call call = getResponse.uploadFileAndProductData1(productId, enTitle, arTitle, enDescription,
                                arDesription, price,productQty ,CompanyId , categoryId);
                        call.enqueue(new Callback() {

                            @Override
                            public void onResponse(Call call, Response response) {
                                ServerResponse serverResponse = (ServerResponse) response.body();

                                try {


                                    Log.i("QPE1",  "Done");

                                 //
                                    finish();

                                    progress.dismiss();
                                } catch (Exception e) {
                                    Log.i("QPE2", "errorE" + e.toString());
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
                    } // if imahepath


                    else {
                        Log.i("QPE1",  "Done2");
                        ///////
                        File file = new File(productImage);

                        // RequestBody userIdRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userID));
                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);

                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                        Log.i("QP22", "fileName : " + filename + "\n file : " + fileToUpload);

                        ApiConfig getResponse = AppConfig.getRetrofit(getApplicationContext()).create(ApiConfig.class);
                        Call call = getResponse.uploadFileAndProductData2(filename, fileToUpload,productId, enTitle, arTitle, enDescription,
                                arDesription, price,CompanyId , categoryId);
                        call.enqueue(new Callback() {

                            @Override
                            public void onResponse(Call call, Response response) {
                                ServerResponse serverResponse = (ServerResponse) response.body();

                                try {

                                    Log.i("QP1",((ServerResponse) response.body()).getMessage());

                                    //ToDO check if every thing is OK
                                    progress.dismiss();
                                    deleteProductImage(productId);


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
                    } // else imagepath

                }

            }.start();
        } // else





    } // update on server

    public  void  getCompany (final int subCategoryId)
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
        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://speed-rocket.com/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        final UserApi userApi = retrofit.create(UserApi.class);

        final Call<ResultModel> getCompanyConnection = userApi.getCategoryById(CompanyId);

        getCompanyConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                try
                {
                    Log.e("categoryId##" , "************************there is response***********************");
                    categoryList = response.body().getCategory();
                   // Log.e("categoryId##" , categoryList.get(1).getEn_title());
                    for(int i = 0 ; i< categoryList.size() ; i++)
                    {
                            categoryId = categoryList.get(i).getId();
                            Log.e("categoryId##" , ""+categoryId);
                            categoryTitle = categoryList.get(i).getEn_title();
                            String CatageroArTitle = categoryList.get(i).getAr_title();

                            category = new Category(categoryId,categoryTitle ,CatageroArTitle);

                            CList.add(category);
                        if (getSharedPreferences("MyPref", Context.MODE_PRIVATE).getString("langa","").equalsIgnoreCase("ar")){
                            spinnerArrayAdapter.add(categoryList.get(i).getAr_title());
                        }else{
                            spinnerArrayAdapter.add(categoryList.get(i).getEn_title());
                        }
                            spinnerArrayAdapter.notifyDataSetChanged();

                    } // for loop
                    if (addProduct.getText().equals(getString(R.string.update))){
                       // Log.e("categoryId##" , "size "+CList.size() );
                        for (int i = 0 ; i < CList.size() ; i++){

                            if (CList.get(i).getId() == subCategoryId){
                               // Log.e("categoryId##" , "categoryName  "+CList.get(i).getEn_title() );
                                sp_companies.setSelection(i);
                            }
                        }

                    }


                    progress.dismiss();
                } // try
                catch (Exception e)
                {
                       Log.e("categoryId##" , e.toString());
                   /* Toast.makeText(getBaseContext(),"Connection Success\n" +
                                    "Exception Navigation menu\n"+e.toString(),
                            Toast.LENGTH_LONG).show();*/progress.dismiss();

                } // catch
            } // onResponse

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

                Toast.makeText(getBaseContext(),"Connection Faild",
                        Toast.LENGTH_LONG).show();progress.dismiss();
                        Log.e("connectionError" , t.toString()) ;

            } // on Failure
        });

            }

        }.start();


// Retrofit
    } // function of getCompany


    // SelectImage
  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == GET_FROM_GALLERY) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }

            Log.i("QP",imagePath);
            ed_productImage.setText(imagePath.toString());
        }
    } // on Activity result*/





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


    public  void  getProductData()
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

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> getProductConnection =
                        userApi.getProductDetails(productId);

                getProductConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {

                             product = response.body().getProducts();

                            penTitle= product.get(0).getEn_title();
                            parTitle= product.get(0).getAr_title();
                            penDes= product.get(0).getEn_discription();
                            parDes= product.get(0).getAr_discription();
                            pproductImage= product.get(0).getImage();
                            pprice= String.valueOf(product.get(0).getPrice());
                            CompanyId = product.get(0).getCompanyId();
                            subProductId = product.get(0).getSub_categoryId();
                            productQty = product.get(0).getProductQty() ;


                            ed_enTitle.setText(penTitle);
                            ed_arTitle.setText(parTitle);
                            ed_enDescription.setText(penDes);
                            ed_arDescription.setText(parDes);
                            ed_productImage.setText(pproductImage);
                            ed_Price.setText(pprice);

                           qty.setText(""+product.get(0).getProductQty());

                            Log.i("QP","/"+penTitle+"/"+pproductImage);

                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Connection Success\n" +
                                            "Exception"+e.toString()
                                    ,Toast.LENGTH_LONG).show();


                        }
                        progress.dismiss();
                        Log.e("updateProduct##" , "subCategoryId  " + subProductId);
                        getCompany(subProductId);
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(AddProduct.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getProductData();
                                }
                            });
                        }


                    }
                });
                //Retrofit

            }

        }.start();



    } // function of getProductData


    // SelectImage


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

        SearchManager searchManager = (SearchManager) AddProduct.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(AddProduct.this.getComponentName()));
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
}
