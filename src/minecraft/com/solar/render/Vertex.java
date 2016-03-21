package com.solar.render;

import com.solar.util.ColorUtil;

public class Vertex {

	private float x, y, z, texX, texY;
	private float[] rgba = new float[4];
	private int color;
	
	public Vertex(double x, double y, double z, float texX, float texY, int color) {
		this((float) x, (float) y, (float) z, texX, texY, color);
	}
	
	public Vertex(float x, float y, float z, float texX, float texY, int color) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.texX = texX;
		this.texY = texY;
		this.color = color;
		ColorUtil.storeColor(color, rgba);
	}
	
	public Vertex(double x, double y, double z, int color) {
		this(x, y, z, 0, 0, color);
	}
	
	public Vertex(float x, float y, float z, int color) {
		this(x, y, z, 0, 0, color);
	}
	
	public float x() {
		return x;
	}
	
	public float y() {
		return y;
	}
	
	public float z() {
		return z;
	}
	
	public float texX() {
		return texX;
	}
	
	public float texY() {
		return texY;
	}
	
	public float[] rgba() {
		return rgba;
	}
	
	public int color() {
		return color;
	}
}
