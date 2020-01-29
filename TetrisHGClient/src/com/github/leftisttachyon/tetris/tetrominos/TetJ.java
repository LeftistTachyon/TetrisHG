package com.github.leftisttachyon.tetris.tetrominos;

/**
 * An interface that represents a J Tetromino
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public interface TetJ extends Tetromino {
    @Override
    default String getType() {
        return "J";
    }
}