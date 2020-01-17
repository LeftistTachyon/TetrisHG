package com.github.leftisttachyon.tetris.tetrominos.ars;

import com.github.leftisttachyon.tetris.TetrisMatrix;
import com.github.leftisttachyon.tetris.SpinSystem;
import com.github.leftisttachyon.tetris.tetrominos.Tetromino;

/**
 * The SpinSystem object for ARS.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class ARSSpinSystem extends SpinSystem<ARSTet> {

    /**
     * The only object to be created
     */
    private static final ARSSpinSystem SINGLETON = new ARSSpinSystem();

    /**
     * Returns an instance of an ARSSpinSystem
     *
     * @return an instance of an ARSSpinSystem
     */
    public static ARSSpinSystem getSpinSystem() {
        return SINGLETON;
    }

    /**
     * No creation for you!
     */
    private ARSSpinSystem() {
    }

    @Override
    public void rotateRight(ARSTet t, TetrisMatrix m) {
        if (!(t instanceof ARSTet)) {
            throw new IllegalArgumentException(
                    "Tried to rotate a non-ARS piece using ARS.");
        }

        t.rotateRight();
        if (!t.intersects(m)) {
            return;
        }

        if (t instanceof ARS_I) {
            t.rotateLeft();
            return;
        }

        if (t instanceof ARS_L && (t.getRotation() == Tetromino.RIGHT
                && m.getBlock(t.getY(), t.getX()) == 0
                || t.getRotation() == Tetromino.LEFT)) {
            t.rotateLeft();
            return;
        }

        if (t instanceof ARS_J && (t.getRotation() == Tetromino.LEFT
                && m.getBlock(t.getY(), t.getX() + 2) == 0
                || t.getRotation() == Tetromino.RIGHT)) {
            t.rotateLeft();
            return;
        }

        if (!t.intersects(m, 1, 0)) {
            t.transform(1, 0);
            return;
        }

        if (t.intersects(m, -1, 0)) {
            t.rotateLeft();
        } else {
            t.transform(-1, 0);
        }
    }

    @Override
    public void rotateLeft(ARSTet t, TetrisMatrix m) {
        if (!(t instanceof ARSTet)) {
            throw new IllegalArgumentException(
                    "Tried to rotate a non-ARS piece using ARS.");
        }

        t.rotateLeft();
        if (!t.intersects(m)) {
            return;
        }

        if (t instanceof ARS_I) {
            t.rotateRight();
            return;
        }

        if (t instanceof ARS_J && (t.getRotation() == Tetromino.LEFT
                && m.getBlock(t.getY(), t.getX() + 2) == 0
                || t.getRotation() == Tetromino.RIGHT)) {
            t.rotateRight();
            return;
        }

        if (t instanceof ARS_L && (t.getRotation() == Tetromino.RIGHT
                && m.getBlock(t.getY(), t.getX()) == 0
                || t.getRotation() == Tetromino.LEFT)) {
            t.rotateRight();
            return;
        }

        if (!t.intersects(m, 1, 0)) {
            t.transform(1, 0);
            return;
        }

        if (t.intersects(m, -1, 0)) {
            t.rotateRight();
        } else {
            t.transform(-1, 0);
        }
    }

    @Override
    public String getType() {
        return "ARS";
    }

}
