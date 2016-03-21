package com.solar.gui;

import org.lwjgl.opengl.Display;

import com.solar.Solar;
import com.solar.mod.Category;
import com.solar.util.MathUtil;

public class CGuiRadialMods extends CGuiRadial {
	
	public CGuiRadialMods() {
		super(Category.values().length);
		
		x = Display.getWidth() / 2f;
		y = Display.getHeight() / 2f;
		w = (Math.min(Display.getWidth(), Display.getHeight())) / 2f;
		h = w;
	}
	
	@Override
	public void update() {
		x = Display.getWidth() / 2f;
		y = Display.getHeight() / 2f;
		w = (Math.min(Display.getWidth(), Display.getHeight())) / 2f;
		h = w;
		
		float angle = MathUtil.getRotation(x, y, Solar.getInstance().inputManager().mouseX(), Solar.getInstance().inputManager().mouseY());
		selected = Math.round(angle / (360f / segments) + (segments / 2f) - 0.5f);
		
		selectedPartial+= MathUtil.interpolate(selectedPartial, selected * (360f / segments)) / 10;
	}
	
	@Override
	public void mouseEvent(boolean action, int button) {
		if (action) {
			if (button == 0) {
				//Clicked mod
			} else if (button == 1) {
				//Return to parent
			}
		}
	}
	
	@Override
	public void keyboardEvent(boolean action, int key, char keyChar) {
	}

	@Override
	public void render() {
		super.render();
	}

}
