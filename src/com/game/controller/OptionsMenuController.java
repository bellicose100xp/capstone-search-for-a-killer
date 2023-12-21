package com.game.controller;

import com.game.controller.io.JsonMessageParser;
import com.game.model.NotImplementedException;
import com.game.view.*;

import java.util.ArrayList;
import java.util.List;

class OptionsMenuController {
    List<ConsoleText> options = new ArrayList<>();
    private List<ConsoleText> mainText = new ArrayList<>();
    private List<ConsoleText> secondaryText = new ArrayList<>();

    private final List<ConsoleText> gameResult = new ArrayList<>();

    public void run() {
        JsonMessageParser.loadPlayerOptions();
        if (options.isEmpty()) {
            for (String option : JsonMessageParser.getPlayerOptions()) {
                options.add(new ConsoleText(option));
            }
        }
        secondaryText = new ArrayList<>();
        secondaryText.add(new ConsoleText("Main Menu:", AnsiTextColor.BLUE));
        MultipleChoiceConsoleView consoleView = new MultipleChoiceConsoleView(List.of(mainText, secondaryText),
                options);
        while (true) {
            String userInput = consoleView.show();
            switch (userInput) {
                case "0":
                    newGame();
                    break;
                case "1":
                    quitGame();
                    break;
            }
        }
    }

    private void newGame() {
        LoadController.loadAllEntities();
        mainText.clear();
        GameController gameController = new GameController();
        GameResult gameResult = gameController.run();
        if (gameResult == GameResult.LOSS) {
            mainText.add(new ConsoleText(JsonMessageParser.getEndGameMessages().get("lose"), AnsiTextColor.RED));
        } else {
            mainText.add(new ConsoleText(JsonMessageParser.getEndGameMessages().get("win"), AnsiTextColor.GREEN));
        }
    }

    private void quitGame() {
        System.exit(0);
    }
}
