package com.game.view;

import com.game.model.Character;

import java.util.List;

/**
 * ConsoleText class that creates text objects with a text color, background color,
 * and the text string. There are three constructors that can be called (text only,
 * text and text color, text and background color, text and text color and background color.
 */

public class ConsoleText {

    // INSTANCE VARIABLES
    String text;
    AnsiBackgroundColor backgroundColor = AnsiBackgroundColor.NONE;
    AnsiTextColor textColor = AnsiTextColor.NONE;

    // CONSTRUCTOR
    // builds a new ConsoleText object with the given text, text color, and background color.
    public ConsoleText(String text, AnsiTextColor textColor, AnsiBackgroundColor backgroundColor) {
        setText(text);
        setBackgroundColor(backgroundColor);
        setTextColor(textColor);
    }

    // builds a new ConsoleText object with the given text and text color.
    public ConsoleText(String text, AnsiTextColor textColor) {
        setText(text);
        setTextColor(textColor);
        setBackgroundColor(AnsiBackgroundColor.NONE);
    }

    // builds a new ConsoleText object with the given text and background color.
    ConsoleText(String text, AnsiBackgroundColor backgroundColor) {
        setText(text);
        setTextColor(AnsiTextColor.NONE);
        setBackgroundColor(backgroundColor);
    }

    // builds a new ConsoleText object with the given text.
    public ConsoleText(String text) {
        setText(text);
        setTextColor(AnsiTextColor.NONE);
        setBackgroundColor(AnsiBackgroundColor.NONE);
    }

    public ConsoleText(String text, List<String> elements) {
        setText(text + ListToString(elements));
        setTextColor(AnsiTextColor.NONE);
        setBackgroundColor(AnsiBackgroundColor.NONE);
    }

    public ConsoleText(List<String> elements) {
        text = ListToString(elements);
        setTextColor(AnsiTextColor.NONE);
        setBackgroundColor(AnsiBackgroundColor.NONE);
    }

    private String ListToString(List<String> elements) {
        String result = "";
        for (int i = 0; i < elements.size() - 1; i++) {
            result += elements.get(i) + ", ";
        }
        if (elements.size() > 0) {
            result += elements.get(elements.size() - 1);
        }
        return result;
    }

    // GETTERS AND SETTERS
    void setText(String text) {
        this.text = text;
    }

    void setBackgroundColor(AnsiBackgroundColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    void setTextColor(AnsiTextColor textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public AnsiBackgroundColor getBackgroundColor() {
        return backgroundColor;
    }

    public AnsiTextColor getTextColor() {
        return textColor;
    }
}