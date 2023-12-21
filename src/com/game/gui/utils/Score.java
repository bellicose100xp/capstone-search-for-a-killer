package com.game.gui.utils;

import com.game.controller.io.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class Score {
    private static int scoreStart;
    private static int scorePickupDrop;
    private static int scoreTalk;
    private static int scoreMove;

    private Score() {
        // static class
    }


    // GETTERS
    public static int getScoreStart() {
        return scoreStart;
    }

    public static int getScorePickupDrop() {
        return scorePickupDrop;
    }

    public static int getScoreTalk() {
        return scoreTalk;
    }

    public static int getScoreMove() {
        return scoreMove;
    }

    static {
        Gson gson = new Gson();
        String json = FileUtils.readFromFile("data/Utils.json");
        TypeToken<Map<String, String>> mapType = new TypeToken<>() {
        };
        try {
            scoreStart = Integer.parseInt(gson.fromJson(json, mapType).get("scoreStart"));
            scorePickupDrop = Integer.parseInt(gson.fromJson(json, mapType).get("scorePickupDrop"));
            scoreTalk = Integer.parseInt(gson.fromJson(json, mapType).get("scoreTalk"));
            scoreMove = Integer.parseInt(gson.fromJson(json, mapType).get("scoreMove"));
        } catch (Exception ignored) {
        }
    }
}
