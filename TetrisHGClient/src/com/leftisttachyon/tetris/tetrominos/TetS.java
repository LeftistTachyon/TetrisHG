package com.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents an S Tetromino
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public interface TetS extends Tetromino {
    @Override
    public default String getType() {
        return "S";
    }
}