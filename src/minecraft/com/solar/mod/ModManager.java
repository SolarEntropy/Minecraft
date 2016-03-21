package com.solar.mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.solar.event.Event;
import com.solar.event.Events;
import com.solar.util.Option;
import com.solar.util.values.ValueDuo;

import net.minecraft.client.Minecraft;

public class ModManager {

    private Minecraft mc;
    private Map<String, Mod> mods = new LinkedHashMap<>();

    public ModManager(Minecraft mc) {
        this.mc = mc;

        Reflections reflections = new Reflections("com.solar.mod.impl");
        Set<Class<? extends Mod>> mods = reflections.getSubTypesOf(Mod.class);
        for (Class c : mods) {
            try {
                Mod mod = (Mod) c.newInstance();
                this.mods.put(mod.name(), mod);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Mod> mods() {
        return mods;
    }

    public Map<String, Mod> enabled() {
        Map<String, Mod> enabled = new LinkedHashMap<>();
        for (String k : mods.keySet()) {
            Mod mod = mods.get(k);
            if (mod.active()) {
                enabled.put(k, mod);
            }
        }
        return enabled;
    }

    public void event(Event event) {
    	List<Mod> enabled = new ArrayList<>();
        for (Mod mod : enabled().values()) {
        	enabled.add(mod);
        }
        if (event.type().reverse()) {
        	Collections.reverse(enabled);
        }
        for (Mod mod : enabled) {
            try {
                if (mod.getClass().getMethod("event", Event.class).isAnnotationPresent(Events.class)) {
                    Events events = mod.getClass().getMethod("event", Event.class).getAnnotation(Events.class);
                    List<Class> eventsList = Arrays.asList(events.events());
                    if (!eventsList.isEmpty() && eventsList.contains(event.getClass())) {
                        mod.event(event);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void input(boolean action, int button) {
    	Mod mod = null;
    	for (Mod m : mods.values()) {
    		Option<Integer> keybind = m.getOption("keybind");
    		if (keybind.t() == button) {
    			mod = m;
    			break;
    		}
    	}
    	if (mod != null) {
    		Option<Integer> keybind = mod.getOption("keybind");
    		Option<ValueDuo<Integer, String[]>> mode = keybind.getOption("mode");
    		switch (mode.t().t()) {
    		case 0:
    			if (action) {
    				mod.toggle();
    			}
    			break;
    		case 1:
    			if (action) {
    				mod.enable();
    			} else {
    				mod.disable();
    			}
    			break;
    		}
    	}
    }

}
