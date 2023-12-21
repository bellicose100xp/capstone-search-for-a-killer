package com.game.gui.toolbar;

import java.awt.event.MouseEvent;

public class ExitIconLabel extends TitleIconLabel {
    public ExitIconLabel(String iconPath, int x, int y, int width, int height) {
        super(iconPath, x, y, width, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.exit(0);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
