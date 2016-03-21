package com.solar.render;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPointSize;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Render {
	
	private static Minecraft mc = Minecraft.getMinecraft();

	public static void point(float x, float y, int color, float size) {
		glLineWidth(size);
		glPointSize(size);
		
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_POINT_SMOOTH);
		GlStateManager.disableTexture2D();
		
		render(GL_POINTS, new Vertex[] {
				new Vertex(x, y, 0, color)
		});
		
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_POINT_SMOOTH);
		GlStateManager.enableTexture2D();
	}
	
	public static void line(float x1, float y1, float x2, float y2, int color, float size, float originX, float originY, float angle) {
		GlStateManager.pushMatrix();
		float transX = x1 + originX;
		float transY = y1 + originY;
		GlStateManager.translate(transX, transY, 0);
		GlStateManager.rotate(angle, 0, 0, 1);
		GlStateManager.translate(-transX, -transY, 0);
		line(x1, y1, x2, y2, color, size);
		GlStateManager.popMatrix();
	}
	
	public static void line(double x1, double y1, double x2, double y2, int color, float size) {
		line((float) x1, (float) y1, (float) x2, (float) y2, color, size);
	}
	
	public static void line(float x1, float y1, float x2, float y2, int color, float size) {
		glLineWidth(size);
		glPointSize(size);
		
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_POINT_SMOOTH);
		GlStateManager.disableTexture2D();
		
		render(GL_LINE_STRIP, new Vertex[] {
				new Vertex(x1, y1, 0, color),
				new Vertex(x2, y2, 0, color)
		});
		
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_POINT_SMOOTH);
		GlStateManager.enableTexture2D();
	}
	
	public static void triangle(float x1, float y1, float x2, float y2, float x3, float y3, int color, float size, float originX, float originY, float angle) {
		GlStateManager.pushMatrix();
		float transX = x1 + originX;
		float transY = y1 + originY;
		GlStateManager.translate(transX, transY, 0);
		GlStateManager.rotate(angle, 0, 0, 1);
		GlStateManager.translate(-transX, -transY, 0);
		triangle(x1, y1, x2, y2, x3, y3, color, size);
		GlStateManager.popMatrix();
	}
	
	public static void triangle(float x1, float y1, float x2, float y2, float x3, float y3, int color, float size) {
		glLineWidth(size);
		glPointSize(size);
		
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_POINT_SMOOTH);
		GlStateManager.disableTexture2D();
		
		render(GL_TRIANGLE_FAN, new Vertex[] {
				new Vertex(x1, y1, 0, color),
				new Vertex(x2, y2, 0, color),
				new Vertex(x3, y3, 0, color)
		});
		
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_POINT_SMOOTH);
		GlStateManager.enableTexture2D();
	}
	
	public static void rectangle(float x1, float y1, float x2, float y2, int color, float size, float originX, float originY, float angle) {
		GlStateManager.pushMatrix();
		float transX = x1 + originX;
		float transY = y1 + originY;
		GlStateManager.translate(transX, transY, 0);
		GlStateManager.rotate(angle, 0, 0, 1);
		GlStateManager.translate(-transX, -transY, 0);
		rectangle(x1, y1, x2, y2, color, size);
		GlStateManager.popMatrix();
	}
	
	public static void rectangle(float x1, float y1, float x2, float y2, int color, float size) {
		if (size > 0) {
			glLineWidth(size);
			glPointSize(size);
		}
		
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_POINT_SMOOTH);
		GlStateManager.disableTexture2D();
		
		render(GL_QUADS, new Vertex[] {
				new Vertex(x1, y1, 0, color),
				new Vertex(x1, y2, 0, color),
				new Vertex(x2, y2, 0, color),
				new Vertex(x2, y1, 0, color)
		});
		
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_POINT_SMOOTH);
		GlStateManager.enableTexture2D();
	}
	
	public static void rectangle(ResourceLocation texture, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float u1, float v1, float u2, float v2, int color, float size, float originX, float originY, float angle) {
		GlStateManager.pushMatrix();
		float transX = x1 + originX;
		float transY = y1 + originY;
		GlStateManager.translate(transX, transY, 0);
		GlStateManager.rotate(angle, 0, 0, 1);
		GlStateManager.translate(-transX, -transY, 0);
		rectangle(texture, x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, color, size);
		GlStateManager.popMatrix();
	}
	
	public static void rectangle(ResourceLocation texture, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float u1, float v1, float u2, float v2, int color, float size) {
		if (size > 0) {
			glLineWidth(size);
			glPointSize(size);
		}
		
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_POINT_SMOOTH);
		GlStateManager.enableTexture2D();
		
		mc.getTextureManager().bindTexture(texture);
		
		render(GL_QUADS, new Vertex[] {
				new Vertex(x1, y1, 0, u1, v1, color),
				new Vertex(x2, y2, 0, u1, v2, color),
				new Vertex(x3, y3, 0, u2, v2, color),
				new Vertex(x4, y4, 0, u2, v1, color)
		});
		
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_POINT_SMOOTH);
	}
	
	public static void ellipse(float x1, float y1, float x2, float y2, int color, float size) {
		if (size > 0) {
			glLineWidth(size);
			glPointSize(size);
		}
		
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_POINT_SMOOTH);

		float radiusX = (x2 - x1) / 2f;
		float radiusY = (y2 - y1) / 2f;
		float centerX = x1 + radiusX;
		float centerY = y1 + radiusY;
		double increment = 360f / sqrt(pow(radiusX, 2) + pow(radiusY, 2));
		
		List<Vertex> vertices = new ArrayList();
		for (float i = 360; i > 0; i-= increment) {
			double pointX = centerX + (Math.cos(Math.toRadians(i))) * radiusX;
			double pointY = centerY + (Math.sin(Math.toRadians(i))) * radiusY;
			vertices.add(new Vertex(pointX, pointY, 0, color));
		}
		
		render(GL_POLYGON, vertices);
		
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_POINT_SMOOTH);
	}
	
	private static void render(int code, List<Vertex> vertices) {
		render(code, vertices.toArray(new Vertex[0]));
	}
	
	private static void render(int code, Vertex[] vertices) {
		Tessellator t = Tessellator.getInstance();
		VertexBuffer vb = t.getBuffer();
		
		vb.begin(code, DefaultVertexFormats.POSITION_TEX_COLOR);
		for (Vertex vertex : vertices) {
			vb.pos(vertex.x(), vertex.y(), vertex.z()).tex(vertex.texX(), vertex.texY()).color(vertex.rgba()[0], vertex.rgba()[1], vertex.rgba()[2], vertex.rgba()[3]).endVertex();
		}
		t.draw();
	}
	
}
