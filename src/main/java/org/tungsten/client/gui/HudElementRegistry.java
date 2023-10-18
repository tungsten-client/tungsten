package org.tungsten.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.font.FontRenderer;
import me.x150.renderer.render.Renderer2d;
import net.minecraft.client.util.Window;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.registry.ModuleRegistry;
import org.tungsten.client.util.Texture;
import org.tungsten.client.util.render.FontUtils;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

import static org.tungsten.client.feature.module.modules.render.Hud.*;

public class HudElementRegistry {
    private static FontRenderer defaultFontRenderer;
    private static java.util.List<GenericModule> sortedMods = List.of();
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

    public static void renderModuleList() {
        if (Tungsten.client.currentScreen == null || Tungsten.client.currentScreen instanceof HudEditorGui) {
            float fontSize = 12F;
            if (HudElementRegistry.defaultFontRenderer == null) {
                try { HudElementRegistry.defaultFontRenderer = new FontRenderer(new Font[] { FontUtils.primaryFont }, fontSize); // Replace with Hud.fontSize when sliderSetting is working.
                } catch (Exception e) { throw new RuntimeException(e); }
            }
            Window window = Tungsten.client.getWindow();
            if (HudElementRegistry.sortedMods.isEmpty()) {
                HudElementRegistry.sortedMods = ModuleRegistry.modules.stream().sorted(
                        Comparator.comparingDouble(
                        value -> -HudElementRegistry.defaultFontRenderer.getStringWidth(value.getName())
                        )).toList();
            }
            float y = 5;
            for (GenericModule sortedMod : HudElementRegistry.sortedMods) {
                if (sortedMod.isEnabled()) {
                    float stringWidth = HudElementRegistry.defaultFontRenderer.getStringWidth(sortedMod.getName());
                    HudElementRegistry.defaultFontRenderer.drawString(
                            // MatrixStack
                            Tungsten.stack,
                            // String
                            sortedMod.getName(),
                            // X Coord
                            window.getScaledWidth() - 5 - stringWidth,
                            // Y Coord
                            y,
                            // All colors are as an RGB percent.
                            // Red
                            1.F,
                            // Green
                            0.953F,
                            // Blue
                            0.773F,
                            // Alpha (Percentage)
                            1.F
                    );
                    y += HudElementRegistry.defaultFontRenderer.getStringHeight(sortedMod.getName());
                }
            }
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
