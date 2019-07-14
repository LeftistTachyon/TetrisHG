package com.leftisttachyon.tetris;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * An interface that controls how each mino is drawn.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public interface MinoStyle {

    /**
     * The size of each block/mino.
     */
    public static final int MINO_SIZE = 24;

    /**
     * Draws a mino of the given color with the given Graphics2D object at the x
     * and y positions given.
     *
     * @param g2D the Graphics2D object to draw the mino with
     * @param x the x coordinate of the top left corner of the mino to be drawn
     * @param y the y coordinate of the top left corner of the mino to be drawn
     * @param color the color of the mino to be drawn
     */
    void drawMino(Graphics2D g2D, int x, int y, int color);

    /**
     * Draws a mino of the given color with the given Graphics object at the x
     * and y positions given.
     *
     * @param g the Graphics object to draw the mino with
     * @param x the x coordinate of the top left corner of the mino to be drawn
     * @param y the y coordinate of the top left corner of the mino to be drawn
     * @param color the color of the mino to be drawn
     */
    default void drawMino(Graphics g, int x, int y, int color) {
        drawMino((Graphics2D) g, x, y, color);
    }

    /**
     * The translucent composite for drawing translucent stuff
     */
    public static final AlphaComposite TRANSLUCENT_COMPOSITE
            = AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER,
                    0.5f);

    // cyan, blue, orange, yellow, green, purple, red
    /**
     * Number representing a cyan mino
     */
    public static final int CYAN = 1;

    /**
     * Number representing a blue mino
     */
    public static final int BLUE = 2;

    /**
     * Number representing an orange mino
     */
    public static final int ORANGE = 3;

    /**
     * Number representing a yellow mino
     */
    public static final int YELLOW = 4;

    /**
     * Number representing a green mino
     */
    public static final int GREEN = 5;

    /**
     * Number representing a purple mino
     */
    public static final int PURPLE = 6;

    /**
     * Number representing a red mino
     */
    public static final int RED = 7;

    /**
     * Number representing a wall/out of bounds
     */
    public static final int WALL = 8;

    /**
     * Number representing a mino that is flashing (when a tetromino locks)
     */
    public static final int FLASH = 9;

    /**
     * Number representing no mino
     */
    public static final int EMPTY = 0;
}
