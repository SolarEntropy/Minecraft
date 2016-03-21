package com.solar.hook;

import com.solar.Solar;
import com.solar.event.Event;
import com.solar.event.EventPlayerLiving;
import com.solar.event.EventPlayerMoveDirection;
import com.solar.event.EventPlayerUpdate;
import com.solar.event.EventPlayerUpdateWalking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HEntityPlayerSP extends EntityPlayerSP {

    public HEntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile) {
        super(mcIn, worldIn, netHandler, statFile);
    }
    
    @Override
    public void onUpdate() {
    	EventPlayerUpdate ePlayerUpdate = new EventPlayerUpdate(Event.Type.PRE);
    	Solar.getInstance().modManager().event(ePlayerUpdate);
    	
    	if (!ePlayerUpdate.isCancelled()) {
	    	super.onUpdate();
	    	
	    	ePlayerUpdate.type(Event.Type.POST);
	    	Solar.getInstance().modManager().event(ePlayerUpdate);
    	}
    }
    
    @Override
    public void onUpdateWalkingPlayer() {
    	EventPlayerUpdateWalking ePlayerUpdateWalking = new EventPlayerUpdateWalking(Event.Type.PRE);
    	Solar.getInstance().modManager().event(ePlayerUpdateWalking);
    	
    	if (!ePlayerUpdateWalking.isCancelled()) {
	    	super.onUpdateWalkingPlayer();
	
	    	ePlayerUpdateWalking.type(Event.Type.POST);
	    	Solar.getInstance().modManager().event(ePlayerUpdateWalking);
    	}
    }

    @Override
    public void sendChatMessage(String message) {
        if (message.startsWith(".")) {
            Solar.getInstance().commandManager().execute(message.substring(1));
            return;
        }
        this.sendQueue.addToSendQueue(new CPacketChatMessage(message));
    }
    
    @Override
    public void closeScreen() {
    	super.closeScreen();
    }
    
    @Override
    public void closeScreenAndDropStack() {
    	super.closeScreenAndDropStack();
    }
    
    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z) {
    	return super.pushOutOfBlocks(x, y, z);
    }
    
    @Override
    protected boolean isOpenBlockSpace(BlockPos pos) {
    	return super.isOpenBlockSpace(pos);
    }
    
    @Override
    public float getHorseJumpPower() {
    	return super.getHorseJumpPower();
    }
    
    @Override
    public void openEditSign(TileEntitySign signTile) {
    	super.openEditSign(signTile);
    }
    
    @Override
    public void onLivingUpdate() {
    	EventPlayerLiving ePlayerLiving= new EventPlayerLiving(Event.Type.PRE);
    	Solar.getInstance().modManager().event(ePlayerLiving);
    	
    	if (!ePlayerLiving.isCancelled()) {
	    	super.onLivingUpdate();
	    	
	    	ePlayerLiving.type(Event.Type.POST);
	    	Solar.getInstance().modManager().event(ePlayerLiving);
    	}
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        EventPlayerMoveDirection ePlayerMoveDirection = new EventPlayerMoveDirection(Event.Type.PRE, strafe, forward);
        Solar.getInstance().modManager().event(ePlayerMoveDirection);

        if (!ePlayerMoveDirection.isCancelled()) {
            super.moveEntityWithHeading(ePlayerMoveDirection.strafe(), ePlayerMoveDirection.forward());

            ePlayerMoveDirection.type(Event.Type.POST);
            Solar.getInstance().modManager().event(ePlayerMoveDirection);
        }
    }
}
