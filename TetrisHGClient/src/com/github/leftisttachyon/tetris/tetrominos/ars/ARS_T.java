package com.github.leftisttachyon.tetris.tetrominos.ars;

import com.github.leftisttachyon.tetris.TetrisMatrix;
import com.github.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetT;
import java.awt.Point;

/**
 * A class that represents the T tetromino in the Arika Rotation System.
 *
 * @author Jed Wang
 * @since 0.9.0
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
    
    /**
     * The center when in the down rotation state
     */
    private static final Point DOWN_CENTER = new Point(2, 1);
    
    /**
     * The center otherwise
     */
    private static final Point CENTER = new Point(1, 1);

    @Override
    public Point getCenter() {
        return rotation == DOWN ? DOWN_CENTER : CENTER;
    }

    @Override
    public int filledFaceCorners(TetrisMatrix m) {
        Point center = getCenter();
        int cx = center.x + x, cy = center.y + y;
        
        int output = 0;
        if ((rotation == UP || rotation == RIGHT) && m.getBlock(cx - 1, cy - 1) != 0) {
            output++;
        }
        if ((rotation == DOWN || rotation == RIGHT) && m.getBlock(cx + 1, cy - 1) != 0) {
            output++;
        }
        if ((rotation == UP || rotation == LEFT) && m.getBlock(cx - 1, cy  + 1) != 0) {
            output++;
        }
        if ((rotation == DOWN || rotation == LEFT) && m.getBlock(cx + 1, cy + 1) != 0) {
            output++;
        }
        return output;
    }
    
}