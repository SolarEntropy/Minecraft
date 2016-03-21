package com.solar.mod.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;

import com.solar.FontAwesome;
import com.solar.Solar;
import com.solar.event.Event;
import com.solar.event.EventRender;
import com.solar.event.Events;
import com.solar.gui.font.Font;
import com.solar.gui.font.FontManager;
import com.solar.mod.Mod;
import com.solar.render.Render;
import com.solar.util.MathUtil;
import com.solar.util.Option;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class Location extends Mod {

	private Map<Entity, List<Vec3d>> entityData = new HashMap<>();
	
	public Location() {
		super("Location", FontAwesome.FA_LOCATION_ARROW, "location,loc");
	}
	
	@Override
	protected void buildOptions(List<String> args) {
		options.put("colors", new Option<>("Colors", "colors,clr", Option.Type.PARENT, null, new Option[] {
				new Option<String>("Pin Color", "pincolor,pc", Option.Type.STRING, "FF4080FF")
		}));
		
		super.buildOptions(args);
	}
	
	@Override
	@Events(events={EventRender.class})
	public void event(Event event) {
		if (event instanceof EventRender) {
			switch (event.type()) {
			case RENDER2D:
				Font fontAwesome = Solar.getInstance().fontManager().getFont(FontManager.FontFile.FONTAWESOME, FontManager.FontSize.S);
				Font verdana = Solar.getInstance().fontManager().getFont(FontManager.FontFile.VERDANA, FontManager.FontSize.XXS);
				float screen = (float) Math.sqrt(Math.pow(Display.getWidth(), 2) + Math.pow(Display.getHeight(), 2)) / 2f;
				for (Entity e : entityData.keySet()) {
					List<Vec3d> data = entityData.get(e);
					Vec3d point = data.get(0);
					
					float x = (float) point.xCoord;
					float y = (float) point.yCoord;
					float z = (float) point.zCoord;
					float dist = MathUtil.increment(mc.thePlayer.getDistanceToEntity(e), 0.1f);
					
					if (z <= 1) {
						String icon = FontAwesome.FA_MAP_MARKER.toString();

						float xx1 = x - (fontAwesome.getWidth(icon) / 2f);
						float yy1 = y - fontAwesome.getHeight(icon);
						float xx2 = x + (fontAwesome.getWidth(icon) / 2f);
						float yy2 = y;

						Render.rectangle(xx1 - 2, yy1, xx2 + 2, yy2, 0x80000000, 0);
						fontAwesome.drawString((mc.thePlayer.canEntityBeSeen(e) ? "\u00a7a" : "\u00a7c") + icon, x - (fontAwesome.getWidth(icon) / 2f), y - fontAwesome.getHeight(icon), -1, false);
						
						xx1 = x - (verdana.getWidth(dist + "m") / 2f);
						yy1 = y + 1;
						xx2 = x + (verdana.getWidth(dist + "m") / 2f);
						yy2 = yy1 + verdana.getHeight(dist + "m");
						
						Render.rectangle(xx1 - 2, yy1, xx2 + 2, yy2, 0x80000000, 0);
						verdana.drawString(dist + "m", x - (verdana.getWidth(dist + "m") / 2f), yy1, 0xFFDDDDDD, false);
					}
				}
				entityData.clear();
				break;
			case RENDER3D:
				mc.entityRenderer.setupCameraTransformExtended(mc.timer.elapsedPartialTicks, 2);
				entityData.clear();
				for (Object e : mc.theWorld.getLoadedEntityList()) {
					Entity entity = (Entity) e;
					Vec3d position = MathUtil.getPartial(entity.posX, entity.lastTickPosX, entity.posY, entity.lastTickPosY, entity.posZ, entity.lastTickPosZ, mc.timer.renderPartialTicks);
					position = position.addVector(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
					
					List<Vec3d> bounds = new ArrayList<>();
					bounds.add(position.addVector(0, entity.height + 0.2, 0));
					
					List<Vec3d> data = new ArrayList<>();
					for (Vec3d vec : bounds) {
						Vec3d coords = MathUtil.to2D(vec);
						if (coords != null) {
							data.add(coords);
						}
					}
					
					entityData.put(entity, data);
				}
				break;
			}
		}
	}
}
