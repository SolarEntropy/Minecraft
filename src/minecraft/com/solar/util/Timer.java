package com.solar.util;

public class Timer {

    private long time = 0;

    public void reset() {
        time = System.currentTimeMillis();
    }

    public boolean isTime(long time) {
        return (System.currentTimeMillis() - this.time) > time;
    }
}
