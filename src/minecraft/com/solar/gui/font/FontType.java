package com.solar.gui.font;

import java.util.HashMap;
import java.util.Map;

public class FontType {

	private Map<FontManager.FontSize, Font> fonts = new HashMap<>();
	
	private String file;
	
	public FontType(String file) {
		this.file = file;
	}
	
	public void addFont(FontManager.FontSize size, Font font) {
		fonts.put(size, font);
		System.out.println(font + " : " + size);
	}
	
	public Font getFont(FontManager.FontSize size) {
		return fonts.get(size);
	}
	
	public String file() {
		return file;
	}
}
