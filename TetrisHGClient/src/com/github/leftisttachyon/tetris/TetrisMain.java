package com.github.leftisttachyon.tetris;

import com.github.leftisttachyon.ui.LobbyWindow;

/**
 * The main class; entry point of the application
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class TetrisMain {

    /**
     * The main method; where the application starts
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*TetrisFrame frame = new TetrisFrame();
        frame.start();*/

        LobbyWindow.run();
    }
}
