package com.solar.mod;

import com.solar.FontAwesome;

public enum Category {

	BUILD("Build", FontAwesome.FA_CUBE),
	COMBAT("Combat", FontAwesome.FA_CROSSHAIRS),
	MISC("Misc", FontAwesome.FA_STAR),
	MOVEMENT("Movement", FontAwesome.FA_ARROWS),
	PLAYER("Player", FontAwesome.FA_USER),
	WORLD("World", FontAwesome.FA_GLOBE);
	
	String title;
	FontAwesome icon;
	
	Category(String title, FontAwesome icon) {
		this.title = title;
		this.icon = icon;
	}
	
	public String title() {
		return title;
	}
	
	public FontAwesome icon() {
		return icon;
	}
	
}
