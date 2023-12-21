package com.game.gui.result;

import javax.swing.*;
import java.awt.*;

import static com.game.gui.utils.ColorUtils.*;
import static com.game.gui.utils.ColorUtils.GREEN_EXIT_BUTTON_BACKGROUND;

public class WrongCharacterScreen extends ResultScreen {
    public WrongCharacterScreen(Point parentLocation, int score) {
        super(parentLocation, "data/result/fail-wrong-person.png", score);
    }

    @Override
    public JButton createExitButton() {
        Color textColor = RED_EXIT_BUTTON_TEXT.color();
        Color backgroundColor = RED_EXIT_BUTTON_BACKGROUND.color();
        return new ExitButton(757, 473, textColor, backgroundColor);
    }

    @Override
    public JLabel createScoreLabel(String text) {
        Color textColor = RED_SCORE_LABEL_TEXT.color();
        Color backgroundColor = RED_SCORE_LABEL_BACKGROUND.color();
        return new ScoreLabel(text, 680, 45,  textColor, backgroundColor);
    }

    public static void main(String[] args) {
        new WrongCharacterScreen(new Point(300, 300), 250);
    }
}
