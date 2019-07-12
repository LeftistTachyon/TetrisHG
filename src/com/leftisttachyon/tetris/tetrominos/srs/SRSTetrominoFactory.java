package com.leftisttachyon.tetris.tetrominos.srs;

import com.leftisttachyon.tetris.tetrominos.TetrominoFactory;

/**
 * A Tetromino factory that manufactures SRS tetrominos.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class SRSTetrominoFactory extends TetrominoFactory<SRSTet> {

    @Override
    public SRSTet createTetrominoOf(String type) {
        switch (type) {
            case "I":
                return new SRS_I();
            case "J":
                return new SRS_J();
            case "L":
                return new SRS_L();
            case "O":
                return new SRS_O();
            case "S":
                return new SRS_S();
            case "T":
                return new SRS_T();
            case "Z":
                return new SRS_Z();
            default:
                return null;
        }
    }

}
