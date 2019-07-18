package com.leftisttachyon.tetris;

import com.leftisttachyon.tetris.tetrominos.Tetromino;
import static com.leftisttachyon.util.TetrisUtils.*;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * An abstract that controls how each mino is drawn.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public abstract class MinoStyle {

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
    public void drawMino(Graphics2D g2D, int x, int y, int color) {
        drawMino(g2D, x, y, MINO_SIZE, color);
    }

    /**
     * Draws a mino of the given color with the given Graphics object at the x
     * and y positions given.
     *
     * @param g the Graphics object to draw the mino with
     * @param x the x coordinate of the top left corner of the mino to be drawn
     * @param y the y coordinate of the top left corner of the mino to be drawn
     * @param color the color of the mino to be drawn
     */
    public void drawMino(Graphics g, int x, int y, int color) {
        drawMino((Graphics2D) g, x, y, MINO_SIZE, color);
    }

    /**
     * Draws a mino of the given color and size with the given Graphics object
     * at the x and y positions given.
     *
     * @param g the Graphics object to draw the mino with
     * @param x the x coordinate of the top left corner of the mino to be drawn
     * @param y the y coordinate of the top left corner of the mino to be drawn
     * @param size the size of the mino to be drawn
     * @param color the color of the mino to be drawn
     */
    public void drawMino(Graphics g, int x, int y, int size, int color) {
        drawMino((Graphics2D) g, x, y, size, color);
    }

    /**
     * Draws a mino of the given color and size with the given Graphics2D object
     * at the x and y positions given.
     *
     * @param g2D the Graphics2D object to draw the mino with
     * @param x the x coordinate of the top left corner of the mino to be drawn
     * @param y the y coordinate of the top left corner of the mino to be drawn
     * @param size the size of the mino to be drawn
     * @param color the color of the mino to be drawn
     */
    public abstract void drawMino(Graphics2D g2D, int x, int y, int size, int color);

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
     * Number representing a mino that is flashing (when a tetromino locks) or a
     * garbage mino
     */
    public static final int GREY = 9;

    /**
     * Number representing no mino
     */
    public static final int EMPTY = 0;

    /**
     * Draws the given tetromino's up state with the given Graphics at the given
     * x and y value, with the given width;
     *
     * @param g the Graphics to draw with
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     * @param width the width of the tetromino to draw
     * @param t the tetromino to draw
     */
    public void drawTetromino(
            Graphics g, int x, int y, int width, Tetromino t) {
        drawTetromino((Graphics2D) g, x, y, width, t);
    }

    /**
     * Draws the given tetromino's up state with the given Graphics2D at the
     * given x and y value, with the given width;
     *
     * @param g2D the Graphics2D to draw with
     * @param x_ the x-coordinate to draw at
     * @param y_ the y-coordinate to draw at
     * @param width the width of the tetromino to draw
     * @param t the tetromino to draw
     */
    public void drawTetromino(
            Graphics2D g2D, int x_, int y_, int width, Tetromino t) {
        int minoSize = width / 5;
        int[][] upState = t.getUpState();
        int top = highestRow(upState), bottom = lowestRow(upState),
                left = leftmostCol(upState), right = rightmostCol(upState);
        int minoHeight = bottom - top + 1, minoWidth = right - left + 1;
        int horizGap = (5 - minoWidth) * minoSize / 2,
                vertGap = (3 - minoHeight) * minoSize / 2;

        for (int i = left, x = x_ + horizGap; i <= right; i++, x += minoSize) {
            for (int j = top, y = y_ + vertGap; j <= bottom; j++, y += minoSize) {
                drawMino(g2D, x, y, minoSize, upState[j][i]);
            }
        }
    }

    /**
     * Draws the given tetromino's up state with the given Graphics at the given
     * x and y value, with default width;
     *
     * @param g the Graphics to draw with
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     * @param t the tetromino to draw
     */
    public void drawTetromino(
            Graphics g, int x, int y, Tetromino t) {
        drawTetromino((Graphics2D) g, x, y, 4 * MINO_SIZE, t);
    }

    /**
     * Draws the given tetromino's up state with the given Graphics2D at the
     * given x and y value, with default width;
     *
     * @param g2D the Graphics2D to draw with
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     * @param t the tetromino to draw
     */
    public void drawTetromino(
            Graphics2D g2D, int x, int y, Tetromino t) {
        drawTetromino(g2D, x, y, 4 * MINO_SIZE, t);
    }
}
