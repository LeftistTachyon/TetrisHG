package com.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents a Z Tetromino
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public interface TetZ extends Tetromino {
    @Override
    public default String getType() {
        return "Z";
    }
}