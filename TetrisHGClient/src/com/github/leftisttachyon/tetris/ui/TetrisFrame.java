package com.github.leftisttachyon.tetris.ui;

import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 * A JFrame that contains everything.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public final class TetrisFrame extends JFrame {
    
    /**
     * The internal TetrisPanel
     */
    private TetrisPanel panel;

    /**
     * Creates a new TetrisFrame
     *
     * @throws HeadlessException the exception thrown from the super constructor
     */
    public TetrisFrame() throws HeadlessException {
        panel = new TetrisPanel();
        
        add(panel);
        pack();
        
        setTitle("Tetris HG");
        addKeyListener(panel.handler);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Starts the application.
     */
    public void start() {
        setVisible(true);
        panel.startFrames();
    }
    
    /**
     * Stops the application.
     */
    public void stop() {
        panel.stopFrames();
        setVisible(false);
    }
    
    /**
     * Just a test
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TetrisFrame frame = new TetrisFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.start();
    }
}
