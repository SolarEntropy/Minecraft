package com.solar.util;

import java.awt.Color;

import com.solar.util.values.ValueSin;

public class ColorUtil {

	public static int colorFromHex(String hex) {
		String code = "";
		if (hex.startsWith("0x")) {
			code = hex.substring(2);
		} else if (hex.startsWith("#")) {
			code = hex.substring(1);
		} else {
			code = hex;
		}
		
		if (code.length() == 3) {
			code+= code;
			code = "FF" + code;
		} else if (code.length() == 4) {
			String a = code.substring(0, 1);
			String r = code.substring(1, 2);
			String g = code.substring(2, 3);
			String b = code.substring(3, 4);
			code = a + a + r + r + g + g + b + b;
		}
		
		if (code.length() == 8) {
			String a = code.substring(0, 2);
			String r = code.substring(2, 4);
			String g = code.substring(4, 6);
			String b = code.substring(6, 8);
			
			int ia = (int) Long.parseLong(a + "000000", 16);
			int ir = Integer.parseInt(r + "0000", 16);
			int ig = Integer.parseInt("00" + g + "00", 16);
			int ib = Integer.parseInt("0000" + b, 16);
			
			return new Color(ir >> 16 & 255, ig >> 8 & 255, ib & 255, ia >> 24 & 255).getRGB();
		}
		return 0;
	}
	
	public static int colorFromStored(float[] rgba) {
		return ((int) (rgba[0] * 255) << 16) | ((int) (rgba[1] * 255) << 8) | (int) (rgba[2] * 255) | ((int) (rgba[3] * 255) << 24);
	}
	
	public static void storeColor(int color, float[] rgba) {
		rgba[0] = ((color >> 16 & 255) / 255f);
		rgba[1] = ((color >> 8 & 255) / 255f);
		rgba[2] = ((color & 255) / 255f);
		rgba[3] = ((color >> 24 & 255) / 255f);
	}
	
}
