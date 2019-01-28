package com.speedrocket.progmine.speedrocket.Model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// K.M

public class ApiUsersSearch {
    public static String BASE_URL ="http://speed-rocket.com/api/";
    private static Retrofit retrofit;
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}