package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.Category;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Ibrahim on 5/20/2018.
 */




public class UpdateCompany  extends AppCompatActivity
{

    EditText ed_enName , ed_arName , ed_email , ed_companyLogo , ed_mobile , ed_fax ;
    Spinner sp_country , sp_city , sp_category ;
    public static final int GET_FROM_GALLERY = 10;
    private static final int REQUEST_CAMERA = 1888;
    List<Category> categoryList;

    ArrayAdapter<String> spinnerArrayAdapter;
    private List<Category> offerList = new ArrayList<>();

    String imagePath="";
    String enName="";
    String arName="";
    String email="";
    String mobile="";
    String fax="";
    String country="";
    String city="";
    int categoryS;
    String logo="";
    String categoryTitle="";

    int categoryId , userId ;

    Category category;



    private ProgressDialog progress;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        ed_enName = (EditText) findViewById(R.id.cEnName);
        ed_arName = (EditText) findViewById(R.id.cArName);
        ed_email = (EditText) findViewById(R.id.cEmail);
        ed_companyLogo = (EditText) findViewById(R.id.cCompanyLogo);
        ed_mobile = (EditText) findViewById(R.id.cMobile);
        ed_fax = (EditText) findViewById(R.id.cFax);

        sp_country = (Spinner) findViewById(R.id.cCountry);
        sp_city = (Spinner) findViewById(R.id.cCity);
        sp_category= (Spinner) findViewById(R.id.cCategory);



        spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item);

        getCategory();

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

    } // on create function




    public  void getCategory()
    {
        //Retrofit
         Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());

        final UserApi userApi = retrofit.create(UserApi.class);

        final Call<ResultModel> getCategoryConnection = userApi.getCategory();

        getCategoryConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                try
                {

                    categoryList = response.body().getCategory();

                    for(int i = 0 ; i<= categoryList.size() ; i++)
                    {
                        categoryId = categoryList.get(i).getId();
                        categoryTitle = categoryList.get(i).getEn_title();

                        category = new Category(categoryId,categoryTitle);

                        offerList.add(category);
                        spinnerArrayAdapter.add(categoryList.get(i).getEn_title());
                        spinnerArrayAdapter.notifyDataSetChanged();
                    } // for loop


                   /* Toast.makeText(getBaseContext(),"Connection Success\n"
                                    ,

                            Toast.LENGTH_LONG).show();*/


                } // try
                catch (Exception e)
                {
                   /* Toast.makeText(getBaseContext(),"Connection Success\n" +
                                    "Exception Navigation menu\n"+e.toString(),
                            Toast.LENGTH_LONG).show();*/

                } // catch
            } // onResponse

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                progress.dismiss();
                if (t instanceof IOException){
                    final Dialog noInternet = AppConfig.InternetFaild(UpdateCompany.this);
                    final Button btn = noInternet.findViewById(R.id.Retry);
                    noInternet.show();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noInternet.cancel();
                            getCategory();
                        }
                    });
                }

            } // on Failure
        });




// Retrofit

    } // function of getCategory
    } // class of UpdateCompany
