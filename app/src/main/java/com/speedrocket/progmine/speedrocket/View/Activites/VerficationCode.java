package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.alahammad.otp_view.OTPListener;
import com.alahammad.otp_view.OtpView;
import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class
  VerficationCode extends AppCompatActivity {

CountDownTimer timer ;
private  int code ;
    private String mobileNumber = "" ;
    int seconds , minutes ;
    TextView min , sec ;
    OtpView pin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication_code);
        setTitle(R.string.verification);


        min = findViewById(R.id.min);
        sec = findViewById(R.id.sec);

        final Bundle b = getIntent().getExtras();

        if (b != null){
            if (b.containsKey("number")){
                mobileNumber = (String)b.get("number");
            }
        }

        if (!mobileNumber.equalsIgnoreCase("")){
            needVerification();
        }
        pin = findViewById(R.id.otp);
        pin.setOnOtpFinished(new OTPListener() {
            @Override
            public void otpFinished(String s) {
                onFinish(s);
            }
        });
        timerStart();


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
               needVerification();
               if (timer != null )
                   timer = null ;
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

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/" , VerficationCode.this);
                ApiConfig api = retrofit.create(ApiConfig.class);

                Call<ResultModel> needVerification = api.needVerificationCode(mobileNumber);
                needVerification.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        Log.e("verefication###",response.body().getMessage());
                        code= response.body().getCode();
                        switch (code){
                            case 3:
                                Toast.makeText(getApplicationContext() , "Code Sent Successfully " , Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(getBaseContext() , "User Not Found" , Toast.LENGTH_LONG).show();
                                break;
                            case 4 :
                                Toast.makeText(getApplicationContext() , "Code not Expired Yet .. " , Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(VerficationCode.this);
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
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        final Handler handler ;

        if (!((Activity) this).isFinishing()) {
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

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/" , VerficationCode.this);
                ApiConfig api = retrofit.create(ApiConfig.class);

                Call<ResultModel> verifyIt = api.verifyCode(verificationCode , mobileNumber);

                verifyIt.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                    code = response.body().getCode() ;
                    switch (code){
                        case 0 :
                            Toast.makeText(getApplicationContext() , "Code Sent Successfully " , Toast.LENGTH_SHORT).show();
                            break;
                        case 1 :
                            Toast.makeText(getApplicationContext() , "Code dosn't match " , Toast.LENGTH_SHORT).show();
                            break;
                        case 2 :
                            Toast.makeText(getApplicationContext() , "please Resend Code Time Expired .. " , Toast.LENGTH_SHORT).show();
                            break;
                        case 3 :
                            if (timer != null){
                                timer = null ;
                            }
                            Intent intent = new Intent(VerficationCode.this , LoginScreen.class);
                            startActivity(intent);
                            finish();
                            break;

                    }

                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {

                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(VerficationCode.this);
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


    public void reSend(View view ){

        timerStart();
        needVerification();
    }

    @Override
    public void onBackPressed() {

    }
}
