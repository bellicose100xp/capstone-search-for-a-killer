package com.game.model;

import com.game.controller.AudioController;
import com.game.gui.GameFrame;
import com.game.view.AnsiTextColor;
import com.game.view.ConsoleText;

import java.util.ArrayList;
import java.util.List;

/**
 * Player class that is used to create the player object for the game.
 * The player will have a current location and an inventory relationship.
 */

public class Player extends Character {
    // INSTANCE VARIABLES
    private List<String> playerHistory = new ArrayList<>();

    // CONSTRUCTOR
    public Player(String name, String description, String currentLocation) {
        super(name, description, currentLocation);
    }

    // METHODS
    // move the player to a new location
    public void move(String newLocation) {
        // update the player location
        setCurrentLocation(newLocation);
    }

    public boolean pickupItem(Item item) {
        Room currentRoom = getRoom();
        if (item.isPickUpable() && currentRoom.getInventory().getItems().contains(item)) {
            AudioController.playSFX(3);
            // add item to player inventory
            getInventory().add(item);
            // remove item from room inventory
            currentRoom.getInventory().getItems().remove(item);
            return true;
        }
        return false;
    }

    public boolean dropItem(Item item) {
        if (item.isPickUpable() && getInventory().getItems().contains(item)) {
            AudioController.playSFX(4);
            Room currentRoom = getRoom();
            // add item to current location inventory
            currentRoom.getInventory().add(item);
            // remove item from player inventory
            getInventory().getItems().remove(item);
            return true;
        }
        return false;
    }

    // GETTERS AND SETTERS

    public StringBuilder getInventoryString() {
        // builds a string with all items in the inventory separated by a comma and a space.
        StringBuilder sb = new StringBuilder();
        // appends each item to the StringBuilder
        for (Item item : getInventory().getItems()) {
            sb.append(item.getName()).append(", ");
        }
        // removes the last coma for formatting purposes
        return sb.deleteCharAt(sb.length() - 2);
    }

    public List<String> getPlayerHistory() {
        return playerHistory;
    }

    public void setPlayerHistory(List<String> playerHistory) {
        this.playerHistory = playerHistory;
    }

    public void addToPlayerHistory(String history) {
        playerHistory.add(history);
    }
}