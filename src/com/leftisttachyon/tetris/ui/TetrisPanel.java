package com.leftisttachyon.tetris.ui;

import com.leftisttachyon.tetris.MinoStyle;
import static com.leftisttachyon.tetris.MinoStyle.MINO_SIZE;
import com.leftisttachyon.tetris.TetrisMatrix;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import static java.awt.event.KeyEvent.*;
import java.awt.geom.NoninvertibleTransformException;
import java.util.HashSet;
import javax.swing.JPanel;

/**
 * A class that draws everything and takes in key events.
 *
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
     * Whether the player has selected to use SRS or ARS
     */
    private volatile boolean selected;

    /**
     * The amount of avaliable selections
     */
    private final int selections = 2;

    /**
     * Your current selection
     */
    private int selection = 0;

    /**
     * Creates a new, default TetrisPanel.
     */
    public TetrisPanel() {
        super();

        handler = new DASHandler();
        // handler.setListener(VK_DOWN, new Point(14, 1));
        handler.setListener(VK_DOWN, new Point(-1, -1));
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

        selected = false;

        m = new TetrisMatrix();
    }

    /**
     * Starts rendering frames, accepting inputs, and running code.
     */
    public void startFrames() {
        new Thread(() -> {
            while (!stop) {
                // double start = System.nanoTime();
                repaint();
                /*double total = System.nanoTime() - start;
                total /= 1_000_000;
                System.out.printf("Frame: %.2f ms%n", total);*/

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
        Graphics2D g2D = (Graphics2D) g;
        
        // first take inputs
        HashSet<Integer> actions = handler.advanceFrame();

        if (selected) {
            // then make updates
            m.executeActions(actions);
            m.advanceFrame(handler);
        } else {
            if (actions.remove(VK_DOWN)) {
                selection++;
                selection %= selections;
            }
            if (actions.remove(VK_UP)) {
                selection--;
                if (selection < 0) {
                    selection += selections;
                }
            }

            if (actions.contains(VK_Z) || actions.contains(VK_X)
                    || actions.contains(VK_C) || actions.contains(VK_SPACE)) {
                switch (selection) {
                    case 0:
                        TetrisMatrix.setAsSRSMatrix(m);
                        break;
                    case 1:
                        TetrisMatrix.setAsARSMatrix(m);
                        break;
                }

                handler.setListener(VK_DOWN, new Point(14, 1));
                m.startGame();

                selected = true;
                selection = -1;
            }
        }

        // lastly draw
        g2D.setColor(new Color(127, 127, 127));
        g2D.fillRect(0, 0, getWidth(), getHeight());
        try {
            m.paint(g2D, 10, 10);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }

        if (!selected) {
            g2D.setFont(new Font("Arial Black", Font.PLAIN, 15));
            FontMetrics metrics = g2D.getFontMetrics();
            g2D.setColor(Color.WHITE);
            
            String toDraw;
            
            toDraw = "SRS (normal garbage)";
            g2D.drawString(toDraw, 110 + (10 * MINO_SIZE - metrics.stringWidth(toDraw)) / 2, 150);
            
            toDraw = "ARS/TGM (2-5x garbage)";
            g2D.drawString(toDraw, 110 + (10 * MINO_SIZE - metrics.stringWidth(toDraw)) / 2, 250);
            
            g2D.setColor(Color.RED);
            g2D.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_SQUARE, 
                    BasicStroke.JOIN_MITER));
            
            int height = metrics.getHeight();
            switch(selection) {
                case 0:
                    g2D.drawRect(115, 130 - height / 4, 10 * MINO_SIZE - 10, 40);
                    break;
                case 1:
                    g2D.drawRect(115, 230 - height / 4, 10 * MINO_SIZE - 10, 40);
                    break;
            }
        }
    }
}
