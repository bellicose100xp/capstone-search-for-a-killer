package com.game.gui;

import com.game.model.Player;
import com.game.model.Room;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static com.game.gui.utils.ColorUtils.GAME_BACKGROUND;

public class GameFrame extends JFrame {
    public GameFrame(Player player, Map<String, Room> rooms, Point prevFrameLocation) {

        /* Player Section */
        PlayerPanel playerPanel = new PlayerPanel(player);
        this.add(playerPanel);

        /* Title Bar Panel */
        TitlePanel titlePanel = new TitlePanel(this, playerPanel);
        this.add(titlePanel);

        /* Location Section */
        LocationPanel locationPanel = new LocationPanel(playerPanel, player);
        playerPanel.setLocationSection(locationPanel);
        this.add(locationPanel);

        /* Location Description Section */
        LocationDescriptionPanel locationDescriptionPanel = new LocationDescriptionPanel();
        this.add(locationDescriptionPanel);

        /* Map Section */
        MapPanel mapPanel = new MapPanel(locationDescriptionPanel, locationPanel, playerPanel, player, rooms);
        this.add(mapPanel);

        /* Game Frame Specific Settings */
        this.setTitle("Search For A Killer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000, 625);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocation(prevFrameLocation);
        this.getContentPane().setBackground(GAME_BACKGROUND.color());
    }
}
