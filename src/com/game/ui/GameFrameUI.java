package com.game.ui;

import javax.swing.*;

public class GameFrameUI extends JFrame {
    public GameFrameUI() {
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);

        this.setTitle("Search for a murderer");
        this.setSize(1200, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
}
