package com.leftisttachyon.tetris.tetrominos.ars;

import com.leftisttachyon.tetris.Matrix;
import com.leftisttachyon.tetris.SpinSystem;
import com.leftisttachyon.tetris.tetrominos.Tetromino;

/**
 * The SpinSystem object for ARS.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class ARSSpinSystem extends SpinSystem<ARSTet> {

    @Override
    public void rotateRight(ARSTet t, Matrix m) {
        if (!(t instanceof ARSTet)) {
            throw new IllegalArgumentException(
                    "Tried to rotate a non-ARS piece using ARS.");
        }

        t.rotateRight();
        if (!t.intersects(m)) {
            return;
        }

        if (t instanceof ARS_L && t.getRotation() == Tetromino.UP
                && m.getBlock(t.getY(), t.getX()) == 0) {
            t.rotateLeft();
            return;
        }

        if (t instanceof ARS_J && t.getRotation() == Tetromino.DOWN
                && m.getBlock(t.getY(), t.getX() + 2) == 0) {
            t.rotateLeft();
            return;
        }

        if (!t.intersects(m, 1, 0)) {
            t.transform(1, 0);
            return;
        }

        if (t.intersects(m, -2, 0)) {
            t.rotateLeft();
        } else {
            t.transform(-2, 0);
        }
    }

    @Override
    public void rotateLeft(ARSTet t, Matrix m) {
        if (!(t instanceof ARSTet)) {
            throw new IllegalArgumentException(
                    "Tried to rotate a non-ARS piece using ARS.");
        }

        t.rotateLeft();
        if (!t.intersects(m)) {
            return;
        }

        if (t instanceof ARS_J && t.getRotation() == Tetromino.UP
                && m.getBlock(t.getY(), t.getX() + 2) == 0) {
            t.rotateRight();
            return;
        }
        
        if (t instanceof ARS_L && t.getRotation() == Tetromino.DOWN 
                && m.getBlock(t.getY(), t.getX()) == 0) {
            t.rotateRight();
            return;
        }

        if (!t.intersects(m, 1, 0)) {
            t.transform(1, 0);
            return;
        }

        if (t.intersects(m, -2, 0)) {
            t.rotateRight();
        } else {
            t.transform(-2, 0);
        }
    }

}
