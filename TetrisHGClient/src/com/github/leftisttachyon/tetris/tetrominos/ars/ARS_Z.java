package com.github.leftisttachyon.tetris.tetrominos.ars;

import com.github.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetZ;

/**
 * A class that represents the Z tetromino in the Arika Rotation System.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public final class ARS_Z extends AbstractTetromino implements TetZ, ARSTet {

    // green, so use 5
    /**
     * The "vertical" state (i.e. up and down) of this tetromino
     */
    private static final int[][] VERTICAL_STATE = {
        {0, 0, 0, 0},
        {5, 5, 0, 0},
        {0, 5, 5, 0},
        {0, 0, 0, 0}
    };

    /**
     * The "horizontal" state (i.e. left and right) of this tetromino
     */
    private static final int[][] HORIZONTAL_STATE = {
        {0, 0, 5, 0},
        {0, 5, 5, 0},
        {0, 5, 0, 0},
        {0, 0, 0, 0}
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
        return "ARS Z";
    }

}
