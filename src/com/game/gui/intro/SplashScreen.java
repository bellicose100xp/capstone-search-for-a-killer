package com.game.gui.intro;

import com.game.gui.utils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;

public class SplashScreen extends JFrame {
    public static Integer LAYER_0 = 0;
    public static Integer LAYER_1 = 1;
    private final CountDownLatch latch;

    public SplashScreen(CountDownLatch latch) {
        this.latch = latch;
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 998, 600);

        ImageIcon image = new ImageIcon("data/splash_screen/splash-screen.jpg");
        JLabel label = new JLabel(image);
        label.setBounds(0, 0, 998, 600);
        layeredPane.add(label, LAYER_0);

        NextButton nextButton = new NextButton();
        layeredPane.add(nextButton, LAYER_1);

        this.add(layeredPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(998, 625);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new SplashScreen(new CountDownLatch(1));
    }

    private class NextButton extends JButton {
        public NextButton() {
            this.setBounds(836, 193, 164, 56);
            this.setText("Next");
            this.setBackground(Color.BLACK);
            this.setForeground(Color.WHITE);
            this.setFont(FontUtils.EDOSZ_FONT.deriveFont(32f));
            this.setFocusable(false);
            this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

            this.addActionListener(e -> {
                latch.countDown();
                //System.out.println(latch.getCount());
            });
        }
    }
}
