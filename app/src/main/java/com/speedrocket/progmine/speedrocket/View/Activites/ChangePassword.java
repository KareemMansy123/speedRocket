package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

/**
 * Created by Ibrahim on 9/4/2018.
 */

public class ChangePassword extends AppCompatActivity {
    String firstName1 , lastName1 , email ,userProfileImage , companyName  ;
    int userID ;
    String newPasswordS = "", confiremPasswordS = "" ;
    EditText oldPassword , newPassword , confirmPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass_layout);
        setTitle(R.string.changePassword);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        firstName1 = prefs.getString("firstName", "");//"No name defined" is the default value.
        lastName1 = prefs.getString("lastName", "");//"No name defined" is the default value.
        email = prefs.getString("email", "");//"No name defined" is the default value.
        userID = prefs.getInt("id", 0);
        userProfileImage = prefs.getString("profileImage", "");

        oldPassword = findViewById(R.id.old_pass) ;
        newPassword = findViewById(R.id.new_pass);
        confirmPassword = findViewById(R.id.confirm_pass);


        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("ediatable1 " , editable.toString() );
                newPasswordS = editable.toString() ;
                if (!(newPasswordS.isEmpty() || newPasswordS.equalsIgnoreCase("")) &&!( confiremPasswordS.isEmpty()||confiremPasswordS.equalsIgnoreCase("")) ){
                    Log.e("confirm##" , "newpass :"+newPasswordS  +"  \n " +"confirm  :"+confiremPasswordS);
                    if (newPasswordS.equals(confiremPasswordS)){
                        Log.e("confirm##" , "true");
                        if (getSharedPreferences("MyPref" , 0).getString("langa" , "en").equalsIgnoreCase("ar")){
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check2 , 0 , 0 ,  0 );
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check2 , 0 , 0 ,  0 );
                        }else{
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.check2 ,  0 );
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.check2 ,  0 );
                        }
                    }else {
                        if (getSharedPreferences("MyPref" , 0).getString("langa" , "en").equalsIgnoreCase("ar")){
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cross2 , 0 , 0 ,  0 );
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cross2 , 0 , 0 ,  0 );
                        }else{
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.cross2 ,  0 );
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.cross2 ,  0 );
                        }
                    }

                }
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("ediatable1 " , editable.toString() );
                   confiremPasswordS = editable.toString();
                if (!(newPasswordS.isEmpty() || newPasswordS.equalsIgnoreCase("")) &&!( confiremPasswordS.isEmpty()||confiremPasswordS.equalsIgnoreCase("")) ){
                    Log.e("confirm##" , "newpass :"+newPasswordS  +"  \n " +"confirm  :"+confiremPasswordS);
                    if (newPasswordS.equals(confiremPasswordS)){
                        Log.e("confirm##" , "true");
                        if (getSharedPreferences("MyPref" , 0).getString("langa" , "").equalsIgnoreCase("ar")){
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check2 , 0 , 0 ,  0 );
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check2 , 0 , 0 ,  0 );
                        }else{
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.check2 ,  0 );
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.check2 ,  0 );
                        }
                    }else {
                        if (getSharedPreferences("MyPref" , 0).getString("langa" , "en").equalsIgnoreCase("ar")){
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cross2 , 0 , 0 ,  0 );
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cross2 , 0 , 0 ,  0 );
                        }else{
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.cross2 ,  0 );
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.cross2 ,  0 );
                        }

                    }

                }
            }
        });

    }
         public void supmite(View v){

          Boolean check  =   newPasswordS.equals(confiremPasswordS)&&!oldPassword.getText().toString().isEmpty() ? true : false ;
          Log.e("bool" , " " +check);
          if (check){
          supmit();
          }else {

             Toast.makeText(getBaseContext() , "please make sure that all fileds is correct " , Toast.LENGTH_LONG).show();
          }


          }

          private void supmit(){

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
              handler =    new Handler( ){
                  @Override
                  public void handleMessage(Message msg) {
                      super.handleMessage(msg);

                  }
              };



              new Thread(){
                  @Override
                  public void run() {
                      Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",getApplicationContext());
                      final UserApi api = retrofit.create(UserApi.class);

                      Call<ResultModel> isPass  = api.isPasswordConfirmed(email , oldPassword.getText().toString() , newPasswordS ,userID);

                      isPass.enqueue(new Callback<ResultModel>() {
                          @Override
                          public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                              if (response.body().isConfirmed()){
                                  progress.dismiss();
                                  Toast.makeText(getBaseContext() , "password updated correctly " , Toast.LENGTH_LONG).show();
                                  finish();
                              }else {
                                  progress.dismiss();
                                  Toast.makeText(getBaseContext() , "please make sure that your current pass is right " , Toast.LENGTH_LONG).show();

                              }
                          }

                          @Override
                          public void onFailure(Call<ResultModel> call, Throwable t) {
                              progress.dismiss();
                              if (t instanceof IOException){
                                  final Dialog noInternet = AppConfig.InternetFaild(ChangePassword.this);
                                  final Button btn = noInternet.findViewById(R.id.Retry);
                                  noInternet.show();
                                  btn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          noInternet.cancel();
                                          supmit();
                                      }
                                  });
                              }
                          }
                      });
                  }
              }.start();



          }

    }
