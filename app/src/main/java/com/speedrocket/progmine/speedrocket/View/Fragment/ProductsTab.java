package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Control.MyProductAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ProductsWinner;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.CompanyProfile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductsTab extends Fragment {


    int userID , offerId , srCoin;

    Timestamp created_at ;

    List <ProductsWinner> productList ;

    String productTitle ;

    private List<ProductsWinner> productWinnerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyProductAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View rootview = inflater.inflate(R.layout.fragment_products_tab, container
                , false);



        Intent iin= getActivity().getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            userID=(int)b.get("userID");
        }


        getProductsWinnerByUser();

        recyclerView = (RecyclerView) rootview.findViewById(R.id.myProductList);
        mAdapter = new MyProductAdapter(productWinnerList ,getActivity() , ((CompanyProfile)getActivity()).getBadge());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);





        return rootview;

    } // onCreateView function


    public  void getProductsWinnerByUser()
    {
        /*Toast.makeText(getActivity() , "Product Tap : "+userID,Toast.LENGTH_LONG).show();*/

        //Retrofit
        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getContext());

        final UserApi userApi = retrofit.create(UserApi.class);
        retrofit2.Call<ResultModel> winnerProductConnection =
                userApi.getProductWinnersByUser(userID);

        winnerProductConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(retrofit2.Call<ResultModel> call, Response<ResultModel> response) {

                try {

                    productList = response.body().getWinners();

                    for(int i = 0 ; i<productList.size() ; i++)
                    {
                        offerId = productList.get(i).getOfferId();
                        srCoin = productList.get(i).getSrCoin();
                     //   created_at = productList.get(i).getCreated_at();
                        productTitle = productList.get(i).getTitle();

                       ProductsWinner p = new ProductsWinner(userID,offerId,
                               srCoin,productTitle);

                       productWinnerList.add(p);
                       mAdapter.notifyDataSetChanged();

                    } // for Loop

                    /*Toast.makeText(getActivity(), "Connection Success\n"
                            ,Toast.LENGTH_LONG).show();*/





                } catch (Exception e) {
             /*       Toast.makeText(getActivity(), "Connection Success\n" +
                                    "Exception"+e.toString()
                            ,Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResultModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Connection faild\n" +
                                "Exception\n Product Tap"+t.toString()
                        , Toast.LENGTH_LONG).show();


            }
        });
        //Retrofit
    } // function getProductsWinnerByUser


} // product spcialize personal user
