package com.game.gui.toolbar;

import com.game.gui.PlayerPanel;
import com.game.gui.ReportKiller;
import com.game.gui.utils.FontUtils;

import javax.swing.*;

import static com.game.gui.utils.ColorUtils.GAME_REPORT_BACKGROUND;
import static com.game.gui.utils.ColorUtils.GAME_REPORT_TEXT;


public class ReportKillerButton extends JButton {
    public ReportKillerButton(JFrame gameFrame, PlayerPanel playerPanel) {

        this.setText("Report Killer");
        this.setFont(FontUtils.DEFAULT_FONT.deriveFont(24f));
        this.setBackground(GAME_REPORT_BACKGROUND.color());
        this.setForeground(GAME_REPORT_TEXT.color());
        this.setBounds(550, 12, 150, 44);
        this.setFocusable(false);
        this.setBorder(null);
        this.addActionListener(e -> {
            new ReportKiller(gameFrame, playerPanel);
            gameFrame.setVisible(false);
        });
    }
}
