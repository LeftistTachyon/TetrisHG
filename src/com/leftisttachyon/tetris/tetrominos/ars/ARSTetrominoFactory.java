package com.leftisttachyon.tetris.tetrominos.ars;

import com.leftisttachyon.tetris.tetrominos.TetrominoFactory;

/**
 * A Tetromino factory that manufactures ARS tetrominos.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class ARSTetrominoFactory extends TetrominoFactory<ARSTet> {

    /**
     * No creation for you!
     */
    private ARSTetrominoFactory() {
    }

    /**
     * The only object to be created
     */
    private static final ARSTetrominoFactory SINGLETON
            = new ARSTetrominoFactory();

    /**
     * Returns an instance of an ARSTetrominoFactory
     *
     * @return an instance of an ARSTetrominoFactory
     */
    public static ARSTetrominoFactory getTetrominoFactory() {
        return SINGLETON;
    }

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
