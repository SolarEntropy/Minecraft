package com.solar.mod.impl.inventory;

import com.solar.util.values.ValueTri;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class InventoryTool extends InventoryItem<IBlockState> {

	public InventoryTool() {
		super(9, 44);
	}

	@Override
	protected ValueTri<Integer, Float, ItemStack> getBestSlotInRange(int slotA, int slotB, IBlockState t) {
		if (t == null) {
			return new ValueTri(-1, -1, null);
		}
		int slot = -1;
		float strength = 0;
		ItemStack stack = null;
		for (int i = Math.min(slotA, slotB); i <= Math.max(slotA, slotB); i++) {
			Slot s = mc.thePlayer.inventoryContainer.inventorySlots.get(i);
			if (s.getHasStack()) {
				ItemStack stackInSlot = s.getStack();
				if (stackInSlot.getStrVsBlock(t) > strength) {
					slot = i;
					strength = stackInSlot.getStrVsBlock(t);
					stack = stackInSlot;
				}
			}
		}
		return new ValueTri(slot, strength, stack);
	}

}
