package com.leftisttachyon.tetris.ui;

import com.leftisttachyon.tetris.MinoStyle;
import com.leftisttachyon.tetris.TetrisMatrix;
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
        handler.setListener(VK_DOWN, new Point(14, 1));
        handler.setListener(VK_Z, new Point(-1, -1));
        handler.setListener(VK_X, new Point(-1, -1));
        handler.setListener(VK_C, new Point(-1, -1));
        handler.setListener(VK_SPACE, new Point(-1, -1));
        handler.setListener(VK_LEFT, new Point(14, 1));
        handler.setListener(VK_RIGHT, new Point(14, 1));
        handler.setListener(VK_UP, new Point(-1, -1));
        
        addKeyListener(handler);
        
        setPreferredSize(new Dimension(10 * MinoStyle.MINO_SIZE + 220, 
                21 * MinoStyle.MINO_SIZE + 20));
        
        m = TetrisMatrix.generateSRSMatrix();
        
        // to the MAX
        m.setStandardARE(25);
        m.setLineClearARE(25);
        m.setLockDelay(30);
        m.setLineClearDelay(25);
        
        m.setGravity(20); // 20G!
        
        handler.setListener(VK_DOWN, new Point(8, 1));
        handler.setListener(VK_LEFT, new Point(8, 1));
        handler.setListener(VK_RIGHT, new Point(8, 1));
    }
    
    /**
     * Starts rendering frames, accepting inputs, and running code.
     */
    public void startFrames() {
        new Thread(() -> {
            m.startGame();
            
            while(!stop && m.isInGame()) {
                double start = System.nanoTime();
                repaint();
                double total = System.nanoTime() - start;
                total /= 1_000_000;
                System.out.printf("Frame: %.2f ms%n", total);
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
        // first take inputs
        HashSet<Integer> actions = handler.advanceFrame();
        
        // then make updates
        m.executeActions(actions);
        m.advanceFrame(handler);

        // lastly draw
        try {
            m.paint(g, 10, 10);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
    }
}