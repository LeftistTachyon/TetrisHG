package com.leftisttachyon.tetris;

import com.leftisttachyon.tetris.tetrominos.Tetromino;
import com.leftisttachyon.tetris.tetrominos.TetrominoFactory;
import com.leftisttachyon.util.Paintable;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A queue of tetrominos that control the bag randomization system as well as
 * the piece preview.
 *
 * @author Jed Wang
 * @param <T> the type of tetromino in the queue
 */
public class TetQueue<T extends Tetromino> implements Paintable {

    /**
     * The internal queue
     */
    private Queue<T> tetQueue;

    /**
     * The TetrominoFactory to use to create the tetrominos
     */
    private TetrominoFactory<T> tf;

    /**
     * The amount of pieces you can see in the outlook
     */
    private final int outlook;

    /**
     * Stores whether this queue, when drawn, should lean towards the left or
     * right.<br>
     * <code>
     * Left&nbsp;&nbsp;|&nbsp;&nbsp;Right<br>
     * ____&nbsp;&nbsp;|&nbsp;&nbsp;____<br>
     * |&nbsp;&nbsp;|&nbsp;&nbsp;|&nbsp;&nbsp;|&nbsp;&nbsp;|<br>
     * &nbsp;|&nbsp;|&nbsp;&nbsp;|&nbsp;&nbsp;|&nbsp;|<br>
     * &nbsp;|&nbsp;|&nbsp;&nbsp;|&nbsp;&nbsp;|&nbsp;|<br>
     * </code>
     */
    private boolean isLeft;

    /**
     * Creates a default TetQueue, with 5 pieces in the outlook.
     *
     * @see #TetQueue(int)
     */
    public TetQueue() {
        this(5);
    }

    /**
     * Creates a new TetQueue with the given piece outlook
     *
     * @param outlook the number of pieces to be seen in the outlook.
     */
    public TetQueue(int outlook) {
        tetQueue = new LinkedList<>();
        this.outlook = outlook;
        isLeft = true;
        tf = null;
    }

    @Override
    public void paint(Graphics2D g2D) {
    }

    /**
     * Adds a new bag of tetrominos generated from the given TetrominoFactory
     *
     * @see TetrominoFactory#createRandomBag()
     */
    public void addBag() {
        tetQueue.addAll(tf.createRandomBag());
    }

    /**
     * Adds a new bag of tetrominos as per the given String using the given
     * TetrominoFactory.
     *
     * @param bag the String representation of the bag to add
     * @see TetrominoFactory#createBagOf(java.lang.String)
     */
    public void addBag(String bag) {
        tetQueue.addAll(tf.createBagOf(bag));
    }

    /**
     * Adds a tetromino to the queue
     *
     * @param t the tetromino to add to the queue
     */
    void addTetromino(T t) {
        tetQueue.add(t);
    }

    /**
     * Removes the next tetromino from the queue. If the queue is empty, this
     * method throws an exception.
     *
     * @return the tetromino removed from the head of this queue
     */
    public T removeTetromino() {
        T output = tetQueue.remove();
        if (tetQueue.size() < outlook) {
            addBag();
        }
        return output;
    }

    /**
     * Sets the TetrominoFactory to use for tetromino generation
     *
     * @param tf the TetrominoFactory to use for tetromino generation
     */
    public void setTetrominoFactory(TetrominoFactory<T> tf) {
        this.tf = tf;
    }

    /**
     * Returns the currently used TetrominoFactory being used for tetromino
     * generation
     *
     * @return the currently used TetrominoFactory being used for tetromino
     * generation
     */
    public TetrominoFactory<T> getTetrominoFactory() {
        return tf;
    }
    
    /**
     * Clears all tetrominos from this queue.
     */
    public void clearQueue() {
        tetQueue.clear();
    }
}
