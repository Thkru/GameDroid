package com.thkru.gamedroid.utils;

import com.thkru.gamedroid.data.Game;

import java.util.List;

public class GamesLoadedEvent {

    private List<Game> games;

    public GamesLoadedEvent(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }

}
