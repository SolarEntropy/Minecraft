package com.solar.util;

import net.minecraft.client.renderer.GlStateManager;

import static org.lwjgl.opengl.GL11.*;

public class StencilUtil {

	public static void start() {
        glClearStencil(0);
        glClear(GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);
    }

    public static void hide() {
        glStencilFunc(GL_ALWAYS, 1, 0xFFFF);
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        GlStateManager.colorMask(false, false, false, false);
    }

    public static void show() {
        glStencilFunc(GL_NOTEQUAL, 1, 0xFFFF);
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        GlStateManager.colorMask(true, true, true, true);
    }

    public static void end() {
        glDisable(GL_STENCIL_TEST);
    }
	
}
