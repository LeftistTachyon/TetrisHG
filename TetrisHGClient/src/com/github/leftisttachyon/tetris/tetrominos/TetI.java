package com.github.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents an I Tetromino
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public interface TetI extends Tetromino {
    @Override
    public default String getType() {
        return "I";
    }
}