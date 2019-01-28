package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alahammad.otp_view.OTPListener;
import com.alahammad.otp_view.OtpView;
import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.RegistrationCustom;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistrationVirevifyTap extends Fragment {

    private String mobileNumber = "" ;
    int seconds , minutes ;
    TextView min , sec ;
    OtpView pin ;

    CountDownTimer timer ;
    private  int code ;
    OnCallBack2 callBack ;
    public interface OnCallBack2 {

        void stepThreeTreger();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_verfication_code,
                container,false);

        min = view.findViewById(R.id.min);
        sec = view.findViewById(R.id.sec);

        //final Bundle b = getIntent().getExtras();
//
//        if (b != null){
//            if (b.containsKey("number")){
//                mobileNumber = (String)b.get("number");
//            }
//        }
        view.findViewById(R.id.resendCode).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                needVerification();
            }
        });
        pin = view.findViewById(R.id.otp);
        pin.setOnOtpFinished(new OTPListener() {
            @Override
            public void otpFinished(String s) {
                onFinish(s);
            }
        });

        return view ;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callBack = (RegistrationVirevifyTap.OnCallBack2) (Activity)context;
        } catch (ClassCastException e) {

        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //RegistrationCustom.buttonVisibalty(false);
        if (isVisibleToUser){

            mobileNumber = RegistrationCustom.rMobileNumber ;
            Log.e("mobileNumber::" ,"mobile ::  " + mobileNumber) ;
            if (!mobileNumber.equalsIgnoreCase("")){

                needVerification();
            }



        }
    }

    private void timerStart (){

        timer = new CountDownTimer(120000 , 1000) {
            @Override
            public void onTick(long l) {
                seconds = (int)l/1000 ;
                minutes = seconds/60 ;
                seconds = seconds % 60;

                min.setText(""+minutes);
                sec.setText(""+seconds);
            }

            @Override
            public void onFinish() {
                if (timer != null )
                    timer = null ;

              //  needVerification();

            }
        }.start();
    }
    public void verify(View view){

    }

    private void needVerification(){


        Log.e("verefication###",""+mobileNumber);
        new Thread(){
            @Override
            public void run() {

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/" , getActivity());
                ApiConfig api = retrofit.create(ApiConfig.class);

                Call<ResultModel> needVerification = api.needVerificationCode(mobileNumber);
                needVerification.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        Log.e("verefication###",response.body().getMessage());
                        code= response.body().getCode();
                        switch (code){
                            case 3:

                                if (timer != null)timer =null ;
                                timerStart();
                                Toast.makeText(getContext(), "Code Sent Successfully " , Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(getContext() , "User Not Found" , Toast.LENGTH_LONG).show();
                                break;
                            case 4 :
                                Toast.makeText(getContext() , "Code not Expired Yet .. " , Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(getActivity());
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    needVerification();
                                }
                            });
                        }
                    }
                });

            }
        }.start();
    }

    private void onFinish(final String verificationCode){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        final Handler handler ;

        if (!((Activity) getActivity()).isFinishing()) {
            //show dialog
            progress.show();
        }

        handler =    new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        new Thread(){
            @Override
            public void run() {

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/" , getActivity());
                ApiConfig api = retrofit.create(ApiConfig.class);

                Call<ResultModel> verifyIt = api.verifyCode(verificationCode , mobileNumber);

                verifyIt.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        code = response.body().getCode() ;
                        switch (code){
                            case 0 :
                                progress.dismiss();
                                Toast.makeText(getContext() , "Code Sent Successfully " , Toast.LENGTH_SHORT).show();
                                pin.setOTP("");

                                break;
                            case 1 :
                                progress.dismiss();
                                Toast.makeText(getContext() , "Code dosn't match " , Toast.LENGTH_SHORT).show();
                                break;
                            case 2 :
                                progress.dismiss();
                                Toast.makeText(getContext() , "please Resend Code Time Expired .. " , Toast.LENGTH_SHORT).show();
                                break;
                            case 3 :
                                progress.dismiss();
                                if (timer != null){
                                    timer = null ;
                                }
                                getContext().getSharedPreferences("RegistrationNumber" , 0 ).edit().putInt("active",1).apply();
                                callBack.stepThreeTreger();
                                break;

                        }

                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(getActivity());
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    onFinish(verificationCode);
                                }
                            });
                        }
                    }
                });
            }
        }.start();
    }


}
