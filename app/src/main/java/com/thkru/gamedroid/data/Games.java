package com.thkru.gamedroid.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Games {
    @SerializedName("games")
    @Expose
    public List<Game> games = new ArrayList<Game>();

}
