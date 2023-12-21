package com.game.view;

import com.game.view.framework.InputCollector;
import com.game.view.framework.InvalidInputException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// import static com.game.view.framework.InputCollector.collectInput;

/**
 * ConsoleView class that contains a list with items that represent each item in the expected
 * output. Once the list is populated with all the text data its passed to the Console class
 * from within the show() method.
 */

public class ConsoleView {
    // INSTANCE VARIABLES
    // This holds the text that will be presented in the screen, a List of List of Strings so we can individualy manage each list
    public List<List<ConsoleText>> textList = new ArrayList<>();
    public String errorMessage;

    // CONSTRUCTORS
    public ConsoleView() {
    }

    // This constructor is better since it keeps the reference
    public ConsoleView(List<List<ConsoleText>> textList) {
        this();
        this.textList = textList;
    }

    // For each ConsoleText in text call Console.print and pass the color and text
    public String show() {
        while (true) {
            displayText();
            clearMainText(); // Stops mainText from repeating when invalid input is provided.
            executeViewLogic();
            try {
                return collectInput();
            } catch (InvalidInputException exception) {
                errorMessage = exception.getMessage();
            }
        }
    }

    private void clearMainText() {
        List<ConsoleText> mainText = textList.get(0);
        if (mainText != null) {
            mainText.clear();
        }
    }

    // show map
    public void showMap() {
        displayText();
    }

    void executeViewLogic() {
        // EMPTY ON PURPOSE :D
    }

    void displayText() {
        // Print all the text on this View
        for (var list : textList) {
            if (list != null) {
                for (ConsoleText t : list) {
                    Console.printNewLine(t);
                }
            }
        }
        // Print any error message
        if (errorMessage != null)
            Console.printNewLine(new ConsoleText(errorMessage, AnsiTextColor.RED));
    }

    String collectInput() {
        return InputCollector.collectInput();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void clearErrorMessage() {
        this.errorMessage = null;
    }

    public void setText(List<List<ConsoleText>> text) {
        this.textList = text;
    }

    public List<List<ConsoleText>> getText() {
        return textList;
    }

    public List<List<ConsoleText>> getTextList() {
        return textList;
    }
}