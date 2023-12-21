package com.game.gui.result;

import javax.swing.*;
import java.awt.*;

public class ScoreLabel extends JLabel{
    public ScoreLabel(String text, int x, int y, Color textColor, Color backgroundColor) {
        super(text);
        this.setBounds(x, y, 288, 64);
        this.setFont(new Font(null, Font.BOLD, 32));
        this.setFocusable(false);
        this.setOpaque(true);
        this.setBackground(backgroundColor);
        this.setForeground(textColor);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }
}
