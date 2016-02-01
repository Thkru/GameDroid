package com.thkru.gamedroid.cloud;

import com.thkru.gamedroid.data.Games;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RestGameInterface {

    @GET("games/search")
    Call<Games> listRepos(@Query("offset") int offset,
                          @Query("limit") int limit,
                          @Query("token") String token,
                          @Query("filters[platforms.id_eq]") int id
    );
}
