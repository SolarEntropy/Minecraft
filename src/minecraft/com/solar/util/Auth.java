package com.solar.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

public class Auth {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int setSessionData(String user, String pass) {
        if (pass.length() != 0) {
            YggdrasilAuthenticationService authentication = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) authentication
                    .createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(user);
            auth.setPassword(pass);
            try {
                auth.logIn();
                mc.session = new Session(auth.getSelectedProfile().getName(),
                        auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "legacy");
                return 1;
            } catch (Exception ignored) {
            }
            return 0;
        } else {
            mc.session = new Session(user, "", "", "legacy");
            return 2;
        }
    }
}
