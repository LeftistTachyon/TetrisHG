package com.github.leftisttachyon.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A utility class for this Tetris program
 *
 * @author Jed Wang
 * @since 0.9.0
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
        return path == null 
                ? null
                : ImageIO.read(TetrisUtils.class.getResource(path));
    }

    /**
     * Finds the leftmost column with a nonzero value, if any. If none exist,
     * then -1 is returned. NOTE: may not work with jagged matrixes
     *
     * @param mat the matrix of integers to look at
     * @return leftmost column with a nonzero value, if any
     */
    public static int leftmostCol(int[][] mat) {
        int output = 0;
        for (; output < mat[0].length; output++) {
            for (int i = 0; i < mat.length; i++) {
                if (mat[i][output] != 0) {
                    return output;
                }
            }
        }

        return -1;
    }

    /**
     * Finds the rightmost column with a nonzero value, if any. If none exist,
     * then -1 is returned. NOTE: may not work with jagged matrixes
     *
     * @param mat the matrix of integers to look at
     * @return rightmost column with a nonzero value, if any
     */
    public static int rightmostCol(int[][] mat) {
        int output = mat[0].length - 1;
        for (; output >= 0; output--) {
            for (int i = 0; i < mat.length; i++) {
                if (mat[i][output] != 0) {
                    return output;
                }
            }
        }
        return -1;
    }

    /**
     * Finds the highest row with a nonzero value, if any. If non exist, then -1
     * is returned.
     *
     * @param mat the matrix of integers to look at
     * @return the highest column with a nonzero value, if any
     */
    public static int highestRow(int[][] mat) {
        int output = 0;
        for (; output < mat.length; output++) {
            for (int i = 0; i < mat[output].length; i++) {
                if (mat[output][i] != 0) {
                    return output;
                }
            }
        }

        return -1;
    }

    /**
     * Finds the lowest row with a nonzero value, if any. If non exist, then -1
     * is returned.
     *
     * @param mat the matrix of integers to look at
     * @return the lowest column with a nonzero value, if any
     */
    public static int lowestRow(int[][] mat) {
        int output = mat.length - 1;
        for (; output >= 0; output--) {
            for (int i = 0; i < mat[output].length; i++) {
                if (mat[output][i] != 0) {
                    return output;
                }
            }
        }

        return -1;
    }
}
