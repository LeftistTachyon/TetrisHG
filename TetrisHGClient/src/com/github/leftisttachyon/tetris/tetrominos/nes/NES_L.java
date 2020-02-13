package com.github.leftisttachyon.tetris.tetrominos.nes;

import com.github.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetL;

/**
 * A class that represents the L tetromino in the Nintendo Rotation System.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class NES_L extends AbstractTetromino implements TetL, NESTet{
    // use 3
    private static final int[][] UP_STATE = {
        {0, 0, 0, 0},
        {3, 3, 3, 0},
        {3, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    private static final int[][] RIGHT_STATE = {
        {3, 3, 0, 0},
        {0, 3, 0, 0},
        {0, 3, 0, 0},
        {0, 0, 0, 0}
    };
    
    private static final int[][] DOWN_STATE = {
        {0, 0, 3, 0},
        {3, 3, 3, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
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
        return "NES L";
    }
}