package com.leftisttachyon.tetris.tetrominos.ars;

import com.leftisttachyon.tetris.tetrominos.TetrominoFactory;

/**
 * A Tetromino factory that manufactures ARS tetrominos.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class ARSTetrominoFactory extends TetrominoFactory<ARSTet> {

    @Override
    public ARSTet createTetrominoOf(String type) {
        switch (type) {
            case "I":
                return new ARS_I();
            case "J":
                return new ARS_J();
            case "L":
                return new ARS_L();
            case "O":
                return new ARS_O();
            case "S":
                return new ARS_S();
            case "T":
                return new ARS_T();
            case "Z":
                return new ARS_Z();
            default:
                return null;
        }
    }

}
