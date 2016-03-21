package com.solar.util.values;

public class ValueSin<T> {

    private T t;

    public ValueSin() {}

    public ValueSin(T t) {
        t(t);
    }
    
    public T t() {
        return t;
    }
    
    public void t(T t) {
        this.t = t;
    }
}
