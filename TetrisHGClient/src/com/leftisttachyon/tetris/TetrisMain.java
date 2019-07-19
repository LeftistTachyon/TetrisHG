package com.leftisttachyon.tetris;

import com.leftisttachyon.ui.LobbyWindow;
import java.awt.Dimension;
import java.awt.Toolkit;

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

        LobbyWindow frame = LobbyWindow.run();
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(
                (ss.width - frame.getWidth()) / 2,
                (ss.height - frame.getHeight()) / 2);
    }
}
