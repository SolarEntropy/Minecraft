package com.solar.gui.font;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class FontManager {
	
	public static enum FontFile {
		FONTAWESOME("fontawesome"),
		VERDANA("verdana");
		
		String file;
		
		FontFile(String file) {
			this.file = file;
		}
		
		public String file() {
			return file;
		}
	}
	
	public static enum FontSize {
		XXS(10),
		XS(12),
		S(16),
		M(24),
		L(32),
		XL(48),
		XXL(64);
		
		int size;
		
		FontSize(int size) {
			this.size = size;
		}
		
		public int size() {
			return size;
		}
		
	}
	
	private final String _ID = "id";
	private final String _X = "x";
	private final String _Y = "y";
	private final String _WIDTH = "width";
	private final String _HEIGHT = "height";
	private final String _XOFFSET = "xoffset";
	private final String _YOFFSET = "yoffset";
	private final String _XADVANCE = "xadvance";
	
	private Map<FontFile, FontType> fontTypes = new HashMap<>();
	
	public FontManager() {
		load();
	}
	
	public void load() {
		for (FontFile f : FontFile.values()) {
			FontType fontType = new FontType(f.file);
			for (FontSize s : FontSize.values()) {
				String filename = "client/fonts/" + f.file() + "/" + s.size();
				ResourceLocation texture = new ResourceLocation(filename + ".png");
				int width = -1, height = -1, lineHeight = -1, base = -1;
				int[] padding = new int[4];
				Map<Integer, Char> charMap = new HashMap<>();
				
				InputStream input = FontManager.class.getResourceAsStream("/assets/minecraft/" + filename + ".fnt");
				BufferedReader reader;
				try {
					reader = new BufferedReader(new InputStreamReader(input));
					String line;
					while ((line = reader.readLine()) != null) {
						line = line.replaceAll("\\s+", " ");
						if (line.startsWith("info ")) {
							line = line.substring("info ".length());
							String[] split = line.split(" ");
							for (String info : split) {
								String[] i = info.split("=");
								if (i[0].equalsIgnoreCase("padding")) {
									String[] p = i[1].split(",");
									for (int index = 0; index < p.length; index++) {
										padding[index] = Integer.parseInt(p[index]);
									}
								}
							}
						} else if (line.startsWith("common ")) {
							line = line.substring("common ".length());
							String[] split = line.split(" ");
							for (String info : split) {
								String[] i = info.split("=");
								if (i[0].equalsIgnoreCase("lineHeight")) {
									lineHeight = Integer.parseInt(i[1]);
								} else if (i[0].equalsIgnoreCase("base")) {
									base = Integer.parseInt(i[1]);
								} else if (i[0].equalsIgnoreCase("scaleW")) {
									width = Integer.parseInt(i[1]);
								} else if (i[0].equalsIgnoreCase("scaleH")) {
									height = Integer.parseInt(i[1]);
								}
							}
						} else if (line.startsWith("char ")) {
							line = line.substring("char ".length());
							int id = -1, x = -1, y = -1, w = -1, h = -1, xo = -1, yo = -1, a = -1;
							String[] split = line.split(" ");
							for (String info : split) {
								String[] i = info.split("=");
								int value;
								try {
									value = Integer.parseInt(i[1]);
								} catch (Exception e) {
									e.printStackTrace();
									continue;
								}
								switch (i[0]) {
								case _ID:
									id = value;
									break;
								case _X:
									x = value;
									break;
								case _Y:
									y = value;
									break;
								case _WIDTH:
									w = value;
									break;
								case _HEIGHT:
									h = value;
									break;
								case _XOFFSET:
									xo = value;
									break;
								case _YOFFSET:
									yo = value;
									break;
								case _XADVANCE:
									a = value;
									break;
								}
							}
							charMap.put(id, new Char(id, x, y, w, h ,xo, yo, a));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				fontType.addFont(s, new Font(texture, width, height, padding, lineHeight, base, charMap));
			}
			fontTypes.put(f, fontType);
		}
	}
	
	public Font getFont(FontFile f, FontSize s) {
		return fontTypes.get(f).getFont(s);
	}
	
}
