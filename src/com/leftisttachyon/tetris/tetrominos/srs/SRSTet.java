package com.leftisttachyon.tetris.tetrominos.srs;

import com.leftisttachyon.tetris.tetrominos.Tetromino;

/**
 * A marker interface that denotes a tetromino from the SRS spin system.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public interface SRSTet extends Tetromino {

    @Override
    public default String getName() {
        return "SRS " + getType();
    }

}
