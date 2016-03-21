package com.solar.gui.font;

public class Char {

	private int id, x, y, w, h, xo, yo, a;

	public Char(int id, int x, int y, int w, int h, int xo, int yo, int a) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.xo = xo;
		this.yo = yo;
		this.a = a;
	}

	public int id() {
		return id;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int w() {
		return w;
	}

	public int h() {
		return h;
	}

	public int xo() {
		return xo;
	}

	public int yo() {
		return yo;
	}

	public int a() {
		return a;
	}
	
}
