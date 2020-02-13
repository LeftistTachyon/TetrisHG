package com.github.leftisttachyon.tetris.tetrominos.nes;

import com.github.leftisttachyon.tetris.TetrisMatrix;
import com.github.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetT;
import java.awt.Point;

/**
 * A class that represents the T tetromino in the Nintendo Rotation System.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class NES_T extends AbstractTetromino implements TetT, NESTet{
    // use 6
    private static final int[][] UP_STATE = {
        {0, 0, 0, 0},
        {6, 6, 6, 0},
        {0, 6, 0, 0},
        {0, 0, 0, 0}
    };
    
    private static final int[][] RIGHT_STATE = {
        {0, 6, 0, 0},
        {6, 6, 0, 0},
        {0, 6, 0, 0},
        {0, 0, 0, 0}
    };
    
    private static final int[][] DOWN_STATE = {
        {0, 6, 0, 0},
        {6, 6, 6, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    private static final int[][] LEFT_STATE = {
        {0, 6, 0, 0},
        {0, 6, 6, 0},
        {0, 6, 0, 0},
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
        return "NES T";
    }
    
    private static final Point CENTER = new Point(1, 1);

    @Override
    public Point getCenter() {
        return CENTER;
    }

    @Override
    public int filledFaceCorners(TetrisMatrix m) {
        int output = 0;
        if ((rotation == UP || rotation == RIGHT) && m.getBlock(x, y + 2) != 0) {
            output++;
        }
        if ((rotation == DOWN || rotation == RIGHT) && m.getBlock(x, y) != 0) {
            output++;
        }
        if ((rotation == UP || rotation == LEFT) && m.getBlock(x + 2, y + 2) != 0) {
            output++;
        }
        if ((rotation == DOWN || rotation == LEFT) && m.getBlock(x + 2, y) != 0) {
            output++;
        }
        return output;
    }
}