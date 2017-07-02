package com.marcdejonge.video.scenes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.marcdejonge.video.objects.ObjectRenderer;

public class BounceScene
    extends Scene {
    private static final int ACCURACY = 25;

    private final ObjectRenderer objectRenderer;

    private double x;
    private double y;
    private double vx;
    private double vy;
    private double bouncyEfficiency;
    private final double gravity;

    public BounceScene(ObjectRenderer objectRenderer) {
        this(objectRenderer, -9.81);
    }

    public BounceScene(ObjectRenderer objectRenderer, double gravity) {
        this.objectRenderer = objectRenderer;
        this.x = 0.1;
        this.y = 0.9;
        this.vx = 0.1;
        this.vy = 0;
        bouncyEfficiency = 0.85;
        this.gravity = gravity;
    }

    public BounceScene setX(double x) {
        this.x = x;
        return this;
    }

    public BounceScene setY(double y) {
        this.y = y;
        return this;
    }

    public BounceScene setVx(double vx) {
        this.vx = vx;
        return this;
    }

    public BounceScene setVy(double vy) {
        this.vy = vy;
        return this;
    }

    public BounceScene setBouncyEfficiency(double bouncyEfficiency) {
        if (bouncyEfficiency > 0)
            bouncyEfficiency = -bouncyEfficiency;
        this.bouncyEfficiency = bouncyEfficiency;
        return this;
    }

    public boolean renderFrame(double timeStep, BufferedImage img, Graphics2D g) {

        double calcTimestep = timeStep / ACCURACY;
        for (int i = 0; i < ACCURACY; i++) {
            vy += calcTimestep * gravity;
            x += calcTimestep * vx;
            y += calcTimestep * vy;
            if (y <= 0.1) {
                y = 0.1;
                vy = Math.max(0, vy * bouncyEfficiency);
            }

            if (x > 0.9)
                return false;
        }

        g.setColor(new Color(255, 255, 255, 80));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        objectRenderer.render(g, img, x, y);

        return true;
    }
}
