package com.thkru.gamedroid.database;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Facade for Database/SharedPrefs persistance
 */
public class FavHelper {

    private static final String TAG = "FavHelper";

    public static boolean isFav(Context c, int id) {
        boolean isFav = new FavDbHandler(c).containsFav(id);// PreferenceManager.getDefaultSharedPreferences(c).contains(code);//old impl
        Log.d(TAG, "IsFav: " + id + " " + isFav);
        return isFav;
    }

    public static List<Integer> getFavCodes(Context c) {
        return new FavDbHandler(c).getAllFavs();
    }

    public static boolean handleFavourite(Context c, int id) {

        if (isFav(c, id)) { //when fav, remove it
            Log.d(TAG, "RemoveFav: " + id);
            new FavDbHandler(c).deleteFav(id);
//            PreferenceManager.getDefaultSharedPreferences(c).edit().remove(code).commit();//old impl
            return false;
        } else {  //when not fav, add it
            Log.d(TAG, "AddFav: " + id);
            new FavDbHandler(c).addFav(id);
//            PreferenceManager.getDefaultSharedPreferences(c).edit().putBoolean(code, true).commit();//old impl
            return true;
        }
    }
}
