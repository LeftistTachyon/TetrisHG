package com.leftisttachyon.tetris.ui;

import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 * A JFrame that contains everything.
 *
 * @author Jed Wang
 * @since 1.0.0
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
        
        addKeyListener(panel.handler);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
}
