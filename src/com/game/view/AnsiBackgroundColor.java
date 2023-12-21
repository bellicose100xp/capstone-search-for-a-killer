package com.game.view;

public enum AnsiBackgroundColor {

    RED("\u001B[41m"),
    GREEN("\u001B[42m"),
    YELLOW("\u001B[43m"),
    BLUE("\u001B[44m"),
    PURPLE("\u001B[45m"),
    CYAN("\u001B[46m"),
    WHITE("\u001B[47m"),
    RESET("\u001B[0m"),
    NONE("");

    private final String color;

    AnsiBackgroundColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}

