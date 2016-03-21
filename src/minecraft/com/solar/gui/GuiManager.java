package com.solar.gui;

import org.lwjgl.input.Keyboard;

import com.solar.Solar;

import net.minecraft.client.Minecraft;

public class GuiManager {

	private Minecraft mc;
	private CGui screen;
	
	public GuiManager(Minecraft mc) {
		this.mc = mc;
	}
	
	public void update() {
		if (screen != null) {
			screen.update();
			Solar.getInstance().inputManager().update();
			
			if (!Keyboard.isKeyDown(Keyboard.KEY_TAB) || mc.currentScreen != null) {
				screen = null;
				if (mc.currentScreen == null) {
					mc.mouseHelper.grabMouseCursor();
				}
			}
		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && mc.currentScreen == null) {
				screen = new CGuiRadialMods();
				mc.mouseHelper.ungrabMouseCursor();
			}
		}
		if (mc.theWorld == null && screen != null) {
			screen = null;
		}
	}
	
	public void mouseEvent(boolean action, int button) {
		if (screen != null) {
			screen.mouseEvent(action, button);
		}
	}
	
	public void keyboardEvent(boolean action, int key, char keyChar) {
		if (screen != null) {
			screen.keyboardEvent(action, key, keyChar);
		}
	}
	
	public void render() {
		if (screen != null) {
			screen.render();
		}
	}
	
	public CGui screen() {
		return screen;
	}
}