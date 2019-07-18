package com.leftisttachyon.tetris.tetrominos.ars;

import com.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.leftisttachyon.tetris.tetrominos.TetL;

/**
 * A class that represents the L tetromino in the Arika Rotation System.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public final class ARS_L extends AbstractTetromino implements TetL, ARSTet {
    // orange, so use 3
    
    /**
     * The UP state
     */
    private static final int[][] UP_STATE = {
        {0, 0, 0, 0}, 
        {3, 3, 3, 0}, 
        {3, 0, 0, 0}, 
        {0, 0, 0, 0}
    };
    
    /**
     * The RIGHT state
     */
    private static final int[][] RIGHT_STATE = {
        {3, 3, 0, 0}, 
        {0, 3, 0, 0}, 
        {0, 3, 0, 0}, 
        {0, 0, 0, 0}
    };
    
    /**
     * The DOWN state
     */
    private static final int[][] DOWN_STATE = {
        {0, 0, 0, 0}, 
        {0, 0, 3, 0}, 
        {3, 3, 3, 0}, 
        {0, 0, 0, 0}
    };
    
    /**
     * The LEFT state
     */
    private static final int[][] LEFT_STATE = {
        {0, 3, 0, 0}, 
        {0, 3, 0, 0}, 
        {0, 3, 3, 0}, 
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
        return "ARS L";
    }
}