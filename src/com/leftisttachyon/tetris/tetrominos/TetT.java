package com.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents a T Tetromino
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public interface TetT extends Tetromino {
    @Override
    public default String getType() {
        return "T";
    }
}
