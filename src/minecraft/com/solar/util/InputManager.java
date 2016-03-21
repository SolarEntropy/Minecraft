package com.solar.util;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.solar.Solar;

import net.minecraft.client.Minecraft;

public class InputManager {

	private int mouseX, mouseY;
	
	public InputManager() {
	}
	
	public void update() {
		while (Mouse.next()) {
			mouseX = Mouse.getEventX();
			mouseY = Mouse.getEventY();
			
			boolean action = Mouse.getEventButtonState();
			int button = Mouse.getEventButton();

			Solar.getInstance().guiManager().mouseEvent(action, button);
		}
		
		/*while (Keyboard.next()) {
			boolean action = Keyboard.getEventKeyState();
			int key = Keyboard.getEventKey();
			char keyChar = Keyboard.getEventCharacter();
			
			Solar.getInstance().guiManager().keyboardEvent(action, key, keyChar);
		}*/
	}
	
	public void ingameMouse(boolean action, int button) {
		Solar.getInstance().modManager().input(action, button - 100);
	}
	
	public void ingameKeyboard(boolean action, int key, char keyChar) {
		Solar.getInstance().modManager().input(action, key);
	}
	
	public int mouseX() {
		return mouseX;
	}
	
	public int mouseY() {
		return Display.getHeight() - mouseY;
	}
	
}
