package com.github.leftisttachyon.tetris;

import com.github.leftisttachyon.comm.ClientSocket;
import com.github.leftisttachyon.tetris.resources.tgm.TGMMinoStyle;
import com.github.leftisttachyon.tetris.tetrominos.TetT;
import com.github.leftisttachyon.tetris.tetrominos.Tetromino;
import com.github.leftisttachyon.tetris.tetrominos.TetrominoFactory;
import com.github.leftisttachyon.tetris.tetrominos.ars.ARSSpinSystem;
import com.github.leftisttachyon.tetris.tetrominos.ars.ARSTetrominoFactory;
import com.github.leftisttachyon.tetris.resources.srs.SRSMinoStyle;
import com.github.leftisttachyon.tetris.tetrominos.srs.SRSSpinSystem;
import com.github.leftisttachyon.tetris.tetrominos.srs.SRSTetrominoFactory;
import com.github.leftisttachyon.tetris.ui.DASHandler;
import com.github.leftisttachyon.util.Paintable;

import java.awt.*;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.function.Consumer;

import static com.github.leftisttachyon.tetris.MinoStyle.*;
import static java.awt.event.KeyEvent.*;

/**
 * A class that represents a playing field
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class TetrisMatrix implements Paintable {

    /**
     * Just another internal Paintable object.
     */
    private final PaintableMatrix paintableMatrix = new PaintableMatrix();

    /**
     * The amount of frames to delay blocks falling down after a line clear.
     */
    private int lineClearDelay = 25;

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
     * A TreeSet of cleared lines
     */
    private TreeSet<Integer> linesToClear;

    /**
     * The actual matrix
     */
    private int[][] matrix;

    /**
     * The internal TetQueue for this TetrisMatrix
     */
    private final TetQueue queue;

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
     * Extra Y for when you need it ;). When the board falls, of course.
     */
    private int extraY;

    /**
     * Extra Y speed for when you need it.
     *
     * @see #extraY
     */
    private double extraYSpeed;

    /**
     * The last move
     */
    private int lastMove;

    /**
     * +2 kick?
     */
    private boolean bigSpin;

    /**
     * The combo
     */
    private int combo;

    /**
     * Stores back to back bonus
     */
    private boolean back2Back;

    /**
     * The GarbageManager for this Matrix
     */
    private GarbageManager garbageManager;

    /**
     * Whether this Matrix is on the left
     */
    private final boolean onLeft;

    /**
     * Creates a new TetrisMatrix.
     *
     * @param onLeft is this matrix on the left?
     */
    public TetrisMatrix(boolean onLeft) {
        this.onLeft = onLeft;
        matrix = new int[40][10];
        queue = new TetQueue(onLeft);
        factory = null;
        spinSystem = null;
        currentTet = null;
        holdTet = null;
        inGame = false;
        holdAvaliable = false;
        minoStyle = null;
        linesToClear = new TreeSet<>();
        gravity = 0.015625;
        drawGhost = true;
        extraY = 0;
        extraYSpeed = 0;
        lastMove = -1;
        bigSpin = false;
        combo = -1;
        back2Back = false;
        garbageManager = new GarbageManager();
        gravityCnt = 0;
        gravityNum = -1;
        lockCnt = 0;

        if (!onLeft && ClientSocket.isConnected()) {
            ClientSocket.getConnection().addServerListener((line) -> {
                try {
                    if (inGame) {
                        if (line.equals("PAUSE0")) {
                            pauseAnimationCnt = 0;
                        }
                        if (line.startsWith("LOCK")) {
                            String[] data = line.substring(4).split(" ");
                            int x = Integer.parseInt(data[0]),
                                    y = Integer.parseInt(data[1]),
                                    rotation = Integer.parseInt(data[2]);
                            if (currentTet != null) {
                                currentTet.setX(x);
                                currentTet.setY(y);
                                currentTet.setRotation(rotation);
                                lock();
                            }
                        } else if (line.startsWith("ENTER")) {
                            boolean left = line.charAt(5) == '1',
                                    right = line.charAt(6) == '1',
                                    hold = line.charAt(7) == '1';
                            enter(left, right, hold);
                        } else if (line.startsWith("GL")) {
                            String[] data = line.substring(2).split(" ");
                            System.out.println("Garbage input: "
                                    + Arrays.toString(data));
                            for (String s : data) {
                                if (!s.isEmpty()) {
                                    addGarbage(Integer.parseInt(s));
                                    garbageManager.counterGarbage(1);
                                }
                            }
                        } else if (line.charAt(0) == 'G') {
                            if (currentTet != null) {
                                currentTet.moveDown(
                                        Integer.parseInt(line.substring(1)));
                            }
                        }
                    }
                } catch (NumberFormatException nfe) {
                    throw new NumberFormatException(
                            nfe.getMessage() + " and message \"" + line + "\"");
                }
            });
        }
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

        queue.setMinoStyle(minoStyle);
    }

    /**
     * Returns the currently used MinoStyle, or if the current style, the
     * default one.
     *
     * @return the MinoStyle currently being used
     */
    public MinoStyle getMinoStyle() {
        return minoStyle == null
                ? getDefaultMinoStyle()
                : minoStyle;
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

        try {
            paintableMatrix.paint(g2D, 100, -19 * MinoStyle.MINO_SIZE + extraY);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }

        if (onLeft) {
            try {
                queue.paint(g2D, 10 * MinoStyle.MINO_SIZE + 120, 0);

                garbageManager.paint(g2D, 80, 10 + 5 * MINO_SIZE);
            } catch (NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }

            g2D.setColor(Color.WHITE);
            g2D.fillRect(0, 0, 80, 48);

            if (holdTet != null) {
                MinoStyle style = getMinoStyle();
                style.drawTetromino(g2D, 0, 0, 80, holdTet);
            }
        } else {
            g2D.setColor(Color.WHITE);
            g2D.fillRect(10 * MinoStyle.MINO_SIZE + 120, 0, 80, 48);

            if (holdTet != null) {
                MinoStyle style = getMinoStyle();
                style.drawTetromino(g2D, 10 * MinoStyle.MINO_SIZE + 120, 0, 80, holdTet);
            }

            try {
                queue.paint(g2D, 0, 0);

                garbageManager.paint(g2D, 110 + 10 * MINO_SIZE, 10 + 5 * MINO_SIZE);
            } catch (NoninvertibleTransformException ex) {
                ex.printStackTrace();
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
        t.setY(19);
        for (int i = 0; i < 2 && t.intersects(this); i++) {
            t.moveDown(-1);
        }

        lockDelayCnt = lockDelay;
        lastMove = -1;
        bigSpin = false;
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

        if (keycodes.contains(VK_SPACE)) {
            if (currentTet != null) {
                hardDrop();
            }
        } else if (keycodes.contains(VK_UP)) {
            if (currentTet != null) {
                sonicDrop();
            }
        } else if (keycodes.contains(VK_DOWN)) {
            if (currentTet != null) {
                softDrop();
            }
        }

        if (keycodes.contains(VK_C) && currentTet != null) {
            hold();
        }

        if (keycodes.contains(VK_Z)) {
            if (spinSystem == null) {
                System.err.println("No spin system installed, cannot rotate left");
            } else if (currentTet != null) {
                int prevY = currentTet.getY();
                spinSystem.rotateLeft(currentTet, this);
                bigSpin = currentTet.getY() - prevY == 2;
                lastMove = VK_Z;
            }
        }

        if (keycodes.contains(VK_X)) {
            if (spinSystem == null) {
                System.err.println("No spin system installed, cannot rotate right");
            } else if (currentTet != null) {
                int prevY = currentTet.getY();
                spinSystem.rotateRight(currentTet, this);
                bigSpin = currentTet.getY() - prevY == 2;
                lastMove = VK_X;
            }
        }

        if (keycodes.contains(VK_LEFT)
                && currentTet != null
                && !currentTet.intersects(this, -1, 0)) {
            currentTet.moveLeft();
            lastMove = VK_LEFT;
        }

        if (keycodes.contains(VK_RIGHT)
                && currentTet != null
                && !currentTet.intersects(this, 1, 0)) {
            currentTet.moveRight();
            lastMove = VK_RIGHT;
        }

        if (onLeft && !keycodes.isEmpty()) {
            StringBuilder message = new StringBuilder("ACTIONS");
            for (Integer keycode : keycodes) {
                message.append(keycode).append(" ");
            }

            ClientSocket.getConnection().send(message.toString().trim());
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
            lastMove = VK_DOWN;
        } else if (spinSystem.getType().equals("ARS")) {
            lock();
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
                            = GREY;
                }
            }
        }

        lockingTet = currentTet;
        currentTet = null;
        holdAvaliable = false;

        for (int i = 0; i < matrix.length; i++) {
            if (isLineFull(i)) {
                linesToClear.add(i);
            }
        }

        if (linesToClear.isEmpty()) {
            pauseAnimationCnt = standardARE + 1;
        } else {
            pauseAnimationCnt = lineClearDelay + 1;
        }
        lockFlashCnt = 5;

        if (++lockCnt == 10) {
            gravity = GRAVITY[++gravityNum];
            lockCnt = 0;
        }
    }

    /**
     * Adds a bag of tetrominos
     *
     * @param bag the bag of tetrominos to add
     */
    public void addBag(String bag) {
        queue.addBag(bag);
    }

    /**
     * Starts gameplay on this matrix.
     */
    public void startGame() {
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
        drawGhost = true;
        gravity = 0.015625;
        lineClearARE = 25;
        standardARE = 25;
        lineClearDelay = 40;
        linesToClear = new TreeSet<>();
        lockDelay = 30;
        lockDelayCnt = 30;
        lockFlashCnt = -1;
        lockingTet = null;
        pauseAnimationCnt = -1;
        previousY = -1;
        extraY = 0;
        extraYSpeed = 0;
        lastMove = -1;
        bigSpin = false;
        combo = -1;
        back2Back = false;
        garbageManager.reset();
        gravityCnt = 0;
        gravityNum = -1;
        lockCnt = 0;
    }

    /**
     * An internal level counter
     */
    private int lockCnt;

    /**
     * The entry in the gravity table currently on
     */
    private int gravityNum;

    /**
     * An array of the progression of gravity
     */
    private static final double[] GRAVITY = {
        0.015625, 0.0234375, 0.03125, 0.0390625, 0.046875, 0.0625, 0.125,
        0.1875, 0.25, 0.3125, 0.375, 0.4375, 0.5, 0.5625, 0.015625, 0.125,
        0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1, 2, 3, 4, 5, 4, 3, 20
    };

    /**
     * The amount of gravity per frame
     */
    private double gravity;

    /**
     * A counter for gravity
     */
    private double gravityCnt;

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
     * Called when player dies.
     */
    private void die() {
        if (currentTet != null) {
            int[][] temp = currentTet.getState();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (temp[i][j] > 0) {
                        matrix[i + currentTet.getY()][j + currentTet.getX()]
                                = temp[i][j];
                    }
                }
            }
            currentTet = null;
            holdAvaliable = false;
        }
        inGame = false;

        holdAvaliable = false;

        extraYSpeed = 0.1;

        if (sendGarbo != null) {
            sendGarbo.accept(-1);
        }
    }

    /**
     * Determines if this matrix is empty
     *
     * @return if this matrix is empty
     */
    private boolean isClear() {
        for (int[] row : matrix) {
            for (int i : row) {
                if (i != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Advances a frame.
     *
     * @param handler a DASHandler so I know what to do
     */
    public void advanceFrame(DASHandler handler) {
        if (lockingTet != null) {
            // lockFlashCnt >= 0
            if (lockFlashCnt > 0) {
                lockFlashCnt--;
            } else {
                int linesToSend = linesToSend();

                int[][] temp = lockingTet.getState();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (temp[i][j] > 0) {
                            matrix[i + lockingTet.getY()][j + lockingTet.getX()]
                                    = temp[i][j];
                        }
                    }
                }

                for (int i : linesToClear) {
                    clearLine(i);
                }

                if (isClear()) {
                    System.out.println("PC!");
                    linesToSend += 7;
                }

                lockingTet = null;
                lockFlashCnt = -1;

                int through = garbageManager.counterGarbage(linesToSend);
                if (through != 0) {
                    System.out.println("You sent " + through + " lines of garbage!");
                    if (sendGarbo != null) {
                        sendGarbo.accept(through);
                    }
                }

                placeGarbage();
            }
        }

        if (pauseAnimationCnt >= 0 && onLeft) {
            pauseAnimationCnt--;
        }

        if (pauseAnimationCnt == 0) {
            if (onLeft) {
                ClientSocket.getConnection().send("PAUSE0");
            }

            pause0(handler);
        }

        if (currentTet != null) {
            if (onLeft) {
                processGravity(handler);
            }

            if (currentTet.intersects(this, 0, 1)
                    && previousY == currentTet.getY()) {
                if (onLeft && lockDelayCnt == 0) {
                    lockDelayCnt = lockDelay;
                    ClientSocket.getConnection().clearSendQueue();
                    ClientSocket.getConnection().send("LOCK" + currentTet.getX()
                            + " " + currentTet.getY() + " "
                            + currentTet.getRotation());
                    lock();
                }
                
                if (lockDelay > 0) {
                    lockDelayCnt--;
                }
            } else {
                lockDelayCnt = lockDelay;
            }
        }

        if (currentTet == null) {
            previousY = -1;
        } else {
            previousY = currentTet.getY();
        }

        if (extraYSpeed != 0) {
            extraY += extraYSpeed;
            extraYSpeed += 0.1;

            if (extraY > 10000) {
                extraYSpeed = 0;
            }
        }
    }

    /**
     * Places garbage at the bottom of the matrix, randomizing the holes and
     * also notifying the opponent that this matrix just took garbage.
     */
    private void placeGarbage() {
        if (onLeft && !garbageManager.isEmpty()) {
            int total = 0;

            StringBuilder message = new StringBuilder("GL");
            System.out.println("Before: " + garbageManager);

            while (true) {
                int newG = garbageManager.peekGarbage();
                if (newG == 0) {
                    break;
                }
                
                if(total != 0 && (total += newG) >= 5) {
                    break;
                }
                garbageManager.pollGarbage();

                int hole = (int) (Math.random() * 10);
                for (int i = 0; i < newG; i++) {
                    if (Math.random() > 0.8) {
                        hole = (int) (Math.random() * 10);
                    }

                    addGarbage(hole);
                    message.append(hole).append(' ');
                }
            }

            ClientSocket.getConnection().send(message.toString());

            System.out.println("After: " + garbageManager);

            if (total != 0) {
                System.out.println("Oof! " + total + " lines of garbage!");
            }
        }
    }

    /**
     * Enters the next tetromino with the given parameters.
     *
     * @param left whether to rotate left
     * @param right whether to rotate right
     * @param hold whether to hold
     */
    private void enter(boolean left, boolean right, boolean hold) {
        currentTet = queue.removeTetromino();
        activate(currentTet);

        if (left && spinSystem != null) {
            // currentTet.rotateLeft();
            spinSystem.rotateLeft(currentTet, this);
        }
        if (right && spinSystem != null) {
            // currentTet.rotateRight();
            spinSystem.rotateRight(currentTet, this);
        }
        holdAvaliable = true;
        if (hold) {
            hold();
        }

        pauseAnimationCnt = -1;

        if (currentTet.intersects(this)) {
            System.out.println("Topped out");
            die();
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
        Arrays.fill(matrix[r], 0);
    }

    /**
     * Sets the gravity to the given integer
     *
     * @param gravity the gravity to use from now on
     */
    public void setGravity(double gravity) {
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
     * Determines how many lines should be sent to the opponent, given that the
     * tetromino is now locked down. This method does not take into account
     * perfect clears.
     *
     * @return how many lines should be sent
     */
    private int linesToSend() {
        int linesToSend = 0;
        boolean b2b;

        if (linesToClear.isEmpty()) {
            combo = -1;
            b2b = false;
        } else {
            combo++;
            b2b = back2Back;
            back2Back = linesToClear.size() == 4;
        }

        if (combo > 0) {
            int geg = combo + 1;
            if (geg > 10) {
                linesToSend += 5;
            } else if (geg > 7) {
                linesToSend += 4;
            } else if (geg > 5) {
                linesToSend += 3;
            } else if (geg > 3) {
                linesToSend += 2;
            } else {
                linesToSend++;
            }
        }

        boolean tSpin = false;

        // T-spin?                
        if (lockingTet instanceof TetT
                && (lastMove == VK_Z || lastMove == VK_X)) {
            TetT tee = (TetT) lockingTet;
            Point center = tee.getCenter();
            int x = tee.getX() + center.y, y = tee.getY() + center.x;
            int corners = 0;
            if (getBlock(y - 1, x + 1) != 0) {
                corners++;
            }
            if (getBlock(y + 1, x + 1) != 0) {
                corners++;
            }
            if (getBlock(y - 1, x - 1) != 0) {
                corners++;
            }
            if (getBlock(y + 1, x - 1) != 0) {
                corners++;
            }

            // corners can be 4
            if (corners >= 3) {
                // Ok, so T-spin
                if (!linesToClear.isEmpty()) {
                    back2Back = true;
                }
                // but mini?
                tSpin = bigSpin || tee.filledFaceCorners(this) == 2;
            }
        }

        switch (linesToClear.size()) {
            case 0:
                linesToSend = 0;
                break;
            case 1:
                if (tSpin) {
                    linesToSend += 2;
                }
                break;
            case 2:
                if (tSpin) {
                    linesToSend += 4;
                } else {
                    linesToSend++;
                }
                break;
            case 3:
                if (tSpin) {
                    linesToSend += 6;
                } else {
                    linesToSend += 2;
                }
                break;
            case 4:
                linesToSend += 4;
                break;
            default:
                assert false : "You cleared less than 0 lines or more than 4.";
        }

        if (b2b && back2Back) {
            linesToSend++;
        }

        return linesToSend;
    }

    /**
     * Called when the pause counter is 0. That means either enter the next
     * piece or clear some lines.
     *
     * @param handler the DAS Handler used
     */
    private void pause0(DASHandler handler) {
        if (linesToClear.isEmpty()) {
            if (onLeft) {
                boolean left = handler.isPressed(VK_Z),
                        right = handler.isPressed(VK_X),
                        hold = handler.isPressed(VK_C);
                enter(left, right, hold);
                String message = "ENTER";
                message += left ? "1" : "0";
                message += right ? "1" : "0";
                message += hold ? "1" : "0";
                ClientSocket.getConnection().send(message);
            }
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

    /**
     * Processes gravity on the piece
     *
     * @param handler the DAS Handler being used
     */
    private void processGravity(DASHandler handler) {
        if (gravity >= 1) {
            int i;
            for (i = 0; i < gravity
                    && !currentTet.intersects(this, 0, 1); i++) {
                currentTet.moveDown();
                lastMove = -1;
            }

            ClientSocket.getConnection().send("G" + i);
        } else {
            if (!handler.isPressed(VK_DOWN)
                    && !currentTet.intersects(this, 0, 1)) {
                gravityCnt += gravity;
                if (gravityCnt >= 1) {
                    currentTet.moveDown();
                    lastMove = -1;
                    gravityCnt = 0;

                    ClientSocket.getConnection().send("G1");
                }
            } else {
                gravityCnt = 0;
            }
        }
    }

    /**
     * Just an imitation
     */
    private class PaintableMatrix implements Paintable {

        @Override
        public void paint(Graphics2D g2D) {
            MinoStyle style = getMinoStyle();

            g2D.setStroke(new BasicStroke(1.0f));

            g2D.setColor(Color.BLACK);
            g2D.fillRect(0, (int) (19.5 * MINO_SIZE),
                    10 * MINO_SIZE, (int) (20.5 * MINO_SIZE));

            Color locked = new Color(0, 0, 0, 100);

            if (currentTet != null) {
                int addY = 0;
                while (currentTet != null
                        && !currentTet.intersects(TetrisMatrix.this, 0, addY + 1)) {
                    addY++;
                }

                int[][] state = currentTet.getState();

                if (drawGhost && addY != 0) {
                    g2D.setComposite(MinoStyle.TRANSLUCENT_COMPOSITE);
                    drawTetromino(state, style, g2D, currentTet.getX(),
                            currentTet.getY() + addY);
                    g2D.setComposite(AlphaComposite.SrcOver);
                }

                if (lockDelayCnt <= lockDelay) {
                    // System.out.println("lDC: " + lockDelayCnt 
                    //         + "\tlD: " + lockDelay);
                    double shade = 1 - ((double) lockDelayCnt) / lockDelay;
//                    System.out.println("shade: " + shade + " = " + lockDelayCnt
//                            + " / " + lockDelay);

                    if (shade * 100 > 255) {
                        System.out.println("shade bad");
                        System.out.println(lockDelayCnt + " " + lockDelay);
                    }
                    g2D.setColor(new Color(0, 0, 0, (int) (shade * 100)));

                    if (currentTet != null) {
                        for (int i = 0, x = currentTet.getX() * MINO_SIZE;
                                i < state.length && currentTet != null;
                                i++, x += MINO_SIZE) {
                            for (int j = 0, y = currentTet.getY() * MINO_SIZE;
                                    j < state[i].length && currentTet != null;
                                    j++, y += MINO_SIZE) {
                                if (state[j][i] > 0) {
                                    style.drawMino(g2D, x, y, state[j][i]);
                                    g2D.fillRect(x, y, MINO_SIZE, MINO_SIZE);
                                }
                            }
                        }
                    }
                } else {
                    drawTetromino(state, style, g2D,
                            currentTet.getX(), currentTet.getY());
                }
            }

            for (int i = 19, y = 19 * MINO_SIZE; i < matrix.length; i++, y += MINO_SIZE) {
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

            g2D.setColor(Color.WHITE);
            g2D.fillRect(0, 19 * MINO_SIZE, 10 * MINO_SIZE, MINO_SIZE / 2);
        }
    }

    /**
     * Draws a tetromino
     *
     * @param state the state of the tetromino
     * @param style the style to draw it in
     * @param g2D the Graphics2D object to use
     * @param startX the starting x coordinate
     * @param startY the starting y coordinate
     */
    private void drawTetromino(int[][] state, MinoStyle style,
            Graphics2D g2D, int startX, int startY) {
        for (int i = 0, x = startX * MINO_SIZE;
                i < state.length; i++, x += MINO_SIZE) {
            for (int j = 0, y = startY * MINO_SIZE;
                    j < state[i].length; j++, y += MINO_SIZE) {
                style.drawMino(g2D, x, y, state[j][i]);
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
     * @param onLeft whether this matrix should be on the left
     * @return a new ARS-style matrix.
     */
    public static TetrisMatrix generateARSMatrix(boolean onLeft) {
        TetrisMatrix output = new TetrisMatrix(onLeft);
        output.setSpinSystem(ARSSpinSystem.getSpinSystem());
        output.setTetrominoFactory(ARSTetrominoFactory.getTetrominoFactory());
        output.setMinoStyle(TGMMinoStyle.getMinoStyle());
        return output;
    }

    /**
     * Creates a new SRS-style matrix
     *
     * @param onLeft whether this matrix should be on the left
     * @return a new SRS-style matrix
     */
    public static TetrisMatrix generateSRSMatrix(boolean onLeft) {
        TetrisMatrix output = new TetrisMatrix(onLeft);
        output.setSpinSystem(SRSSpinSystem.getSpinSystem());
        output.setTetrominoFactory(SRSTetrominoFactory.getTetrominoFactory());
        output.setMinoStyle(SRSMinoStyle.getMinoStyle());
        return output;
    }

    /**
     * Makes the given matrix a ARS-style matrix
     *
     * @param matrix the matrix to convert
     */
    public static void setAsARSMatrix(TetrisMatrix matrix) {
        if (matrix == null) {
            return;
        }

        if (matrix.queue != null) {
            matrix.queue.clearQueue();
        }

        matrix.setSpinSystem(ARSSpinSystem.getSpinSystem());
        matrix.setTetrominoFactory(ARSTetrominoFactory.getTetrominoFactory());
        matrix.setMinoStyle(TGMMinoStyle.getMinoStyle());
    }

    /**
     * Makes the given matrix an SRS-style matrix
     *
     * @param matrix the matrix to convert
     */
    public static void setAsSRSMatrix(TetrisMatrix matrix) {
        if (matrix == null) {
            return;
        }

        if (matrix.queue != null) {
            matrix.queue.clearQueue();
        }

        matrix.setSpinSystem(SRSSpinSystem.getSpinSystem());
        matrix.setTetrominoFactory(SRSTetrominoFactory.getTetrominoFactory());
        matrix.setMinoStyle(SRSMinoStyle.getMinoStyle());
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

    /**
     * Adds a row of garbage to the bottom of the matrix.
     *
     * @param emptyColumn the column that is empty in the row of garbage.
     */
    public void addGarbage(int emptyColumn) {
        for (int i = 1; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, matrix[i - 1], 0, matrix[i].length);
        }

        for (int i = 0; i < matrix[matrix.length - 1].length; i++) {
            if (i == emptyColumn) {
                matrix[matrix.length - 1][i] = 0;
            } else {
                matrix[matrix.length - 1][i] = MinoStyle.GREY;
            }
        }

        if (linesToClear != null && !linesToClear.isEmpty()) {
            TreeSet<Integer> temp = linesToClear;
            linesToClear = new TreeSet<>();

            for (Integer i : temp) {
                linesToClear.add(i - 1);
            }
        }

        if (currentTet != null && currentTet.intersects(this)) {
            currentTet.moveDown(-1);
        }

        if (lockingTet != null && lockingTet.intersects(this)) {
            lockingTet.moveDown(-1);
        }
    }

    /**
     * A Consumer object that is called when garbage is to be sent.
     */
    private Consumer<Integer> sendGarbo = null;

    /**
     * Sets the Consumer object to call when garbage is sent.
     *
     * @param consumer the consumer to use from now on
     */
    public void setGarbageConsumer(Consumer<Integer> consumer) {
        sendGarbo = consumer;
    }

    /**
     * Queues garbage up for the queue
     *
     * @param lines the lines to queue up
     */
    public void queueGarbage(int lines) {
        if (lines != 0) {
            garbageManager.offerGarbage(lines);
        }
    }

    /**
     * Returns the amount of gravity this TetrisMatrix is using
     *
     * @return the amount of gravity this TetrisMatrix is using
     */
    public double getGravity() {
        return gravity;
    }

    /**
     * Sets the parameters in the given matrix to the type of the given number:
     * 0 means SRS, and 1 means ARS. Any other value will result in an error.
     *
     * @param matrix the matrix to configure
     * @param type the type of the matrix after this operation
     */
    public static void setMatrixAs(TetrisMatrix matrix, int type) {
        // System.out.println("Before:");
        // matrix.printAllInfo();
        switch (type) {
            case 0:
                TetrisMatrix.setAsSRSMatrix(matrix);
                break;
            case 1:
                TetrisMatrix.setAsARSMatrix(matrix);
                break;
            default:
                throw new IllegalArgumentException("Invalid parameter type with value " + type);
        }
        // System.out.println("After:");
        // matrix.printAllInfo();
    }

    /**
     * Attempts to print as much info about this object as possible.
     */
    public void printAllInfo() {
        System.out.println(toString());
        System.out.println("  Objects:");
        System.out.printf("    currentTet:%20s%n", currentTet);
        System.out.printf("    factory:%23s%n", factory == null ? "null"
                : factory.getClass().getSimpleName());
        System.out.printf("    garbageManager:%16s%n", garbageManager);
        System.out.printf("    holdTet:%22s%n", holdTet);
        System.out.printf("    linesToClear:%18s%n", linesToClear);
        System.out.printf("    lockingTet:%20s%n", lockingTet);
        System.out.println("    matrix:");
        for (int[] is : matrix) {
            System.out.print("    ");
            for (int i : is) {
                System.out.print(i);
            }
            System.out.println();
        }
        System.out.printf("    minoStyle:%21s%n", minoStyle == null ? "null"
                : minoStyle.getClass().getSimpleName());
        System.out.printf("    paintableMatrix:%15s%n", paintableMatrix);
        System.out.printf("    queue:%24s%n", queue);
        System.out.printf("    sendGarbo:%21s%n", sendGarbo);
        System.out.printf("    spinSystem:%20s%n", spinSystem == null ? "null"
                : spinSystem.getClass().getSimpleName());
        System.out.println("  Numbers:");
        System.out.println("    combo:             " + combo);
        System.out.println("    extraY:            " + extraY);
        System.out.println("    extraYSpeed:       " + extraYSpeed);
        System.out.println("    gravity:           " + gravity);
        System.out.println("    gravityCnt:        " + gravityCnt);
        System.out.println("    gravityNum:        " + gravityNum);
        System.out.println("    lastMove:          " + lastMove);
        System.out.println("    lineClearARE:      " + lineClearARE);
        System.out.println("    lineClearDelay:    " + lineClearDelay);
        System.out.println("    lockCnt:           " + lockCnt);
        System.out.println("    lockDelay:         " + lockDelay);
        System.out.println("    lockDelayCnt:      " + lockDelayCnt);
        System.out.println("    lockFlashCnt:      " + lockFlashCnt);
        System.out.println("    pauseAnimationCnt: " + pauseAnimationCnt);
        System.out.println("    previousY:         " + previousY);
        System.out.println("    standardARE:       " + standardARE);
        System.out.println("  Booleans:");
        System.out.println("    back2Back:     " + back2Back);
        System.out.println("    bigSpin:       " + bigSpin);
        System.out.println("    drawGhost:     " + drawGhost);
        System.out.println("    holdAvaliable: " + holdAvaliable);
        System.out.println("    inGame:        " + inGame);
        System.out.println("    onLeft:        " + onLeft);
    }

    /**
     * Returns the GarbageManager internally stored in this TetrisMatrix
     *
     * @return the GarbageManager internally stored in this TetrisMatrix
     */
    public GarbageManager getGarbageManager() {
        return garbageManager;
    }
}
