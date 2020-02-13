package com.github.leftisttachyon.tetris.tetrominos.nes;

import com.github.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetI;

/**
 * A class that represents the I tetromino in the Nintendo Rotation System.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class NES_I extends AbstractTetromino implements TetI, NESTet {
    // use cyan: 1
    private static final int[][] UP_DOWN = {
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {1, 1, 1, 1},
        {0, 0, 0, 0}
    };
    
    private static final int[][] LEFT_RIGHT = {
        {0, 0, 1, 0},
        {0, 0, 1, 0},
        {0, 0, 1, 0},
        {0, 0, 1, 0}
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
        return "NES I";
    }
}