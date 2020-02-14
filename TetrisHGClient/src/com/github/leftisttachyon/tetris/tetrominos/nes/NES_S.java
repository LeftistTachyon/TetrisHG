package com.github.leftisttachyon.tetris.tetrominos.nes;

import com.github.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetS;

/**
 * A class that represents the S tetromino in the Nintendo Rotation System.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class NES_S extends AbstractTetromino implements TetS, NESTet {
    // use 5
    private static final int[][] UP_DOWN = {
        {0, 0, 0, 0},
        {0, 0, 5, 5},
        {0, 5, 5, 0},
        {0, 0, 0, 0}
    };
    
    private static final int[][] LEFT_RIGHT = {
        {0, 0, 5, 0},
        {0, 0, 5, 5},
        {0, 0, 0, 5},
        {0, 0, 0, 0}
    };

    @Override
    public int[][] getUpState() {
        return UP_DOWN;
    }

    @Override
    public int[][] getRightState() {
        return LEFT_RIGHT;
    }

    @Override
    public int[][] getDownState() {
        return UP_DOWN;
    }

    @Override
    public int[][] getLeftState() {
        return LEFT_RIGHT;
    }

    @Override
    public String getName() {
        return "NES S";
    }
}