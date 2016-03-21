package com.solar.mod.impl;

import java.util.List;

import com.solar.FontAwesome;
import com.solar.event.Event;
import com.solar.event.EventPlayerUpdate;
import com.solar.event.Events;
import com.solar.mod.Mod;
import com.solar.util.Option;

public class Zoom extends Mod {

	private float zoom;
	private boolean smooth;
	
	public Zoom() {
		super("Zoom", FontAwesome.FA_SEARCH_PLUS, "zoom");
	}
	
	@Override
	protected void buildOptions(List<String> args) {
		options.put("zoom", new Option<Float>("Zoom", "zoom", Option.Type.FLOAT, 2f));
		
		super.buildOptions(args);
	}
	
	@Override
	public void enable() {
		super.enable();
		zoom = mc.gameSettings.fovSetting;
		smooth = mc.gameSettings.smoothCamera;
	}
	
	@Override
	public void disable() {
		mc.gameSettings.fovSetting = zoom;
		mc.gameSettings.smoothCamera = smooth;
		super.disable();
	}
	
	@Override
	@Events(events={EventPlayerUpdate.class})
	public void event(Event event) {
		if (event instanceof EventPlayerUpdate) {
			switch (event.type()) {
			case PRE:
				mc.gameSettings.smoothCamera = true;
				Option<Float> zoom = getOption("zoom");
				mc.gameSettings.fovSetting = this.zoom / zoom.t();
				break;
			}
		}
	}
}
