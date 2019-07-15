package com.leftisttachyon.tetris.tetrominos.ars;

import com.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.leftisttachyon.tetris.tetrominos.TetO;

/**
 * A class that represents the O tetromino in the Arika Rotation System.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public final class ARS_O extends AbstractTetromino implements TetO, ARSTet {
    
    // yellow, so use 4

    /**
     * Since the O doesn't rotate, then this is the only state that the O can be
     * in. It's different for each class since the color's different.
     */
    private static final int[][] ONLY_STATE = {
        {0, 0, 0, 0},
        {0, 4, 4, 0},
        {0, 4, 4, 0},
        {0, 0, 0, 0}
    };

    @Override
    public int[][] getUpState() {
        return ONLY_STATE;
    }

    @Override
    public int[][] getRightState() {
        return ONLY_STATE;
    }

    @Override
    public int[][] getDownState() {
        return ONLY_STATE;
    }

    @Override
    public int[][] getLeftState() {
        return ONLY_STATE;
    }

    @Override
    public String getName() {
        return "ARS O";
    }
}
