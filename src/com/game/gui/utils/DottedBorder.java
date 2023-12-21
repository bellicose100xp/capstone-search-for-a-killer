package com.game.gui.utils;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class DottedBorder extends AbstractBorder {
    private final Color color;
    private final int thickness;
    private final int dotSpacing;

    public DottedBorder(Color color, int thickness, int dotSpacing) {
        this.color = color;
        this.thickness = thickness;
        this.dotSpacing = dotSpacing;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);

        BasicStroke stroke =
                new BasicStroke(
                        thickness,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND,
                        0,
                        new float[]{dotSpacing},
                        0);
        g2d.setStroke(stroke);

        g2d.drawRect(x, y, width - 1, height - 1);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = insets.left = insets.bottom = insets.right = thickness;
        return insets;
    }
}
