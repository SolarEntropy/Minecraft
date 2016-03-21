package com.solar.util;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

import net.minecraft.util.math.Vec3d;

public class MathUtil {

	public static float getRotation(double x1, double y1, double x2, double y2) {
		return getRotation((float) x1, (float) y1, (float) x2, (float) y2);
	}
	
	public static float getRotation(float x1, float y1, float x2, float y2) {
		return (float) toDegrees(atan2(y1 - y2, x1 - x2));
	}
	
	public static Vector2f getRotation(Vec3d from, Vec3d to) {
		Vec3d difference = from.subtract(to);
		Vector2f rotation = new Vector2f((float) toDegrees(atan2(difference.zCoord, difference.xCoord)), (float) toDegrees(atan2(difference.yCoord, sqrt(pow(difference.xCoord, 2) + pow(difference.zCoord, 2)))));
		return rotation;
	}
	
	public static Vec3d getRotationVec(float yaw, float pitch) {
		float xzLen = (float) (cos(pitch));
		float x = (float) (xzLen * cos(yaw));
		float y = (float) (sin(pitch));
		float z = (float) (xzLen * sin(-yaw));
		return new Vec3d(x,  y, z);
	}
	
	public static float getDistance(double x1, double y1, double x2, double y2) {
		return getDistance((float) x1, (float) y1, (float) x2, (float) y2);
	}
	
	public static float getDistance(float x1, float y1, float x2, float y2) {
		float hor = x2 - x1;
		float ver = y2 - y1;
		return (float) hypot(hor, ver);
	}
	
	public static float getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return getDistance((float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2);
	}
	
	public static float getDistance(float x1, float y1, float z1, float x2, float y2, float z2) {
		return getDistance(new Vec3d(x1, y1, z1), new Vec3d(x2, y2, z2));
	}
	
	public static float getDistance(Vec3d from, Vec3d to) {
		return (float) from.distanceTo(to);
	}
	
	public static float interpolate(double from, double to) {
		return interpolate((float) from, (float) to);
	}
	
	public static float interpolate(float from, float to) {
		return ((((to - from) % 360f) + 540f) % 360f) - 180f;
	}
	
	public static float increment(double val, double inc) {
		return increment((float) val, (float) inc);
	}
	
	public static float increment(float val, float inc) {
		return round(val * (1 / inc)) / (1 / inc);
	}
	
	public static float getPartial(double val, double prev, float partial) {
		return getPartial((float) val, (float) prev, partial);
	}
	
	public static float getPartial(float val, float prev, float partial) {
		return prev + (val - prev) * partial;
	}
	
	public static Vec3d getPartial(double x, double prevX, double y, double prevY, double z, double prevZ, float partial) {
		return getPartial((float) x, (float) prevX, (float) y, (float) prevY, (float) z, (float) prevZ, partial);
	}
	
	public static Vec3d getPartial(float x, float prevX, float y, float prevY, float z, float prevZ, float partial) {
		return new Vec3d(getPartial(x, prevX, partial),
				getPartial(y, prevY, partial),
				getPartial(z, prevZ, partial));
	}
	
	public static float getPartialInc(double val, double prev, float partial) {
		return getPartialInc((float) val, (float) prev, partial);
	}
	
	public static float getPartialInc(float val, float prev, float partial) {
		return (val - prev) * partial;
	}
	
	public static Vec3d to2D(Vec3d vec) {
		return to2D(vec.xCoord, vec.yCoord, vec.zCoord);
	}
	
	public static Vec3d to2D(double x, double y, double z) {
		return to2D((float) x, (float) y, (float) z);
	}
	
	public static Vec3d to2D(float x, float y, float z) {
		FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);

        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);

        if (result) {
            return new Vec3d(screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2));
        }
        return null;
	}
	
}
