package com.github.leftisttachyon.tetris.tetrominos;

import com.github.leftisttachyon.tetris.TetrisMatrix;
import java.awt.Point;

/**
 * An interface that represents a T Tetromino
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public interface TetT extends Tetromino {

    @Override
    public default String getType() {
        return "T";
    }

    /**
     * Returns the center of this T piece, for T-spin checking purposes
     *
     * @return the center of this T piece, for T-spin checking purposes
     */
    Point getCenter();

    /**
     * Determines how many corners are filled in on the "face" side; aka the one
     * opposite of the flat side
     *
     * @param m the TetrisMatrix that contains the information about the playing
     * field
     * @return how many corners are filled in on the "face" side
     */
    int filledFaceCorners(TetrisMatrix m);
}
