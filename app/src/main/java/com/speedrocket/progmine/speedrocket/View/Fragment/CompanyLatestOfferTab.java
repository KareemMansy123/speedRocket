package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Control.PostsAdapter1;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.Offer;
import com.speedrocket.progmine.speedrocket.Model.Post;
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

public class CompanyLatestOfferTab extends Fragment {



    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostsAdapter1 mAdapter;

    int companyId;
    private List<Offer> offerList1 = new ArrayList<>();

    private ProgressDialog progress;
    private Handler handler;

    List <Offer> offerList ;
    String o_firstName , o_lastName , o_description , o_image ,o_createdat , o_companyName
            ,o_logo , o_time;
    int o_srcoin , o_view , o_userid , o_id ,o_companyId ;
    double o_price ;
    Offer offer;
    int post_im = (R.drawable.post_image);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_company_latest_offer_tab, container, false);

       recyclerView = (RecyclerView) rootview.findViewById(R.id.item_postlist_company);


        Intent iin= getActivity().getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            companyId=(int)b.get("companyId");
        }

        preparePostData();
        mAdapter = new PostsAdapter1(offerList1, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);




        return rootview ;





    }

    private void preparePostData()
    {
      /*  int profile_im = (R.drawable.profile_image);
        final int post_im = (R.drawable.post_image);
        int country_im = (R.drawable.f1);
        Post post = new Post("Muhammed","Hassn",profile_im,post_im,"20","300","25",a1,"Egypt",country_im);
        postList.add(post);

        post= new Post("James","Wilson",profile_im,post_im,"13","200","18",2,"Egypt",country_im);
        postList.add(post);

        post= new Post("Ali","Ibrahim",profile_im,post_im,"37","347","36",3,"Egypt",country_im);
        postList.add(post);
         post = new Post("Muhammed","Hassn",profile_im,post_im,"20","300","25",a1,"Egypt",country_im);
        postList.add(post);

        post= new Post("James","Wilson",profile_im,post_im,"13","200","18",2,"Egypt",country_im);
        postList.add(post);

        post= new Post("Ali","Ibrahim",profile_im,post_im,"37","347","36",3,"Egypt",country_im);
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

                final Call<ResultModel> getOfferConnection = userApi.getOffersToCompanyProfile(companyId);

                getOfferConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try
                        {


                            offerList = response.body().getOffers();
                            for(int i =0 ; i< offerList.size(); i++)
                            {
                                o_id=offerList.get(i).getId();
                                o_userid=offerList.get(i).getUserId();
                                o_price=offerList.get(i).getPrice();
                                o_view=offerList.get(i).getView();
                                o_srcoin=offerList.get(i).getSrcoin();
                                o_description=offerList.get(i).getEn_description();
                                o_image=offerList.get(i).getImage();
                                o_logo=offerList.get(i).getCompanyLogo();
                                o_time = offerList.get(i).getTime();
                                o_companyId = offerList.get(i).getCompanyId();




                                o_companyName = offerList.get(i).getCompanyName();


                                offer = new Offer(o_id,0,o_userid,0,0,o_srcoin,
                                        o_view,o_price,"","","",o_description
                                        ,"","",post_im,o_image,o_createdat,o_companyName,o_logo,
                                        o_time,o_companyId);


                                offerList1.add(offer);
                /*    // firebase
                         String sId = String.valueOf(o_id);
                      DatabaseReference mDatabase;
                      mDatabase = FirebaseDatabase.getInstance().getReference();
                      mDatabase.child("offers").child(sId).setValue(offer);

                         //firebase*/



                                mAdapter.notifyDataSetChanged();
                            }// or loop


                  /*  Toast.makeText(getApplicationContext(),"Connection Success\n"
                                    +"userId : "+o_userid+"\n"
                                    +"Price : "+o_price+"\n"
                                    +"Views: "+o_view+"\n"
                                    +"srcoin : "+o_srcoin+"\n"
                                    +"description : "+o_description+"\n",

                            Toast.LENGTH_LONG).show();*/
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
