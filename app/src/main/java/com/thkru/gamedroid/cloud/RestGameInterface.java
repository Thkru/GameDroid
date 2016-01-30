package com.thkru.gamedroid.cloud;

import com.thkru.gamedroid.data.Games;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RestGameInterface {

    @GET("games/search")//?limit={limit}&token={token}
    Call<Games> listRepos(@Query("limit") int limit, @Query("token") String token);

}
