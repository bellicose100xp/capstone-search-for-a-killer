package com.game.gui;

import com.game.controller.AudioController;
import com.game.controller.LoadController;
import com.game.gui.result.ReportedSelections;
import com.game.gui.result.SuccessScreen;
import com.game.gui.result.WrongCharacterScreen;
import com.game.gui.result.WrongWeaponScreen;
import com.game.gui.utils.FontUtils;
import com.game.gui.utils.ImageUtils;
import com.game.model.Character;
import com.game.model.Item;
import com.game.model.Player;

import static com.game.gui.utils.ColorUtils.*;
import static com.game.gui.result.ReportedSelections.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class ReportKiller extends JFrame {
    public static Integer LAYER_0 = 0;
    public static Integer LAYER_1 = 1;
    public static Integer LAYER_2 = 2;
    private final Map<String, Character> characters;
    private final Player player;
    private final JPanel weaponPanel;
    private final JPanel characterPanel;
    private Item selectedWeapon;
    private Character selectedCharacter;
    private Item murderWeapon;
    private Character murderer;
    private final JFrame gameFrame;
    private final PlayerPanel playerPanel;


    public ReportKiller(JFrame gameFrame, PlayerPanel playerPanel) {
        this.playerPanel = playerPanel;
        this.gameFrame = gameFrame;
        // Get reference to all characters and players
        characters = LoadController.getCharacters();
        player = LoadController.getPlayer();
        murderer = LoadController.getMurderer();
        murderWeapon = LoadController.getMurderWeapon();

        /*
         * Create Layered Pane
         */
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1000, 600);

        /*
         * Create Background Label image and add it to layer 0
         */
        JLabel backgroundImage = createBackgroundImage();
        layeredPane.add(backgroundImage, LAYER_0);

        /*
         * Create center red background label on layer 1
         */
        JLabel redBackground = createRedBackground();
        layeredPane.add(redBackground, LAYER_1);

        /*
         * Create Question Labels
         */
        JLabel murdererLabel = createQuestionLabel("Who was the murderer?", 256, 113, 487, 64);
        layeredPane.add(murdererLabel, LAYER_2);

        JLabel weaponLabel = createQuestionLabel("What was the murder weapon?", 256, 255, 487, 64);
        layeredPane.add(weaponLabel, LAYER_2);

        /*
         * Evidence and Weapons Icon Panel
         */
        characterPanel = createIconPanel(256, 177, 487, 70);
        layeredPane.add(characterPanel, LAYER_2);

        weaponPanel = createIconPanel(256, 319, 487, 70);
        layeredPane.add(weaponPanel, LAYER_2);


        /*
         * render evidence panel
         */
        renderWeaponPanel(null);

        /*
         * render character panel
         */
        renderCharacterPanel(null);

        /*
         * Add Cancel Button
         */
        JButton cancel = createCancelButton();
        layeredPane.add(cancel, LAYER_2);

        /*
         * Add Solve Button
         */
        JButton solve = createSolveButton();
        layeredPane.add(solve, LAYER_2);

        // Add layered pane to the frame
        this.add(layeredPane);

        /*
         * Frame specific settings
         */
        this.setTitle("Report Killer");
        this.setSize(1000, 600);
        this.setLayout(null);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(gameFrame);
        this.setVisible(true);
    }

    private void renderCharacterPanel(Character selectedCharacter) {
        clearPanel(characterPanel);

        for (Character character : characters.values()) {
            if (!character.isSuspect()) {
                continue;
            }

            ImageIcon icon = ImageUtils.getResizedIcon(character.getIconPath(), 50, 50);
            JLabel label = new JLabel(icon);
            label.setToolTipText(character.getName());
            if (character == selectedCharacter) {
                label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
            }
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    AudioController.playSFX(8);
                    setSelectedCharacter(character);
                    renderCharacterPanel(character);
                }
            });

            characterPanel.add(label);
        }
    }

    private void renderWeaponPanel(Item selectedItem) {
        clearPanel(weaponPanel);

        for (Item item : player.getInventory().getItems()) {
            if (!item.isWeapon()) {
                continue;
            }

            ImageIcon icon = ImageUtils.getResizedIcon(item.getIconPath(), 50, 50);
            JLabel label = new JLabel(icon);
            label.setToolTipText(item.getName());
            if (item == selectedItem) {
                label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
            }
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    AudioController.playSFX(8);
                    setSelectedWeapon(item);
                    renderWeaponPanel(item);
                }
            });
            weaponPanel.add(label);
        }
    }

    private void clearPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }


    private JLabel createBackgroundImage() {
        JLabel backgroundImage = new JLabel(new ImageIcon("data/background/report-killer-background.png"));
        backgroundImage.setBounds(0, 0, 1000, 600);
        return backgroundImage;
    }

    private JLabel createRedBackground() {
        JLabel redBackground = new JLabel();
        redBackground.setBounds(188, 87, 624, 432);
        redBackground.setBackground(REPORT_KILLER_BACKGROUND.color());
        redBackground.setOpaque(true);
        return redBackground;
    }

    private JButton createSolveButton() {
        JButton solve = new JButton("Solve");
        solve.setFont(FontUtils.EDOSZ_FONT.deriveFont(26f));
        solve.setFocusable(false);
        solve.setBorder(BorderFactory.createEmptyBorder());
        solve.setHorizontalTextPosition(JButton.CENTER);
        solve.setVerticalTextPosition(JButton.CENTER);
        solve.setBackground(SOLVE_BUTTON_BACKGROUND.color());
        solve.setForeground(SOLVE_BUTTON_TEXT.color());
        solve.setBounds(552, 420, 150, 61);

        solve.addActionListener(e -> {
            // This Frame's Location — to have the next frame open at this location instead of default screen
            Point parentLocation = this.getLocation();
            ReportedSelections reportedSelections = reportedSelectionsHandler(selectedCharacter, selectedWeapon);

            int score = 0; // Player Score
            switch (reportedSelections) {
                case MISSING_SELECTION:
                    String errorMessage = "You must select both the murderer and the weapon used!";
                    JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                case WRONG_CHARACTER:
                    AudioController.playSFX(6);
                    // Score is always 0 if you select wrong character
                    new WrongCharacterScreen(parentLocation, score);
                    break;
                case WRONG_WEAPON:
                    AudioController.playSFX(6);
                    // Player gets half the score, if they at least selected correct character
                    score = playerPanel.getScore() / 2;
                    new WrongWeaponScreen(parentLocation, score);
                    break;
                case CORRECT_SELECTION:
                    AudioController.playSFX(5);
                    score = playerPanel.getScore();
                    new SuccessScreen(parentLocation, score);
            }

            // Dispose of gameFrame and Report Killer Frame
            gameFrame.dispose();
            this.dispose();
        });

        return solve;
    }

    public ReportedSelections reportedSelectionsHandler(Character selectedCharacter, Item selectedWeapon) {
        if (selectedWeapon == null || selectedCharacter == null) {
            return MISSING_SELECTION;
        }

        if (selectedCharacter != murderer) {
            return WRONG_CHARACTER;
        }

        if (selectedWeapon != murderWeapon) {
            return WRONG_WEAPON;
        }

        return CORRECT_SELECTION;
    }

    private JButton createCancelButton() {
        JButton cancel = new JButton("Cancel");
        cancel.setFont(FontUtils.EDOSZ_FONT.deriveFont(26f));
        cancel.setFocusable(false);
        cancel.setBorder(BorderFactory.createEmptyBorder());
        cancel.setHorizontalTextPosition(JButton.CENTER);
        cancel.setVerticalTextPosition(JButton.CENTER);
        cancel.setBackground(CANCEL_BUTTON_BACKGROUND.color());
        cancel.setForeground(CANCEL_BUTTON_TEXT.color());
        cancel.setBounds(297, 420, 150, 61);

        cancel.addActionListener(e -> {
            this.dispose();
            gameFrame.setVisible(true);
        });
        return cancel;
    }

    private JPanel createIconPanel(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(REPORT_KILLER_ICON_BACKGROUND.color());
        return panel;
    }

    private JLabel createQuestionLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setBounds(x, y, width, height);
        label.setFont(FontUtils.EDOSZ_FONT.deriveFont(32f));
        label.setForeground(REPORT_KILLER_TEXT.color());
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }

    public Item getSelectedWeapon() {
        return selectedWeapon;
    }

    public void setSelectedWeapon(Item selectedWeapon) {
        this.selectedWeapon = selectedWeapon;
    }

    public Character getSelectedCharacter() {
        return selectedCharacter;
    }

    public void setSelectedCharacter(Character selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }

    public Item getMurderWeapon() {
        return murderWeapon;
    }

    public void setMurderWeapon(Item murderWeapon) {
        this.murderWeapon = murderWeapon;
    }

    public Character getMurderer() {
        return murderer;
    }

    public void setMurderer(Character murderer) {
        this.murderer = murderer;
    }
}
