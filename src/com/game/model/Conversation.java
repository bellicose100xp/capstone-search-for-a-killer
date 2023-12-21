package com.game.model;

import com.game.controller.commands.CommandCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conversation {
    private List<Dialog> conversation = new ArrayList<>();
    private int count = 0;


    public List<Dialog> getConversation() {
        return this.conversation.stream()
                .filter(dialog -> dialog.getDept() >= this.count)
                .collect(Collectors.toList());
    }

    public List<String> getConversationQuestions() {
        return this.getConversation().stream()
                .map(Dialog::getQuestion)
                .collect(Collectors.toList());
    }

    public Dialog getDialog(int index) {
        return conversation.get(index);
    }

    public void increaseCount() {
        this.count++;
    }

    public void addDialog(Dialog dialog) {
        conversation.add(dialog);
    }

    public void insertDialog(Dialog dialog) {
        if (conversation.size() == 0) {
            addDialog(dialog);
        } else {
            conversation.add(conversation.size() - 1, dialog);
        }
    }
}
