package com.game.controller;

import com.game.controller.commands.CommandCallBack;
import com.game.controller.commands.CommandType;
import com.game.model.Entity;

import java.util.List;

public class Command {
    // Main keyword of the command, it should be unique
    private String keyWord;
    // Any synonym that can be used instead of the main keyword e.g. command "go" may have "run", "move", "travel" as synonyms
    private List<String> synonyms;
    // A description to be shown when the user checks the list of commands and what they do
    private String description;
    // This will be a functional interface instance that receives an Entity and returns true/false
    private CommandCallBack callBack;
    // A standalone command is one that doesn't need a target e.g. "quit", "help"
    // A two parts command would be one with the <command> <target> e.g. "go there", "get egg"
    // A hybrid command is one that can be standalone and two parts e.g. "look sword" or just "look"
    private CommandType commandType = CommandType.STANDALONE;

    public Command(String keyWord, List<String> synonyms, String description) {
        this.keyWord = keyWord;
        this.synonyms = synonyms;
        this.description = description;
    }

    public Command(String keyWord, List<String> synonyms, String description, CommandCallBack callBack) {
        this(keyWord, synonyms, description);
        this.callBack = callBack;
    }

    public Command(String keyWord, List<String> synonyms, String description, CommandType commandType,
                   CommandCallBack callBack) {
        this(keyWord, synonyms, description);
        this.callBack = callBack;
        this.commandType = commandType;
    }

    // Returns true/false if the keyWord matches the command's keyWord or one of its synonyms
    public boolean isAMatch(String keyWord) {
        return (keyWord.equalsIgnoreCase(this.keyWord)
                || this.synonyms.contains(keyWord));
    }

    public boolean executeCommand(Entity target) {
        return callBack.execute(target);
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
}
