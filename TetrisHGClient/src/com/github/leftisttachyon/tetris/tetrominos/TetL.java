package com.github.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents a L Tetromino
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public interface TetL extends Tetromino {
    @Override
    public default String getType() {
        return "L";
    }
}