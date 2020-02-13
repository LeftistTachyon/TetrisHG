package com.github.leftisttachyon.tetris.tetrominos.nes;

import com.github.leftisttachyon.tetris.SpinSystem;
import com.github.leftisttachyon.tetris.TetrisMatrix;

/**
 * The SpinSystem for the Nintendo Spin System
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class NESSpinSystem extends SpinSystem<NESTet> {

    /**
     * No instantiation for you!
     */
    private NESSpinSystem() {
    }

    /**
     * The one and only
     */
    private static final NESSpinSystem SINGLETON = new NESSpinSystem();

    /**
     * Returns an instance of an NESSpinSystem
     *
     * @return an instance of an NESSpinSystem
     */
    public static NESSpinSystem getSpinSystem() {
        return SINGLETON;
    }

    @Override
    public void rotateRight(NESTet t, TetrisMatrix m) {
        if (t == null) {
            throw new IllegalArgumentException("Tried to rotate a null piece using NES.");
        }

        t.rotateRight();
        if (t.intersects(m)) {
            t.rotateLeft();
        }
    }

    @Override
    public void rotateLeft(NESTet t, TetrisMatrix m) {
        if (t == null) {
            throw new IllegalArgumentException("Tried to rotate a null piece using NES.");
        }

        t.rotateLeft();
        if (t.intersects(m)) {
            t.rotateRight();
        }
    }

    @Override
    public String getType() {
        return "NES";
    }

}
