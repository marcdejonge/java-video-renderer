package com.marcdejonge.video.objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@FunctionalInterface
public interface ObjectRenderer {
    void render(Graphics2D g, BufferedImage img, double x, double y);
}
