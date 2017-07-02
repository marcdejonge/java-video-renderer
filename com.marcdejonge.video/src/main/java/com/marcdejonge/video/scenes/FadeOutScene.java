package com.marcdejonge.video.scenes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class FadeOutScene
    extends Scene {
    private final Color toColor;
    private final double totalDuration;
    private double now;
    private BufferedImage startFrame;

    public FadeOutScene(Color toColor, double totalDuration) {
        this.toColor = toColor;
        this.totalDuration = totalDuration;
        this.now = 0;
    }

    @Override
    public boolean renderFrame(double timeStep, BufferedImage img, Graphics2D g) {
        if (now > totalDuration)
            return false;

        if (now == 0) {
            ColorModel cm = img.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = img.copyData(null);
            startFrame = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        }

        now += timeStep;
        int alpha = Math.min(255, (int) (255 * now / totalDuration));
        g.drawImage(startFrame,
                    0,
                    0,
                    img.getWidth(),
                    img.getHeight(),
                    0,
                    0,
                    startFrame.getWidth(),
                    startFrame.getHeight(),
                    null);
        g.setColor(new Color(toColor.getRed(), toColor.getGreen(), toColor.getBlue(), alpha));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        return true;
    }
}
