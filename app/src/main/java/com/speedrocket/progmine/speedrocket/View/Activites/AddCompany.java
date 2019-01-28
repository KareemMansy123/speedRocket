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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.Category;
import com.speedrocket.progmine.speedrocket.Model.Company;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.ServerResponse;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class    AddCompany extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    NotificationBadge mBadge ;
    EditText ed_enName , ed_arName , ed_email , ed_companyLogo , ed_mobile , ed_fax ;
    Spinner sp_country , sp_city , sp_category ;
    public static final int GET_FROM_GALLERY = 10;
    private static final int REQUEST_CAMERA = 1888;
    List<Category> categoryList;

    List<Company> company ;
    int CategoryId ;
    String companyEnName , companyLogo , companyEmail ,companyArName , companyFax , companyMobile , cityData = "" , countryData = "";


    ArrayAdapter<String> spinnerArrayAdapter;
    private List<Category> offerList = new ArrayList<>();

    String imagePath="";
    String enName="";
    String arName="";
    String mobile="";
    String fax="";
    String country="";
    String city="";
    int categoryS;
    String logo="";
    String categoryTitle="";

    int categoryId , userId , copmpanyId=0 ;

    Category category;

    Button bt_addCompany ;



    private ProgressDialog progress;
    private Handler handler;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_company_navigation_menu);
        setTitle(R.string.addCompany);
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


        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userId=prefs1.getInt("id",0);


        ed_enName = (EditText) findViewById(R.id.cEnName);
        ed_arName = (EditText) findViewById(R.id.cArName);
        ed_email = (EditText) findViewById(R.id.cEmail);
        ed_companyLogo = (EditText) findViewById(R.id.cCompanyLogo);
        ed_mobile = (EditText) findViewById(R.id.cMobile);
        ed_fax = (EditText) findViewById(R.id.cFax);

        sp_country = (Spinner) findViewById(R.id.cCountry);
        sp_city = (Spinner) findViewById(R.id.cCity);
        sp_category= (Spinner) findViewById(R.id.cCategory);


        bt_addCompany = (Button) findViewById(R.id.cAddCompany);


        spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item);



        sp_category.setAdapter(spinnerArrayAdapter);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.country, R.layout.spinner_item);
        sp_country.setAdapter(adapter1);

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.cityEgypt, R.layout.spinner_item);
        sp_city.setAdapter(adapter2);

        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0 :
                        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(view.getContext(), R.array.cityEgypt,
                                R.layout.spinner_item);
                        sp_city.setAdapter(adapter2);
                        break;
                    case 1 :
                        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(view.getContext(), R.array.cityEmirat,
                                R.layout.spinner_item);
                        sp_city.setAdapter(adapter3);
                        break;
                    case 2 :
                        ArrayAdapter adapter4 = ArrayAdapter.createFromResource(view.getContext(), R.array.cityKuwait,
                                R.layout.spinner_item);
                        sp_city.setAdapter(adapter4);
                        break;
                    case 3 :
                        ArrayAdapter adapter5 = ArrayAdapter.createFromResource(view.getContext(), R.array.citysaudi,
                                R.layout.spinner_item);
                        sp_city.setAdapter(adapter5);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            if (b.containsKey("companyId")) {
                copmpanyId = (int) b.get("companyId");
                bt_addCompany.setText("Update");
                getCompanyDataToUpdate();
            }
        }
       else {
            getCategory(0);
        }


        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutAddCompany);
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


    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public void shoppingCart(View view ){

        //TODO shopping cart action here
        Log.e("shopping Caret" , "ssssssssss");

        Intent intent = new Intent (AddCompany.this , shoppingCaretActivity.class);
        startActivity(intent);
        finish();
    }
    public void uploadCompanyLogo(View view)
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

        //ed_companyLogo.setText(imagePath.toString());

    } // uploadCompanyLogo function

    public void addCompanyButton(View view)
    {

        if(bt_addCompany.getText().equals("Update"))
        {
            Log.i("QPUpdate","hi");
            updateCompanyToserver();
        }
        else
        {
            Log.i("QPAdd","hi");
            addCopmanyToServer();

        }

    } // addCompanyButton function

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }
    public  void addCopmanyToServer ()
    {
        enName = ed_enName.getText().toString();
        arName = ed_arName.getText().toString();
        email = ed_email.getText().toString();
        mobile = ed_mobile.getText().toString();
        fax = ed_fax.getText().toString();
        logo = ed_companyLogo.getText().toString();

        country = sp_country.getSelectedItem().toString();
        city = sp_city.getSelectedItem().toString();
        categoryS = sp_category.getSelectedItemPosition();

        if(enName.equals("") || arName.equals("") || email.equals("") || mobile.equals("")
                || fax.equals("") || logo.equals(""))

            Toast.makeText(AddCompany.this,"Complete Fields",Toast.LENGTH_LONG).show();


        else {
            if (isProbablyArabic(arName)) {



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
                    File file = new File(imagePath);

                    // RequestBody userIdRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userID));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);

                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                    ApiConfig getResponse = AppConfig.getRetrofit(getApplicationContext()).create(ApiConfig.class);
                    Call call = getResponse.uploadFileAndCompanyProfile(filename, fileToUpload, enName, arName,
                            email, "1", offerList.get(categoryS).getId(), "3", mobile, fax, userId);
                    call.enqueue(new Callback() {

                        @Override
                        public void onResponse(Call call, Response response) {
                            ServerResponse serverResponse = (ServerResponse) response.body();

                            try {


                                Log.i("QP1", ((ServerResponse) response.body()).getMessage() + "");

                                Intent intent = new Intent(AddCompany.this, MyCompanyProfile.class);
                                startActivity(intent);
                                progress.dismiss();
                            }catch (Exception e) {
                                Log.i("QP2", "errorE" + e.toString());
                                progress.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            progress.dismiss();
                            if (t instanceof IOException){
                                final Dialog   noInternet = AppConfig.InternetFaild(AddCompany.this);
                                final Button btn = noInternet.findViewById(R.id.Retry);
                                noInternet.show();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        noInternet.cancel();
                                       addCopmanyToServer();
                                    }
                                });
                            }
                        }


                    });

                }

            }.start();
        }else {
                Toast.makeText(AddCompany.this, R.string.arabic_namee_warrning,Toast.LENGTH_LONG).show();
            }
        }//else

    } // function addCopmanyToServer


    public  void updateCompanyToserver()
    {
        enName = ed_enName.getText().toString();
        arName = ed_arName.getText().toString();
        email = ed_email.getText().toString();
        mobile = ed_mobile.getText().toString();
        fax = ed_fax.getText().toString();
        logo = ed_companyLogo.getText().toString();

        country = sp_country.getSelectedItem().toString();
        city = sp_city.getSelectedItem().toString();
        categoryS = sp_category.getSelectedItemPosition();

        if(enName.equals("") || arName.equals("") || email.equals("") || mobile.equals("")
                || fax.equals("") || logo.equals(""))

            Toast.makeText(AddCompany.this,"Complete Fields",Toast.LENGTH_LONG).show();


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


                    Log.i("QP","image : "+imagePath);
                    ///////
                    if (imagePath.equals("")) {



                        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());
                        ApiConfig getResponse = retrofit.create(ApiConfig.class);
                        Call call = getResponse.uploadFileAndUpdateCompanyProfile2(copmpanyId, enName, arName,
                                email, "1", offerList.get(categoryS).getId(), "3", mobile, fax, userId);
                        call.enqueue(new Callback() {

                            @Override
                            public void onResponse(Call call, Response response) {
                                ServerResponse serverResponse = (ServerResponse) response.body();

                                try {


                                    Log.i("QP1U", ((ServerResponse) response.body()).getMessage() + "");

                                    Intent intent = new Intent(AddCompany.this, MyCompanyProfile.class);
                                    startActivity(intent);
                                    progress.dismiss();
                                } catch (Exception e) {
                                    Log.i("QP2U", "errorE" + e.toString());
                                    progress.dismiss();
                                }

                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                progress.dismiss();
                                if (t instanceof IOException){
                                  final Dialog noInternet = AppConfig.InternetFaild(AddCompany.this);
                                    final Button btn = noInternet.findViewById(R.id.Retry);
                                    noInternet.show();
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            noInternet.cancel();
                                            updateCompanyToserver();
                                        }
                                    });
                                }
                            }


                        });

                    } // if

                    else {
                        File file = new File(imagePath);

                        // RequestBody userIdRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userID));
                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);

                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                        ApiConfig getResponse = AppConfig.getRetrofit(getApplicationContext()).create(ApiConfig.class);
                        Call call = getResponse.uploadFileAndUpdateCompanyProfile( filename, fileToUpload,copmpanyId, enName, arName,
                                email, "1", offerList.get(categoryS).getId(), "3", mobile, fax, userId);
                        call.enqueue(new Callback() {

                            @Override
                            public void onResponse(Call call, Response response) {
                                ServerResponse serverResponse = (ServerResponse) response.body();

                                try {


                                    Log.i("QP1", ((ServerResponse) response.body()).getMessage() + "");

                                    Intent intent = new Intent(AddCompany.this, MyCompanyProfile.class);
                                    startActivity(intent);
                                    progress.dismiss();
                                } catch (Exception e) {
                                    Log.i("QP2", "errorE" + e.toString());
                                    progress.dismiss();
                                }

                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                progress.dismiss();
                                if (t instanceof IOException){
                                    final Dialog noInternet = AppConfig.InternetFaild(AddCompany.this);
                                    final Button btn = noInternet.findViewById(R.id.Retry);
                                    noInternet.show();
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            noInternet.cancel();
                                            updateCompanyToserver();
                                        }
                                    });
                                }
                            }


                        });

                    }
                } // else



            }.start();

        }//else

    } // function updateCompanyToserver


    public  void getCategory( final int check)
    {
        //Retrofit

        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());
        final UserApi userApi = retrofit.create(UserApi.class);

        final Call<ResultModel> getCategoryConnection = userApi.getCategory();

        getCategoryConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                try
                {
                    categoryList = response.body().getCategory();

                    for(int i = 0 ; i< categoryList.size() ; i++)
                    {
                        if (categoryList.get(i).getSubCategory() == 0){
                            categoryId = categoryList.get(i).getId();
                            categoryTitle = categoryList.get(i).getEn_title();
                            String CatageroArTitle = categoryList.get(i).getAr_title();
                            category = new Category(categoryId,categoryTitle , CatageroArTitle);
                            offerList.add(category);
                            if (getSharedPreferences("MyPref", Context.MODE_PRIVATE).getString("langa","").equalsIgnoreCase("ar")){
                                spinnerArrayAdapter.add(categoryList.get(i).getAr_title());
                            }else{
                                spinnerArrayAdapter.add(categoryList.get(i).getEn_title());
                                Log.e("category" , categoryList.get(i).getEn_title());
                            }

                            spinnerArrayAdapter.notifyDataSetChanged();
                        }
                    } // for loop


                   /* Toast.makeText(AddCompany.this,"Connection Success\n"
                                    ,

                            Toast.LENGTH_LONG).show();*/

                   if (check == 1){
                  Log.e("category" , "if : "+offerList.size() );
                       for (int i = 0 ; i<offerList.size() ; i++){
                           Log.e("category" , "for");
                           if (offerList.get(i).getId() == CategoryId){
                               Log.e("category" , "if : "+ i);
                               sp_category.setSelection(i);
                               break;
                           }
                       }

                   }


                } // try
                catch (Exception e)
                {
                    Log.e("category", e.toString());

                   /* Toast.makeText(AddCompany.this,"Connection Success\n" +
                                    "Exception Navigation menu\n"+e.toString(),
                            Toast.LENGTH_LONG).show();*/

                } // catch
            } // onResponse

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

                progress.dismiss();
                if (t instanceof IOException){
                    final Dialog noInternet = AppConfig.InternetFaild(AddCompany.this);
                    final Button btn = noInternet.findViewById(R.id.Retry);
                    noInternet.show();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noInternet.cancel();
                            getCategory(check);
                        }
                    });
                }

            } // on Failure
        });




// Retrofit

    } // function of getCategory
    // SelectImage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == RESULT_OK) {

            if (requestCode == GET_FROM_GALLERY) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }

            Log.i("QP",imagePath);
            ed_companyLogo.setText(imagePath.toString());

            uploadProfileImageFast();


        }
    } // on Activity result

    public  void  uploadProfileImageFast ()
    {
    } // uploadProfileImageFast

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
            }



            // upload image
        }
    } // onSelectFromGalleryResult
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(AddCompany.this, contentUri, proj, null, null, null);
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



    public  void  getCompanyDataToUpdate()
    {
        //Retrofit
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
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext()) ;

                final UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> getProfileConnection =
                        userApi.getCompanyAccount(copmpanyId);

                getProfileConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {
                            company = response.body().getCompany();
                            companyEnName=company.get(0).getEn_name();
                            companyLogo = company.get(0).getLogo();
                            companyEmail = company.get(0).getEmail();
                            companyArName = company.get(0).getAr_name();
                            companyFax = company.get(0).getFax();
                            companyMobile = company.get(0).getPhone();
                            CategoryId = company.get(0).getCategoryId();
                            cityData = company.get(0).getCity();
                            countryData = company.get(0).getCountry();

                            ed_enName.setText(companyEnName);
                            ed_arName.setText(companyArName);
                            ed_email.setText(companyEmail);
                            ed_fax.setText(companyFax);
                            ed_mobile.setText(companyMobile);
                            ed_companyLogo.setText(companyLogo);

                            Log.i("QP","/"+companyMobile+"/"+companyEnName);

                           sp_city.setSelection(Integer.valueOf(cityData)-1);
                           sp_country.setSelection(Integer.valueOf(countryData)-1);
                   /* Toast.makeText(AddCompany.this, "Connection Success\n"
                            ,Toast.LENGTH_LONG).show();*/




                            progress.dismiss();


                            getCategory(1);




                        } catch (Exception e) {
                            Toast.makeText(AddCompany.this, "Connection Success\n" +
                                            "Exception"+e.toString()
                                    ,Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(AddCompany.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getCompanyDataToUpdate();
                                }
                            });
                        }


                    }
                });
                //Retrofit

            }

        }.start();

    } // getCompanyDataToUpdate function
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


        Picasso.with(AddCompany.this).load("https://speed-rocket.com/upload/users/"
                +userProfileImage).
                fit().centerCrop().into(nav_profileimage);
        nav_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCompany.this,MyCompanyProfile.class);
                intent.putExtra("userID",userID);
                startActivity(intent);


            }
        });
        MenuItem searchItem = menu.findItem(R.id.action_settings);

        SearchManager searchManager = (SearchManager) AddCompany.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(AddCompany.this.getComponentName()));
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


            Intent intent = new Intent(AddCompany.this,ProductMenuList.class);
            startActivity(intent);


        }
        else if (id == R.id.nav_myProducts)
        {
            Intent intent = new Intent(AddCompany.this, MyWinnerProducts.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_offers)
        {
            Intent intent = new Intent(AddCompany.this,OfferMenuList.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_country) {

            return true;

        } else if (id == R.id.nav_language) {
            Intent intent = new Intent(AddCompany.this,ChangeLanguage.class);
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
                Intent intent = new Intent(AddCompany.this, LoginScreen.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(AddCompany.this, NavigationMenu.class);
            startActivity(intent);
            return true;

        }  else if (id == R.id.nav_buycoins) {
            Intent intent = new Intent(AddCompany.this, BuyCoins.class);
            startActivity(intent);
            return true;

        }
        else if (id == R.id.nav_cPanal) {
            if(userID == 0)
            {
                Intent intent = new Intent(AddCompany.this, LoginScreen.class);
                startActivity(intent);
            } // check if login 0 ---> user not login
            else {
                Intent intent = new Intent(AddCompany.this, MyCompanyProfile.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
            return true;

        }else if (id == R.id.yourOrders){
            Intent intent = new Intent(AddCompany.this,yourOrder.class);
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
