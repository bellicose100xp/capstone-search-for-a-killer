package com.game.view;

import com.game.ui.GamePanel;

/**
 * Console class that actually outputs to the terminal by calling System.out.println
 * and passing the data that was sent to it from the ConsoleView object
 */

public class Console {

    // print method without a color selector
    public static void print(String message) {
        System.out.print(message);
        GamePanel.appendTextToOutputConsole(message);
    }

    public static void printNewLine(String message) {
        System.out.println(message);
        GamePanel.appendTextToOutputConsole(message + "\n");
    }

    // print method overloaded with a color selector
    public static void print(String text, AnsiTextColor textColor, AnsiBackgroundColor backgroundColor) {
        System.out.println(backgroundColor.getColor() + textColor.getColor() + text + AnsiTextColor.RESET.getColor());
        GamePanel.appendTextToOutputConsole(text);
    }

    public static void printNewLine(ConsoleText text) {
        System.out.println(
                text.getBackgroundColor().getColor() + text.getTextColor().getColor() + text.getText() + AnsiTextColor.RESET.getColor());
        GamePanel.appendTextToOutputConsole(text.getText() + "\n");
    }

    public static void print(ConsoleText text) {
        System.out.print(
                text.getBackgroundColor().getColor() + text.getTextColor().getColor() + text.getText() + AnsiTextColor.RESET.getColor());
        GamePanel.appendTextToOutputConsole(text.getText());
    }
}