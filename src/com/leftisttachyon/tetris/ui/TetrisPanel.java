package com.leftisttachyon.tetris.ui;

import com.leftisttachyon.tetris.TetrisMatrix;
import com.leftisttachyon.tetris.tetrominos.Tetromino;
import com.leftisttachyon.tetris.tetrominos.ars.ARSSpinSystem;
import com.leftisttachyon.tetris.tetrominos.ars.ARSTetrominoFactory;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import static java.awt.event.KeyEvent.*;
import java.awt.geom.NoninvertibleTransformException;
import java.util.HashSet;
import javax.swing.JPanel;

/**
 * A class that draws everything and takes in key events.
 * @author Jed Wang
 * @since 1.0.0
 */
public final class TetrisPanel extends JPanel {
    
    /**
     * The matrix being drawn
     */
    private TetrisMatrix m;
    
    /**
     * The KeyAdapter for this JPanel
     */
    DASHandler handler;
    
    /**
     * A flag for stopping
     */
    private volatile boolean stop;

    /**
     * Creates a new, default TetrisPanel.
     */
    public TetrisPanel() {
        super();
        
        handler = new DASHandler();
        handler.setListener(VK_DOWN, new Point(9, 3));
        handler.setListener(VK_Z, new Point(-1, -1));
        handler.setListener(VK_X, new Point(-1, -1));
        handler.setListener(VK_C, new Point(-1, -1));
        handler.setListener(VK_SPACE, new Point(-1, -1));
        handler.setListener(VK_LEFT, new Point(9, 3));
        handler.setListener(VK_RIGHT, new Point(9, 3));
        handler.setListener(VK_UP, new Point(-1, -1));
        
        addKeyListener(handler);
        
        setPreferredSize(new Dimension(10 * Tetromino.MINO_SIZE + 20, 
                21 * Tetromino.MINO_SIZE + 20));
        
        m = new TetrisMatrix();
        m.setSpinSystem(ARSSpinSystem.getSpinSystem());
        m.setTetrominoFactory(ARSTetrominoFactory.getTetrominoFactory());
    }
    
    /**
     * Starts rendering frames, accepting inputs, and running code.
     */
    public void startFrames() {
        new Thread(() -> {
            while(!stop) {
                // long start = System.nanoTime();
                repaint();
                /*long total = System.nanoTime() - start;
                total /= 1_000_000;
                System.out.println(total + " ms");*/
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
    
    /**
     * Starts rendering frames, accepting inputs, and running code.
     */
    public void stopFrames() {
        stop = true;
    }

    @Override
    public void paint(Graphics g) {
        if (!m.isInGame()) {
            m.startGame();
        }
        
        // first take inputs
        HashSet<Integer> actions = handler.advanceFrame();
        
        // then make updates
        m.executeActions(actions);
        m.advanceAnimationFrame();

        // lastly draw
        try {
            m.paint(g, 10, 10 - 19 * Tetromino.MINO_SIZE);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
    }
}