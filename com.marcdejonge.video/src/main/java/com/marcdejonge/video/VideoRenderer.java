package com.marcdejonge.video;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marcdejonge.video.scenes.Scene;

import io.humble.video.Codec;
import io.humble.video.ContainerFormat.Flag;
import io.humble.video.Encoder;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.Muxer;
import io.humble.video.MuxerFormat;
import io.humble.video.PixelFormat.Type;
import io.humble.video.Rational;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;

public class VideoRenderer {
    private static final Logger logger = LoggerFactory.getLogger(VideoRenderer.class);

    private final int width, height;
    private final Rational frametime;

    public VideoRenderer(int width, int height, int fps) {
        this.width = width;
        this.height = height;
        this.frametime = Rational.make(1, fps);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getFrameTime() {
        return frametime.getDouble();
    }

    public void render(File out, List<Scene> scenes)
        throws IOException, InterruptedException {
        Dimension videoSize = new Dimension(1920, 1080);

        Rational framerate = Rational.make(1, 60);
        logger.debug("Creating muxer");
        Muxer muxer = Muxer.make("out.mkv", null, null);
        MuxerFormat format = muxer.getFormat();
        logger.debug("Format: {}", format.getName());
        Codec codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());
        logger.debug("Found codec: {}", codec.getName());

        Encoder encoder = Encoder.make(codec);

        logger.debug("Setting encoder to {}x{} pixels", videoSize.width, videoSize.height);
        encoder.setWidth(videoSize.width);
        encoder.setHeight(videoSize.height);
        encoder.setPixelFormat(Type.PIX_FMT_YUV420P);
        encoder.setTimeBase(framerate);
        encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, format.getFlag(Flag.GLOBAL_HEADER));

        logger.debug("Opening encoder and muxer");
        encoder.open(null, null);
        muxer.addNewStream(encoder);
        muxer.open(null, null);

        MediaPicture picture = MediaPicture.make(encoder.getWidth(), encoder.getHeight(), encoder.getPixelFormat());
        picture.setTimeBase(framerate);

        MediaPacket packet = MediaPacket.make(1024 * 1024);

        BufferedImage frame = new BufferedImage(videoSize.width, videoSize.height, BufferedImage.TYPE_3BYTE_BGR);
        MediaPictureConverter converter = MediaPictureConverterFactory.createConverter(frame, picture);

        double timeStep = getFrameTime();
        int frameCount = 0;

        for (Scene scene : scenes) {
            while (scene.renderFrame(timeStep, frame)) {
                converter.toPicture(picture, frame, frameCount);

                encoder.encode(packet, picture);
                if (packet.isComplete()) {
                    muxer.write(packet, false);
                }

                logger.debug("Written frame {}", frameCount);
                frameCount++;
            }
        }

        // Flush any remaining stuff
        encoder.encode(packet, null);
        muxer.write(packet, false);

        logger.debug("Done, closing file");
        muxer.close();
    }
}
