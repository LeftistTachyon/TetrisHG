package com.leftisttachyon.tetris.tetrominos;

import com.leftisttachyon.tetris.TetrisMatrix;

/**
 * An implementing class of Tetromino that has more utility methods.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public abstract class AbstractTetromino implements Tetromino {

    /**
     * The rotation state of this tetromino.
     */
    protected int rotation;

    /**
     * The x-position of this tetromino
     */
    protected int x;

    /**
     * The y-position of this tetromino
     */
    protected int y;

    /**
     * Creates a blank AbstractTetromino.
     */
    public AbstractTetromino() {
        this(UP, 0, 0);
    }

    /**
     * Creates a new AbstractTetromino as dictated by the parameters.
     *
     * @param x the initial x-value of this tetromino
     * @param y the initial y-value of this tetromino
     */
    public AbstractTetromino(int x, int y) {
        this(UP, x, y);
    }

    /**
     * Creates a new AbstractTetromino as dictated by the parameters.
     *
     * @param rotation the rotation state of this tetromino. If an invalid value
     * is passed, the rotation state is set to UP.
     * @param x the initial x-value of this tetromino
     * @param y the initial y-value of this tetromino
     */
    public AbstractTetromino(int rotation, int x, int y) {
        if (rotation >= 0 && rotation <= 3) {
            this.rotation = rotation;
        } else {
            this.rotation = UP;
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public void moveDown() {
        y++;
    }

    @Override
    public void moveDown(int amount) {
        y += amount;
    }

    @Override
    public void moveLeft() {
        x--;
    }

    @Override
    public void moveRight() {
        x++;
    }

    @Override
    public void rotateLeft() {
        rotation--;
        if (rotation < 0) {
            rotation += 4;
        }
    }

    @Override
    public void rotateRight() {
        rotation++;
        rotation %= 4;
    }

    /**
     * {@inheritDoc} In {@code AbstractTetromino}, added slight performance
     * enhancements.
     *
     * @return the current visual state of this tetromino
     */
    @Override
    public int[][] getState() {
        switch (rotation) {
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

    @Override
    public int getRotation() {
        return rotation;
    }

    @Override
    public boolean intersects(TetrisMatrix m) {
        return intersects(m, 0, 0);
    }

    @Override
    public boolean intersects(TetrisMatrix m, int x_offset, int y_offset) {
        int[][] myMat = getState();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (myMat[j][i] > 0
                        && m.getBlock(y + j + y_offset, x + i + x_offset) > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void transform(int dX, int dY) {
        x += dX;
        y += dY;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return getName() + " x=" + x + " y=" + y + " rotation=" + rotation;
    }
}
