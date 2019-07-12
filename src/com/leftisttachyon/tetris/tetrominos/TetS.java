package com.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents an S Tetromino
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public interface TetS extends Tetromino {
    @Override
    public default String getType() {
        return "S";
    }
}