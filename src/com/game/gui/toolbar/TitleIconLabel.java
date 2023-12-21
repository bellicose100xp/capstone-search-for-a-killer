package com.game.gui.toolbar;

import com.game.gui.utils.ImageUtils;

import javax.swing.*;
import java.awt.event.MouseListener;

public abstract class TitleIconLabel extends JLabel implements MouseListener {

    public TitleIconLabel(String iconPath, int x, int y, int width, int height) {
        ImageIcon icon = ImageUtils.getResizedIcon(iconPath, width, height);
        this.setIcon(icon);
        this.setBounds(x, y, width, height);
        this.addMouseListener(this);
    }
}
