package com.solar.mod.impl;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.solar.FontAwesome;
import com.solar.Solar;
import com.solar.command.impl.Command;
import com.solar.event.Event;
import com.solar.event.EventPlayerMoveDirection;
import com.solar.event.EventRender;
import com.solar.event.Events;
import com.solar.mod.Mod;
import com.solar.util.MathUtil;
import com.solar.util.Option;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Pathfinder extends Mod {

	private EntityLiving ghost;
	private float yaw, pitch;
	private boolean pathFinding;
	private BlockPos set;
	private PathFinder pathFinder = new PathFinder(new WalkNodeProcessor());
	
	public Pathfinder() {
		super("Path Finder", FontAwesome.FA_EYE, "pathfinder,pf", "set", "start", "stop", "clear");
	}
	
	@Override
	protected void buildOptions(List<String> args) {
		options.put("colors", new Option<>("Colors", "colors,clr", Option.Type.PARENT, null, new Option[] {
				new Option<String>("Waypoint Fill", "waypointfill,wf", Option.Type.STRING, "80000000"),
				new Option<String>("Waypoint Outline", "waypointoutline,wo", Option.Type.STRING, "FFFF8040"),
				new Option<Float>("Waypoint Width", "waypointwidth,ww", Option.Type.STRING, 1f),
				new Option<String>("Path Fill", "pathfill,pf", Option.Type.STRING, "80000000"),
				new Option<String>("Path Outline", "pathoutline,po", Option.Type.STRING, "FF4080FF"),
				new Option<Float>("Path Outline", "pathwidth,pw", Option.Type.FLOAT, 1f)
		}));
		
		super.buildOptions(args);
	}
	
	@Override
	public void enable() {
		super.enable();
		ghost = new EntityBat(mc.theWorld);
		ghost.setPosition(0, -10, 0);
		ghost.setInvisible(true);
		mc.theWorld.addEntityToWorld(-1, ghost);
		pathFinding = false;
	}
	
	@Override
	public void disable() {
		mc.theWorld.removeEntityFromWorld(-1);
		super.disable();
	}
	
	@Override
	@Events(events={EventPlayerMoveDirection.class, EventRender.class})
	public void event(Event event) {
		if (event instanceof EventPlayerMoveDirection) {
			switch (event.type()) {
			case PRE:
				yaw = mc.thePlayer.rotationYaw;
				pitch = mc.thePlayer.rotationPitch;
				
				if (pathFinding) {
					if (set != null) {
						ghost.copyLocationAndAnglesFrom(mc.thePlayer);
						
						PathEntity path = pathFinder.func_186336_a(mc.theWorld, ghost, set, 50);
						
						if (path != null && path.getCurrentPathLength() > 1) {
							PathPoint point = path.getPathPointFromIndex(1);
							
							Vec3d from = new Vec3d(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
							Vec3d to = new Vec3d(point.xCoord + 0.5, point.yCoord + 0.5, point.zCoord + 0.5);
							
							Vector2f rotation = MathUtil.getRotation(from, to);
							
							mc.thePlayer.rotationYaw = rotation.x + 90;
							mc.thePlayer.rotationPitch = rotation.y;
							
							((EventPlayerMoveDirection) event).forward(1);
							if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
								mc.thePlayer.motionY = 0.42;
							}
							break;
						} else {
							pathFinding = false;
						}
					}
				}
				ghost.setPosition(0, -10, 0);
				break;
			case POST:
				mc.thePlayer.rotationYaw = yaw;
				mc.thePlayer.rotationPitch = pitch;
				break;
			}
		} else if (event instanceof EventRender) {
			switch (event.type()) {
			case RENDER3D:
				boolean depth = GlStateManager.getDepth();
				GlStateManager.disableDepth();
				
				Option colors = getOption("colors");
				
				Option<String> waypointFill = colors.getOption("waypointfill");
				Option<String> waypointOutline = colors.getOption("waypointoutline");
				Option<Float> waypointWidth = colors.getOption("waypointwidth");
				Option<String> pathFill = colors.getOption("pathfill");
				Option<String> pathOutline = colors.getOption("pathoutline");
				Option<Float> pathWidth = colors.getOption("pathwidth");
				
				if (set != null) {
					/*Render.cube(set.getX(), set.getY(), set.getZ(),
							set.getX() + 1, set.getY() + 1, set.getZ() + 1,
							ColorUtil.colorFromHex(waypointOutline.t()), ColorUtil.colorFromHex(waypointFill.t()), waypointWidth.t());*/

					PathEntity path = pathFinder.func_186336_a(mc.theWorld, ghost, set, 50);
					
					if (path != null) {
						double s = 0.05;
						for (int i = 0; i < path.getCurrentPathLength(); i++) {
							PathPoint point = path.getPathPointFromIndex(i);
							/*Render.cube(point.xCoord + 0.5 - s, point.yCoord + 0.5 - s, point.zCoord + 0.5 - s,
									point.xCoord + 0.5 + s, point.yCoord + 0.5 + s, point.zCoord + 0.5 + s,
									ColorUtil.colorFromHex(pathOutline.t()), ColorUtil.colorFromHex(pathFill.t()), pathWidth.t());*/
						}
					}
				}
				
				if (depth) {
					GlStateManager.enableDepth();
				}
				break;
			}
		}
	}

	@Override
    public void executeCommand(Command command, String[] arguments) {
    	super.executeCommand(command, arguments);
    	if (arguments.length == 1) {
    		if (command.isArgument("set", arguments[0])) {
    			set = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    			Solar.getInstance().alertManager().add("Set position to " + set.getX() + "," + set.getY() + "," + set.getZ());
    		} else if (command.isArgument("go", arguments[0])) {
    			if (set != null) {
	    			pathFinding = true;
	    			Solar.getInstance().alertManager().add("Finding path to set position");
    			} else {
    				Solar.getInstance().alertManager().add("Please select an end position");
    			}
    		} else if (command.isArgument("cancel", arguments[0])) {
    			pathFinding = false;
    			Solar.getInstance().alertManager().add("Cancelled pathfinding");
    		} else if (command.isArgument("clear", arguments[0])) {
    			set = null;
    			Solar.getInstance().alertManager().add("Cleared position");
    		}
    	}
    }
}
