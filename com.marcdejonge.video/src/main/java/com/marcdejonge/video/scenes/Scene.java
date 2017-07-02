package com.marcdejonge.video.scenes;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public abstract class Scene {
    public boolean renderFrame(double timeStep, BufferedImage img) {
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        try {
            return renderFrame(timeStep, img, g);
        } finally {
            g.dispose();
        }
    }

    public boolean renderFrame(double timeStep, BufferedImage img, Graphics2D g) {
        return false;
    }
}
