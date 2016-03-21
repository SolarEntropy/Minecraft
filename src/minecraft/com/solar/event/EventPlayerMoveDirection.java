package com.solar.event;

public class EventPlayerMoveDirection extends Event {

    private float strafe, forward;

    public EventPlayerMoveDirection(Type type, float strafe, float forward) {
        super(type);
        this.strafe = strafe;
        this.forward = forward;
    }

    public float strafe() {
        return strafe;
    }

    public void strafe(float strafe) {
        this.strafe = strafe;
    }

    public float forward() {
        return forward;
    }

    public void forward(float forward) {
        this.forward = forward;
    }

}
