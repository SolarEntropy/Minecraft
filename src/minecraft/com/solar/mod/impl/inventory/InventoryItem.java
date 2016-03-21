package com.solar.mod.impl.inventory;

import com.solar.util.Timer;
import com.solar.util.values.ValueSin;
import com.solar.util.values.ValueTri;

import net.minecraft.inventory.ClickType;

public abstract class InventoryItem<T> implements InventoryHandler<T> {

	protected final int DELAY = 250;
	protected Timer timer = new Timer();
	
	protected int min, max;
	
	public InventoryItem(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public boolean update(T t) {
		ValueTri<Integer, Float, Void> slot = getBestSlotInRange(min, max, t);

		if (slot.t() != -1 && slot.u() > 1 && slot.v() != null) {
			if (mc.thePlayer.inventory.currentItem != slot.t() - 36 && timer.isTime(DELAY)) {
				timer.reset();
				mc.playerController.clickWindow(mc.thePlayer.inventoryContainer.windowId, slot.t(), mc.thePlayer.inventory.currentItem, ClickType.SWAP, mc.thePlayer);
				return true;
			}
		}
		return false;
	}
	
	protected abstract ValueTri getBestSlotInRange(int slotA, int slotB, T t);

}
