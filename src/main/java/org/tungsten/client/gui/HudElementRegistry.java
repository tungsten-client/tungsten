package org.tungsten.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.font.FontRenderer;
import me.x150.renderer.render.Renderer2d;
import org.tungsten.client.Tungsten;
import org.tungsten.client.util.Texture;
import org.tungsten.client.util.render.notification.NotificationUtil;

import java.awt.*;

import static org.tungsten.client.feature.module.modules.render.Hud.*;
import static org.tungsten.client.util.render.FontUtils.primaryFont;

public class HudElementRegistry {

    public static void renderLogo() {
        if (Tungsten.client.currentScreen == null || Tungsten.client.currentScreen instanceof HudEditorGui) {
            float alpha = imgAlpha / 100F;
            RenderSystem.setShaderTexture(0, new Texture("icon/TungstenNoBG.png"));
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
            RenderSystem.setShaderTexture(0, new Texture("icon/resize_arrow.png"));
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
        HudEditorGui.shouldRenderLogo = true;
    }
}
