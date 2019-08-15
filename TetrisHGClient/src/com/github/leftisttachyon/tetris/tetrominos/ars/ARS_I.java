package com.github.leftisttachyon.tetris.tetrominos.ars;

import com.github.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetI;

/**
 * A class that represents the I tetromino in the Arika Rotation System.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public final class ARS_I extends AbstractTetromino implements TetI, ARSTet {

    // red, so use 7
    /**
     * The "vertical" state (i.e. up and down) of this tetromino
     */
    private static final int[][] VERTICAL_STATE = {
        {0, 0, 0, 0},
        {7, 7, 7, 7},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };

    /**
     * The "horizontal" state (i.e. left and right) of this tetromino
     */
    private static final int[][] HORIZONTAL_STATE = {
        {0, 0, 7, 0},
        {0, 0, 7, 0},
        {0, 0, 7, 0},
        {0, 0, 7, 0}
    };

    @Override
    public int[][] getUpState() {
        return VERTICAL_STATE;
    }

    @Override
    public int[][] getRightState() {
        return HORIZONTAL_STATE;
    }

    @Override
    public int[][] getDownState() {
        return VERTICAL_STATE;
    }

    @Override
    public int[][] getLeftState() {
        return HORIZONTAL_STATE;
    }

    @Override
    public String getName() {
        return "ARS I";
    }

}
