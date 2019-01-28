package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.speedrocket.progmine.speedrocket.Control.ProductMenuListAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.Category;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductMenuList extends AppCompatActivity {

    Category category;
    List<Category> categoryList;
    public  static  int numOfCategoryItems ;
    int categoryId , subCategpryId =0 , subCategory;
    String categoryTitle , sub ;
    private RecyclerView recyclerView;
    private ProductMenuListAdapter mAdapter;

    int type ;
    private ProgressDialog progress;
    private Handler handler;
    private List<Category> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_menu_list);
        setTitle(R.string.products);
        recyclerView = (RecyclerView) findViewById(R.id.list_offerMenuList);

        mAdapter = new ProductMenuListAdapter(productList,ProductMenuList.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            if (b.containsKey("cat")){
                subCategpryId=(int)b.get("cat");
            }

        }
        getOffersInNavigationMenu();

    } // onCreate Function


    public  void getOffersInNavigationMenu()
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

                final Call<ResultModel> getCategoryConnection = userApi.getCategory();

                getCategoryConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try {

                            categoryList = response.body().getCategory();

                            numOfCategoryItems = categoryList.size();
                                //get all categories
                            for (int i = 0; i <= categoryList.size(); i++) {
                                categoryId = categoryList.get(i).getId();
                                if (getBaseContext().getSharedPreferences("MyPref", 0).getString("langa","").equalsIgnoreCase("ar"))
                                {
                                    categoryTitle = categoryList.get(i).getAr_title();
                                }else {
                                    categoryTitle = categoryList.get(i).getEn_title();
                                }

                                subCategory = categoryList.get(i).getSubCategory();
                                type = categoryList.get(i).getType();

                                if(subCategpryId != 0 && subCategpryId == subCategory) {
                                    category = new Category(categoryId, categoryTitle, type);
                                    productList.add(category);
                                    mAdapter.notifyDataSetChanged();
                                } // if
                                else if (subCategpryId == 0 && subCategory == 0)
                                    {
                                        category = new Category(categoryId, categoryTitle, type);
                                        productList.add(category);
                                        mAdapter.notifyDataSetChanged();
                                    } // else



                            } // for loop

                            progress.dismiss();
                        } // try

                        catch (Exception e) {
                            progress.dismiss();

                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(ProductMenuList.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    getOffersInNavigationMenu();
                                }
                            });
                        }

                    } // on Failure
                });


            } }.start();

// Retrofit

    } // function of getOffersInNavigationMenu
} // classs of PrductMenyList
