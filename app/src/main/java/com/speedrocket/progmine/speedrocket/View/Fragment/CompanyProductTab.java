package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Control.ProductAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.Post;
import com.speedrocket.progmine.speedrocket.Model.Product;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.CompanyProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class CompanyProductTab extends Fragment {


    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;

    int companyId;

    List <Product> productList ;

    String enTitle , enDescription , productImage ;
    int productId ;
    double price ;

    Product product;
    private List<Product> productList1 = new ArrayList<>();

    private ProgressDialog progress;
    private Handler handler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_company_product_tab, container
                , false);


        Intent iin= getActivity().getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            companyId=(int)b.get("companyId");
        }


        recyclerView = (RecyclerView) rootview.findViewById(R.id.item_postlist_product);

        mAdapter = new ProductAdapter(productList1 ,getActivity(),((CompanyProfile)getActivity()).getBadge());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        preparePostData();

        return rootview ;
    }
    private void preparePostData()
    {
        /*int profile_im = (R.drawable.profile_image);
        int post_im = (R.drawable.product);
        int country_im = (R.drawable.f1);
        Post post = new Post("Laptop","Hassn",profile_im,post_im,"20","300","25",a1,"Egypt",country_im);
        postList.add(post);

        post= new Post("Power bank","Wilson",profile_im,post_im,"13","200","18",2,"Egypt",country_im);
        postList.add(post);

        post= new Post("mobile","Ibrahim",profile_im,post_im,"37","347","36",3,"Egypt",country_im);
        postList.add(post);
        post = new Post("Laptop","Hassn",profile_im,post_im,"20","300","25",a1,"Egypt",country_im);
        postList.add(post);

        post= new Post("Power bank","Wilson",profile_im,post_im,"13","200","18",2,"Egypt",country_im);
        postList.add(post);

        post= new Post("mobile","Ibrahim",profile_im,post_im,"37","347","36",3,"Egypt",country_im);
        postList.add(post);
        mAdapter.notifyDataSetChanged();*/

        progress = new ProgressDialog(getContext());
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
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getContext());

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
                                String ar_title = productList.get(i).getAr_title();
                                enDescription = productList.get(i).getEn_discription();
                                String arDescription =  productList.get(i).getAr_discription();
                                productImage = productList.get(i).getImage();
                                productId = productList.get(i).getId();
                                price = productList.get(i).getPrice();

                                product = new Product(enTitle, ar_title,enDescription,arDescription,productImage,productId,price);

                                productList1.add(product);
                                mAdapter.notifyDataSetChanged();
                            }// or loop


                            progress.dismiss();

                        } // try
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(),"Connection Success\n" +
                                            "Exception Home Page\n"+e.toString(),
                                    Toast.LENGTH_LONG).show();
                            progress.dismiss();

                        } // catch



                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

               /* Toast.makeText(getApplicationContext(),"Connection Faild",
                        Toast.LENGTH_LONG).show();*/
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(getContext());
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
}
