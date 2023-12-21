package com.game.view;

import com.game.view.framework.InputCollector;
import com.game.view.framework.InvalidInputException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.game.view.framework.InputCollector.collectInput;

public class MultipleChoiceConsoleView extends ConsoleView {
    private List<ConsoleText> options;

    public MultipleChoiceConsoleView(List<List<ConsoleText>> textList, List<ConsoleText> options) {
        super(textList);
        this.options = options;
    }

    public MultipleChoiceConsoleView(List<List<ConsoleText>> textList, List<String> options, boolean flag) {
        super(textList);
        this.options = new ArrayList<>();
        for (var option : options) {
            addOptions(new ConsoleText(option));
        }
    }

    public void addOptions(ConsoleText... options) {
        this.options.addAll(Arrays.asList(options));
    }

    @Override
    void executeViewLogic() {
        for (int i = 0; i < options.size(); i++) {
            Console.print((i + 1) + ": ");
            Console.printNewLine(this.options.get(i));
        }
    }

    @Override
    String collectInput() {
        return InputCollector.collectInput(options);
    }

    public List<ConsoleText> getOptions() {
        return options;
    }

    public void setOptions(List<ConsoleText> options) {
        this.options.clear();
        this.options.addAll(options);
    }
}

