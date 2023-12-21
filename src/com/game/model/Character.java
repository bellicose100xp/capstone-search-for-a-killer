package com.game.model;

/*
 * This class represents a character in the game, like a suspect
 */
public class Character extends Entity {
    // A character must be in a Room
    private String currentLocation;
    private Room room;
    private Conversation conversation = new Conversation();
    private boolean isSuspect;
    private boolean isSergeant;
    private boolean isMurderer;
    private String clue;

    public Character(String name, String description, String currentLocation) {
        super(name, description);
        setCurrentLocation(currentLocation);
    }

    // GETTERS AND SETTERS

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public boolean isSuspect() {
        return isSuspect;
    }

    public boolean isSergeant() {
        return isSergeant;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        setCurrentLocation(room.getName());
    }

    public boolean isMurderer() {
        return isMurderer;
    }

    public void setMurderer(boolean value) {
        this.isMurderer = value;
    }
}
