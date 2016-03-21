package com.solar.util.values;

public class ValueDuo<T, U> {

    private T t;
    private U u;

    public ValueDuo() {}

    public ValueDuo(T t, U u) {
    	t(t);
    	u(u);
    }
    
    public T t() {
        return t;
    }
    
    public U u() {
    	return u;
    }

    public void t(T t) {
        this.t = t;
    }
    
    public void u(U u) {
    	this.u = u;
    }
}
