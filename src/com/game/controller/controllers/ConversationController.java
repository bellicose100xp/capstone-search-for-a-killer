package com.game.controller.controllers;


import com.game.controller.CheckWinningConditions;
import com.game.controller.GameResult;
import com.game.controller.LoadController;
import com.game.model.Conversation;
import com.game.model.Dialog;
import com.game.model.Player;
import com.game.view.ConsoleText;
import com.game.view.MultipleChoiceConsoleView;

import java.util.ArrayList;
import java.util.List;

import com.game.model.Character;

public class ConversationController {
    private MultipleChoiceConsoleView consoleView;
    private List<ConsoleText> mainText = new ArrayList<>();
    private List<ConsoleText> secondaryText = new ArrayList<>();
    private Character character;
    private CheckWinningConditions checkWinningConditions;


    public ConversationController(List<ConsoleText> mainText, CheckWinningConditions checkWinningConditions) {
        this.mainText = mainText;
        this.checkWinningConditions = checkWinningConditions;
    }

    public void run(Player player, Character character) {
        run(player, character, character.getConversation());
    }

    private void run(Player player, Character character, Conversation currentConversation) {
        this.character = character;
        List<String> questions = currentConversation.getConversationQuestions();

        int result = -1;
        while (result != questions.size() - 1 && (checkWinningConditions == null || checkWinningConditions.checkWinningConditions() == GameResult.UNDEFINED)) {
            if (checkWinningConditions != null && checkWinningConditions.checkWinningConditions() != GameResult.UNDEFINED)
                break;
            consoleView = new MultipleChoiceConsoleView(List.of(mainText, secondaryText), questions, false);
            secondaryText.clear();
            secondaryText.add(
                    new ConsoleText(String.format("This is a conversation between you and %s:", character.getName())));
            if (result != -1) {
                secondaryText.add(new ConsoleText(String.format("%s: %s", player.getName(), questions.get(result))));
                secondaryText.add(new ConsoleText(String.format("%s: %s", character.getName(),
                        currentConversation.getDialog(result).getResponse())));
                // This will report in case is possible, triggering a callback to report when the player tells the detective which one was the murder
            }
            // We check if the option selected has follow-up questions/dialog
            result = Integer.parseInt(consoleView.show());
            currentConversation.getDialog(result).reportIfAble();

            if (currentConversation.getDialog(result).getFollowUpConversation() != null) {
                run(player, character, currentConversation.getDialog(result).getFollowUpConversation());
                result = -1;
            }
        }
    }

    public void setCheckWinningConditions(CheckWinningConditions checkWinningConditions) {
        this.checkWinningConditions = checkWinningConditions;
    }
}
