package com.leftisttachyon.tetris.ui;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A KeyListener that controls DAS.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class DASHandler extends KeyAdapter {

    /**
     * A HashMap that stores the pressed keys
     */
    private HashMap<Integer, Integer> pressed;

    /**
     * A HashMap that stores the DAS settings for each key
     */
    private HashMap<Integer, Point> keys;

    /**
     * Creates a new DASHandler.
     */
    public DASHandler() {
        pressed = new HashMap<>();
        keys = new HashMap<>();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keys.containsKey(keyCode) && pressed.get(keyCode) == -1) {
            pressed.put(keyCode, keys.get(keyCode).x);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keys.containsKey(keyCode)) {
            pressed.put(keyCode, -1);
        }
    }

    /**
     * "Adds" a "listener" to a certain key. In reality it just pays attention
     * to them now that you care.
     *
     * @param keycode the key to listen to
     * @param preferences a Point object that stores the preferences for the DAS
     * settings: the {@code x} portion represents the initial delay and the
     * {@code y} portion represents how many frames until it repeats (a value of
     * 0 means no repeat)
     */
    public void addListener(int keycode, Point preferences) {
        keys.put(keycode, preferences);
        pressed.put(keycode, -1);
    }

    /**
     * Advances a frame internally and determines which actions (conveyed by
     * their respected keycodes) should be performed.
     *
     * @return a HashSet of actions to perform
     */
    public HashSet<Integer> advanceFrame() {
        HashSet<Integer> output = new HashSet<>();
        for (Map.Entry<Integer, Integer> entry : pressed.entrySet()) {
            entry.setValue(entry.getValue() - 1);
            if (entry.getValue() == 0) {
                entry.setValue(keys.get(entry.getKey()).y);
                output.add(entry.getKey());
            }
        }
        return output;
    }
}
