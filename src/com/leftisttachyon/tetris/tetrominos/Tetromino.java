package com.leftisttachyon.tetris.tetrominos;

import com.leftisttachyon.tetris.TetrisMatrix;

/**
 * An interface that represents a Tetris piece. (aka a tetromino)
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public interface Tetromino {

    /**
     * Rotates this tetromino in the clockwise direction. So pretty much to the
     * right.
     */
    void rotateRight();

    /**
     * Rotates this tetromino in the counter-clockwise direction. So pretty much
     * to the left.
     */
    void rotateLeft();

    /**
     * Moves this tetromino to the left one unit. This method does not do any
     * checking for validity of this move, it just does it. Please do validation
     * for movement before executing this.
     */
    void moveLeft();

    /**
     * Moves this tetromino to the right one unit. This method does not do any
     * checking for validity of this move, it just does it. Please do validation
     * for movement before executing this.
     */
    void moveRight();

    /**
     * Moves the tetromino down one unit. This method does not check the
     * validity of this move, it just does it. Please do validation for movement
     * before executing this.
     */
    default void moveDown() {
        moveDown(1);
    }

    /**
     * Moves the tetromino down as many units as specified. This method does not
     * check the validity of this move, it just does it. Please do validation
     * for movement before executing this.
     *
     * @param amount the amount to move this tetromino downwards
     */
    void moveDown(int amount);

    /**
     * Transforms this tetromino in the x direction dX units and in the y
     * direction dY units
     *
     * @param dX units to transform the tetromino in the x direction
     * @param dY units to transform the tetromino in the y direction
     */
    void transform(int dX, int dY);

    /**
     * Finds and returns the rotation state of this tetromino, dictated by the
     * Rotation enum.
     *
     * @return the rotation state of this tetromino
     * @see Rotation
     */
    int getRotation();

    /**
     * Returns a 4x4 matrix of integers that represent the visual state of the
     * tetromino (taking into account the spin system of this tetromino) while
     * in the {@code UP} rotation state.
     *
     * @return the visual state of the tetromino while in the UP state
     * @see Rotation#UP
     */
    int[][] getUpState();

    /**
     * Returns a 4x4 matrix of integers that represent the visual state of the
     * tetromino (taking into account the spin system of this tetromino) while
     * in the {@code RIGHT} rotation state.
     *
     * @return the visual state of the tetromino while in the RIGHT state
     * @see Rotation#RIGHT
     */
    int[][] getRightState();

    /**
     * Returns a 4x4 matrix of integers that represent the visual state of the
     * tetromino (taking into account the spin system of this tetromino) while
     * in the {@code DOWN} rotation state.
     *
     * @return the visual state of the tetromino while in the DOWN state
     * @see Rotation#DOWN
     */
    int[][] getDownState();

    /**
     * Returns a 4x4 matrix of integers that represent the visual state of the
     * tetromino (taking into account the spin system of this tetromino) while
     * in the {@code LEFT} rotation state.
     *
     * @return the visual state of the tetromino while in the LEFT state
     * @see Rotation#LEFT
     */
    int[][] getLeftState();

    /**
     * Returns the x-value stored in this tetromino.
     *
     * @return the x-value stored in this tetromino
     */
    int getX();

    /**
     * Returns the y-value stored in this tetromino.
     *
     * @return the y-value stored in this tetromino
     */
    int getY();

    /**
     * Returns a 4x4 matrix of integers that represent the visual state of the
     * tetromino (taking into account the spin system of this tetromino) while
     * in the current rotation state.
     *
     * @return the current visual state of the tetromino
     */
    default int[][] getState() {
        switch (getRotation()) {
            case UP:
                return getUpState();
            case DOWN:
                return getDownState();
            case LEFT:
                return getLeftState();
            case RIGHT:
                return getRightState();
            default:
                return null;
        }
    }

    /**
     * The default, unrotated state of a tetromino.
     */
    public static final int UP = 0;

    /**
     * The rotation state of a tetromino after it has been rotated once in the
     * clockwise direction.
     */
    public static final int RIGHT = 1;

    /**
     * The rotation state of a tetromino after it has been rotated twice in the
     * same direction.
     */
    public static final int DOWN = 2;

    /**
     * The rotation state of a tetromino after it has been rotated once in the
     * counter-clockwise direction.
     */
    public static final int LEFT = 3;

    /**
     * Returns the type of this tetromino (e.g. T, O, or I).
     *
     * @return the type of this tetromino
     */
    String getType();

    /**
     * Returns the full name of this tetromino, including spin system and type.
     *
     * @return the full name of this tetromino
     */
    String getName();

    /**
     * Determines, if overlaid, whether any minos or blocks would overlap or
     * intersect. Determines collisions.
     *
     * @param other the matrix to overlay across this one
     * @return whether any parts of this matrix intersect with the other
     */
    default boolean intersects(int[][] other) {
        if (other.length == 4) {
            int[][] myMat = getState();
            for (int i = 0; i < 4; i++) {
                if (other[i].length == 4) {
                    for (int j = 0; j < 4; j++) {
                        if (myMat[i][j] > 0 && other[i][j] > 0) {
                            return true;
                        }
                    }
                } else {
                    throw new IllegalArgumentException(
                            "Expected a matrix with width 4, recieved a matrix with width "
                            + other[i].length);
                }
            }

            return false;
        } else {
            throw new IllegalArgumentException(
                    "Expected a matrix with height 4, recieved a matrix with height "
                    + other.length);
        }
    }

    /**
     * Determines whether this tetromino intersects with the given TetrisMatrix with
 the internally stored position.
     *
     * @param m the TetrisMatrix to compare with
     * @return whether this tetromino intersects with the given TetrisMatrix with the
 internally stored position
     * @see #intersects(int[][])
     * @see #intersects(com.leftisttachyon.tetris.Matrix, int, int)
     */
    boolean intersects(TetrisMatrix m);

    /**
     * Determines whether this tetromino intersects with the given TetrisMatrix with
 the internally stored position with the offset factored in.
     *
     * @param m the TetrisMatrix to compare with
     * @param x_offset the offset in the x direction
     * @param y_offset the offset in the y direction
     * @return whether this tetromino intersects with the given TetrisMatrix with the
 internally stored position with the offset factored in
     * @see #intersects(int[][])
     * @see #intersects(com.leftisttachyon.tetris.Matrix)
     */
    boolean intersects(TetrisMatrix m, int x_offset, int y_offset);

    /**
     * Sets the current x value of this tetromino to the given integer.
     *
     * @param x the new x value of this tetromino
     */
    void setX(int x);

    /**
     * Sets the current y value of this tetromino to the given integer.
     *
     * @param y the new y value of this tetromino
     */
    void setY(int y);

    /**
     * Sets the current rotation state of this tetromino to the given one.
     *
     * @param rotation the new rotation state of this tetromino
     */
    void setRotation(int rotation);
    
    /**
     * Prints the current rotation state of this tetromino to the console.
     */
    default void printState() {
        for (int[] is : getState()) {
            for (int i : is) {
                System.out.print(i == 0 ? " " : i);
            }
            System.out.println();
        }
    }
}
