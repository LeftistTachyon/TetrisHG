package com.leftisttachyon.tetris.tetrominos.ars;

import com.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.leftisttachyon.tetris.tetrominos.TetT;

/**
 * A class that represents the T tetromino in the Arika Rotation System.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public final class ARS_T extends AbstractTetromino implements TetT, ARSTet {
    // cyan, so use 1
    
    /**
     * The UP state
     */
    private static final int[][] UP_STATE = {
        {0, 0, 0, 0}, 
        {1, 1, 1, 0}, 
        {0, 1, 0, 0}, 
        {0, 0, 0, 0}
    };
    
    /**
     * The RIGHT state
     */
    private static final int[][] RIGHT_STATE = {
        {0, 1, 0, 0}, 
        {1, 1, 0, 0}, 
        {0, 1, 0, 0}, 
        {0, 0, 0, 0}
    };
    
    /**
     * The LEFT state
     */
    private static final int[][] LEFT_STATE = {
        {0, 1, 0, 0}, 
        {0, 1, 1, 0}, 
        {0, 1, 0, 0}, 
        {0, 0, 0, 0}
    };
    
    /**
     * The DOWN state
     */
    private static final int[][] DOWN_STATE = {
        {0, 0, 0, 0}, 
        {0, 1, 0, 0}, 
        {1, 1, 1, 0}, 
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
        return "ARS T";
    }
    
}