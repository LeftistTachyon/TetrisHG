package com.leftisttachyon.tetris.ui;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A KeyListener that controls DAS.
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class DASHandler extends KeyAdapter {

    /**
     * A HashMap that stores the pressed keys
     */
    private HashMap<Integer, int[]> pressed;

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
        if (keys.containsKey(keyCode) && pressed.get(keyCode)[1] == -2) {
            pressed.put(keyCode, new int[]{1, 0});
            
            if (keyCode == VK_LEFT && isPressed(VK_RIGHT)) {
                pressed.put(VK_RIGHT, new int[]{2, -1});
            }
            if (keyCode == VK_RIGHT && isPressed(VK_LEFT)) {
                pressed.put(VK_LEFT, new int[]{2, -1});
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keys.containsKey(keyCode)) {
            pressed.put(keyCode, new int[]{0, -2});
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
     * -1 means no repeat)
     */
    public void setListener(int keycode, Point preferences) {
        keys.put(keycode, preferences);
        pressed.put(keycode, new int[]{0, -2});
    }

    /**
     * Advances a frame internally and determines which actions (conveyed by
     * their respected keycodes) should be performed.
     *
     * @return a HashSet of actions to perform
     */
    public HashSet<Integer> advanceFrame() {
        // System.out.println("FS: " + pressed);

        HashSet<Integer> output = new HashSet<>();
        for (Map.Entry<Integer, int[]> entry : pressed.entrySet()) {
            int value0 = entry.getValue()[0], value1 = entry.getValue()[1];
            // System.out.println(entry.getKey() + ": " + value0 + ", " + value1);
            if (value1 > 0) {
                value1--;
            }
            if (value1 == 0) {
                int temp;
                Point pref = keys.get(entry.getKey());
                if (value0 == 1) {
                    value0 = 2;
                    if (pref.x == -1) {
                        temp = -1;
                    } else {
                        temp = pref.x + 1;
                    }
                } else {
                    // == 2
                    if (pref.y == -1) {
                        temp = -1;
                    } else {
                        temp = pref.y + 1;
                    }
                }
                entry.setValue(new int[]{value0, temp});
                output.add(entry.getKey());
            } else {
                entry.setValue(new int[]{value0, value1});
            }
        }

        // if(!output.isEmpty()) System.out.println(output);
        // System.out.println("FE: " + pressed);
        return output;
    }

    /**
     * Determines whether a key is being pressed or not.
     *
     * @param keycode the keycode of the key that is being investigated
     * @return whether the given key is being pressed
     */
    public boolean isPressed(int keycode) {
        /*int[] array = pressed.get(keycode);
        if (array != null) {
            System.out.println(keycode + ": " + array[0] + ", " + array[1]);
        }
        System.out.println(pressed.containsKey(keycode) && pressed.get(keycode)[0] > 0);*/

        return pressed.containsKey(keycode) && pressed.get(keycode)[0] > 0;
    }
}
