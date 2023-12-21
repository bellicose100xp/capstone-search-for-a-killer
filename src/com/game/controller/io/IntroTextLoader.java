package com.game.controller.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class IntroTextLoader {
    public static Map<String, String> introText = new HashMap<>();

    public IntroTextLoader() {
    }

    public static void loadIntroText() {
        Gson gson = new Gson();
        String json = FileUtils.readFromFile("data/introText.json");
        TypeToken<Map<String, String>> mapType = new TypeToken<>() {
        };
        introText = gson.fromJson(json, mapType);
    }
}
