package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.speedrocket.progmine.speedrocket.Control.OfferMenuListAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
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

public class OfferMenuList extends AppCompatActivity {






    List<String> interstList ;
    private RecyclerView recyclerView;
    private OfferMenuListAdapter mAdapter;
    private List<String> offerList = new ArrayList<>();

    private ProgressDialog progress;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_menu_list);
        setTitle(R.string.offers);
        recyclerView = (RecyclerView) findViewById(R.id.list_offerMenuList);

        mAdapter = new OfferMenuListAdapter(offerList, OfferMenuList.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getOffersInNavigationMenu();



    } // function of OnCreate


    public  void getOffersInNavigationMenu()
    {


        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait ");
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

        final Call<ResultModel> getInterestConnection = userApi.getUsersInterest(getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE).getString("langa",""));

        getInterestConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                try
                {
                    interstList = response.body().getType();
                    for(int i = 0 ; i< interstList.size();i++)
                    {
                        offerList.add(interstList.get(i));
                        mAdapter.notifyDataSetChanged();

                    } // for loop

/*Toast.makeText(getBaseContext(),"Connection Success\n",
                            Toast.LENGTH_LONG).show();*/
                    progress.dismiss();
                } // try
                catch (Exception e)
                {
                 /*   Toast.makeText(getBaseContext(),"Connection Success\n" +
                                    "Exception\n"+e.toString(),
                            Toast.LENGTH_LONG).show();*/    progress.dismiss();
                } // catch
            } // onResponse

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

                progress.dismiss();
                if (t instanceof IOException){
                    final Dialog noInternet = AppConfig.InternetFaild(OfferMenuList.this);;
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
} // class off OfferMenuList
