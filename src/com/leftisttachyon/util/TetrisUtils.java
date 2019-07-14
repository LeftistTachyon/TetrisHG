package com.leftisttachyon.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A utility class for this Tetris program
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public final class TetrisUtils {

    /**
     * No creation for you!
     */
    private TetrisUtils() {
    }

    /**
     * Finds a resource in the given path and reads an image from the file at
     * the given location.
     *
     * @param path the path of the image to be read
     * @return the image
     * @throws IOException the standard IOExceptions
     */
    public static BufferedImage getResource(String path) throws IOException {
        return ImageIO.read(TetrisUtils.class.getResource(path));
    }
}
