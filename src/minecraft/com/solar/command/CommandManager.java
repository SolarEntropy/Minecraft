package com.solar.command;

import com.solar.command.impl.Command;
import com.solar.command.impl.CommandInfo;
import net.minecraft.client.Minecraft;
import org.reflections.Reflections;

import java.util.*;

public class CommandManager {

    private Minecraft mc;
    private List<Command> commands = new ArrayList<>();

    public CommandManager(Minecraft mc) {
        this.mc = mc;

        Reflections reflections = new Reflections("com.solar.command.impl");
        Set<Class<?>> commands = reflections.getTypesAnnotatedWith(CommandInfo.class);
        for (Class c : commands) {
            try {
                Command command = (Command) c.newInstance();
                this.commands.add(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Command> commands() {
        return commands;
    }

    public void execute(String line) {
        String[] arguments = line.split(" ");
        if (arguments.length > 0) {
            String base = arguments[0];
            String[] argsOnly = new String[arguments.length - 1];
            for (int i = 1; i < arguments.length; i++) {
                argsOnly[i - 1] = arguments[i];
            }

            for (Command command : commands) {
                if (command.isCommand(base)) {
                    command.execute(argsOnly);
                }
            }
        }
    }

}
