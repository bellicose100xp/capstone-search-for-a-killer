package com.game.controller;

import com.game.controller.io.JsonConversation;
import com.game.model.Character;
import com.game.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class LoadController {
    private static Map<String, Room> rooms;
    private static Map<String, Item> items;
    private static Map<String, Character> characters;
    private static Player player;
    private static Character detective;
    private static Item murderWeapon;
    private static Character murderer;

    private LoadController() {
    }

    public static void loadAllEntities() {
        rooms = loadRooms();
        items = loadItems();
        characters = loadCharacters();
        setRandomMurderer();
        setRandomMurderWeapon();
        player = loadPlayer();
        loadDialogue();
        loadConversations();
        fixAllHasAs();
        randomizeItemsInRooms();
    }

    private static void setRandomMurderer() {
        List<Map.Entry<String, Character>> characterList = new ArrayList<>(characters.entrySet());
        Random rand = new Random();
        int index = rand.nextInt(characterList.size());
        Map.Entry<String, Character> randomCharacter = characterList.get(index);

        if (!randomCharacter.getValue().isSuspect()) {
            setRandomMurderer();
            return;
        } else {
            characters.get(randomCharacter.getKey()).setMurderer(true);
            murderer = characters.get(randomCharacter.getKey());
        }
        //System.out.println(randomCharacter.getKey() + " is the murderer");
    }

    private static void setRandomMurderWeapon() {
        List<Map.Entry<String, Item>> itemsList = new ArrayList<>(items.entrySet());
        List<Map.Entry<String, Item>> weaponItemsOnly = new ArrayList<>();
        for (Map.Entry<String, Item> item : itemsList) {
            if (item.getValue().isWeapon()) {
                weaponItemsOnly.add(item);
            }
        }
        Random rand = new Random();
        int index = rand.nextInt(weaponItemsOnly.size());
        Map.Entry<String, Item> randomItem = weaponItemsOnly.get(index);

        items.get(randomItem.getKey()).setMurderWeapon(true);
        items.get(randomItem.getKey()).setDescription(items.get(randomItem.getKey()).getBadDescription());
        murderWeapon = items.get(randomItem.getKey());
        //System.out.println(murderWeapon.getName() + " is the murder weapon.");
    }

    private static void randomizeItemsInRooms() {
        List<Map.Entry<String, Room>> roomList = new ArrayList<>(rooms.entrySet());
        Random rand = new Random();

        // loop through items
        for (Item item : items.values()) {
            if (item.isPickUpable()) {
                // place pickupAble items randomly in rooms
                int index = rand.nextInt(roomList.size());
                Map.Entry<String, Room> randomRoom = roomList.get(index);
                randomRoom.getValue().getInventory().add(item);
                rooms.get(randomRoom.getKey()).setInventory(randomRoom.getValue().getInventory());
            }
        }
    }

    private static void loadDialogue() {
        try (FileReader reader = new FileReader("data/Dialogue.json")) {
            Gson gson = new Gson();
            TypeToken<Map<String, List<String>>> typeToken = new TypeToken<>() {
            };
            Map<String, List<String>> dialogueJson = gson.fromJson(reader, typeToken);

            Random rand = new Random();
            List<String> randomStrings;
            for (Character character : characters.values()) {
                if (character.isMurderer()) {
                    // set text to murderer text
                    randomStrings = dialogueJson.get("murderer");
                } else if (!character.isSuspect()) {
                    continue;
                } else {
                    // set text to non-murderer text
                    randomStrings = dialogueJson.get("nonMurderer");
                }
                String randomString = randomStrings.get(rand.nextInt(randomStrings.size()));
                character.setClue(randomString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Adds characters, inventory and adjacent rooms objects to each room
    private static void fixAllHasAs() {
        for (Room room : rooms.values()) {
            room.setInventory(new Inventory());
            room.setAdjacentRooms(new ArrayList<>());
            room.setCharactersInRoom(new ArrayList<>());
            for (String key : room.getJsonInventory()) {
                Item item = items.get(key);
                Inventory inventory = room.getInventory();
                inventory.add(item);
            }
            for (String key : room.getJsonAdjacentRooms()) {
                Room r = rooms.get(key);
                room.addAdjacentRoom(r);
            }
        }
        // Add the HAS-A for each character inventory item
        for (Character character : characters.values()) {
            character.setInventory(new Inventory());
            character.setRoom(rooms.get(character.getCurrentLocation()));
            character.getRoom().getCharactersInRoom().add(character);
            for (String key : character.getJsonInventory()) {
                Item item = items.get(key);
                character.getInventory().add(item);
            }
        }

        // Add the HAS-A for each player inventory item
        player.setInventory(new Inventory());
        for (String key : player.getJsonInventory()) {
            Item item = items.get(key);
            player.getInventory().add(item);
        }

        // Add initial player location
        Room room = rooms.get(player.getCurrentLocation());
        player.setRoom(room);
    }

    private static Map<String, Room> loadRooms() {
        try (FileReader reader = new FileReader("data/Rooms.json")) {
            Room[] rooms = new Gson().fromJson(reader, Room[].class);
            Map<String, Room> roomMap = new HashMap<>();

            for (Room room : rooms) {
                roomMap.put(room.getName(), room);
            }

            return roomMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, Item> loadItems() {
        try (FileReader reader = new FileReader("data/Items.json")) {
            Item[] items = new Gson().fromJson(reader, Item[].class);
            Map<String, Item> itemMap = new HashMap<>();

            for (Item item : items) {
                itemMap.put(item.getName(), item);
                if (item.isMurderWeapon())
                    murderWeapon = item;
            }

            return itemMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, Character> loadCharacters() {
        try (FileReader reader = new FileReader("data/Character.json")) {
            Character[] characters = new Gson().fromJson(reader, Character[].class);
            Map<String, Character> characterMap = new HashMap<>();

            for (Character character : characters) {
                characterMap.put(character.getName(), character);
                if (character.isSergeant())
                    detective = character;
                if (character.isMurderer())
                    murderer = character;
            }
            return characterMap;
        } catch (Exception e) {
            System.out.printf("Error reading the character json file: %s%n", e.getMessage());
        }
        return null;
    }

    private static Player loadPlayer() {
        try (FileReader reader = new FileReader("data/Player.json")) {
            return new Gson().fromJson(reader, Player.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void loadConversations() {
        JsonConversation conversations;
        try (FileReader reader = new FileReader("data/Conversation.json")) {
            conversations = new Gson().fromJson(reader, JsonConversation.class);
            for (Character character : getCharacters().values()) {
                character.setConversation(new Conversation());
                character.getConversation().addDialog(new Dialog(conversations.getRandomGreeting(player),
                        conversations.getRandomGreeting(character)));
                character.getConversation().addDialog(new Dialog(conversations.getRandomIntroduction(player),
                        conversations.getRandomIntroduction(character)));
                character.getConversation().addDialog(
                        new Dialog(conversations.getRandomInquiry(), character.getClue()));
                character.getConversation().addDialog(new Dialog(conversations.getRandomFarewell(player),
                        conversations.getRandomFarewell(character)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Room> getRooms() {
        if (rooms == null)
            loadAllEntities();
        return rooms;
    }

    public static Map<String, Item> getItems() {
        if (items == null)
            loadAllEntities();
        return items;
    }

    public static Map<String, Character> getCharacters() {
        if (characters == null)
            loadAllEntities();
        return characters;
    }

    public static Player getPlayer() {
        if (player == null)
            loadAllEntities();
        return player;
    }

    public static Map<String, Character> getSuspects() {
        if (characters == null)
            loadAllEntities();
        return characters.entrySet().stream()
                .filter(character -> character.getValue().isSuspect())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, Item> getWeapons() {
        if (items == null)
            loadAllEntities();
        return items.entrySet().stream()
                .filter(entry -> entry.getValue().isWeapon())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Character getDetective() {
        if (detective == null)
            loadAllEntities();
        return detective;
    }

    public static Item getMurderWeapon() {
        if (murderWeapon == null)
            loadAllEntities();
        return murderWeapon;
    }

    public static Character getMurderer() {
        if (murderer == null)
            loadAllEntities();
        return murderer;
    }
}