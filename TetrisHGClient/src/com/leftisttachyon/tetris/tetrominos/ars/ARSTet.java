package com.leftisttachyon.tetris.tetrominos.ars;

import com.leftisttachyon.tetris.tetrominos.Tetromino;

/**
 * A marker interface that denotes a tetromino from the ARS spin system.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public interface ARSTet extends Tetromino {
    @Override
    public default String getName() {
        return "ARS " + getType();
    }
}
