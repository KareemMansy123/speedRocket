package com.speedrocket.progmine.speedrocket;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Ibrahim on 9/30/2018.
 */

public class TokenAuthenticator implements Authenticator {
    private Context context ;

    public TokenAuthenticator(Context context){
        this.context = context ;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/" , context);
        UserApi user = retrofit.create(UserApi.class);

        String email = context.getSharedPreferences("Auth",0).getString("email","");
        String pass = context.getSharedPreferences("Auth",0).getString("pass","");
        if (email.isEmpty() || pass.isEmpty() || pass.equalsIgnoreCase("") || email.equalsIgnoreCase("")) {
            Intent logIn = new Intent(context , LoginScreen.class);
            context.startActivity(logIn);
//            Toast.makeText(context , "please login first .." , Toast.LENGTH_LONG).show();
            return null ;
        }
        int check = 0 ;
        if(isEmailValid(email)){
            check = 1 ;
        }else{
            check =2 ;
        }
        int userId = context.getSharedPreferences("MyPrefsFile",0).getInt("id",0);
        Call<ResultModel> result  = user.getTokens(email,pass,userId, check);

        String token =result.execute().body().getToken();




        Log.e("tokenz  :  " , token);
        return response.request().newBuilder()
                .header("Authorization", "Bearer "+token)
                .build();
    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
}
