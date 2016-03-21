package com.solar.mod.impl.inventory;

import com.solar.util.values.ValueSin;
import com.solar.util.values.ValueTri;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class InventoryFood extends InventoryItem<Void> {

	public InventoryFood() {
		super(9, 44);
	}

	@Override
	protected ValueTri<Integer, Float, ItemStack> getBestSlotInRange(int slotA, int slotB, Void v) {
		ItemStack held = mc.thePlayer.getHeldItemMainhand();
		if (held != null && held.getItem() != null && held.getItem() instanceof ItemFood) {
			return new ValueTri(-1, -1f, null);
		}
		
		int slot = -1;
		ItemStack stack = null;
		for (int i = Math.min(slotA, slotB); i <= Math.max(slotA, slotB); i++) {
			Slot s = mc.thePlayer.inventoryContainer.inventorySlots.get(i);
			if (s.getHasStack()) {
				ItemStack stackInSlot = s.getStack();
				if (stackInSlot.getItem() instanceof ItemFood) {
					slot = i;
					stack = stackInSlot;
					break;
				}
			}
		}
		return new ValueTri(slot, 2f, stack);
	}

}
