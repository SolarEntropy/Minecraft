package com.solar.command.impl;

import com.solar.mod.Mod;
import com.solar.util.Auth;

public class CommandMod extends Command {

    private Mod mod;

    public CommandMod(Mod mod, String command, String... arguments) {
        super(command, arguments);
        this.mod = mod;
    }

    @Override
    public void execute(final String[] arguments) {
        mod.executeCommand(this, arguments);
    }

}
