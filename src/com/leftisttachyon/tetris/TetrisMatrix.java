package com.leftisttachyon.tetris;

import static com.leftisttachyon.tetris.MinoStyle.MINO_SIZE;
import com.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import static com.leftisttachyon.tetris.MinoStyle.*;
import com.leftisttachyon.tetris.tetrominos.Tetromino;
import com.leftisttachyon.tetris.tetrominos.TetrominoFactory;
import com.leftisttachyon.tetris.tetrominos.ars.ARSSpinSystem;
import com.leftisttachyon.tetris.tetrominos.ars.ARSTetrominoFactory;
import com.leftisttachyon.tetris.tetrominos.srs.SRSSpinSystem;
import com.leftisttachyon.tetris.tetrominos.srs.SRSTetrominoFactory;
import com.leftisttachyon.tetris.ui.DASHandler;
import com.leftisttachyon.util.Paintable;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
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
     * Just another internal Paintable object.
     */
    private PaintableMatrix paintableMatrix = new PaintableMatrix();

    /**
     * The amount of frames to delay blocks falling down after a line clear.
     */
    private int lineClearDelay = 40;

    /**
     * ARE after a line clear; amount of frames to pause the tetromino coming
     * in.
     */
    private int lineClearARE = 25;

    /**
     * ARE without a line clear; amount of frames to pause the tetromino coming
     * in.
     */
    private int standardARE = 25;

    /**
     * The amount of frames while touching the ground until the tetromino locks
     * down.
     */
    private int lockDelay = 30;

    /**
     * A HashSet of cleared lines
     */
    private HashSet<Integer> linesToClear;

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
     * The MinoStyle used for drawing individual minos.
     */
    private MinoStyle minoStyle;

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
     * Whether to draw the ghost piece
     */
    private boolean drawGhost;

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
        minoStyle = null;
        linesToClear = new HashSet<>();
        gravity = 0;
        drawGhost = true;
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
     * Sets the currently used MinoStyle to the given one
     *
     * @param minoStyle the MinoStyle to use from now on
     */
    public void setMinoStyle(MinoStyle minoStyle) {
        this.minoStyle = minoStyle;
    }

    /**
     * Returns the currently used MinoStyle
     *
     * @return the MinoStyle currently being used
     */
    public MinoStyle getMinoStyle() {
        return minoStyle;
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
        paintableMatrix.paint(g2D);
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
        if (!inGame) {
            return;
        }

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
     * The tetromino currently being locked
     */
    private Tetromino lockingTet = null;

    /**
     * A counter for lock flash
     */
    private int lockFlashCnt = -1;

    /**
     * Locks the current piece to the playing field and sets flags for the
     * animation and piece change.
     */
    private void lock() {
        int[][] temp = currentTet.getState();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (temp[i][j] > 0) {
                    matrix[i + currentTet.getY()][j + currentTet.getX()]
                            = FLASH;
                }
            }
        }

        lockingTet = currentTet;
        currentTet = null;
        holdAvaliable = false;

        if (linesToClear.isEmpty()) {
            pauseAnimationCnt = standardARE + 1;
        } else {
            pauseAnimationCnt = lineClearDelay + 1;
        }
        lockFlashCnt = 5;
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
     * The amount of gravity per frame
     */
    private int gravity;

    /**
     * A counter for a pause
     */
    private int pauseAnimationCnt = -1;

    /**
     * The lock delay counter
     */
    private int lockDelayCnt = lockDelay;

    /**
     * The previous Y value
     */
    private int previousY = -1;

    /**
     * Advances a frame.
     *
     * @param handler a DASHandler so I know what to do
     */
    public void advanceFrame(DASHandler handler) {
        if (currentTet != null && currentTet.intersects(this)) {
            System.out.println(currentTet.getRotation());
            System.out.println("**********");
            currentTet.printState();
            System.out.println("**********");
            for (int[] is : matrix) {
                for (int i : is) {
                    System.out.print(i == 0 ? " " : i);
                }
                System.out.println();
            }
        }

        if (lockingTet != null) {
            // lockFlashCnt >= 0
            if (lockFlashCnt > 0) {
                lockFlashCnt--;
            } else {
                int[][] temp = lockingTet.getState();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (temp[i][j] > 0) {
                            matrix[i + lockingTet.getY()][j + lockingTet.getX()]
                                    = temp[i][j];
                        }
                    }
                }

                if (!linesToClear.isEmpty()) {
                    throw new IllegalStateException("linesToClear ought to be empty!");
                }

                for (int i = 0; i < matrix.length; i++) {
                    if (isLineFull(i)) {
                        clearLine(i);
                        linesToClear.add(i);
                    }
                }

                lockingTet = null;
                lockFlashCnt = -1;
            }
        }

        if (pauseAnimationCnt >= 0) {
            pauseAnimationCnt--;
        }

        if (pauseAnimationCnt == 0) {
            if (linesToClear.isEmpty()) {
                currentTet = queue.removeTetromino();
                activate(currentTet);

                if (handler.isPressed(VK_Z)) {
                    currentTet.rotateLeft();
                }
                if (handler.isPressed(VK_X)) {
                    currentTet.rotateRight();
                }
                holdAvaliable = true;
                if (handler.isPressed(VK_C)) {
                    hold();
                }

                pauseAnimationCnt = -1;
            } else {
                for (int i = matrix.length - 1, temp = i; i >= 0; i--) {
                    if (!linesToClear.contains(i)) {
                        if (temp != i) {
                            System.arraycopy(matrix[i], 0,
                                    matrix[temp], 0, matrix[i].length);
                        }
                        temp--;
                    }
                }

                linesToClear.clear();

                pauseAnimationCnt = lineClearARE;
            }
        }

        if (currentTet != null) {
            for (int i = 0; i < gravity
                    && !currentTet.intersects(this, 0, 1); i++) {
                currentTet.moveDown();
            }

            if (currentTet.intersects(this, 0, 1)
                    && previousY == currentTet.getY()) {
                if (lockDelayCnt == 0) {
                    lockDelayCnt = lockDelay;
                    lock();
                }
                lockDelayCnt--;
            } else {
                lockDelayCnt = lockDelay;
            }
        }

        if (currentTet == null) {
            previousY = -1;
        } else {
            previousY = currentTet.getY();
        }
    }

    /**
     * Determines whether this matrix is in a game or not
     *
     * @return whether this matrix is in a game or not
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Determines whether the given row is full or not
     *
     * @param r the row to investigate
     * @return whether the given row is full
     */
    private boolean isLineFull(int r) {
        for (int i : matrix[r]) {
            if (i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears the given line
     *
     * @param r the row to clear
     */
    private void clearLine(int r) {
        for (int i = 0; i < matrix[r].length; i++) {
            matrix[r][i] = 0;
        }
    }

    /**
     * Sets the gravity to the given integer
     *
     * @param gravity the gravity to use from now on
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    /**
     * Increases gravity by the given amount
     *
     * @param increase the amount to increase the gravity by
     */
    public void increaseGravity(int increase) {
        gravity += increase;
    }

    /**
     * Based on this matrix's SpinSystem, determines the default MinoStyle.
     *
     * @return the default MinoStyle
     */
    private MinoStyle getDefaultMinoStyle() {
        if (spinSystem == null) {
            return BasicMinoStyle.getMinoStyle();
        } else {
            if (spinSystem instanceof SRSSpinSystem) {
                return SRSMinoStyle.getMinoStyle();
            } else if (spinSystem instanceof ARSSpinSystem) {
                return TGMMinoStyle.getMinoStyle();
            } else {
                return BasicMinoStyle.getMinoStyle();
            }
        }
    }

    /**
     * Just an imitation
     */
    private class PaintableMatrix implements Paintable {

        @Override
        public void paint(Graphics2D g2D) {
            MinoStyle style = minoStyle == null
                    ? getDefaultMinoStyle()
                    : minoStyle;

            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setStroke(new BasicStroke(1.0f));

            g2D.setColor(Color.BLACK);
            g2D.fillRect(0, (int) (19.5 * MINO_SIZE),
                    10 * MINO_SIZE, (int) (20.5 * MINO_SIZE));

            Color locked = new Color(0, 0, 0, 100);

            if (currentTet != null) {
                int[][] state = currentTet.getState();

                int addY = 0;
                while (!currentTet.intersects(TetrisMatrix.this, 0, addY + 1)) {
                    addY++;
                }

                if (addY != 0) {
                    g2D.setComposite(MinoStyle.TRANSLUCENT_COMPOSITE);
                    for (int i = 0, x = currentTet.getX() * MINO_SIZE;
                            i < state.length; i++, x += MINO_SIZE) {
                        for (int j = 0, y = (currentTet.getY() + addY) * MINO_SIZE;
                                j < state[i].length; j++, y += MINO_SIZE) {
                            style.drawMino(g2D, x, y, state[j][i]);
                        }
                    }
                    g2D.setComposite(AlphaComposite.SrcOver);
                }

                if (lockDelayCnt != 0 && lockDelayCnt == lockDelay) {
                    for (int i = 0, x = currentTet.getX() * MINO_SIZE;
                            i < state.length; i++, x += MINO_SIZE) {
                        for (int j = 0, y = currentTet.getY() * MINO_SIZE;
                                j < state[i].length; j++, y += MINO_SIZE) {
                            style.drawMino(g2D, x, y, state[j][i]);
                        }
                    }
                } else {
                    // System.out.println("lDC: " + lockDelayCnt + "\tlD: " + lockDelay);
                    double shade = 1 - ((double) lockDelayCnt) / lockDelay;
                    g2D.setColor(new Color(0, 0, 0, (int) (shade * 100)));

                    for (int i = 0, x = currentTet.getX() * MINO_SIZE; i < state.length;
                            i++, x += MINO_SIZE) {
                        for (int j = 0, y = currentTet.getY() * MINO_SIZE;
                                j < state[i].length; j++, y += MINO_SIZE) {
                            if (state[j][i] > 0) {
                                style.drawMino(g2D, x, y, state[j][i]);
                                g2D.fillRect(x, y, MINO_SIZE, MINO_SIZE);
                            }
                        }
                    }
                }
            }

            for (int i = 0, y = 0; i < matrix.length; i++, y += MINO_SIZE) {
                for (int j = 0, x = 0; j < matrix[i].length; j++, x += MINO_SIZE) {
                    if (matrix[i][j] > 0) {
                        style.drawMino(g2D, x, y, matrix[i][j]);

                        g2D.setColor(locked);
                        g2D.fillRect(x, y, MINO_SIZE, MINO_SIZE);
                    } else {
                        g2D.setColor(Color.WHITE);

                        if (getBlock(i + 1, j) > 0) {
                            g2D.drawLine(x, y + MINO_SIZE - 1,
                                    x + MINO_SIZE - 1, y + MINO_SIZE - 1);
                        }
                        if (getBlock(i - 1, j) > 0) {
                            g2D.drawLine(x, y, x + MINO_SIZE - 1, y);
                        }
                        if (getBlock(i, j + 1) > 0) {
                            g2D.drawLine(x + MINO_SIZE - 1, y,
                                    x + MINO_SIZE - 1, y + MINO_SIZE - 1);
                        }
                        if (getBlock(i, j - 1) > 0) {
                            g2D.drawLine(x, y, x, y + MINO_SIZE - 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the line clear ARE
     *
     * @param lineClearARE the new line clear ARE
     */
    public void setLineClearARE(int lineClearARE) {
        this.lineClearARE = lineClearARE;
    }

    /**
     * Sets the line clear delay
     *
     * @param lineClearDelay the new lnie clear delay
     */
    public void setLineClearDelay(int lineClearDelay) {
        this.lineClearDelay = lineClearDelay;
    }

    /**
     * Sets ARE under normal conditions
     *
     * @param standardARE the new standard ARE
     */
    public void setStandardARE(int standardARE) {
        this.standardARE = standardARE;
    }

    /**
     * Sets whether to draw the ghost piece
     *
     * @param drawGhost whether to draw the ghost piece
     */
    public void setDrawGhost(boolean drawGhost) {
        this.drawGhost = drawGhost;
    }

    /**
     * Creates a new ARS-style matrix.
     *
     * @return a new ARS-style matrix.
     */
    public static TetrisMatrix generateARSMatrix() {
        TetrisMatrix output = new TetrisMatrix();
        output.setSpinSystem(ARSSpinSystem.getSpinSystem());
        output.setTetrominoFactory(ARSTetrominoFactory.getTetrominoFactory());
        output.setMinoStyle(TGMMinoStyle.getMinoStyle());
        return output;
    }

    /**
     * Creates a new SRS-style matrix
     *
     * @return a new SRS-style matrix
     */
    public static TetrisMatrix generateSRSMatrix() {
        TetrisMatrix output = new TetrisMatrix();
        output.setSpinSystem(SRSSpinSystem.getSpinSystem());
        output.setTetrominoFactory(SRSTetrominoFactory.getTetrominoFactory());
        output.setMinoStyle(SRSMinoStyle.getMinoStyle());
        return output;
    }

    /**
     * Sets the lock delay
     *
     * @param lockDelay the new lock delay
     */
    public void setLockDelay(int lockDelay) {
        this.lockDelay = lockDelay;
        if (lockDelayCnt > lockDelay) {
            lockDelayCnt = lockDelay;
        }
    }
}
