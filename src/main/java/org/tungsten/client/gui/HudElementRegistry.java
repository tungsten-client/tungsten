package org.tungsten.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.render.Renderer2d;
import me.x150.renderer.util.RendererUtils;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.modules.misc.HUD;
import org.tungsten.client.util.Texture;

import java.awt.*;

import static org.tungsten.client.feature.module.modules.misc.HUD.*;

public class HudElementRegistry {

    public static void renderLogo() {
        if (Tungsten.client.currentScreen == null || Tungsten.client.currentScreen instanceof HudEditorGui) {
            float alpha = imgAlpha / 100F;
            RenderSystem.setShaderTexture(0, new Texture("TungstenNoBG.png"));
            RenderSystem.setShaderColor(1, 1, 1, 0.5F); // Set it to 50% opacity. Once SliderSetting works, add alpha value instead.
            // Render Tungsten LOGO.
            Renderer2d.renderTexture(
                    Tungsten.stack,
                    logoX,
                    logoY,
                    logoWidth,
                    logoHeight
            );
        }
    }

    public static void highlightLogo() {
        if(HudEditorGui.selected) {
            Renderer2d.renderRoundedOutline(
                    Tungsten.stack,
                    Color.GREEN,
                    logoX,
                    logoY,
                    logoX + logoWidth,
                    logoY + logoHeight,
                    0F,
                    1,
                    1F
                    );
        }
    }

    public static void drawResizeTriangle() {
            RenderSystem.setShaderTexture(0, new Texture("resize_arrow.png"));
            RenderSystem.setShaderColor(1, 1, 1, 0.8F);
            Renderer2d.renderTexture(
                    Tungsten.stack,
                    resizeX,
                    resizeY,
                    resizeWidth,
                    resizeHeight
            );
    }

    public static void resetLogoSize() {
        logoX = -5;
        logoY = -15;
        logoWidth = 100;
        logoHeight = 100;
    }
}
