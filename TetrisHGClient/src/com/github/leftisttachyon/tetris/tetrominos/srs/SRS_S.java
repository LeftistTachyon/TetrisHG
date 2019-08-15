package com.github.leftisttachyon.tetris.tetrominos.srs;

import com.github.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetS;

/**
 * A class that represents the S tetromino in the Super Rotation System.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public final class SRS_S extends AbstractTetromino implements TetS, SRSTet {
    // green, so use 5
    
    /**
     * The UP state
     */
    private static final int[][] UP_STATE = {
        {0, 0, 0, 0},
        {0, 5, 5, 0},
        {5, 5, 0, 0},
        {0, 0, 0, 0}
    };
        
    /**
     * The RIGHT state
     */
    private static final int[][] RIGHT_STATE = {
        {0, 0, 0, 0},
        {0, 5, 0, 0},
        {0, 5, 5, 0},
        {0, 0, 5, 0}
    };
        
    /**
     * The DOWN state
     */
    private static final int[][] DOWN_STATE = {
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 5, 5, 0},
        {5, 5, 0, 0}
    };
        
    /**
     * The LEFT state
     */
    private static final int[][] LEFT_STATE = {
        {0, 0, 0, 0},
        {5, 0, 0, 0},
        {5, 5, 0, 0},
        {0, 5, 0, 0}
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
        return "SRS S";
    }
}
