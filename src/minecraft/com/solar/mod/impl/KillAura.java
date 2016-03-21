package com.solar.mod.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.solar.FontAwesome;
import com.solar.command.impl.Command;
import com.solar.event.Event;
import com.solar.event.EventPlayerUpdateWalking;
import com.solar.event.EventRender;
import com.solar.event.Events;
import com.solar.mod.Mod;
import com.solar.render.Render;
import com.solar.util.MathUtil;
import com.solar.util.Option;
import com.solar.util.Timer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class KillAura extends Mod {

	private float yaw, pitch;
	private EntityLivingBase target;
	
	public KillAura() {
		super("Kill Aura", FontAwesome.FA_DOT_CIRCLE_O, "killaura,ka,aura,mobaura");
	}

	@Override
	protected void buildOptions(List<String> args) {
		options.put("reach", new Option<Float>("Reach", "reach", Option.Type.FLOAT, 4f));
		
		super.buildOptions(args);
	}
	
	@Override
	@Events(events={EventPlayerUpdateWalking.class, EventRender.class})
	public void event(Event event) {
		if (event instanceof EventPlayerUpdateWalking) {
			switch (event.type()) {
			case PRE:
				yaw = mc.thePlayer.rotationYaw;
				pitch = mc.thePlayer.rotationPitch;
				
				Option<Float> reach = getOption("reach");
				
				List<EntityLivingBase> targets = new ArrayList<>();
				for (Object o : mc.theWorld.getLoadedEntityList()) {
					if (o instanceof EntityLivingBase) {
						EntityLivingBase entity = (EntityLivingBase) o;
						if (entity != mc.thePlayer &&
								entity.isEntityAlive() &&
								entity.getDistanceToEntity(mc.thePlayer) < reach.t() &&
								entity.canEntityBeSeen(mc.thePlayer)) {
							targets.add(entity);
						}
					}
				}

				target = null;
				if (!targets.isEmpty()) {
					Collections.sort(targets, new NearestSort());
					target = targets.get(0);
				}
				
				break;
			case POST:
				if (target != null && canAttack()) {
					mc.playerController.attackEntity(mc.thePlayer, target);
					mc.thePlayer.swingArm(EnumHand.MAIN_HAND);
				}
				
				mc.thePlayer.rotationYaw = yaw;
				mc.thePlayer.rotationPitch = pitch;
				break;
			}
		} else if (event instanceof EventRender) {
			switch (event.type()) {
			case RENDER3D:
				boolean depth = GlStateManager.getDepth();
				GlStateManager.disableDepth();
				
				if (target != null) {
					AxisAlignedBB aabb = target.getEntityBoundingBox();
					
					aabb.addCoord(MathUtil.getPartialInc(target.posX, target.lastTickPosX, mc.timer.renderPartialTicks),
							MathUtil.getPartialInc(target.posY, target.lastTickPosY, mc.timer.renderPartialTicks),
							MathUtil.getPartialInc(target.posZ, target.lastTickPosZ, mc.timer.renderPartialTicks));
					
					/*Render.cube(aabb.minX, aabb.minY, aabb.minZ,
							aabb.maxX, aabb.maxY, aabb.maxZ,
							0xFF4080FF, 0x80000000, 1);*/
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
    }
	
	private boolean canAttack() {
		return mc.thePlayer.getAttackTime(0) == 1;
	}

	private class NormalSort implements Comparator<EntityLivingBase> {
		@Override
		public int compare(EntityLivingBase e1, EntityLivingBase e2) {
			return 0;
		}
	}
	
	private class NearestSort implements Comparator<EntityLivingBase> {
		@Override
		public int compare(EntityLivingBase e1, EntityLivingBase e2) {
			return 0;
		}
	}
}
