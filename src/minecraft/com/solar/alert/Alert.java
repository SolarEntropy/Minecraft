package com.solar.alert;

import com.solar.Solar;
import com.solar.render.Render;
import com.solar.util.Timer;

public class Alert {

    private String message;
    private long time;
    private Timer timer = new Timer();

    public Alert(String message, long time) {
        this.message = message;
        this.time = time;
        timer.reset();
    }

    public void update() {
        if (timer.isTime(time)) {
            timer.reset();
            Solar.getInstance().alertManager().remove(this);
        }
    }

    public String message() {
        return message;
    }
}
