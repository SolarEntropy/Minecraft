package com.solar.mod.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.solar.FontAwesome;
import com.solar.Solar;
import com.solar.command.impl.Command;
import com.solar.event.Event;
import com.solar.event.EventPlayerMoveDirection;
import com.solar.event.EventPlayerUpdateWalking;
import com.solar.event.EventRender;
import com.solar.event.Events;
import com.solar.mod.Mod;
import com.solar.mod.impl.inventory.InventoryFood;
import com.solar.mod.impl.inventory.InventoryTool;
import com.solar.util.ColorUtil;
import com.solar.util.MathUtil;
import com.solar.util.Option;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class WorldEdit extends Mod {

	private EntityLiving ghost;
	private float yaw, pitch, yawBreak, pitchBreak;
	private BlockPos pos1, pos2, target;
	private PathFinder pathFinder = new PathFinder(new WalkNodeProcessor());
	private RayTraceResult result;
	private InventoryTool invTool = new InventoryTool();
	private InventoryFood invFood = new InventoryFood();
	
	public WorldEdit() {
		super("World Edit", FontAwesome.FA_PENCIL, "worldedit,we", "pos1,p1", "pos2,p2", "clear,clr", "expand,exp", "contract,cnt", "move,mv");
	}
	
	@Override
	protected void buildOptions(List<String> args) {
		options.put("range", new Option<Float>("Range", "range", Option.Type.FLOAT, 50f));
		options.put("reach", new Option<Float>("Reach", "reach", Option.Type.FLOAT, 4f));
		options.put("moverange", new Option<Float>("Move Range", "moverange,mr", Option.Type.FLOAT, 1f));

		options.put("colors", new Option<>("Colors", "colors,clr", Option.Type.PARENT, null, new Option[] {
				new Option<String>("Pos1 Fill", "pos1fill,p1fill,p1f", Option.Type.STRING, "80000000"),
				new Option<String>("Pos1 Outline", "pos1outline,p1outline,p1o", Option.Type.STRING, "FFFF8040"),
				new Option<String>("Pos2 Fill", "pos2fill,p2fill,p2f", Option.Type.STRING, "80000000"),
				new Option<String>("Pos2 Outline", "pos2outline,p2outline,p2o", Option.Type.STRING, "FFFF8040"),
				new Option<String>("Target Fill", "targetfill,tfill,tf", Option.Type.STRING, "80000000"),
				new Option<String>("Target Outline", "targetoutline,toutline,to", Option.Type.STRING, "FF4080FF"),
				new Option<String>("Grid Fill", "gridfill,gfill,gf", Option.Type.STRING, "40000000"),
				new Option<String>("Grid Outline", "gridoutline,goutline,go", Option.Type.STRING, "C0FF8040")
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
	}
	
	@Override
	public void disable() {
		mc.theWorld.removeEntityFromWorld(-1);
		super.disable();
	}
	
	@Override
	@Events(events={EventPlayerMoveDirection.class, EventPlayerUpdateWalking.class, EventRender.class})
	public void event(Event event) {
		if (event instanceof EventPlayerMoveDirection) {
			switch (event.type()) {
			case PRE:
				yaw = mc.thePlayer.rotationYaw;
				pitch = mc.thePlayer.rotationPitch;
				
				target = nextBlock();
				if (target != null) {
					ghost.copyLocationAndAnglesFrom(mc.thePlayer);

					Option<Float> range = getOption("range");
					PathEntity path = pathFinder.func_186336_a(mc.theWorld, ghost, target, range.t());
					
					if (path != null && path.getCurrentPathLength() > 2) {
						PathPoint point = path.getPathPointFromIndex(2);
						
						Vec3d from = new Vec3d(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
						Vec3d to = new Vec3d(point.xCoord + 0.5, point.yCoord + 0.5, point.zCoord + 0.5);
						
						Vector2f rotation = MathUtil.getRotation(from, to);
						
						mc.thePlayer.rotationYaw = rotation.x + 90;
						mc.thePlayer.rotationPitch = rotation.y;

						Option<Float> moveRange = getOption("moverange");
						if (MathUtil.getDistance(from, new Vec3d(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5)) >= moveRange.t()) {
							((EventPlayerMoveDirection) event).forward(1);
							if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
								mc.thePlayer.motionY = 0.42;
							}
						}
					}
				} else {
					ghost.setPosition(0, -10, 0);
				}
				break;
			case POST:
				mc.thePlayer.rotationYaw = yaw;
				mc.thePlayer.rotationPitch = pitch;
				break;
			}
		} else if (event instanceof EventPlayerUpdateWalking) {
			Option<Float> reach = getOption("reach");
			
			switch (event.type()) {
			case PRE:
				yawBreak = mc.thePlayer.rotationYaw;
				pitchBreak = mc.thePlayer.rotationPitch;
				
				result = null;

				if (target != null) {
					Vec3d from = new Vec3d(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
					
					IBlockState state = mc.theWorld.getBlockState(target);
					AxisAlignedBB aabb = state.getCollisionBoundingBox(mc.theWorld, target);
					
					Vec3d to = new Vec3d(aabb.minX + ((aabb.maxX - aabb.minX) / 2.0), aabb.minY + ((aabb.maxY - aabb.minY) / 2.0), aabb.minZ + ((aabb.maxZ - aabb.minZ) / 2.0));
					
					if (MathUtil.getDistance(from, to) <= reach.t()) {
						result = mc.theWorld.rayTraceBlocks(from, to);
						if (result != null) {
							Vec3d hit = result.hitVec;
							Vector2f rotation = MathUtil.getRotation(from, hit);
							
							mc.thePlayer.rotationYaw = rotation.x + 90;
							mc.thePlayer.rotationPitch = rotation.y;
						}
					}
				}
				
				break;
			case POST:
				if (result != null) {
					Vec3d from = new Vec3d(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
					Vec3d to = result.hitVec;
					
					if (MathUtil.getDistance(from, to) <= reach.t()) {
						IBlockState state = mc.theWorld.getBlockState(result.getBlockPos());
						boolean eat = mc.thePlayer.getFoodStats().needFood();
						
						boolean mine = false;
						if (!eat) {
							mine = !invTool.update(state);
						} else {
							mine = !invFood.update(null);
						}
						if (eat) {
							mc.playerController.func_187101_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItemMainhand(), EnumHand.MAIN_HAND);
							mine = false;
						}
						if (mc.thePlayer.isCreative() || mine) {
							mc.playerController.onPlayerDamageBlock(result.getBlockPos(), result.sideHit);
							mc.thePlayer.swingArm(EnumHand.MAIN_HAND);
						}
					}
				}
				
				mc.thePlayer.rotationYaw = yawBreak;
				mc.thePlayer.rotationPitch = pitchBreak;
				break;
			}
		} else if (event instanceof EventRender) {
			switch (event.type()) {
			case RENDER3D:
				boolean depth = GlStateManager.getDepth();
				GlStateManager.disableDepth();

				Option colors = getOption("colors");
				Option<String> pos1Fill = colors.getOption("pos1fill");
				Option<String> pos1Outline = colors.getOption("pos1outline");
				Option<String> pos2Fill = colors.getOption("pos2fill");
				Option<String> pos2Outline = colors.getOption("pos2outline");
				Option<String> targetFill = colors.getOption("targetfill");
				Option<String> targetOutline = colors.getOption("targetoutline");
				Option<String> gridFill = colors.getOption("gridfill");
				Option<String> gridOutline = colors.getOption("gridoutline");
				
				List<BlockPos> cubes = new ArrayList<>();
				List<Integer> colorList = new ArrayList<>();
				if (pos1 != null) {
					cubes.add(pos1);
					colorList.add(ColorUtil.colorFromHex(pos1Outline.t()));
					colorList.add(ColorUtil.colorFromHex(pos1Fill.t()));
				}
				if (pos2 != null) {
					cubes.add(pos2);
					colorList.add(ColorUtil.colorFromHex(pos2Outline.t()));
					colorList.add(ColorUtil.colorFromHex(pos2Fill.t()));
				}
				if (target != null) {
					cubes.add(target);
					colorList.add(ColorUtil.colorFromHex(targetOutline.t()));
					colorList.add(ColorUtil.colorFromHex(targetFill.t()));
				}
				
				AxisAlignedBB selectionAABB = null;
				
				float s = 0.5f;
				for (int i = 0; i < cubes.size(); i++) {
					BlockPos blockPos = cubes.get(i);
					IBlockState state = mc.theWorld.getBlockState(blockPos);
					
					AxisAlignedBB aabb = state.getCollisionBoundingBox(mc.theWorld, blockPos);
					if (selectionAABB == null) {
						selectionAABB = aabb;
					} else {
						selectionAABB = selectionAABB.merge(aabb);
					}
					
					int outline = colorList.get(i * 2);
					int fill = colorList.get(i * 2 + 1);
					
					if (aabb != null) {
						/*Render.cube(aabb.minX, aabb.minY, aabb.minZ,
								aabb.maxX, aabb.maxY, aabb.maxZ,
								outline, fill, 1);*/
					}
				}
				
				if (selectionAABB != null && pos1 != null && pos2 != null) {
					/*Render.cube(selectionAABB.minX, selectionAABB.minY, selectionAABB.minZ,
							selectionAABB.maxX, selectionAABB.maxY, selectionAABB.maxZ,
							ColorUtil.colorFromHex(gridOutline.t()), ColorUtil.colorFromHex(gridFill.t()), 1, 1);*/
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
    		if (command.isArgument("pos1", arguments[0])) {
    			pos1 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    			Solar.getInstance().alertManager().add("Set corner 1 to " + pos1.getX() + "," + pos1.getY() + "," + pos1.getZ());
    		} else if (command.isArgument("pos2", arguments[0])) {
    			pos2 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    			Solar.getInstance().alertManager().add("Set corner 2 to " + pos2.getX() + "," + pos2.getY() + "," + pos2.getZ());
    		} else if (command.isArgument("clear", arguments[0])) {
    			pos1 = (pos2 = null);
    			Solar.getInstance().alertManager().add("Cleared selection");
    		}
    	} else if (arguments.length == 2) {
    		if (pos1 != null && pos2 != null) {
	    		int dist = Integer.parseInt(arguments[1]);
	    		//0 = -z, 1 = x, 2 = z, 3 = -x
	    		int dir = MathHelper.floor_double((double) (mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
	    		boolean up = mc.thePlayer.rotationPitch < -45;
	    		boolean down = mc.thePlayer.rotationPitch > 45;
	    		int xInc = (!up && !down) ? (dir == 1 ? -1 : dir == 3 ? 1 : 0) : 0;
	    		int yInc = up ? 1 : down ? -1 : 0;
	    		int zInc = (!up && !down) ? (dir == 0 ? 1 : dir == 2 ? -1 : 0) : 0;
	    		
	    		if (command.isArgument("expand", arguments[0])) {
	    			expand(xInc, yInc, zInc, up, down, dist);
	    		} else if (command.isArgument("contract", arguments[0])) {
	    			contract(xInc, yInc, zInc, up, down, dist);
	    		} else if (command.isArgument("move", arguments[0])) {
	    			move(xInc, yInc, zInc, dist);
	    		}
    		}
    	}
    }
	
	private void expand(int x, int y, int z, boolean up, boolean down, int dist) {
		x*= dist;
		y*= dist;
		z*= dist;

		pos1 = pos1.add(
				pos1.getX() >= pos2.getX() && x > 0 || pos1.getX() < pos2.getX() && x < 0 ? x : 0,
				pos1.getY() >= pos2.getY() && up || pos1.getY() < pos2.getY() && down ? y : 0,
				pos1.getZ() >= pos2.getZ() && z > 0 || pos1.getZ() < pos2.getZ() && z < 0 ? z : 0);
		pos2 = pos2.add(
				pos2.getX() > pos1.getX() && x > 0 || pos2.getX() <= pos1.getX() && x < 0 ? x : 0,
				pos2.getY() > pos1.getY() && up || pos2.getY() <= pos1.getY() && down ? y : 0,
				pos2.getZ() > pos1.getZ() && z > 0 || pos2.getZ() <= pos1.getZ() && z < 0 ? z : 0);
	}

	private void contract(int x, int y, int z, boolean up, boolean down, int dist) {
		x*= Math.min(Math.max(0, dist), Math.max(pos1.getX(), pos2.getX()) - Math.min(pos1.getX(), pos2.getX()));
		y*= Math.min(Math.max(0, dist), Math.max(pos1.getY(), pos2.getY()) - Math.min(pos1.getY(), pos2.getY()));
		z*= Math.min(Math.max(0, dist), Math.max(pos1.getZ(), pos2.getZ()) - Math.min(pos1.getZ(), pos2.getZ()));

		pos1 = pos1.add(
				pos1.getX() > pos2.getX() && x < 0 || pos1.getX() < pos2.getX() && x > 0 ? x : 0,
				pos1.getY() > pos2.getY() && down || pos1.getY() < pos2.getY() && up ? y : 0,
				pos1.getZ() > pos2.getZ() && z < 0 || pos1.getZ() < pos2.getZ() && z > 0 ? z : 0);
		pos2 = pos2.add(
				pos2.getX() > pos1.getX() && x < 0 || pos2.getX() < pos1.getX() && x > 0 ? x : 0,
				pos2.getY() > pos1.getY() && down || pos2.getY() < pos1.getY() && up ? y : 0,
				pos2.getZ() > pos1.getZ() && z < 0 || pos2.getZ() < pos1.getZ() && z > 0 ? z : 0);
	}

	private void move(int x, int y, int z, int dist) {
		x*= dist;
		y*= dist;
		z*= dist;

		pos1 = pos1.add(x, y, z);
		pos2 = pos2.add(x, y, z);
	}
	
	private BlockPos nextBlock() {
		if (pos1 != null && pos2 != null) {
			BlockPos min = getMin(pos1, pos2);
			BlockPos max = getMax(pos1, pos2);

			List<BlockPos> blocks = new ArrayList<>();
			int y = getY();
			
			if (y != -1) {
				for (int x = min.getX(); x <= max.getX(); x++) {
					for (int z = min.getZ(); z <= max.getZ(); z++) {
						BlockPos pos = new BlockPos(x, y, z);
						if (canSelect(pos)) {
							blocks.add(pos);
						}
					}
				}
				Collections.sort(blocks, new NearestSort());
			}
			
			if (!blocks.isEmpty()) {
				return blocks.get(0);
			}
		}
		return null;
	}
	
	private int getY() {
		if (pos1 != null && pos2 != null) {
			BlockPos min = getMin(pos1, pos2);
			BlockPos max = getMax(pos1, pos2);

			for (int y = max.getY(); y >= min.getY(); y--) {
				for (int x = min.getX(); x <= max.getX(); x++) {
					for (int z = min.getZ(); z <= max.getZ(); z++) {
						BlockPos pos = new BlockPos(x, y, z);
						if (canSelect(pos)) {
							return y;
						}
					}
				}
			}
		}
		return -1;
	}
	
	private boolean canSelect(BlockPos pos) {
		IBlockState state = mc.theWorld.getBlockState(pos);
		return state != null && state.getBlock() != null &&
				state.getBlock() != Blocks.air &&
				state.getBlock() != Blocks.water &&
				state.getBlock() != Blocks.flowing_water &&
				state.getBlock() != Blocks.lava &&
				state.getBlock() != Blocks.flowing_lava;
	}
	
	private BlockPos getMin(BlockPos pos1, BlockPos pos2) {
		return new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
	}
	
	private BlockPos getMax(BlockPos pos1, BlockPos pos2) {
		return new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
	}
	
	private class NormalSort implements Comparator<BlockPos> {
		@Override
		public int compare(BlockPos b1, BlockPos b2) {
			return b2.getY() - b1.getY();
		}
	}
	
	private class NearestSort implements Comparator<BlockPos> {
		@Override
		public int compare(BlockPos b1, BlockPos b2) {
			float b1d = MathUtil.getDistance(b1.getX() + 0.5f, b1.getY() + 0.5f, b1.getZ() + 0.5f, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			float b2d = MathUtil.getDistance(b2.getX() + 0.5f, b2.getY() + 0.5f, b2.getZ() + 0.5f, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			return (int) Math.signum(b1d - b2d);
		}
	}

}
