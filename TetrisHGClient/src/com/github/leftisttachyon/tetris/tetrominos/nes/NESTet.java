package com.github.leftisttachyon.tetris.tetrominos.nes;

import com.github.leftisttachyon.tetris.tetrominos.Tetromino;

/**
 * A marker interface that denotes a tetromino from the NES spin system.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public interface NESTet extends Tetromino {
    @Override
    default String getName() {
        return "NES " + getType();
    }
}