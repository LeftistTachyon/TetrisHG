package com.github.leftisttachyon.tetris.tetrominos.srs;

import com.github.leftisttachyon.tetris.tetrominos.TetrominoFactory;

/**
 * A Tetromino factory that manufactures SRS tetrominos.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class SRSTetrominoFactory extends TetrominoFactory<SRSTet> {

    /**
     * No creation for you!
     */
    private SRSTetrominoFactory() {
    }

    /**
     * The only object to be created
     */
    private static final SRSTetrominoFactory SINGLETON
            = new SRSTetrominoFactory();

    /**
     * Returns an instance of an SRSTetrominoFactory
     *
     * @return an instance of an SRSTetrominoFactory
     */
    public static SRSTetrominoFactory getTetrominoFactory() {
        return SINGLETON;
    }

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
