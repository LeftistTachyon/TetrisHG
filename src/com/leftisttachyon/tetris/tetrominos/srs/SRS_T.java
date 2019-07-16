package com.leftisttachyon.tetris.tetrominos.srs;

import com.leftisttachyon.tetris.TetrisMatrix;
import com.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.leftisttachyon.tetris.tetrominos.TetT;
import java.awt.Point;

/**
 * A class that represents the T tetromino in the Super Rotation System.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public final class SRS_T extends AbstractTetromino implements TetT, SRSTet {
    //purple, so use 6
    
    /**
     * The UP state
     */
    private static final int[][] UP_STATE = {
        {0, 0, 0, 0},
        {0, 6, 0, 0},
        {6, 6, 6, 0},
        {0, 0, 0, 0}
    };
        
    /**
     * The RIGHT state
     */
    private static final int[][] RIGHT_STATE = {
        {0, 0, 0, 0},
        {0, 6, 0, 0},
        {0, 6, 6, 0},
        {0, 6, 0, 0}
    };
        
    /**
     * The DOWN state
     */
    private static final int[][] DOWN_STATE = {
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {6, 6, 6, 0},
        {0, 6, 0, 0}
    };
        
    /**
     * The LEFT state
     */
    private static final int[][] LEFT_STATE = {
        {0, 0, 0, 0},
        {0, 6, 0, 0},
        {6, 6, 0, 0},
        {0, 6, 0, 0}
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
        return "SRS T";
    }
    
    /**
     * Always the center
     */
    private static final Point CENTER = new Point(2, 1);

    @Override
    public Point getCenter() {
        return CENTER;
    }

    @Override
    public int filledFaceCorners(TetrisMatrix m) {
        int output = 0;
        if ((rotation == UP || rotation == LEFT) && m.getBlock(x, y + 1) != 0) {
            output++;
        }
        if ((rotation == UP || rotation == RIGHT) && m.getBlock(x + 2, y + 1) != 0) {
            output++;
        }
        if ((rotation == DOWN || rotation == LEFT) && m.getBlock(x, y + 3) != 0) {
            output++;
        }
        if ((rotation == DOWN || rotation == RIGHT) && m.getBlock(x + 2, y + 3) != 0) {
            output++;
        }
        return output;
    }
}
