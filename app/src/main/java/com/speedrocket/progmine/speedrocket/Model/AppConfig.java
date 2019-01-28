package com.speedrocket.progmine.speedrocket.Model;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.google.gson.Gson;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.TokenAuthenticator;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class AppConfig
{
    private static String BASE_URL = "https://speed-rocket.com/api/";
    public static Retrofit getRetrofit(final Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5,TimeUnit.MINUTES)
                 .authenticator(new TokenAuthenticator(context))
                .build();
        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(AppConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public static Retrofit getRetrofit2(String baseUrl ,final Context context){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .authenticator(new TokenAuthenticator(context))
                .build();
       return   new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public static Retrofit getRetrofit3(String baseUrl , final Context context){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .authenticator(new TokenAuthenticator(context))
                .build();


        return   new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }




    public static Dialog InternetFaild(final Context context ){
        final Dialog internetError= new Dialog(context);
        internetError.requestWindowFeature(Window.FEATURE_NO_TITLE);
        internetError.setContentView(R.layout.no_internet);
        internetError.setCanceledOnTouchOutside(false);
        internetError.setCancelable(false);

         return internetError ;
    }


} // class AppConfiq
