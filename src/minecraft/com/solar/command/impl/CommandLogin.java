package com.solar.command.impl;

import com.solar.Solar;
import com.solar.alert.AlertManager;
import com.solar.util.Auth;

@CommandInfo
public class CommandLogin extends Command {

    private boolean login;

    public CommandLogin() {
        super("login,l");
    }

    @Override
    public void execute(final String[] arguments) {
        if (arguments.length == 2) {
            if (!login) {
                login = true;
                new Thread(new Runnable() {
                	@Override
                	public void run() {
	                    Solar.getInstance().alertManager().add("Logging into " + arguments[0]);
	                    int response = Auth.setSessionData(arguments[0], arguments[1]);
	                    login = false;
	                    Solar.getInstance().alertManager().add(response == 0 ? "Failed to login to " + arguments[0] : response == 1 ? "Successfully logged into " + arguments[0] : "Changed username to " + arguments[0]);
                	}
                }).start();
            } else {
                Solar.getInstance().alertManager().add("Already logging into an account");
            }
        }
    }
}
