package com.leftisttachyon.tetris.tetrominos.srs;

import com.leftisttachyon.tetris.TetrisMatrix;
import com.leftisttachyon.tetris.SpinSystem;
import static com.leftisttachyon.tetris.tetrominos.Tetromino.*;

/**
 * The SpinSystem object for SRS.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class SRSSpinSystem extends SpinSystem<SRSTet> {

    /**
     * The only object to be created
     */
    private static final SRSSpinSystem SINGLETON = new SRSSpinSystem();

    /**
     * Returns an instance of an SRSSpinSystem
     *
     * @return an instance of an SRSSpinSystem
     */
    public static SRSSpinSystem getSpinSystem() {
        return SINGLETON;
    }

    /**
     * No creation for you!
     */
    private SRSSpinSystem() {
    }

    @Override
    public void rotateRight(SRSTet t, TetrisMatrix m) {
        t.rotateRight();
        if (!t.intersects(m)) {
            return;
        }

        if (t instanceof SRS_I) {
            switch (t.getRotation()) {
                case UP:
                    // +1, 0
                    if (checkKick(t, m, 1, 0)) {
                        return;
                    }
                    // -2, 0
                    if (checkKick(t, m, -2, 0)) {
                        return;
                    }
                    // +1, -2
                    if (checkKick(t, m, 1, -2)) {
                        return;
                    }
                    // -2, +1
                    if (checkKick(t, m, -2, 1)) {
                        return;
                    }
                    break;
                case RIGHT:
                    // -2, 0
                    if (checkKick(t, m, -2, 0)) {
                        return;
                    }
                    // +1, 0
                    if (checkKick(t, m, 1, 0)) {
                        return;
                    }
                    // -2, -1
                    if (checkKick(t, m, -2, -1)) {
                        return;
                    }
                    // +1, +2
                    if (checkKick(t, m, 1, 2)) {
                        return;
                    }
                    break;
                case DOWN:
                    // -1, 0
                    if (checkKick(t, m, -1, 0)) {
                        return;
                    }
                    // +2, 0
                    if (checkKick(t, m, 2, 0)) {
                        return;
                    }
                    // -1, +2
                    if (checkKick(t, m, -1, 2)) {
                        return;
                    }
                    // +2, -1
                    if (checkKick(t, m, 2, -1)) {
                        return;
                    }
                    break;
                case LEFT:
                    // +2, 0
                    if (checkKick(t, m, 2, 0)) {
                        return;
                    }
                    // -1, 0
                    if (checkKick(t, m, -1, 0)) {
                        return;
                    }
                    // +2, +1
                    if (checkKick(t, m, 2, 1)) {
                        return;
                    }
                    // -1, -2
                    if (checkKick(t, m, -1, -2)) {
                        return;
                    }
                    break;
            }

            t.rotateLeft();
        } else {
            switch (t.getRotation()) {
                case UP:
                    // -1, 0
                    if (checkKick(t, m, -1, 0)) {
                        return;
                    }
                    // -1, -1
                    if (checkKick(t, m, -1, -1)) {
                        return;
                    }
                    // 0, +2
                    if (checkKick(t, m, 0, 2)) {
                        return;
                    }
                    // -1, +2
                    if (checkKick(t, m, -1, 2)) {
                        return;
                    }
                    break;
                case RIGHT:
                    // -1, 0
                    if (checkKick(t, m, -1, 0)) {
                        return;
                    }
                    // -1, +1
                    if (checkKick(t, m, -1, 1)) {
                        return;
                    }
                    // 0, -2
                    if (checkKick(t, m, 0, -2)) {
                        return;
                    }
                    // -1, -2
                    if (checkKick(t, m, -1, -2)) {
                        return;
                    }
                    break;
                case DOWN:
                    // +1, 0
                    if (checkKick(t, m, 1, 0)) {
                        return;
                    }
                    // +1, -1
                    if (checkKick(t, m, 1, -1)) {
                        return;
                    }
                    // 0, +2
                    if (checkKick(t, m, 0, 2)) {
                        return;
                    }
                    // +1, +2
                    if (checkKick(t, m, 1, 2)) {
                        return;
                    }
                    break;
                case LEFT:
                    // +1, 0
                    if (checkKick(t, m, 1, 0)) {
                        return;
                    }
                    // +1, +1
                    if (checkKick(t, m, 1, 1)) {
                        return;
                    }
                    // 0, -2
                    if (checkKick(t, m, 0, -2)) {
                        return;
                    }
                    // +1, -2
                    if (checkKick(t, m, 1, -2)) {
                        return;
                    }
                    break;
            }

            t.rotateLeft();
        }
    }

    @Override
    public void rotateLeft(SRSTet t, TetrisMatrix m) {
        t.rotateLeft();
        if (!t.intersects(m)) {
            return;
        }

        if (t instanceof SRS_I) {
            switch (t.getRotation()) {
                case UP:
                    // +2, 0
                    if (checkKick(t, m, 2, 0)) {
                        return;
                    }
                    // -1, 0
                    if (checkKick(t, m, -1, 0)) {
                        return;
                    }
                    // +2, +1
                    if (checkKick(t, m, 2, 1)) {
                        return;
                    }
                    // -1, -2
                    if (checkKick(t, m, -1, -2)) {
                        return;
                    }
                    break;
                case RIGHT:
                    // +1, 0
                    if (checkKick(t, m, 1, 0)) {
                        return;
                    }
                    // -2, 0
                    if (checkKick(t, m, -2, 0)) {
                        return;
                    }
                    // +1, -2
                    if (checkKick(t, m, 1, -2)) {
                        return;
                    }
                    // -2, +1
                    if (checkKick(t, m, -2, 1)) {
                        return;
                    }
                    break;
                case DOWN:
                    // -2, 0
                    if (checkKick(t, m, -2, 0)) {
                        return;
                    }
                    // +1, 0
                    if (checkKick(t, m, 1, 0)) {
                        return;
                    }
                    // -2, -1
                    if (checkKick(t, m, -2, -1)) {
                        return;
                    }
                    // +1, +2
                    if (checkKick(t, m, 1, 2)) {
                        return;
                    }
                    break;
                case LEFT:
                    // -1, 0
                    if (checkKick(t, m, -1, 0)) {
                        return;
                    }
                    // +2, 0
                    if (checkKick(t, m, 2, 0)) {
                        return;
                    }
                    // -1, +2
                    if (checkKick(t, m, -1, 2)) {
                        return;
                    }
                    // +2, -1
                    if (checkKick(t, m, 2, -1)) {
                        return;
                    }
                    break;
            }

            t.rotateRight();
        } else {
            switch (t.getRotation()) {
                case UP:
                    // +1, 0
                    if (checkKick(t, m, 1, 0)) {
                        return;
                    }
                    // +1, -1
                    if (checkKick(t, m, 1, -1)) {
                        return;
                    }
                    // 0, +2
                    if (checkKick(t, m, 0, 2)) {
                        return;
                    }
                    // +1, +2
                    if (checkKick(t, m, 1, 2)) {
                        return;
                    }
                    break;
                case RIGHT:
                    // -1, 0
                    if (checkKick(t, m, -1, 0)) {
                        return;
                    }
                    // -1, +1
                    if (checkKick(t, m, -1, 1)) {
                        return;
                    }
                    // 0, -2
                    if (checkKick(t, m, 0, -2)) {
                        return;
                    }
                    // -1, -2
                    if (checkKick(t, m, -1, -2)) {
                        return;
                    }
                    break;
                case DOWN:
                    // -1, 0
                    if (checkKick(t, m, -1, 0)) {
                        return;
                    }
                    // -1, -1
                    if (checkKick(t, m, -1, -1)) {
                        return;
                    }
                    // 0, +2
                    if (checkKick(t, m, 0, 2)) {
                        return;
                    }
                    // -1, +2
                    if (checkKick(t, m, -1, 2)) {
                        return;
                    }
                    break;
                case LEFT:
                    // +1, 0
                    if (checkKick(t, m, 1, 0)) {
                        return;
                    }
                    // +1, +1
                    if (checkKick(t, m, 1, 1)) {
                        return;
                    }
                    // 0, -2
                    if (checkKick(t, m, 0, -2)) {
                        return;
                    }
                    // +1, -2
                    if (checkKick(t, m, 1, -2)) {
                        return;
                    }
                    break;
            }

            t.rotateRight();
        }
    }

}
