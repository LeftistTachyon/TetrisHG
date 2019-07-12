package com.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents a L Tetromino
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public interface TetL extends Tetromino {
    @Override
    public default String getType() {
        return "L";
    }
}