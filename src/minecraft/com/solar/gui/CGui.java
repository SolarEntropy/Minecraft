package com.solar.gui;

public interface CGui {

	public void update();
	public void mouseEvent(boolean action, int button);
	public void keyboardEvent(boolean action, int key, char keyChar);
	public void render();
	
}
