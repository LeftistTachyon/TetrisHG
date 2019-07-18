package com.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents an O Tetromino
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public interface TetO extends Tetromino {
    @Override
    public default String getType() {
        return "O";
    }
}