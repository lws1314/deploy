package com.ftp.deploy.config.entity;

/**
 * @author - ZEROES
 */

public enum Digit {

    /**
     * 0
     */
    ZERO(0),

    /**
     * 1
     */
    ONE(1),

    /**
     * 2
     */
    TWO(2),
    /**
     * 3
     */
    THREE(3),
    /**
     * 4
     */
    FOUR(4),
    /**
     * 5
     */
    FIVE(5),
    /**
     * 6
     */
    SIX(6),
    /**
     * 7
     */
    SEVEN(7),
    /**
     * 8
     */
    EIGHT(8),
    /**
     * 9
     */
    NINE(9);

    private int value;

    Digit(int value) {
        this.value = value;
    }

    public int getInt() {
        return value;
    }
    public short getShort() {
        return (short) value;
    }
    public long getLong() {
        return (long) value;
    }
}
