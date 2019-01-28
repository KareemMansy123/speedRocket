package com.speedrocket.progmine.speedrocket.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaceUsersSearch {
    @GET("websevsearhUsers/{keyword}")
    Call<List<UsersSearchContent>> getMovies(@Path("keyword") String keyword);
}
