package com.github.leftisttachyon.tetris.tetrominos.srs;

import com.github.leftisttachyon.tetris.tetrominos.Tetromino;

/**
 * A marker interface that denotes a tetromino from the SRS spin system.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public interface SRSTet extends Tetromino {

    @Override
    public default String getName() {
        return "SRS " + getType();
    }

}
