package com.github.leftisttachyon.tetris.tetrominos.srs;

import com.github.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetI;

/**
 * A class that represents the I tetromino in the Super Rotation System.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public final class SRS_I extends AbstractTetromino implements TetI, SRSTet {
    // cyan, so use 1

    /**
     * The UP state
     */
    private static final int[][] UP_STATE = {
        {0, 0, 0, 0},
        {1, 1, 1, 1},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
        
    /**
     * The RIGHT state
     */
    private static final int[][] RIGHT_STATE = {
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {0, 0, 1, 0}
    };
        
    /**
     * The DOWN state
     */
    private static final int[][] DOWN_STATE = {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0}
    };
        
    /**
     * The LEFT state
     */
    private static final int[][] LEFT_STATE = {
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0}
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
        return "SRS I";
    }
}
