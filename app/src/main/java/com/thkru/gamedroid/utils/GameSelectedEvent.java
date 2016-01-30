package com.thkru.gamedroid.utils;

import com.thkru.gamedroid.data.Game;
import com.thkru.gamedroid.data.OldGame;

public class GameSelectedEvent {

    private Game game;

    public GameSelectedEvent(Game f) {
        game = f;
    }

    public Game getGame() {
        return game;
    }
}
