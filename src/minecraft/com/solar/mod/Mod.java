package com.solar.mod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.solar.FontAwesome;
import com.solar.Solar;
import com.solar.command.impl.Command;
import com.solar.command.impl.CommandMod;
import com.solar.event.Event;
import com.solar.util.Option;
import com.solar.util.values.ValueDuo;
import com.solar.util.values.ValueSin;

import net.minecraft.client.Minecraft;

public class Mod {

    protected Minecraft mc = Minecraft.getMinecraft();

    protected ValueSin<String> name = new ValueSin<>();
    protected ValueSin<Boolean> active = new ValueSin<>(false);
    protected Map<String, Option> options = new LinkedHashMap<>();
    protected FontAwesome icon;

    public Mod(String name, FontAwesome icon, String command, String... arguments) {
        this.name.t(name);
        this.icon = icon;
        List<String> args = new ArrayList<>();
        
        for (String a : arguments) {
            args.add(a);
        }
        
        args.add("toggle,t");
        args.add("enable,e");
        args.add("disable,d");

    	options.put("keybind", new Option<Integer>("Keybind", "keybind,key,kb", Option.Type.KEYBIND, Keyboard.KEY_NONE, new Option[] {
    			new Option<ValueDuo<Integer, String[]>>("Mode", "mode,m", Option.Type.CHOICE, new ValueDuo<Integer, String[]>(0, new String[] {"toggle", "hold"}))
    	}));
        buildOptions(args);
    	Solar.getInstance().commandManager().commands().add(new CommandMod(this, command, args.toArray(new String[0])));
    }
    
    protected void buildOptions(List<String> args) {
    	for (String k : options.keySet()) {
    		Option option = options.get(k);
    		args.add(option.command());
    	}
    }

    public String name() {
        return name.t();
    }

    public void name(String name) {
        this.name.t(name);
    }

    public boolean active() {
        return active.t();
    }

    public void active(boolean active) {
        this.active.t(active);
    }

    public void toggle() {
        if (active.t()) {
            disable();
            return;
        }
        enable();
    }

    public void enable() {
        active(true);
    }

    public void disable() {
        active(false);
    }

    public void event(Event event) { }

    public void executeCommand(Command command, String[] arguments) {
        if (arguments.length == 0) {
            toggle();
            Solar.getInstance().alertManager().add(name.t() + (active.t() ? " enabled" : " disabled"));
        } else if (arguments.length == 1) {
            if (command.isArgument("toggle", arguments[0])) {
                toggle();
                Solar.getInstance().alertManager().add(name.t() + (active.t() ? " enabled" : " disabled"));
            } else if (command.isArgument("enable", arguments[0])) {
                enable();
                Solar.getInstance().alertManager().add(name.t() + (active.t() ? " enabled" : " disabled"));
            } else if (command.isArgument("disable", arguments[0])) {
                disable();
                Solar.getInstance().alertManager().add(name.t() + (active.t() ? " enabled" : " disabled"));
            }
        } else if (arguments.length == 2) {
        	for (String k : options.keySet()) {
        		Option option = options.get(k);
        		if (option.isCommandMatch(arguments[0])) {
        			option.setOption(arguments[1]);
        		}
        	}
        } else if (arguments.length == 3) {
        	for (String k : options.keySet()) {
        		Option option = options.get(k);
        		if (option.isCommandMatch(arguments[0])) {
        			for (Object o : option.options().keySet()) {
        				Option option2 = (Option) option.options().get(o);
        				if (option2.isCommandMatch(arguments[1])) {
        					option2.setOption(arguments[2]);
        				}
        			}
        		}
        	}
        }
    }
    
    public Option getOption(String name) {
    	return options.get(name);
    }
}
