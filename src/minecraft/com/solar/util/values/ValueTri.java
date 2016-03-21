package com.solar.util.values;

public class ValueTri<T, U, V> {

    private T t;
    private U u;
    private V v;

    public ValueTri() {}

    public ValueTri(T t, U u, V v) {
    	t(t);
    	u(u);
    	v(v);
    }
    
    public T t() {
        return t;
    }
    
    public U u() {
    	return u;
    }
    
    public V v() {
    	return v;
    }

    public void t(T t) {
        this.t = t;
    }
    
    public void u(U u) {
    	this.u = u;
    }
    
    public void v(V v) {
    	this.v = v;
    }
}
