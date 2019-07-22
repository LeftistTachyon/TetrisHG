package com.leftisttachyon.tetris.ui;

import com.leftisttachyon.comm.ClientSocket;
import com.leftisttachyon.tetris.MinoStyle;
import static com.leftisttachyon.tetris.MinoStyle.MINO_SIZE;
import com.leftisttachyon.tetris.TetrisMatrix;
import com.leftisttachyon.tetris.tetrominos.Tetromino;
import com.leftisttachyon.tetris.tetrominos.TetrominoFactory;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

/**
 * A class that draws everything and takes in key events.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public final class TetrisPanel extends JPanel {

    /**
     * The matrix being drawn
     */
    private TetrisMatrix myMatrix;

    /**
     * Their matrix
     */
    private TetrisMatrix theirMatrix;

    /**
     * The KeyAdapter for this JPanel
     */
    DASHandler handler;

    /**
     * Whether the player has selected to use SRS or ARS
     */
    private volatile boolean meSelected;

    /**
     * Whether the opponent has selected to use SRS or ARS
     */
    private volatile boolean theySelected;

    /**
     * The amount of avaliable selections
     */
    private final int selections = 2;

    /**
     * Your current selection
     */
    private int mySelection = 0;

    /**
     * Their current selection
     */
    private int theirSelection = 0;

    /**
     * A service for a stable (?) framerate
     */
    private ScheduledExecutorService service;

    /**
     * Can I start yet?
     */
    private int canStart = 0;

    /**
     * Creates a new, default TetrisPanel.
     */
    public TetrisPanel() {
        super();

        handler = new DASHandler();
        // handler.setListener(VK_DOWN, new Point(8, 1));
        handler.setListener(VK_DOWN, new Point(-1, -1));
        handler.setListener(VK_Z, new Point(-1, -1));
        handler.setListener(VK_X, new Point(-1, -1));
        handler.setListener(VK_C, new Point(-1, -1));
        handler.setListener(VK_SPACE, new Point(-1, -1));
        handler.setListener(VK_LEFT, new Point(8, 1));
        handler.setListener(VK_RIGHT, new Point(8, 1));
        handler.setListener(VK_UP, new Point(-1, -1));

        addKeyListener(handler);

        setPreferredSize(new Dimension(20 * MinoStyle.MINO_SIZE + 440,
                21 * MinoStyle.MINO_SIZE + 20));

        meSelected = false;
        theySelected = false;

        myMatrix = new TetrisMatrix(true);
        theirMatrix = new TetrisMatrix(false);

        service = null;

        if (ClientSocket.isConnected()) {
            ClientSocket.getConnection().addServerListener((line) -> {
                if (!theySelected) {
                    if (line.startsWith("SELECT")) {
                        theirSelection = Integer.parseInt(line.substring(6));
                    } else if (line.startsWith("CHOOSE")) {
                        theirSelection = Integer.parseInt(line.substring(6));
                        theySelected = true;

                        switch (theirSelection) {
                            case 0:
                                TetrisMatrix.setAsSRSMatrix(theirMatrix);
                                break;
                            case 1:
                                TetrisMatrix.setAsARSMatrix(theirMatrix);
                                break;
                        }

                        if (meSelected) {
                            generateStartBag();

                            if (++canStart == 2) {
                                startGame();
                            }
                        }
                    }
                } else if (meSelected) {
                    if (line.startsWith("ACTIONS")) {
                        HashSet<Integer> actions = new HashSet<>();
                        String[] data = line.substring(7).split(" ");
                        for (String s : data) {
                            actions.add(Integer.parseInt(s));
                        }

                        theirMatrix.executeActions(actions);
                    } else if (line.startsWith("NB")) {
                        theirMatrix.addBag(line.substring(2));
                        if (!myMatrix.isInGame() && ++canStart == 2) {
                            startGame();
                        }
                        // System.out.println("Added a bag of " + line.substring(2));
                    }
                }
            });
        }
    }

    /**
     * Starts rendering frames, accepting inputs, and running code.
     */
    public void startFrames() {
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            try {
                double start = System.nanoTime();
                repaint();
                double total = System.nanoTime() - start;
                total /= 1_000_000;
                System.out.printf("Frame: %.2f ms%n", total);
            } catch (Exception e) {
                System.err.println("Exception occured while executing frame");
                e.printStackTrace();
            }
        }, 0, 16, TimeUnit.MILLISECONDS);
    }

    /**
     * Starts rendering frames, accepting inputs, and running code.
     */
    public void stopFrames() {
        service.shutdown();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        // first take inputs
        HashSet<Integer> actions = handler.advanceFrame();

        // then make updates
        if (meSelected && theySelected) {
            myMatrix.executeActions(actions);
            myMatrix.advanceFrame(handler);

            theirMatrix.advanceFrame(null);
        } else if (!meSelected) {
            if (actions.remove(VK_DOWN)) {
                mySelection++;
                mySelection %= selections;
            }
            if (actions.remove(VK_UP)) {
                mySelection--;
                if (mySelection < 0) {
                    mySelection += selections;
                }
            }

            ClientSocket.getConnection().send("SELECT" + mySelection);

            if (actions.contains(VK_Z) || actions.contains(VK_X)
                    || actions.contains(VK_C) || actions.contains(VK_SPACE)) {
                switch (mySelection) {
                    case 0:
                        TetrisMatrix.setAsSRSMatrix(myMatrix);
                        break;
                    case 1:
                        TetrisMatrix.setAsARSMatrix(myMatrix);
                        break;
                }

                handler.setListener(VK_DOWN, new Point(8, 1));

                meSelected = true;

                ClientSocket.getConnection().send("CHOOSE" + mySelection);

                if (theySelected) {
                    generateStartBag();

                    if (++canStart == 2) {
                        startGame();
                    }
                }
            }
        }

        // lastly draw
        g2D.setColor(new Color(127, 127, 127));
        g2D.fillRect(0, 0, getWidth(), getHeight());
        try {
            myMatrix.paint(g2D, 10, 10);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }

        int temp_x = 110;

        if (!meSelected || !theySelected) {
            g2D.setFont(new Font("Arial Black", Font.PLAIN, 15));
            FontMetrics metrics = g2D.getFontMetrics();

            String toDraw;

            if (meSelected && mySelection == 0) {
                g2D.setColor(Color.RED);
            } else {
                g2D.setColor(Color.WHITE);
            }
            toDraw = "SRS (normal garbage)";
            g2D.drawString(toDraw, temp_x
                    + (10 * MINO_SIZE - metrics.stringWidth(toDraw)) / 2, 150);

            if (meSelected && mySelection == 1) {
                g2D.setColor(Color.RED);
            } else {
                g2D.setColor(Color.WHITE);
            }
            toDraw = "ARS/TGM (1.5-2x garbage)";
            g2D.drawString(toDraw, temp_x
                    + (10 * MINO_SIZE - metrics.stringWidth(toDraw)) / 2, 250);

            if (!meSelected) {
                g2D.setColor(Color.RED);
                g2D.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_SQUARE,
                        BasicStroke.JOIN_MITER));

                int height = metrics.getHeight();
                switch (mySelection) {
                    case 0:
                        g2D.drawRect(temp_x + 5, 130 - height / 4,
                                10 * MINO_SIZE - 10, 40);
                        break;
                    case 1:
                        g2D.drawRect(temp_x + 5, 230 - height / 4,
                                10 * MINO_SIZE - 10, 40);
                        break;
                }
            }
        }

        try {
            theirMatrix.paint(g2D, 10 * MinoStyle.MINO_SIZE + 220, 10);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }

        temp_x += 10 * MINO_SIZE + 210;

        if (!meSelected || !theySelected) {
            g2D.setFont(new Font("Arial Black", Font.PLAIN, 15));
            FontMetrics metrics = g2D.getFontMetrics();

            String toDraw;

            if (theySelected && theirSelection == 0) {
                g2D.setColor(Color.RED);
            } else {
                g2D.setColor(Color.WHITE);
            }
            toDraw = "SRS (normal garbage)";
            g2D.drawString(toDraw, temp_x
                    + (10 * MINO_SIZE - metrics.stringWidth(toDraw)) / 2, 150);

            if (theySelected && theirSelection == 1) {
                g2D.setColor(Color.RED);
            } else {
                g2D.setColor(Color.WHITE);
            }
            toDraw = "ARS/TGM (1.5-2x garbage)";
            g2D.drawString(toDraw, temp_x
                    + (10 * MINO_SIZE - metrics.stringWidth(toDraw)) / 2, 250);

            if (!theySelected) {
                g2D.setColor(Color.RED);
                g2D.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_SQUARE,
                        BasicStroke.JOIN_MITER));

                int height = metrics.getHeight();
                switch (theirSelection) {
                    case 0:
                        g2D.drawRect(temp_x + 5, 130 - height / 4,
                                10 * MINO_SIZE - 10, 40);
                        break;
                    case 1:
                        g2D.drawRect(temp_x + 5, 230 - height / 4,
                                10 * MINO_SIZE - 10, 40);
                        break;
                }
            }
        }
    }

    /**
     * Sets a new value for the DAS
     *
     * @param das the new DAS value
     */
    public void setDAS(int das) {
        Point dasSettings = new Point(das, 1);
        handler.setListener(VK_DOWN, dasSettings);
        handler.setListener(VK_LEFT, dasSettings);
        handler.setListener(VK_RIGHT, dasSettings);
    }

    /**
     * Returns the internal TetrisMatrix
     *
     * @return the internal TetrisMatrix
     */
    public TetrisMatrix getMatrix() {
        return myMatrix;
    }

    /**
     * Starts the game.
     */
    private void startGame() {
        if (!theirMatrix.isInGame()) {
            theirMatrix.startGame();

            theirMatrix.setGarbageConsumer((lines) -> {
                if (lines == -1) {
                    myMatrix.endGame();
                } else {
                    int toSend = (int) (lines * getMultiplier(theirSelection));
                    // myMatrix.addGarbage(0);
                    myMatrix.queueGarbage(toSend);
                }
            });
        }
        if (!myMatrix.isInGame()) {
            myMatrix.startGame();

            myMatrix.setGarbageConsumer((lines) -> {
                if (lines == -1) {
                    theirMatrix.endGame();
                } else {
                    int toSend = (int) (lines * getMultiplier(mySelection));
                    theirMatrix.queueGarbage(toSend);
                }
            });
        }
    }

    /**
     * Generates a starting bag.
     */
    private void generateStartBag() {
        TetrominoFactory factory
                = myMatrix.getTetrominoFactory();
        List<Tetromino> bag = factory.createRandomBag();
        String message = "";
        for (Tetromino t : bag) {
            message += t.getType();
        }
        ClientSocket.getConnection().send("NB" + message);
        myMatrix.addBag(message);
    }

    /**
     * Finds the garbage multiplier depending on the given choice of spin
     * system.
     *
     * @param spinSelection the spin system to query
     * @return the damage multiplier
     */
    private double getMultiplier(int spinSelection) {
        switch (spinSelection) {
            case 0:
                return 1;
            case 1:
                return 1.5;
            default:
                return 0;
        }
    }
}
