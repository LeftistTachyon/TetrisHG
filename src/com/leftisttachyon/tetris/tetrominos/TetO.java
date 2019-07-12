package com.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents an O Tetromino
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public interface TetO extends Tetromino {
    @Override
    public default String getType() {
        return "O";
    }
}