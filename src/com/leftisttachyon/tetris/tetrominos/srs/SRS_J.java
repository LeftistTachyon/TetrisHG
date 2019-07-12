package com.leftisttachyon.tetris.tetrominos.srs;

import com.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.leftisttachyon.tetris.tetrominos.TetJ;

/**
 * A class that represents the J tetromino in the Super Rotation System.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public final class SRS_J extends AbstractTetromino implements TetJ, SRSTet {
    // blue, so use 2

    /**
     * The UP state
     */
    private static final int[][] UP_STATE = {
        {2, 0, 0, 0},
        {2, 2, 2, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };

    /**
     * The RIGHT state
     */
    private static final int[][] RIGHT_STATE = {
        {0, 2, 2, 0},
        {0, 2, 0, 0},
        {0, 2, 0, 0},
        {0, 0, 0, 0}
    };

    /**
     * The DOWN state
     */
    private static final int[][] DOWN_STATE = {
        {0, 0, 0, 0},
        {2, 2, 2, 0},
        {0, 0, 2, 0},
        {0, 0, 0, 0}
    };

    /**
     * The LEFT state
     */
    private static final int[][] LEFT_STATE = {
        {0, 2, 0, 0},
        {0, 2, 0, 0},
        {2, 2, 0, 0},
        {0, 0, 0, 0}
    };
    
    @Override
    public int[][] getUpState() {
        return UP_STATE;
    }

    @Override
    public int[][] getRightState() {
        return RIGHT_STATE;
    }

    @Override
    public int[][] getDownState() {
        return DOWN_STATE;
    }

    @Override
    public int[][] getLeftState() {
        return LEFT_STATE;
    }

    @Override
    public String getName() {
        return "SRS J";
    }
}