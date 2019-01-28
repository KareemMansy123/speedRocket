package com.speedrocket.progmine.speedrocket.Model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class contentSearchAdapter  {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    public static Retrofit retofit = null;

    public static Retrofit getApiClint(){
        if (retofit == null){
            retofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retofit;
    }


}
