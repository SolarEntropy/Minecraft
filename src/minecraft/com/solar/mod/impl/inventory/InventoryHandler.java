package com.solar.mod.impl.inventory;

import net.minecraft.client.Minecraft;

public interface InventoryHandler<T> {

	final Minecraft mc = Minecraft.getMinecraft();
	
	public boolean update(T t);
	
}
