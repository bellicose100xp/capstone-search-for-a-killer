package com.game.view;

import com.game.controller.Command;
import com.game.view.framework.InputCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CommandConsoleView extends ConsoleView {

    List<Command> commands = new ArrayList<>();
    private List<String> entities = new ArrayList<>();
    private List<String> ignoreList = new ArrayList<>();
    private List<String> standaloneCommands = new ArrayList<>();
    private String escapeCommand = "quit";

    public CommandConsoleView(List<List<ConsoleText>> text, List<Command> commands, List<String> entities,
                              List<String> ignoreList) {
        super(text);
        this.commands = commands;
        this.entities = entities;
        this.ignoreList = ignoreList;
    }

    @Override
    String collectInput() {
        return InputCollector.collectInput(commands, entities, ignoreList);
    }
}
