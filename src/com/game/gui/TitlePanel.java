package com.game.gui;

import static com.game.gui.utils.ColorUtils.*;

import com.game.gui.toolbar.*;
import com.game.gui.utils.FontUtils;
import com.game.gui.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

public class TitlePanel extends JPanel {

    public TitlePanel(JFrame gameFrame, PlayerPanel playerPanel) {

        /* Game Title */
        JLabel title = new JLabel("Search For A Killer");
        Font customTitleFont = FontUtils.registerCustomTitleFont();
        Font titleFont = customTitleFont.deriveFont(Font.PLAIN, 48);
        title.setFont(titleFont);
        title.setForeground(GAME_TITLE_TEXT.color());
        title.setBounds(80, 10, 431, 49);
        this.add(title);

        /* Report Killer Button */
        JButton reportKiller = new ReportKillerButton(gameFrame, playerPanel);
        this.add(reportKiller);

        /* Help Icon */
        JLabel helpLabel = new HelpIconLabel("data/icons/toolbar/help.png", 720, 9, 50, 50);
        this.add(helpLabel);

        /* Volume Icon */
        JLabel sfxLabel = new SfxIconLabel("data/icons/toolbar/sfx.png", 788, 9, 50, 50);
        this.add(sfxLabel);

        /* Volume Icon */
        JLabel volumeLabel = new VolumeIconLabel("data/icons/toolbar/volume.png", 856, 9, 50, 50);
        this.add(volumeLabel);

        /* Exit Icon */
        JLabel exitLabel = new ExitIconLabel("data/icons/toolbar/exit.png", 924, 9, 50, 50);
        this.add(exitLabel);

        /* Title Panel Specific Settings */
        this.setLayout(null);
        this.setBounds(0, 0, 1000, 65);
        this.setBackground(GAME_BACKGROUND.color());
    }

    public static JLabel createTitleBarIconLabel(String path, int x, int y, int width, int height) {
        ImageIcon icon = ImageUtils.getResizedIcon(path, width, height);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(x, y, width, height);
        return iconLabel;
    }
}
