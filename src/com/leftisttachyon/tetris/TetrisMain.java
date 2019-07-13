package com.leftisttachyon.tetris;

import com.leftisttachyon.tetris.ui.TetrisFrame;

/**
 * The main class; entry point of the application
 * @author Jed Wang
 * @since 1.0.0
 */
public class TetrisMain {
    /**
     * The main method; where the application starts
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TetrisFrame frame = new TetrisFrame();
        frame.start();
    }
}