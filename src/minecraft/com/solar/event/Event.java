package com.solar.event;

public class Event {

    public static enum Type {
        PRE(false),
        POST(true),
        RENDER2D(false),
        RENDER3D(false);
    	
    	boolean reverse;
    	
    	Type(boolean reverse) {
    		this.reverse = reverse;
    	}
    	
    	public boolean reverse() {
    		return reverse;
    	}
    }

    private Type type;
    private boolean cancelled;

    public Event(Type type) {
        this.type = type;
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Type type() {
        return type;
    }

    public void type(Type type) {
        this.type = type;
    }

}
