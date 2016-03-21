package com.solar.gui.font;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.solar.render.Render;
import com.solar.util.ColorUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Font {
	
	private static int[] colors = new int[32];
	
	static {
		for (int i = 0; i < 32; ++i) {
			int a = (i >> 3 & 1) * 85;
			int r = (i >> 2 & 1) * 170 + a;
			int g = (i >> 1 & 1) * 170 + a;
			int b = (i & 1) * 170 + a;

			if (i == 6) {
				r+= 85;
			}

			if (i >= 16) {
				r/= 4;
				g/= 4;
				b/= 4;
			}

			colors[i] = (r & 255) << 16 | (g & 255) << 8 | b & 255;
		}
	}
	
	private ResourceLocation texture;
	private int width, height, lineHeight, base;
	private int[] padding;
	private Map<Integer, Char> charMap;
	private String colorCodes = "0123456789abcdefklmnor";
	
	public Font(ResourceLocation texture, int width, int height, int[] padding, int lineHeight, int base, Map<Integer, Char> charMap) {
		this.texture = texture;
		this.width = width;
		this.height = height;
		this.padding = padding;
		this.lineHeight = lineHeight;
		this.base = base;
		this.charMap = charMap;
	}
	
	public void drawString(String text, float x, float y, int color, boolean shadow, float originX, float originY, float angle) {
		GlStateManager.pushMatrix();
		float transX = x + originX;
		float transY = y + originY;
		GlStateManager.translate(transX, transY, 0);
		GlStateManager.rotate(angle, 0, 0, 1);
		GlStateManager.translate(-transX, -transY, 0);
		drawString(text, x, y, color, shadow);
		GlStateManager.popMatrix();
	}
	
	public void drawString(String text, float x, float y, int color, boolean shadow) {
		char[] chars = text.toCharArray();
		
		float[] rgba = new float[4];
		ColorUtil.storeColor(color, rgba);
		
		boolean magic = false, bold = false, strike = false, underline = false, italic = false;
		int fontColor = 15;
		
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			Char curChar = charMap.get((int) chars[i]);
			
			if (c == '\u00a7' && i < chars.length - 1) {
				int ind = colorCodes.indexOf((char) chars[i + 1]);
				if (ind >= 0 && ind <= 21) {
					switch (ind) {
					case 16:
						magic = true;
						break;
					case 17:
						bold = true;
						break;
					case 18:
						strike = true;
						break;
					case 19:
						underline = true;
						break;
					case 20:
						italic = true;
						break;
					case 21:
						magic = bold = strike = underline = italic = false;
						fontColor = 15;
						break;
					default:
						magic = bold = strike = underline = italic = false;
						fontColor = ind;
						
						int col = colors[fontColor];
						ColorUtil.storeColor(col, rgba);
						rgba[3] = 1;
						break;
					}
					i++;
					continue;
				}
			}
			
			
			if (curChar == null) {
				continue;
			}
			
			Char oldChar = curChar;
			if (magic) {
				Random random = new Random(text.hashCode() + System.nanoTime());
				List<Char> list = new ArrayList<>();
				list.addAll(charMap.values());
				int a;
				do {
					a = random.nextInt(charMap.size());
				} while (list.get(a).w() != curChar.w());
				curChar = list.get(a);
			}
			
			if (strike) {
				
			}
			
			if (underline) {
				
			}

			for (int j = (shadow ? 1 : 0); j >= 0; j--) {
				for (int k = 0; k < (bold ? 2 : 1); k++) {
					GL11.glPushMatrix();
					int renderColor = ColorUtil.colorFromStored(rgba);
					if (j == 1) {
						renderColor = colors[fontColor + 16];
					}
					GL11.glTranslatef(j + k, j, 0);
					
					float x1 = x + curChar.xo();
					float y1 = y + curChar.yo();
					float x2 = x1 + curChar.w();
					float y2 = y1 + curChar.h();
					
					Render.rectangle(texture,
							x1 + (italic ? 3 : 0), y1, x1, y2, x2, y2, x2 + (italic ? 3 : 0), y1,
							(1f / width) * curChar.x(), (1f / height) * curChar.y(), (1f / width) * (curChar.x() + curChar.w()), (1f / height) * (curChar.y() + curChar.h()),
							renderColor, 0);
					
					GL11.glPopMatrix();
				}
			}
			x+= oldChar.a() - padding[0] - padding[2];
		}
	}
	
	public float getWidth(String text) {
		float x = 0;
		
		char[] chars = text.toCharArray();
		
		boolean magic = false;
		
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			Char curChar = charMap.get((int) chars[i]);
			if (curChar == null) {
				continue;
			}
			
			if (c == '\u00a7' && i < chars.length - 1) {
				Char nexChar = charMap.get((int) chars[i + 1]);
				if (nexChar != null) {
					int ind = colorCodes.indexOf((char) nexChar.id());
					if (ind >= 0 && ind <= 21) {
						switch (ind) {
						case 16:
							magic = true;
							break;
						case 17:
							break;
						case 18:
							break;
						case 19:
							break;
						case 20:
							break;
						default:
							magic = false;
							break;
						}
						i++;
						continue;
					}
				}
			}
			
			x+= curChar.a() - padding[0] - padding[2];
		}
		return x;
	}
	
	public float getHeight(String text) {
		double y = 0;
		
		char[] chars = text.toCharArray();
		
		boolean magic = false;
		
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			Char curChar = charMap.get((int) chars[i]);
			if (curChar == null) {
				continue;
			}
			
			if (c == '\u00a7' && i < chars.length - 1) {
				Char nexChar = charMap.get((int) chars[i + 1]);
				if (nexChar != null) {
					int ind = colorCodes.indexOf((char) nexChar.id());
					if (ind >= 0 && ind <= 21) {
						switch (ind) {
						case 16:
							magic = true;
							break;
						case 17:
							break;
						case 18:
							break;
						case 19:
							break;
						case 20:
							break;
						default:
							magic = false;
							break;
						}
						i++;
						continue;
					}
				}
			}
			
			if (magic) {
				Random random = new Random();
				List<Char> list = new ArrayList<>();
				list.addAll(charMap.values());
				int a;
				do {
					a = random.nextInt(charMap.size());
				} while (list.get(a).w() != curChar.w());
				curChar = list.get(a);
			}
			
			y = Math.max(y, curChar.h());
		}
		return (float) y;
	}
	
	public float getHeight() {
		return base;
	}
	
	@Override
	public String toString() {
		return texture.getResourcePath();
	}
}
