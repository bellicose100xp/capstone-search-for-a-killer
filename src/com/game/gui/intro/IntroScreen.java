package com.game.gui.intro;

import com.game.controller.io.IntroTextLoader;
import com.game.gui.utils.FontUtils;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.game.gui.utils.ColorUtils.*;

public class IntroScreen extends JFrame {
    public static Integer LAYER_0 = 0;
    public static Integer LAYER_1 = 1;

    public IntroScreen(CountDownLatch latch, Point prevFrameLocation) {
        // Create JLayered Pane
        JLayeredPane layeredPane = createIntroLayeredPane();
        this.add(layeredPane);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                latch.countDown();
            }
        });

        /*
         * Intro Panel Specific Settings
         */
        this.setTitle("Game Introduction");
        this.setSize(1000, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(prevFrameLocation);
        this.setVisible(true);
    }

    private JLayeredPane createIntroLayeredPane() {
        JLayeredPane layeredPane = new JLayeredPane();

        /*
         * Add Background Panel as Bottom Layer
         */
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setBounds(0, 0, 1000, 600);
        layeredPane.add(backgroundPanel, LAYER_0);

        /*
         * Add Intro Panel on top side of layer 1
         */
        IntroPanel introPanel = new IntroPanel();
        introPanel.setBounds(0, 0, 1000, 500);
        layeredPane.add(introPanel, LAYER_1);

        /*
         * Add Button in the bottom side of layer 1
         */
        JButton button = new JButton("Start Game");
        button.setBackground(START_BUTTON_BACKGROUND.color());
        button.setForeground(START_BUTTON_TEXT.color());
        // Custom Start Game Font
        Font registerStartGameFont = FontUtils.registerCustomTitleFont();
        Font startGameButtonFont = registerStartGameFont.deriveFont(Font.PLAIN, 36);
        button.setFont(startGameButtonFont);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setBorder(null);
        // Close Intro Frame when the Button is clicked
        button.addActionListener(e -> {
            this.dispose();
        });
        button.setBounds(730, 480, 220, 50);
        layeredPane.add(button, LAYER_1);

        layeredPane.setBounds(0, 0, 1000, 600);
        return layeredPane;
    }
}

// Panel with Background Image
class BackgroundPanel extends JPanel {
    private final Image backgroundImage;

    public BackgroundPanel() {
        backgroundImage = new ImageIcon("data/game_intro/game-intro-background.jpg").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

class IntroPanel extends JPanel {
    public IntroPanel() {
        // Custom Header Font
        Font registeredTitleFont = FontUtils.registerCustomTitleFont();
        Font titleFont = registeredTitleFont.deriveFont(Font.PLAIN, 40);

        /*
         * Story Section
         */

        // Get all section names
        List<String> introSections = new ArrayList<>();
        for (String key : IntroTextLoader.introText.keySet()) {
            if (!key.endsWith("Header")) {
                introSections.add(key);
            }
        }

        // Add all sections to the panel
        for (String section : introSections) {
            String introHeader = IntroTextLoader.introText.get(section + "Header");
            JLabel label = createHeaderLabel(introHeader, titleFont);
            this.add(label);

            String introBody = IntroTextLoader.introText.get(section);
            JTextPane textPane = createBodyTextPane(introBody);
            this.add(textPane);
        }


        /*
         * IntroPanel Specific Settings
         */
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    }

    private static JLabel createHeaderLabel(String storyHeader, Font titleFont) {
        JLabel label = new JLabel(storyHeader);
        label.setFont(titleFont);
        label.setForeground(INTRO_TEXT.color());
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(1000, 50));
        return label;
    }

    private static JTextPane createBodyTextPane(String story) {
        JTextPane textPane = new JTextPane();
        textPane.setText(story);
        textPane.setEditable(false);
        textPane.setFont(FontUtils.DEFAULT_FONT.deriveFont(20f));
        textPane.setForeground(INTRO_TEXT.color());

        // Center text
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet centerAttr = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttr, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), centerAttr, false);

        // Add left and right margins
        int margin = 50;
        textPane.setMargin(new Insets(0, margin, 0, margin));
        textPane.setOpaque(false);
        textPane.setPreferredSize(new Dimension(1000, 80));
        return textPane;
    }
}
