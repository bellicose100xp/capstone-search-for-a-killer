package com.game.gui.result;

import com.game.gui.utils.FontUtils;

import javax.swing.*;
import java.awt.*;

public class ExitButton extends JButton {
    public ExitButton(int x, int y, Color buttonText, Color buttonBackground) {
        super("Exit");
        this.setBounds(x, y, 174, 76);
        this.setFont(FontUtils.EDOSZ_FONT.deriveFont(46f));
        this.setFocusable(false);
        this.setBackground(buttonBackground);
        this.setForeground(buttonText);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
        this.addActionListener(e -> System.exit(0));
    }
}
