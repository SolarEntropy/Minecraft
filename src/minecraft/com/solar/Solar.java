package com.solar;

import java.util.Random;

import org.lwjgl.opengl.Display;

import com.solar.alert.AlertManager;
import com.solar.command.CommandManager;
import com.solar.event.Event;
import com.solar.event.EventRender;
import com.solar.gui.GuiManager;
import com.solar.gui.font.Font;
import com.solar.gui.font.FontManager;
import com.solar.mod.ModManager;
import com.solar.util.InputManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class Solar {

    private static Solar instance = new Solar();

    public static Solar getInstance() {
        return instance;
    }

    private Minecraft mc;

    private GuiManager guiManager;
    private AlertManager alertManager;
    private ModManager modManager;
    private CommandManager commandManager;
    private InputManager inputManager;
    private FontManager fontManager;

    public void construct(Minecraft mc) {
        this.mc = mc;
        guiManager = new GuiManager(mc);
        alertManager = new AlertManager(mc);
        commandManager = new CommandManager(mc);
        modManager = new ModManager(mc);
        inputManager = new InputManager();
        fontManager = new FontManager();
    }

    public void update() {
        alertManager().update();
        guiManager().update();
    }

    public void render2D() {
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, Display.getWidth(), Display.getHeight(), 0.0D, -1, 1);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.enableBlend();
        
        guiManager().render();
        alertManager().render();

        Font verdana = fontManager().getFont(FontManager.FontFile.VERDANA, FontManager.FontSize.XS);
    	float x = Display.getWidth() - 4;
    	float y = 4;
        for (String k : modManager().enabled().keySet()) {
        	Random random = new Random(Math.abs(k.hashCode()));
        	verdana.drawString("\u00a7" + ("abcde".charAt(random.nextInt(5))) + k, x - verdana.getWidth(k), y, -1, false);
        	y+= verdana.getHeight();
        }
		
		Font fontAwesome = fontManager().getFont(FontManager.FontFile.FONTAWESOME, FontManager.FontSize.XXL);
		fontAwesome.drawString(FontAwesome.FA_SUN_O.toString(), 16, 16, -1, false);
        
        EventRender eRender = new EventRender(Event.Type.RENDER2D);
        modManager().event(eRender);
    }

    public void destruct() {

    }
    
    public GuiManager guiManager() {
    	return guiManager;
    }

    public AlertManager alertManager() {
        return alertManager;
    }

    public ModManager modManager() {
        return modManager;
    }

    public CommandManager commandManager() {
        return commandManager;
    }
    
    public InputManager inputManager() {
    	return inputManager;
    }
    
    public FontManager fontManager() {
    	return fontManager;
    }
}
