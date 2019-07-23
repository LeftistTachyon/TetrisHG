package com.leftisttachyon.tetris;

import static com.leftisttachyon.tetris.MinoStyle.MINO_SIZE;
import com.leftisttachyon.util.Paintable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Deque;
import java.util.LinkedList;

/**
 * A class that deals with adding and countering garbage.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class GarbageManager implements Paintable {

    /**
     * An internal queue of garbage
     */
    private Deque<Integer> garbageQueue;

    /**
     * Creates a new GarbageManager.
     */
    public GarbageManager() {
        garbageQueue = new LinkedList<>();
    }

    @Override
    public void paint(Graphics2D g2D) {
        int total = 0;
        total = garbageQueue.stream().map((i) -> i).reduce(total, Integer::sum);
        int div = total / 20, rem = total % 20;

        int yStep = MINO_SIZE * 3 / 4;
        for (int i = 0, y = yStep * 19; i < 20; i++, y -= yStep) {
            if (div >= 3) {
                g2D.setColor(Color.RED);
            } else {
                int color;
                if (i < rem) {
                    color = div + 1;
                } else {
                    color = div;
                }

                if (color <= 0) {
                    continue;
                }
                switch (color) {
                    case 0:
                        break;
                    case 1:
                        g2D.setColor(Color.YELLOW);
                        break;
                    case 2:
                        g2D.setColor(Color.ORANGE);
                        break;
                    default:
                        g2D.setColor(Color.RED);
                        break;
                }
            }

            g2D.fillRect(0, y, 10, yStep);
        }

        g2D.setColor(Color.BLACK);
        g2D.drawRect(0, 0, 10, yStep * 20);
        for (int i = 0, y = yStep * 19; i < 19; i++, y -= yStep) {
            g2D.drawLine(0, y, 10, y);
        }
    }

    /**
     * Determines whether the queue is empty
     *
     * @return whether the queue is empty
     */
    public boolean isEmpty() {
        return garbageQueue.isEmpty();
    }

    /**
     * Queues garbage to drop onto your board.
     *
     * @param lines the amount of lines to add to the queue
     * @return whether the operation was successful
     */
    public boolean offerGarbage(int lines) {
        return garbageQueue.add(lines);
    }

    /**
     * Returns the next item in the queue and removes it
     *
     * @return the next item in the queue and removes it
     */
    public int pollGarbage() {
        Integer temp = garbageQueue.poll();
        return temp == null ? 0 : temp;
    }

    /**
     * Returns the next item in the queue without removing it
     *
     * @return the next item in the queue without removing it
     */
    public int peekGarbage() {
        Integer temp = garbageQueue.peek();
        return temp == null ? 0 : temp;
    }
    
    public int counterGarbage(int toCounter) {
        while(toCounter > 0 && !garbageQueue.isEmpty()) {
            int countered = garbageQueue.remove() - toCounter;
            if (countered < 0) {
                toCounter = -countered;
            } else {
                toCounter = 0;
                garbageQueue.addFirst(countered);
            }
        }
        
        return toCounter;
    }

    /**
     * Resets this GarbageManager to be at the starting state.
     */
    public void reset() {
        garbageQueue = new LinkedList<>();
    }
}
