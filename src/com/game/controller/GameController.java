package com.game.controller;

import com.game.controller.commands.CommandType;
import com.game.controller.controllers.ConversationController;
import com.game.controller.controllers.QuitGameController;
import com.game.controller.io.JsonMessageParser;
import com.game.model.*;
import com.game.ui.GamePanel;
import com.game.view.AnsiTextColor;
import com.game.view.CommandConsoleView;
import com.game.view.ConsoleText;
import com.game.model.Character;

import java.util.*;

public class GameController {
    private CommandConsoleView consoleView;
    private final Player player = LoadController.getPlayer();
    private final Map<String, Room> rooms = LoadController.getRooms();
    private final Map<String, Item> items = LoadController.getItems();
    private final Map<String, Character> characters = LoadController.getCharacters();
    // gameText is an object that has multiple Lists/Maps [general, error, info]
    // that contain text used in the game
    private static final JsonMessageParser gameText = new JsonMessageParser();
    private List<ConsoleText> mainText = new ArrayList<>();
    private List<ConsoleText> secondaryText = new ArrayList<>();
    private final Map<String, Command> commandMap = new TreeMap<>();
    private final MapLoaderController mapLoaderController = new MapLoaderController();
    private final ConversationController conversationController = new ConversationController(mainText,
            this::checkForWinningConditions);
    private Character reportedMurder = null;
    private Item reportedMurderWeapon = null;


    public GameController() {
        setDetectiveEndGameConversation();
    }

    public GameResult run() {

        // load the game map from json
        mapLoaderController.loadMap();

        Map<String, Entity> entityDictionary = new HashMap<>();
        entityDictionary.putAll(rooms);
        entityDictionary.putAll(items);
        entityDictionary.putAll(characters);
        mainText = getViewText();
        commandMap.put("go",
                new Command("go", List.of("run", "move", "walk", "travel"), "Go to a room. e.g. go kitchen",
                        CommandType.TWO_PARTS, this::goCommand));
        commandMap.put("look",
                new Command("look", List.of("see", "inspect", "read"), "Look at an object or room. e.g. look knife",
                        CommandType.HYBRID, this::lookCommand));
        commandMap.put("quit",
                new Command("quit", List.of("exit"), "Quits the game, no questions asked.", CommandType.STANDALONE,
                        this::quitCommand));
        commandMap.put("help",
                new Command("help", List.of(), "It displays this menu.", CommandType.STANDALONE, this::helpCommand));
        commandMap.put("drop", new Command("drop", List.of("place", "put"),
                "Drop an object from your inventory into your current location", CommandType.TWO_PARTS,
                this::dropCommand));
        commandMap.put("get", new Command("get", List.of("grab", "pickup", "take"),
                "Drop an object from your inventory into your current location", CommandType.TWO_PARTS,
                this::getCommand));
        commandMap.put("talk",
                new Command("talk", List.of("chat", "speak"), "Talk to another character", CommandType.TWO_PARTS,
                        this::talkCommand));
        commandMap.put("volume",
                new Command("volume", List.of("sound", "vol"), "Change the volume settings", CommandType.STANDALONE,
                        this::volCommand));

        // Update help in the game gui help panel
        GamePanel.helpText.setText(createHelpText());

        // List of entities
        List<String> entities = new ArrayList<>(entityDictionary.keySet());

        // List of words to ignore
        List<String> ignoreList = gameText.getIgnoreList();

        String escapeCommand = gameText.getGeneralMessages().get("quit");

        consoleView = new CommandConsoleView(List.of(mainText, secondaryText), new ArrayList<>(commandMap.values()),
                entities, ignoreList);
        GameResult gameResult = GameResult.UNDEFINED;
        while (gameResult == GameResult.UNDEFINED) {
            // show() command display some text, performs some logic on the displayed text if needed, and also collects user input
            String userInput = consoleView.show();
            String[] parts = userInput.split(" ", 2);
            boolean result = false;

            Entity entity = parts.length > 1 ? entityDictionary.get(parts[1]) : null;

            if ((commandMap.get(parts[0]).getCommandType() == CommandType.TWO_PARTS)) {
                if (entity == null) {
                    consoleView.setErrorMessage(gameText.getErrorMessages().get("invalidAction"));
                    continue;
                }
            }
            result = commandMap.get(parts[0]).executeCommand(entity);
            mainText.clear();
            mainText.addAll(getViewText());

            if (result) {
                consoleView.clearErrorMessage();
            }

            gameResult = checkForWinningConditions();
        }
        player.getPlayerHistory().clear();
        mapLoaderController.buildMap(player.getCurrentLocation(), player.getPlayerHistory());
        mapLoaderController.displayMap();
        return gameResult;
    }

    private boolean goCommand(Entity target) {
        if (target instanceof Room) {
            // if the room they are trying to go to is in current location's adjacent rooms
            if (rooms.get(player.getCurrentLocation()).getAdjacentRooms().contains(target)) {
                // mainText.clear();
                AudioController.playSFX(2);
                secondaryText.clear();
                Room room = (Room) target;
                player.setCurrentLocation(room.getName());
                // add the new room to the player's location history for map rendering
                if (!player.getPlayerHistory().contains(room.getName())) {
                    player.addToPlayerHistory(room.getName());
                }
                // mainText.addAll(getViewText());
                return true;
            }
            // if they are trying to go to a room, but not an adjacent one
            consoleView.setErrorMessage(
                    String.format(gameText.getErrorMessages().get("invalidRoomTravers"), target.getName()));
            return false;
        }
        // if they are not trying to go to a valid room
        consoleView.setErrorMessage(String.format(gameText.getErrorMessages().get("invalidRoomName"),
                target != null ? target.getName() : gameText.getErrorMessages().get("invalidDefaultThat")));
        return false;
    }

    private boolean lookCommand(Entity target) {
        if (target == null) {
            if (lookRoom(rooms.get(player.getCurrentLocation())))
                return true;
        }
        if (target instanceof Room) {
            // This clause is necessary to allow correct error message to print
            if (lookRoom((Room) target)) {
                return true;
            }
        } else if (target instanceof Item) {
            if (lookItem((Item) target)) {
                return true;
            }
        } else if (target instanceof Character) {
            if (lookCharacter((Character) target)) {
                return true;
            }
        }
        consoleView.setErrorMessage(String.format(gameText.getErrorMessages().get("invalidLook"),
                target != null ? target.getName() : gameText.getErrorMessages().get("invalidDefaultThat")));
        return false;
    }

    private boolean lookRoom(Room room) {
        secondaryText.clear();
        // If the room they are looking at is the currentLocation
        if (room == rooms.get(player.getCurrentLocation())) {
            // print the room description
            secondaryText.add(new ConsoleText(room.getDescription()));
            if (!room.getInventory().getItems().isEmpty()) {
                // print items in room if there are any
                secondaryText.add(
                        new ConsoleText(gameText.getInfoMessages().get("visibleItems") + room.getInventory()));
            }
            if (!(room.getCharactersInRoom() == null) && !room.getCharactersInRoom().isEmpty()) {
                secondaryText.add(new ConsoleText(gameText.getInfoMessages().get("personVisible"),
                        room.getCharactersInRoomToString()));
            }
            // print adjacent rooms
            secondaryText.add(
                    new ConsoleText(gameText.getInfoMessages().get("traversableRooms"), room.getJsonAdjacentRooms()));
            secondaryText.add(new ConsoleText(gameText.getGeneralMessages().get("divider"), AnsiTextColor.BLUE));
            return true;
        }
        return false;
    }

    private boolean lookItem(Item item) {
        secondaryText.clear();
        // if the item is in your inventory
        // or in the inventory of the room are you currently in
        if (player.getInventory().getItems().contains(item) || rooms.get(
                player.getCurrentLocation()).getInventory().getItems().contains(item)) {
            secondaryText.add(new ConsoleText(item.getDescription()));
            if (!item.getInventory().getItems().isEmpty()) {
                secondaryText.add(new ConsoleText(
                        String.format(gameText.getInfoMessages().get("observeItem"), item.getName(),
                                item.getInventory())));
            }
            secondaryText.add(new ConsoleText(gameText.getGeneralMessages().get("divider"), AnsiTextColor.BLUE));
            return true;
        }
        return false;
    }

    private String createHelpText() {
        StringBuilder sb = new StringBuilder();
        for (var command : commandMap.values()) {
            String line = String.format("%s: \t%s", command.getKeyWord(), command.getDescription());
            sb.append(line);
            sb.append("\n");
        }

        return sb.toString();
    }

    private boolean helpCommand(Entity target) {
        secondaryText.clear();
        secondaryText.add(new ConsoleText(gameText.getInfoMessages().get("availableCommands")));
        for (var command : commandMap.values()) {
            secondaryText.add(
                    new ConsoleText(String.format("%s: \t%s", command.getKeyWord(), command.getDescription())));
        }
        secondaryText.add(new ConsoleText(gameText.getGeneralMessages().get("divider"), AnsiTextColor.BLUE));
        return true;
    }

    private boolean lookCharacter(Character character) {
        if (player.getCurrentLocation().equals(character.getCurrentLocation())) {
            secondaryText.add(new ConsoleText(character.getDescription()));
            secondaryText.add(new ConsoleText(gameText.getGeneralMessages().get("divider"), AnsiTextColor.BLUE));
            return true;
        }
        return false;
    }

    private boolean dropCommand(Entity target) {
        secondaryText.clear();
        // if target is instance of Item
        if (target instanceof Item) {
            // if target Item is in your inventory
            if (player.getInventory().getItems().contains(target)) {
                AudioController.playSFX(4);
                // add item to current location inventory
                rooms.get(player.getCurrentLocation()).getInventory().add((Item) target);
                // remove item from player inventory
                player.getInventory().getItems().remove((Item) target);
                // Tell the player what happened
                secondaryText.add(
                        new ConsoleText(String.format(gameText.getInfoMessages().get("dropItem"), target.getName())));
                secondaryText.add(new ConsoleText(gameText.getGeneralMessages().get("divider"), AnsiTextColor.BLUE));
                return true;
            }
            // that item is not in your inventory, so you can't drop it
            consoleView.setErrorMessage(gameText.getErrorMessages().get("invalidInventoryDropItem"));
            return false;
        }
        consoleView.setErrorMessage(gameText.getErrorMessages().get("invalidTypeDropItem"));
        return false;
    }

    private boolean talkCommand(Entity target) {
        // If the target is a character
        if (target instanceof Character) {
            Character character = (Character) target;
            // If the target is in the same room as the player
            if (((Character) target).getCurrentLocation().equals(player.getCurrentLocation())) {
                conversationController.run(player, character);
                return true;
            }
            consoleView.setErrorMessage(String.format(gameText.getErrorMessages().get("invalidCharacterPresence")));
            return false;
        }
        consoleView.setErrorMessage(String.format(gameText.getErrorMessages().get("invalidCharacterType")));
        return false;
    }

    private boolean getCommand(Entity target) {
        secondaryText.clear();
        if (target instanceof Item) {
            Room currentRoom = rooms.get(player.getCurrentLocation());
            Item item = (Item) target;
            if (!item.isPickUpable()) {
                consoleView.setErrorMessage(
                        String.format(gameText.getErrorMessages().get("invalidItemPickup"), item.getName()));
                return false;
            }
            if (currentRoom.getInventory().contains(item)) {
                AudioController.playSFX(3);
                player.getInventory().add(item);
                currentRoom.getInventory().remove(item);
                mainText.clear();
                mainText.addAll(getViewText());
                secondaryText.add(
                        new ConsoleText(String.format(gameText.getInfoMessages().get("itemPickup"), target.getName())));
                secondaryText.add(new ConsoleText(gameText.getGeneralMessages().get("divider"), AnsiTextColor.BLUE));
                return true;
            }
            consoleView.setErrorMessage(gameText.getErrorMessages().get("invalidItemNotPresent"));
        }
        consoleView.setErrorMessage(gameText.getErrorMessages().get("invalidNotAnItem"));
        return false;
    }

    private boolean quitCommand(Entity target) {
        QuitGameController quitGameController = new QuitGameController(mainText, secondaryText);
        if (quitGameController.run()) {
            System.exit(0);
        }
        return true;
    }

    private boolean volCommand(Entity target) {
        boolean success = AudioController.volMenu();
        if (success) {
            return true;
        }
        consoleView.setErrorMessage("The action could not be taken.");
        return false;
    }

    private List<ConsoleText> getViewText() {

        // View text to be passed to our view
        List<ConsoleText> result = new ArrayList<>();

        // build the map based on the player location history and current location
        mapLoaderController.buildMap(player.getCurrentLocation(), player.getPlayerHistory());
        // display the updated map that was built using the buildMap method
        mapLoaderController.displayMap();

        result.add(new ConsoleText(gameText.getGeneralMessages().get("divider"), AnsiTextColor.BLUE));
        result.add(new ConsoleText(
                String.format(gameText.getInfoMessages().get("playerLocation"), player.getCurrentLocation())));
        result.add(new ConsoleText(
                String.format(gameText.getInfoMessages().get("playerInventory"), player.getInventory())));
        result.add(new ConsoleText(gameText.getGeneralMessages().get("divider"), AnsiTextColor.BLUE));
        result.add(new ConsoleText(String.format(gameText.getInfoMessages().get("connectedRooms"),
                rooms.get(player.getCurrentLocation()).adjacentRoomToString())));
        result.add(new ConsoleText(gameText.getGeneralMessages().get("divider"), AnsiTextColor.BLUE));
        return result;
    }

    private boolean reportCommand(Entity target) {
        if (target instanceof Item) {
            reportedMurderWeapon = (Item) target;
        }
        if (target instanceof Character) {
            reportedMurder = (Character) target;
        }
        return true;
    }

    private GameResult checkForWinningConditions() {
        if (reportedMurder == null || reportedMurderWeapon == null)
            return GameResult.UNDEFINED;
        else {
            return (reportedMurder == LoadController.getMurderer() && reportedMurderWeapon == LoadController.getMurderWeapon())
                    ? GameResult.WIN : GameResult.LOSS;
        }
    }


    private void setDetectiveEndGameConversation() {
        Conversation mainConversation = new Conversation();
        Dialog murdererDialog = new Dialog("I know who the murderer is",
                "Perfect, who do you think committed the murder?");
        Dialog murdererWeaponDialog = new Dialog("I know which one was the murder weapon",
                "Perfect, which was the murder weapon?");

        Character detective = LoadController.getDetective();

        Conversation murdererConversation = new Conversation();
        for (var suspect : LoadController.getSuspects().values()) {
            Dialog dialog = new Dialog(suspect.getName(), "Noted, you think the murderer was " + suspect.getName());
            dialog.setReport(suspect);
            dialog.setCallBack(this::reportCommand);
            murdererConversation.addDialog(dialog);
        }
        murdererConversation.addDialog(new Dialog("On the other hand.", ""));
        murdererDialog.setFollowUpConversation(murdererConversation);

        Conversation murdererWeaponConversation = new Conversation();
        for (var weapon : LoadController.getWeapons().values()) {
            Dialog dialog = new Dialog(weapon.getName(), "Noted, you think the murder weapon was " + weapon.getName());
            dialog.setReport(weapon);
            dialog.setCallBack(this::reportCommand);
            murdererWeaponConversation.addDialog(dialog);
        }
        murdererWeaponConversation.addDialog(new Dialog("On the other hand.", ""));
        murdererWeaponDialog.setFollowUpConversation(murdererWeaponConversation);

        detective.getConversation().insertDialog(murdererDialog);
        detective.getConversation().insertDialog(murdererWeaponDialog);
    }
}
