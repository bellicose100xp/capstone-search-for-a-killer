package com.game.gui;

import com.game.controller.AudioController;
import com.game.gui.utils.DottedBorder;
import com.game.gui.utils.FontUtils;
import com.game.gui.utils.Score;
import com.game.model.Player;
import com.game.model.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import static com.game.gui.utils.ColorUtils.*;


public class MapPanel extends JPanel {
    private final Map<String, LocationLabel> locationLabels;
    private final LocationDescriptionPanel locationDescriptionPanel;
    private final LocationPanel locationPanel;
    private final PlayerPanel playerPanel;
    private final Player player;
    private final Map<String, Room> rooms;

    public MapPanel(LocationDescriptionPanel locationDescriptionPanel, LocationPanel locationPanel,
                    PlayerPanel playerPanel, Player player,
                    Map<String, Room> rooms) {
        this.locationDescriptionPanel = locationDescriptionPanel;
        this.locationPanel = locationPanel;
        this.playerPanel = playerPanel;
        this.player = player;
        this.rooms = rooms;

        // Set current room's description into location description panel
        locationDescriptionPanel.setText(player.getRoom().getDescription());

        // Create location label for each room
        locationLabels = new HashMap<>();
        for (Room room : this.rooms.values()) {
            LocationLabel locationLabel = new LocationLabel(room);
            locationLabels.put(room.getName(), locationLabel);
        }

        /* Add Grid Layout */
        this.setLayout(new GridLayout(3, 3));

        /* Initial Map Panel Render */
        renderMapPanel();

        int padding = 10;
        this.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        this.setBackground(MAP_DEFAULT_BACKGROUND.color());
        this.setBounds(586, 71, 391, 391);
    }

    private void renderMapPanel() {
        clearPanel(this);

        /* first row  */
        this.add(new JLabel());
        this.add(new JLabel());
        this.add(getLocationLabel("Game Room"));
        // this.add(locationLabels.get("Game Room"));
        /* second row */
        this.add(getLocationLabel("Kitchen"));
        this.add(getLocationLabel("Office"));
        this.add(getLocationLabel("Living Room"));
        /* third row */
        this.add(getLocationLabel("Backyard"));
        this.add(new JLabel());
        this.add(new JLabel());
    }

    private JLabel getLocationLabel(String roomName) {
        Room room = this.rooms.get(roomName);

        // If the room is visited or is an adjacent room, return the location label
        if (room.isVisited() || player.getRoom().isRoomAdjacent(room)) {
            return locationLabels.get(roomName);
        }

        // Otherwise, return a blank label
        return new JLabel();
    }

    private void clearPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    private class LocationLabel extends JLabel implements MouseListener {
        private Room room;

        public LocationLabel(Room room) {
            setRoom(room);

            // Location Label Specific Setting
            this.setText(room.getName());
            this.setFont(FontUtils.DEFAULT_FONT.deriveFont(16f));
            this.setForeground(GAME_MAP_TEXT.color());
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setVerticalAlignment(SwingConstants.CENTER);
            this.setOpaque(true);

            // Set initial background color for location panel
            Color initialBackground = room == player.getRoom()
                    ? CURRENT_LOCATION.color() : MAP_DEFAULT_BACKGROUND.color();

            this.setBackground(initialBackground);
            this.setBorder(new DottedBorder(GAME_MAP_TILE_BORDER.color(), 1, 3));
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // If this room is the current room, do nothing
            if (room == player.getRoom() || !player.getRoom().isRoomAdjacent(room)) {
                return;
            }

            // Play footstep sound to go into next room
            AudioController.playSFX(2);

            // Remove background from current room location label
            LocationLabel currentLocationLabel = locationLabels.get(player.getRoom().getName());
            currentLocationLabel.setBackground(MAP_DEFAULT_BACKGROUND.color());

            // Update player's room to be this room
            player.setRoom(room);

            // Update the items and characters in the room
            locationPanel.reloadLocationPanel();

            // Update background for this room to be green color
            this.setBackground(CURRENT_LOCATION.color());

            // Set this location as visited
            room.setVisited(true);

            // render map panel with new changes
            renderMapPanel();

            // Update description of description panel to be this rooms description
            locationDescriptionPanel.setText(player.getRoom().getDescription());

            // Increment score on player panel
            playerPanel.incrementScore(Score.getScoreMove());
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // If this room is current room do nothing
            if (room == player.getRoom()) {
                return;
            }

            // Show yellow if adjacent room else red color
            if (player.getRoom().isRoomAdjacent(room)) {
                this.setBackground(ADJACENT_LOCATION.color());
            } else {
                this.setBackground(UNREACHABLE_LOCATION.color());
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // If this room is current room do nothing
            if (room == player.getRoom()) {
                return;
            }

            // Set background color to be the default map background color
            this.setBackground(MAP_DEFAULT_BACKGROUND.color());
        }

        public Room getRoom() {
            return room;
        }

        public void setRoom(Room room) {
            this.room = room;
        }
    }
}

