package com.marcdejonge.video;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.marcdejonge.video.objects.ObjectRenderer;
import com.marcdejonge.video.scenes.BounceScene;
import com.marcdejonge.video.scenes.FadeOutScene;
import com.marcdejonge.video.scenes.Scene;

public class Main {
    public static void main(String[] args)
        throws Exception {
        VideoRenderer out = new VideoRenderer(1920, 1080, 60);

        ObjectRenderer renderBall = (g, img, x, y) -> {
            g.setColor(new Color((int) (255 * (1 - x)),
                                 0,
                                 (int) (255 * x)));
            g.fillOval((int) (x * img.getWidth() - 40),
                       (int) ((1 - y) * img.getHeight() - 40),
                       80,
                       80);
        };

        List<Scene> scenes = new ArrayList<>();
        scenes.add(new FadeOutScene(Color.white, 0));
        scenes.add(new BounceScene(renderBall, -7).setVx(.5).setBouncyEfficiency(0.9));
        scenes.add(new FadeOutScene(Color.white, 0.5));
        scenes.add(new BounceScene(renderBall, -7).setVx(.5).setBouncyEfficiency(0.8));
        scenes.add(new FadeOutScene(Color.white, 0.5));
        scenes.add(new BounceScene(renderBall, -7).setVx(.5).setBouncyEfficiency(0.7));
        scenes.add(new FadeOutScene(Color.white, 1));

        out.render(new File("out.mkv"), scenes);
    }
}
