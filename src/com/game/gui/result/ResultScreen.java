package com.game.gui.result;

import com.game.gui.utils.FontUtils;

import javax.swing.*;
import java.awt.*;

public abstract class ResultScreen extends JFrame {
    private static final Integer LAYER_0 = 0;
    private static final Integer LAYER_1 = 1;

    public ResultScreen(Point parentLocation, String backgroundIcon, int score) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1000, 600);

        // Background Image
        JLabel backgroundImage = new JLabel(new ImageIcon(backgroundIcon));
        backgroundImage.setBounds(0, 0, 1000, 600);
        layeredPane.add(backgroundImage, LAYER_0);

        // Add exit button
        JButton exitButton = createExitButton();
        layeredPane.add(exitButton, LAYER_1);

        // Add score label
        String scoreText = "Final Score: " + score;
        JLabel scoreLabel = createScoreLabel(scoreText);
        layeredPane.add(scoreLabel, LAYER_1);

        // Add layered pane to the frame
        this.add(layeredPane);

        // Frame specific settings
        this.setUndecorated(true);
        this.setSize(1000, 600);
        this.setLocation(parentLocation);
        this.setVisible(true);
    }

    public abstract JButton createExitButton();

    public abstract JLabel createScoreLabel(String text);
}
