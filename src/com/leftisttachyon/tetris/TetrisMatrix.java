package com.leftisttachyon.tetris;

import com.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import static com.leftisttachyon.tetris.tetrominos.AbstractTetromino.*;
import com.leftisttachyon.tetris.tetrominos.Tetromino;
import static com.leftisttachyon.tetris.tetrominos.Tetromino.BLUE;
import static com.leftisttachyon.tetris.tetrominos.Tetromino.CYAN;
import static com.leftisttachyon.tetris.tetrominos.Tetromino.EMPTY;
import static com.leftisttachyon.tetris.tetrominos.Tetromino.GREEN;
import static com.leftisttachyon.tetris.tetrominos.Tetromino.ORANGE;
import static com.leftisttachyon.tetris.tetrominos.Tetromino.PURPLE;
import static com.leftisttachyon.tetris.tetrominos.Tetromino.RED;
import static com.leftisttachyon.tetris.tetrominos.Tetromino.YELLOW;
import com.leftisttachyon.tetris.tetrominos.TetrominoFactory;
import com.leftisttachyon.util.Paintable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import static java.awt.event.KeyEvent.*;
import java.util.HashSet;

/**
 * A class that represents a playing field
 *
 * @author Jed Wang
 */
public class TetrisMatrix implements Paintable {

    /**
     * The actual matrix
     */
    private int[][] matrix;

    /**
     * The internal TetQueue for this TetrisMatrix
     */
    private TetQueue queue;

    /**
     * The internal TetrominoFactory for this TetrisMatrix
     */
    private TetrominoFactory factory;

    /**
     * The internal SpinSystem used for rotating tetrominos for this
     * TetrisMatrix
     */
    private SpinSystem spinSystem;

    /**
     * The currently falling tetromino
     */
    private Tetromino currentTet;

    /**
     * The current tetromino being held. Null represents no tetromino being held
     */
    private Tetromino holdTet;

    /**
     * Whether this matrix is in a game or not
     */
    private boolean inGame;
    
    /**
     * Whether hold is avaliable
     */
    private boolean holdAvaliable;

    /**
     * Creates a new TetrisMatrix.
     */
    public TetrisMatrix() {
        matrix = new int[40][10];
        queue = new TetQueue();
        factory = null;
        spinSystem = null;
        currentTet = null;
        holdTet = null;
        inGame = false;
        holdAvaliable = false;
    }

    /**
     * Sets the currently used TetrominoFactory to the given one.
     *
     * @param factory the TetrominoFactory to use from now on
     */
    public void setTetrominoFactory(TetrominoFactory factory) {
        this.factory = factory;
        queue.setTetrominoFactory(factory);
    }

    /**
     * Returns the currently used TetrominoFactory
     *
     * @return the currently used TetrominoFactory
     */
    public TetrominoFactory getTetrominoFactory() {
        return factory;
    }

    /**
     * Sets the currently used SpinSystem to the given name
     *
     * @param spinSystem the SpinSystem to use from now on
     */
    public void setSpinSystem(SpinSystem spinSystem) {
        this.spinSystem = spinSystem;
    }

    /**
     * Returns the currently used SpinSystem
     *
     * @return the SpinSystem currently being used
     */
    public SpinSystem getSpinSystem() {
        return spinSystem;
    }

    /**
     * Returns the internally stored TetQueue
     *
     * @return the internally stored TetQueue
     */
    public TetQueue getQueue() {
        return queue;
    }

    /**
     * Finds and returns the value of the block/mino at the specified row and
     * column. If the row or column are out of bounds, then it returns the value
     * of WALL.
     *
     * @param r the row of the matrix to find
     * @param c the column of the matrix to find
     * @return the value of the wanted block/mino
     * @see AbstractTetromino#WALL
     */
    public int getBlock(int r, int c) {
        if (r < 0 || r >= matrix.length || c < 0 || c >= matrix[r].length) {
            return WALL;
        } else {
            return matrix[r][c];
        }
    }

    /**
     * Copies a portion of the internal matrix to a new matrix. Uses
     * {@code getBlock} to determine the value of each block.
     *
     * @param r the row of the top left corner
     * @param c the column of the top left corner
     * @param w the width of the wanted matrix
     * @param h the height of the wanted matrix
     * @return the copied portion of the matrix
     * @see #getBlock(int, int)
     */
    public int[][] getSection(int r, int c, int w, int h) {
        int[][] output = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                output[i][j] = getBlock(r + i, c + j);
            }
        }
        return output;
    }

    @Override
    public void paint(Graphics2D g2D) {
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.setColor(Color.BLACK);
        g2D.fillRect(0, (int) (19.5 * MINO_SIZE),
                10 * MINO_SIZE, (int) (20.5 * MINO_SIZE));

        Color purple = new Color(128, 0, 128);

        for (int i = 0, y = 0; i < matrix.length; i++, y += Tetromino.MINO_SIZE) {
            for (int j = 0, x = 0; j < matrix[i].length; j++, x += Tetromino.MINO_SIZE) {
                switch (matrix[i][j]) {
                    case CYAN:
                        g2D.setColor(Color.CYAN);
                        break;
                    case BLUE:
                        g2D.setColor(Color.BLUE);
                        break;
                    case ORANGE:
                        g2D.setColor(Color.ORANGE);
                        break;
                    case YELLOW:
                        g2D.setColor(Color.YELLOW);
                        break;
                    case GREEN:
                        g2D.setColor(Color.GREEN);
                        break;
                    case PURPLE:
                        g2D.setColor(purple);
                        break;
                    case RED:
                        g2D.setColor(Color.RED);
                        break;
                    case EMPTY:
                        continue;
                }

                g2D.fillRect(x, y, Tetromino.MINO_SIZE, Tetromino.MINO_SIZE);
            }
        }

        if (currentTet != null) {
            int[][] state = currentTet.getState();
            for (int i = 0, x = currentTet.getX() * MINO_SIZE; i < state.length;
                    i++, x += MINO_SIZE) {
                for (int j = 0, y = currentTet.getY() * MINO_SIZE;
                        j < state[i].length; j++, y += MINO_SIZE) {
                    switch (state[j][i]) {
                        case CYAN:
                            g2D.setColor(Color.CYAN);
                            break;
                        case BLUE:
                            g2D.setColor(Color.BLUE);
                            break;
                        case ORANGE:
                            g2D.setColor(Color.ORANGE);
                            break;
                        case YELLOW:
                            g2D.setColor(Color.YELLOW);
                            break;
                        case GREEN:
                            g2D.setColor(Color.GREEN);
                            break;
                        case PURPLE:
                            g2D.setColor(purple);
                            break;
                        case RED:
                            g2D.setColor(Color.RED);
                            break;
                        case EMPTY:
                            continue;
                    }

                    g2D.fillRect(x, y, Tetromino.MINO_SIZE, Tetromino.MINO_SIZE);
                }
            }
        }
    }

    /**
     * Resets the parameters of a tetromino so that it is ready to become
     * active.
     *
     * @param t the tetromino to activate
     */
    private void activate(Tetromino t) {
        t.setRotation(Tetromino.UP);
        t.setX(3);
        t.setY(20);
    }

    /**
     * Executes the actions outlined in the HashSet of keycodes.
     *
     * @param keycodes the actions (related to the keycodes) that should be
     * executed
     */
    public void executeActions(HashSet<Integer> keycodes) {
        if (keycodes.contains(VK_Z)) {
            if (spinSystem == null) {
                System.err.println("No spin system installed, cannot rotate left");
            } else {
                if (currentTet == null) {
                    System.err.println("Cannot rotate a null tetromino left");
                } else {
                    spinSystem.rotateLeft(currentTet, this);
                }
            }
        }

        if (keycodes.contains(VK_X)) {
            if (spinSystem == null) {
                System.err.println("No spin system installed, cannot rotate right");
            } else {
                if (currentTet == null) {
                    System.err.println("Cannot rotate a null tetromino right");
                } else {
                    spinSystem.rotateRight(currentTet, this);
                }
            }
        }

        if (keycodes.contains(VK_C)) {
            if (currentTet == null) {
                System.err.println("There is no tetromino to hold");
            } else {
                hold();
            }
        }

        if (keycodes.contains(VK_SPACE)) {
            if (currentTet == null) {
                System.err.println("There is no tetromino to hard drop");
            } else {
                hardDrop();
            }
        }

        if (keycodes.contains(VK_UP)) {
            if (currentTet == null) {
                System.err.println("There is no tetromino to sonic drop");
            } else {
                sonicDrop();
            }
        }

        if (keycodes.contains(VK_DOWN)) {
            if (currentTet == null) {
                System.err.println("There is no tetromino to soft drop");
            } else {
                softDrop();
            }
        }

        if (keycodes.contains(VK_LEFT)) {
            if (currentTet == null) {
                System.err.println("There is no tetromino to move to the left");
            } else {
                if (!currentTet.intersects(this, -1, 0)) {
                    currentTet.moveLeft();
                }
            }
        }

        if (keycodes.contains(VK_RIGHT)) {
            if (currentTet == null) {
                System.err.println("There is no tetromino to move to the right");
            } else {
                if (!currentTet.intersects(this, 1, 0)) {
                    currentTet.moveRight();
                }
            }
        }
    }

    /**
     * Swaps the currently held tetromino with the active one. If there is no
     * tetromino being held, the active tetromino becomes held and the next
     * tetromino in the queue becomes active.
     */
    private void hold() {
        if (holdAvaliable) {
            if (holdTet == null) {
                holdTet = currentTet;
                currentTet = queue.removeTetromino();
                activate(currentTet);
            } else {
                Tetromino temp = holdTet;
                holdTet = currentTet;
                activate(temp);
                currentTet = temp;
            }
            
            holdAvaliable = false;
        }
    }

    /**
     * Hard drops the currently active piece (which is locking, consistent with
     * SRS rules) and makes the next piece in the queue active.
     */
    private void hardDrop() {
        sonicDrop();
        lock();
    }

    /**
     * Soft drops the currently active piece downwards once. This action is not
     * locking, consistent with SRS rules.
     */
    private void softDrop() {
        if (!currentTet.intersects(this, 0, 1)) {
            currentTet.moveDown();
        }
    }

    /**
     * Sonic drops the currently active piece. This action is not locking,
     * consistent with the TGM series.
     */
    private void sonicDrop() {
        while (!currentTet.intersects(this, 0, 1)) {
            currentTet.moveDown();
        }
    }

    /**
     * Locks the current piece to the playing field and sets flags for the
     * animation and piece change.
     */
    private void lock() {
        // TODO: animations
        int[][] temp = currentTet.getState();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (temp[i][j] > 0) {
                    matrix[i + currentTet.getY()][j + currentTet.getX()]
                            = temp[i][j];
                }
            }
        }
        
        currentTet = queue.removeTetromino();
        activate(currentTet);
        
        holdAvaliable = true;
    }

    /**
     * Starts gameplay on this matrix.
     */
    public void startGame() {
        queue.addBag();
        currentTet = queue.removeTetromino();
        activate(currentTet);
        inGame = true;
        holdAvaliable = true;
    }

    /**
     * Ends gameplay on this matrix.
     */
    public void endGame() {
        currentTet = null;
        inGame = false;
    }

    /**
     * Resets this matrix to starting conditions.
     */
    public void reset() {
        currentTet = null;
        queue.clearQueue();
        matrix = new int[40][10];
        inGame = false;
        holdAvaliable = false;
    }

    /**
     * Advances a frame for all animations.
     */
    public void advanceAnimationFrame() {
        // System.out.println(currentTet);
    }

    /**
     * Determines whether this matrix is in a game or not
     *
     * @return whether this matrix is in a game or not
     */
    public boolean isInGame() {
        return inGame;
    }
}
