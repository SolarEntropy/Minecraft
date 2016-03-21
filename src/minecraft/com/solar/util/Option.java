package com.solar.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.solar.Solar;
import com.solar.util.values.ValueDuo;
import com.solar.util.values.ValueSin;

public class Option<T> extends ValueSin<T> {

	public static enum Type {
		BOOLEAN("<boolean>"),
		CHOICE("<mode>"),
		FLOAT("<float>"),
		INTEGER("<integer>"),
		KEYBIND("<key>"),
		STRING("<string>"),
		PARENT("");
		
		String argument;
		
		Type(String argument) {
			this.argument = argument;
		}
		
		public String argument() {
			return argument;
		}
	}
	
	private String name, command;
	private Type type;
	private Map<String, Option> options = new LinkedHashMap<>();
	
    public Option(String name, String command, Type type, T t) {
        super(t);
        this.name = name;
        command(command);
        type(type);
    }
    
    public Option(String name, String command, Type type, T t, Option[] options) {
    	this(name, command, type, t);
    	for (Option option : options) {
    		this.options.put(option.name.toLowerCase().replaceAll(" ", ""), option);
    	}
    }
    
    public String name() {
    	return name;
    }
    
    public void command(String command) {
    	this.command = command;
    }
    
    public String command() {
    	return command;
    }
    
    public void type(Type type) {
    	this.type = type;
    }
    
    public Type type() {
    	return type;
    }
    
    public Map<String, Option> options() {
    	return options;
    }
    
    public boolean isCommandMatch(String match) {
    	String[] split = command.split(",");
    	for (String s : split) {
    		if (s.equalsIgnoreCase(match)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public Option getOption(String name) {
    	return options.get(name);
    }
    
    public void setOption(String argument) {
    	String valueDisplay = null;
    	T value = null;
		try {
			switch (type()) {
			case BOOLEAN:
				if (argument.equalsIgnoreCase("true") || argument.equalsIgnoreCase("false")) {
					value = (T) Boolean.valueOf(argument);
				} else {
					throw new Exception("invalid boolean value");
				}
				break;
			case CHOICE:
				ValueDuo<Integer, String[]> val = (ValueDuo) t();
				
				int index = -1;
				for (int i = 0; i < val.u().length; i++) {
					String choice = val.u()[i];
					if (choice.equalsIgnoreCase(argument)) {
						index = i;
						break;
					}
				}
				if (index != -1) {
					val.t(index);
					value = (T) val;
					valueDisplay = val.u()[val.t()];
				} else {
					throw new Exception("invalid choice selection");
				}
				break;
			case FLOAT:
				value = (T) Float.valueOf(argument);
				break;
			case INTEGER:
				value = (T) Integer.valueOf(argument);
				break;
			case KEYBIND:
				if (argument.startsWith("button")) {
					value = (T) new Integer(Mouse.getButtonIndex(argument.toUpperCase()) - 100);
					valueDisplay = Mouse.getButtonName((int) value + 100);
				} else {
					value = (T) new Integer(Keyboard.getKeyIndex(argument.toUpperCase()));
					valueDisplay = Keyboard.getKeyName((int) value);
				}
				break;
			case STRING:
				value = (T) argument;
				break;
			case PARENT:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Solar.getInstance().alertManager().add("Invalid value input. Must be: " + type().argument());
		}
		if (value != null) {
			t(value);
			if (valueDisplay == null) {
				valueDisplay = String.valueOf(value);
			}
			Solar.getInstance().alertManager().add(name().toUpperCase() + " value set to " + valueDisplay.toUpperCase());
		}
    }
}
