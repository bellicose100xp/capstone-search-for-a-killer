package com.game.controller;

import com.game.controller.io.IntroTextLoader;
import com.game.gui.GameFrame;
import com.game.gui.intro.IntroScreen;
import com.game.gui.intro.SplashScreen;
import com.game.model.Player;
import com.game.model.Room;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class MainController {
    public static int INITIAL_AUDIO_VOLUME = 20;
    public static int INITIAL_SFX_VOLUME = 40;

    public static void main(String[] args) throws IOException {
        /*
         * Pre-load static game data
         */
        IntroTextLoader.loadIntroText();
        LoadController.loadAllEntities();

        /*
         * Splash Screen
         */
        CountDownLatch splashScreenLatch = new CountDownLatch(1);
        // Getting atomic reference outside since lambda requires final or effectively final variables
        AtomicReference<Frame> splashScreenRef = new AtomicReference<>();
        SwingUtilities.invokeLater(() -> splashScreenRef.set(new SplashScreen(splashScreenLatch)));
        AtomicReference<Point> splashScreenLocationRef = waitUntilScreenClosed(splashScreenLatch, splashScreenRef);

        /*
         * Intro Screen
         */
        CountDownLatch introScreenLatch = new CountDownLatch(1);
        // Getting atomic reference outside since lambda requires final or effectively final variables
        AtomicReference<Frame> introScreenRef = new AtomicReference<>();
        SwingUtilities.invokeLater(
                () -> introScreenRef.set(new IntroScreen(introScreenLatch, splashScreenLocationRef.get())));
        AtomicReference<Point> introScreenLocationRef = waitUntilScreenClosed(introScreenLatch, introScreenRef);

        /* Swing UI */
        Map<String, Room> rooms = LoadController.getRooms();
        Player player = LoadController.getPlayer();
        SwingUtilities.invokeLater(() -> new GameFrame(player, rooms, introScreenLocationRef.get()));

        /*
         * Load audio into memory and set initial volume
         */
        AudioController.loadMusic();
        AudioController.setMusicVolume((float) INITIAL_AUDIO_VOLUME);
        AudioController.setSfxVolume((float) INITIAL_SFX_VOLUME);
        AudioController.loopMusic();

        // Create the titlePage and devTitlePage objects that will take in the json data and pass it
        // to the ConsoleText class and the Console class, so it's outputted.
//        GsonParserController titlePage = new GsonParserController("data/Title.json");
//        GsonParserController developmentPage = new GsonParserController("data/DevelopmentTitle.json");
//        GsonParserController introText = new GsonParserController("data/IntroText.json");
//
//        titlePage.printJson();
//        developmentPage.printJson();
//        introText.printJson();
//        OptionsMenuController optionsMenuController = new OptionsMenuController();
//        optionsMenuController.run();
    }

    private static AtomicReference<Point> waitUntilScreenClosed(CountDownLatch latch, AtomicReference<Frame> ref) {
        AtomicReference<Point> locationRef = new AtomicReference<>();
        try {
            latch.await(); // Wait until latch is counted down to 0
            locationRef.set(ref.get().getLocation()); // Get the location of the frame before it is disposed
            ref.get().dispose(); // dispose of the frame
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return locationRef; // return atomic reference to previous frame's location
    }
}