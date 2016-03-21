package com.solar.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.solar.render.Render;
import com.solar.util.StencilUtil;

import net.minecraft.client.Minecraft;

public class CGuiRadial implements CGui {

	protected Minecraft mc = Minecraft.getMinecraft();
	protected final int SEGMENT_FILL = 0x80FFFFFF;
	protected final int SELECTED_FILL = 0xFF4080FF;
	
	protected int selected, segments;
	protected float selectedPartial, x, y, w, h;
	
	public CGuiRadial(int segments) {
		this.segments = segments;
	}
	
	@Override
	public void update() {
	}
	
	@Override
	public void mouseEvent(boolean action, int button) {
	}
	
	@Override
	public void keyboardEvent(boolean action, int key, char keyChar) {
	}

	@Override
	public void render() {
		//Main circle
		
		StencilUtil.start();
		StencilUtil.hide();
		Render.ellipse(x - (w / 4f), y - (h / 4f), x + (w / 4f), y + (h / 4f), -1, 0);
		for (float i = 0; i < 360; i+= 360f / segments) {
			double tx = x + (Math.cos(Math.toRadians(i)) * (w / 2f));
			double ty = y + (Math.sin(Math.toRadians(i)) * (h / 2f));
			Render.line(x, y, tx, ty, -1, 1f);
		}
		StencilUtil.show();
		Render.ellipse(x - (w / 2f), y - (h / 2f), x + (w / 2f), y + (h / 2f), SEGMENT_FILL, 0);
		StencilUtil.end();
		
		//Segment
		
		StencilUtil.start();
		StencilUtil.hide();
		Render.ellipse(x - (w / 4f), y - (h / 4f), x + (w / 4f), y + (h / 4f), -1, 0);
		for (float i = 0; i < 360; i+= 360f / segments) {
			double tx = x + (Math.cos(Math.toRadians(i)) * (w / 2f));
			double ty = y + (Math.sin(Math.toRadians(i)) * (h / 2f));
			Render.line(x, y, tx, ty, -1, 1f);
		}
		Render.rectangle(x - (w / 2f), y - (h / 2f), x + (w / 2f), y, -1, 0, w / 2f, h / 2f, selectedPartial);
		Render.rectangle(x - (w / 2f), y, x + (w / 2f), y + (h / 2f), -1, 0, w / 2f, 0, selectedPartial + (360f / segments));
		StencilUtil.show();
		Render.ellipse(x - (w / 2f), y - (h / 2f), x + (w / 2f), y + (h / 2f), SELECTED_FILL, 0);
		StencilUtil.end();
	}

}
