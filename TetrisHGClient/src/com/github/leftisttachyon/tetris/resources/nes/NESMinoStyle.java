package com.github.leftisttachyon.tetris.resources.nes;

import com.github.leftisttachyon.tetris.ImageMinoStyle;

/**
 * The NES style of minos
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class NESMinoStyle extends ImageMinoStyle {
    /**
     * No instantiation for you!
     */
    private NESMinoStyle() {
        super();
    }
    
    /**
     * Returns an instance of a NESMinoStyle
     *
     * @return an instance of a NESMinoStyle
     */
    public static NESMinoStyle getMinoStyle() {
        return SINGLETON;
    }
    
    /**
     * The one and only!
     */
    private static final NESMinoStyle SINGLETON = new NESMinoStyle();

    @Override
    protected String getPath(int color) {
        switch (color) {
            case PURPLE:
            case YELLOW:
            case CYAN:
                return "/com/github/leftisttachyon/tetris/resources/nes/white.png";
            case ORANGE:
            case RED:
                return "/com/github/leftisttachyon/tetris/resources/nes/red.png";
            case BLUE:
            case GREEN:
                return "/com/github/leftisttachyon/tetris/resources/nes/blue.png";
            case GREY:
                return "/com/github/leftisttachyon/tetris/resources/nes/grey.png";
            default:
                return null;
        }
    }
    
}