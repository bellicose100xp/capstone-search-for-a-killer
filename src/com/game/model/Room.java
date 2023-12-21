package com.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Room represents a room in the game, like the Kitchen, the Living Room etc
 */
public class Room extends Entity {
    private List<Room> adjacentRooms = new ArrayList<>();
    private final List<String> jsonAdjacentRooms = new ArrayList<>();
    private final List<String> jsonCharactersInRoom = new ArrayList<>();
    private List<Character> charactersInRoom;
    private boolean isVisited;

    public Room(String name, String description) {
        super(name, description);
    }

    public boolean isRoomAdjacent(Room room) {
        return adjacentRooms.contains(room);
    }

    public void addAdjacentRoom(Room room, Room... rooms) {
        adjacentRooms.add(room);
        for (var room_entry : rooms) {
            addAdjacentRoom(room_entry);
        }
    }

    public List<Room> getAdjacentRooms() {
        return adjacentRooms;
    }

    public void setAdjacentRooms(List<Room> adjacentRooms) {
        this.adjacentRooms = adjacentRooms;
    }

    public void addAdjacentRoom(Room room) {
        this.adjacentRooms.add(room);
    }

    public List<String> getJsonAdjacentRooms() {
        return jsonAdjacentRooms;
    }

    public List<Character> getCharactersInRoom() {
        return charactersInRoom;
    }

    public List<String> getCharactersInRoomToString() {
        return charactersInRoom.stream()
                .map(Character::toString)
                .collect(Collectors.toList());
    }

    public void addCharacterToRoom(String name) {
        jsonCharactersInRoom.add(name);
    }

    // returns a formatted StringBuilder String with commas
    public StringBuilder adjacentRoomToString() {
        StringBuilder sb = new StringBuilder();
        for (var room : adjacentRooms) {
            sb.append(room.getName()).append(", ");
        }
        return sb.deleteCharAt(sb.length() - 2); // removes the last comma for formatting purposes
    }

    public void setCharactersInRoom(List<Character> charactersInRoom) {
        this.charactersInRoom = charactersInRoom;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
