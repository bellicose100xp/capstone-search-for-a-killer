package com.game.gui.result;

import com.game.gui.utils.Score;

import javax.swing.*;
import java.awt.*;

import static com.game.gui.utils.ColorUtils.*;

public class SuccessScreen extends ResultScreen {
    public SuccessScreen(Point parentLocation, int score) {
        super(parentLocation, "data/result/success.png", score);
    }

    @Override
    public JButton createExitButton() {
        Color textColor = GREEN_EXIT_BUTTON_TEXT.color();
        Color backgroundColor = GREEN_EXIT_BUTTON_BACKGROUND.color();
        return new ExitButton(770, 32, textColor, backgroundColor);
    }

    @Override
    public JLabel createScoreLabel(String text) {
        Color textColor = GREEN_SCORE_LABEL_TEXT.color();
        Color backgroundColor = GREEN_SCORE_LABEL_BACKGROUND.color();
        return new ScoreLabel(text, 55, 48,  textColor, backgroundColor);
    }

    public static void main(String[] args) {
        new SuccessScreen(new Point(300, 300), 450);
    }
}
