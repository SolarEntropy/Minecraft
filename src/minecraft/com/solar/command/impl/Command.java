package com.solar.command.impl;

import java.util.*;

public abstract class Command {

    private List<String> alias = new ArrayList<>();
    private Map<String, String> arguments = new HashMap<>();

    public Command(String alias, String... arguments) {
        this.alias.addAll(Arrays.asList(alias.split(",")));
        if (arguments.length > 0) {
            for (String s : arguments) {
                String[] split = s.split(",");
                this.arguments.put(split[0], s);
            }
        }
    }

    public List<String> alias() {
        return alias;
    }

    public Map<String, String> arguments() {
        return arguments;
    }

    public boolean isCommand(String match) {
        return alias.contains(match);
    }

    public boolean isArgument(String arg, String match) {
        String argument = arguments.get(arg);
        String[] split = argument.split(",");
        for (String a : split) {
            if (a.equalsIgnoreCase(match)) {
                return true;
            }
        }
        return false;
    }

    public abstract void execute(String[] arguments);

}
