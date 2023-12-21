package com.game.controller.controllers;

import com.game.controller.Command;
import com.game.controller.commands.CommandType;
import com.game.view.AnsiTextColor;
import com.game.view.CommandConsoleView;
import com.game.view.ConsoleText;
import com.game.view.MultipleChoiceConsoleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class QuitGameController {
    private CommandConsoleView consoleView;
    private List<ConsoleText> mainText;
    private List<ConsoleText> secondaryText;
    private List<ConsoleText> quitGameText = new ArrayList<>();
    private final Map<String, Command> commandMap = new TreeMap<>();

    public QuitGameController(List<ConsoleText> mainText, List<ConsoleText> secondaryText) {
        this.mainText = mainText;
        this.secondaryText = secondaryText;
        quitGameText.add(new ConsoleText("Are you sure you want to exit the game (Y/N)?", AnsiTextColor.RED));
    }

    public boolean run() {
        commandMap.put("yes", new Command("yes", List.of("y"), null, CommandType.STANDALONE, null));
        commandMap.put("no", new Command("no", List.of("n"), null, CommandType.STANDALONE, null));

        consoleView = new CommandConsoleView(List.of(mainText, secondaryText, quitGameText),
                new ArrayList<>(commandMap.values()), null, null);
        String userInput = consoleView.show();

        return userInput.equalsIgnoreCase("yes");
    }
}
