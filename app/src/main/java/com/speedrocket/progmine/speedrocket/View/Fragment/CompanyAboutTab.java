package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.Company;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;

import java.io.IOException;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CompanyAboutTab extends Fragment {

    private ProgressDialog progress;
    private Handler handler;


    List<Company> company ;
    int id  , companyId;
    String companyCity ="" , companyCountry="" , companyMobile="" , companyCreatedAt="";
    String companyFax = "" , companyEmail="" ;
    String category_ar = "" , category_en ="";
    TextView t_companyCity , t_companyCountry , t_companyMobile , t_companyCreatedAt , t_email , t_fax ,t_category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_company_about_tab,
                container,false);

        t_companyCity=(TextView)view.findViewById(R.id.tab_comapnyCity);
        t_companyCountry=(TextView)view.findViewById(R.id.tab_companyCountry);
        t_companyCreatedAt=(TextView)view.findViewById(R.id.tab_companyCreatedAt);
        t_companyMobile=(TextView)view.findViewById(R.id.tab_companyMobile);
         t_email = view.findViewById(R.id.email);
         t_fax = view.findViewById(R.id.fax);
         t_category = view.findViewById(R.id.category);
         t_category.setSelected(true);

        Intent iin= getActivity().getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            companyId=(int)b.get("companyId");
        }



      /*  Toast.makeText(getActivity(),"userID : "+userID,
                Toast.LENGTH_LONG).show();*/

        getProfielData();
        return view;
    }

    public  void getProfielData()
    {
        progress = new ProgressDialog(getActivity());
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
        if (!((Activity) getActivity()).isFinishing()) {
            //show dialog

            progress.show();

        }
        new Thread()
        {
            public void run()
            {
        //Retrofit
        //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getContext());

        final UserApi userApi = retrofit.create(UserApi.class);
        retrofit2.Call<ResultModel> getProfileConnection =
                userApi.getCompanyAccount(companyId);

        getProfileConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(retrofit2.Call<ResultModel> call, Response<ResultModel> response) {

                try {
                    company = response.body().getCompany();

                    companyCity=company.get(0).getCity();
                    companyCountry=company.get(0).getCountry();
                    companyMobile=company.get(0).getPhone();
                    companyCreatedAt=company.get(0).getCreated_at();
                    companyFax = company.get(0).getFax();
                    companyEmail = company.get(0).getEmail();
                    category_ar = company.get(0).getCategory_ar() ;
                    category_en = company.get(0).getCategory_en();


                    t_companyCity.setText(companyCity);
                    t_companyCountry.setText(companyCountry);
                    t_companyMobile.setText(companyMobile);
                    t_companyCreatedAt.setText(companyCreatedAt);
                    t_fax.setText(companyFax);
                    t_email.setText(companyEmail);

                    /*Toast.makeText(getActivity(), "Connection Success\n"
                            ,Toast.LENGTH_LONG).show();*/
                    if (getActivity().getApplicationContext().getSharedPreferences("MyPref",0).getString("langa","en").equals("ar")){

                        t_category.setText(category_ar);
                    }else{
                        t_category.setText(category_en);
                    }
                    progress.dismiss();






                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Connection Success\n" +
                                    "Exception"+e.toString()
                            ,Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResultModel> call, Throwable t) {
                progress.dismiss();
                if (t instanceof IOException){
                    final Dialog noInternet = AppConfig.InternetFaild(getContext());
                    final Button btn = noInternet.findViewById(R.id.Retry);
                    noInternet.show();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noInternet.cancel();
                            getProfielData();
                        }
                    });
                }


            }
        });
            }

        }.start();
        //Retrofit
    } // getProfileData Function
}
