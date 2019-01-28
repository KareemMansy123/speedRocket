package com.speedrocket.progmine.speedrocket.View.Fragment;

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
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.PersonalUser;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.ChangePassword;
import com.speedrocket.progmine.speedrocket.View.Activites.UpdateProfile;

import java.io.IOException;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AboutTab extends Fragment {

    String gender="" , language="" , interest="" , joinAt="" ;


    private ProgressDialog progress;
    private Handler handler;

    List <PersonalUser> user ;
    String firstName , lastName , type  ;
    String user_firstName , user_lastName , user_email , user_password , user_interest
            ,user_language , user_mobile , user_gender , user_joinAt , user_type
            ,user_companyName , user_companyCity , user_companyCountry
            , user_companyMobile;

    String  encodedimage;
    int id  , userID;

    TextView t_gender , t_language , t_interest , t_joinAt , changePass ;

    ImageButton update ;
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view=inflater.inflate(R.layout.fragment_about_tab,
                container,false);
        t_gender = (TextView)view.findViewById(R.id.tab_gender);
        t_language = (TextView)view.findViewById(R.id.tab_language);
        t_interest = (TextView)view.findViewById(R.id.tab_interest);
        t_joinAt = (TextView)view.findViewById(R.id.tab_joinAt);

        update = (ImageButton)view.findViewById(R.id.updateButton);

        changePass = view.findViewById(R.id.change_pass);

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent = new Intent(getContext() , ChangePassword.class);
             startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent intent = new Intent(getContext(), UpdateProfile.class);
                startActivity(intent);
            }
        });



        Intent iin= getActivity().getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            userID=(int)b.get("userID");
        }



        /*Toast.makeText(getActivity(),"userID : "+userID,
                Toast.LENGTH_LONG).show();*/

        getProfielData();

        // SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME,
               // Context.MODE_PRIVATE);

      /*  Toast.makeText(getActivity(),gender +"a1 "+language+"0"+interest+" a1",
                Toast.LENGTH_LONG).show();*/
    //    getProfielData();

        return view;



    } // onCreatView



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

        progress.show();
        new Thread()
        {
            public void run()
            {
        //Retrofit
        //Retrofit
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getContext());

        final UserApi userApi = retrofit.create(UserApi.class);
        retrofit2.Call<ResultModel> getProfileConnection =
                userApi.getProfileAccount(userID);

        getProfileConnection.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(retrofit2.Call<ResultModel> call, Response<ResultModel> response) {

                try {
                    user = response.body().getUser();
                    user_firstName=user.get(0).getFirstName();
                    user_lastName=user.get(0).getLastName();
                    user_email=user.get(0).getEmail();
                    user_gender=user.get(0).getGender();
                    user_password=user.get(0).getPassword();
                    user_interest=user.get(0).getInterest();
                    user_language=user.get(0).getLanguage();
                    user_mobile=user.get(0).getMobile();
                    user_joinAt=user.get(0).getCreated_at();
                    user_type=user.get(0).getType();
                    user_companyName=user.get(0).getCompanyName();
                    user_companyCity=user.get(0).getCompanyCity();
                    user_companyCountry=user.get(0).getCompanyCountry();
                    user_companyMobile=user.get(0).getCompanyMobile();

                   t_gender.setText(user_gender);
                   t_interest.setText(user_interest);
                   t_joinAt.setText(user_joinAt);
                   t_language.setText(user_language);

                  /*  Toast.makeText(getActivity(), "Connection Success\n"+
                                    "FirstName : "+user_firstName
                            ,Toast.LENGTH_LONG).show();*/
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
                  final Dialog noInternet = AppConfig.InternetFaild(getActivity());
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
        //Retrofit

            }

        }.start();

    } // getProfileData Function



}
