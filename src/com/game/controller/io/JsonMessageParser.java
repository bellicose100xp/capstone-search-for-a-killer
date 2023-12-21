package com.game.controller.io;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class uses Gson to read the json data form the Messages.json file and creates
 * three Map<String, String> that correspond to each message type [general, error, info]
 */

public class JsonMessageParser {

    // INSTANCE VARIABLES
    private static Map<String, String> generalMessages = new HashMap<>();
    private static Map<String, String> errorMessages = new HashMap<>();
    private static Map<String, String> infoMessages = new HashMap<>();
    private static Map<String, String> endGameMessages = new HashMap<>();
    private static List<String> ignoreList = new ArrayList<>();
    private static List<String> playerOptions = new ArrayList<>();
    private static List<String> audioOptions = new ArrayList<>();


    // CONSTRUCTORS
    public JsonMessageParser() {
        this.loadMessages();
        this.loadIgnores();
    }

    // METHODS
    public void loadMessages() {
        String filePath = "data/Messages.json";

        // try to read and load the json data form the Messages.json file
        try (FileReader reader = new FileReader(filePath)) {
            // creat the gson object
            Gson gson = new Gson();
            // convert to json data
            Map<String, Map<String, String>> messages = gson.fromJson(reader, Map.class);
            // add all the general messages to the generalMessages map
            getGeneralMessages().putAll(messages.get("general"));
            // add all the error messages to the errorMessages map
            getErrorMessages().putAll(messages.get("error"));
            // add all the info messages to the infoMessages map
            getInfoMessages().putAll(messages.get("info"));
            // add endGame messages
            endGameMessages.putAll(messages.get("endgame"));
        } catch (Exception e) {
            System.out.println("Error reading the Message.json file: " + e.getMessage());
        }
    }

    public void loadIgnores() {
        String filePath = "data/IgnoreList.json";

        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Map<String, List<String>> ignores = gson.fromJson(reader, Map.class);
            getIgnoreList().addAll(ignores.get("ignoreList"));
        } catch (Exception e) {
            System.out.println("Error loading the ignore file " + e.getMessage());
        }
    }

    public static List<String> loadPlayerOptions() {
        String filePath = "data/PlayerOptions.json";

        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Map<String, List<String>> options = gson.fromJson(reader, Map.class);
            getPlayerOptions().addAll(options.get("Main Menu"));
        } catch (Exception e) {
            System.out.println("Error loading the ignore file " + e.getMessage());
        }
        return null;
    }

    public static void loadAudioOptions() {
        String filePath = "data/AudioOptions.json";

        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Map<String, List<String>> options = gson.fromJson(reader, Map.class);
            audioOptions.addAll(options.get("Audio Options"));
        } catch (Exception e) {
            System.out.println("Error loading the audio file: " + e.getMessage());
        }
    }

    // GETTERS AND SETTERS
    public Map<String, String> getGeneralMessages() {
        return generalMessages;
    }

    public void setGeneralMessages(Map<String, String> generalMessages) {
        this.generalMessages = generalMessages;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(Map<String, String> errorMessages) {
        JsonMessageParser.errorMessages = errorMessages;
    }

    public Map<String, String> getInfoMessages() {
        return infoMessages;
    }

    public void setInfoMessages(Map<String, String> infoMessages) {
        JsonMessageParser.infoMessages = infoMessages;
    }

    public List<String> getIgnoreList() {
        return ignoreList;
    }

    public static void setIgnoreList(List<String> ignoreList) {
        JsonMessageParser.ignoreList = ignoreList;
    }

    public static List<String> getPlayerOptions() {
        return playerOptions;
    }

    public static void setPlayerOptions(List<String> playerOptions) {
        JsonMessageParser.playerOptions = playerOptions;
    }

    public static List<String> getAudioOptions() {
        return audioOptions;
    }

    public static void setAudioOptions(List<String> audioOptions) {
        JsonMessageParser.audioOptions = audioOptions;
    }

    public static Map<String, String> getEndGameMessages() {
        return endGameMessages;
    }
}

