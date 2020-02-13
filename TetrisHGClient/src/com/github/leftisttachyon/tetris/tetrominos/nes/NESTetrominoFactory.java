package com.github.leftisttachyon.tetris.tetrominos.nes;

import com.github.leftisttachyon.tetris.tetrominos.TetrominoFactory;
import com.github.leftisttachyon.tetris.tetrominos.ars.ARSTetrominoFactory;

/**
 * A Tetromino factory that manufactures NES tetrominos.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class NESTetrominoFactory extends TetrominoFactory<NESTet> {
    /**
     * No creation for you!
     */
    private NESTetrominoFactory() {
    }
    
    /**
     * The only object to be created
     */
    private static final NESTetrominoFactory SINGLETON
            = new NESTetrominoFactory();

    /**
     * Returns an instance of an NESTetrominoFactory
     *
     * @return an instance of an NESTetrominoFactory
     */
    public static NESTetrominoFactory getTetrominoFactory() {
        return SINGLETON;
    }

    @Override
    public NESTet createTetrominoOf(String type) {
        switch(type) {
            case "I":
                return new NES_I();
            case "J":
                return new NES_J();
            case "L":
                return new NES_L();
            case "S":
                return new NES_S();
            case "T":
                return new NES_T();
            case "Z":
                return new NES_Z();
            case "O":
                return new NES_O();
            default:
                return null;
        }
    }

}
