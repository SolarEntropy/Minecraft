package com.solar.alert;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.Display;

import com.solar.render.Render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class AlertManager {

    public static final long DEFAULT = 3000;

    private Minecraft mc;
    private List<Alert> alerts = new CopyOnWriteArrayList<>();

    public AlertManager(Minecraft mc) {
        this.mc = mc;
    }

    public void add(String message, long time) {
        add(new Alert(message, time));
    }
    
    public void add(String message) {
    	add(message, DEFAULT);
    }

    public void add(Alert alert) {
        alerts.add(alert);
    }

    public void remove(Alert alert) {
        alerts.remove(alert);
    }

    public void update() {
    	for (Alert a : alerts) {
    		a.update();
    	}
    }

    public void render() {
        GlStateManager.pushMatrix();
        GlStateManager.translate(Display.getWidth() - 4, Display.getHeight() - 4, 0);
        GlStateManager.scale(2, 2, 2);

        int y = 0;
        for (int i = alerts.size() - 1; i >= 0; i--) {
            Alert a = alerts.get(i);

            int x = -2 - mc.fontRendererObj.getStringWidth(a.message());
            y-= mc.fontRendererObj.FONT_HEIGHT;

            Render.rectangle(x - 2, y, 0, y + mc.fontRendererObj.FONT_HEIGHT, 0x80000000, 0);
            mc.fontRendererObj.drawString(a.message(), x, y + 1, 0xFFFFFFFF);
        }
        GlStateManager.popMatrix();
    }

}
