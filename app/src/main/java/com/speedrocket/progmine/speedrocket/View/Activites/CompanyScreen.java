package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.speedrocket.progmine.speedrocket.Control.CompanyAdapter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.Company;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class CompanyScreen extends AppCompatActivity {


    TextView fName ;
    CircleImageView myProfileImage ;

    String firstName1 , lastName1 , email1 ,userProfileImage , companyName , companyLogo ;
    int id  , userID , userID1 , companyId;
    int confirmed = 0  , messageNumber;

    private RecyclerView recyclerView;
    private CompanyAdapter mAdapter;
    Company company;
    private List<Company> companyList1 = new ArrayList<>();

    private ProgressDialog progress;
    private Handler handler;
    List<Company> CList;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_screen);
        setTitle(R.string.companies);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        firstName1 = prefs.getString("firstName", "");//"No name defined" is the default value.
        lastName1 = prefs.getString("lastName", "");//"No name defined" is the default value.
        email1 = prefs.getString("email", "");//"No name defined" is the default value.
        userID1=prefs.getInt("id",0);
        userProfileImage = prefs.getString("profileImage","");


        /*fName = (TextView) findViewById(R.id.t_companyName);
        myProfileImage = (CircleImageView) findViewById(R.id.i_profileImage);

        fName.setText(firstName1);
        Picasso.with(getBaseContext()).load("https://speed-rocket.com/upload/users/"
                +userProfileImage).
                fit().centerCrop().into(myProfileImage);*/


        recyclerView = (RecyclerView) findViewById(R.id.companyList);
        mAdapter = new CompanyAdapter(companyList1, CompanyScreen.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getCompanies();



    } // onCreate function
    @Override
    protected void onRestart() {
        companyList1.clear();
        getCompanies();
        super.onRestart();
    }

    public  void  getCompanies()
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
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());

        final UserApi userApi = retrofit.create(UserApi.class);

        final Call<ResultModel> getCompanyConnection = userApi.getCompany(userID1);

        getCompanyConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                try
                {

                    Log.i("QP",userID1+"");
                    CList = response.body().getCompanies();

                    for(int i = 0 ; i <CList.size() ; i++)
                    {
                        companyId = CList.get(i).getId();
                        companyName = CList.get(i).getEn_name();
                        companyLogo = CList.get(i).getLogo();
                        confirmed = CList.get(i).getConfirmed();
                        messageNumber = CList.get(i).getMessageNumber();

                        company = new Company(companyId,companyName,CList.get(i).getAr_name(),companyLogo, confirmed,messageNumber);

                        companyList1.add(company);
                        mAdapter.notifyDataSetChanged();

                    }
                    progress.dismiss();
                } // try
                catch (Exception e)
                {
                   /* Toast.makeText(getBaseContext(),"Connection Success\n" +
                                    "Exception Navigation menu\n"+e.toString(),
                            Toast.LENGTH_LONG).show();*/progress.dismiss();

                } // catch
            } // onResponse

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

                progress.dismiss();
                if (t instanceof IOException){
                    final Dialog noInternet = AppConfig.InternetFaild(CompanyScreen.this);
                    final Button btn = noInternet.findViewById(R.id.Retry);
                    noInternet.show();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noInternet.cancel();
                            getCompanies();
                        }
                    });
                }

            } // on Failure
        });


            }

        }.start();

// Retrofit
    } // finction get Companies

    public void addCompanyButton(View view)
    {
        Intent intent = new Intent(getBaseContext(),AddCompany.class);
        startActivity(intent);
    } // Add Company Button
} // CompanyScreen Class
