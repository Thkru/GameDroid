package com.thkru.gamedroid.cloud;

import android.util.Log;

import com.thkru.gamedroid.data.Game;
import com.thkru.gamedroid.data.Games;
import com.thkru.gamedroid.utils.GamesLoadedEvent;
import com.thkru.gamedroid.utils.ServerErrorEvent;

import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class CloudHelper {

    private static final String BASE = "https://www.igdb.com/api/v1/";
    private static final String BASE_COVER_URL = "https://res.cloudinary.com/igdb/image/upload/t_";
    private static final String TOKEN = "9UWH7rnFWc9FsfRRLvX9gPtv4N1nWx9V1ayPiWmM6Cc";//TODO encrypt

    public static String getCoverUrlForId(String size, String coverHash) {
        return BASE_COVER_URL + size + "/" + coverHash + ".jpg";
    }

    public void doRequest() {
        Log.i("", "DoRequest");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroFitCall(retrofit);
    }//end doRequest


    protected void retroFitCall(Retrofit retrofit) {
        RestGameInterface service = retrofit.create(RestGameInterface.class);
        service.listRepos(0, 250, TOKEN, 6).enqueue(new Callback<Games>() {
            @Override
            public void onResponse(Response<Games> response, Retrofit retrofit) {
                Log.i("", "DoRequest - Success");

                List<Game> games = response.body().games;
                Collections.sort(games);
                EventBus.getDefault().post(new GamesLoadedEvent(games));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("", "DoRequest - Failure");
                EventBus.getDefault().post(new ServerErrorEvent());
            }
        });
    }
}
