package org.tungsten.client.feature.module.modules.render;

import meteordevelopment.orbit.EventHandler;
import org.tungsten.client.event.RenderEvent;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.config.ButtonSetting;
import org.tungsten.client.feature.module.config.SliderSetting;
import org.tungsten.client.gui.HudEditorGui;
import org.tungsten.client.gui.HudElementRegistry;

public class Hud extends GenericModule {
    public Hud() {
        super("HUD", "Enable default HUD elements.", "RENDER");
        this.registerSettings();
    }
    static SliderSetting alpha = new SliderSetting(100.1D, 1D, 100D, "Opacity", "Change the opacity of the Tungsten logo. \n100 = No transparency\n1 = Barely visible");
    static SliderSetting size = new SliderSetting(6D, 0.1D, 20D, "Font Size", "Change the size of the module list font.");
    ButtonSetting edit = new ButtonSetting("Edit HUD", "Edit Elements", () -> client.setScreen(new HudEditorGui(client.currentScreen)));
    ButtonSetting reset = new ButtonSetting("Reset HUD", "Reset Sizes", HudElementRegistry::resetLogoSize);
    public static int logoX = -5;
    public static int logoY = -15;
    public static int resizeX = logoX + 90;
    public static int resizeY = logoY + 90;
    public static int resizeWidth = 10;
    public static int resizeHeight = 10;
    public static int logoWidth = 100;
    public static int logoHeight = 100;
    public static float fontSize = size.getValue().floatValue();

    @Override
    protected void enable() {
    }

    @Override
    protected void disable() {
    }

    @Override
    protected void tickClient() {
    }

    @EventHandler
    void onRender(RenderEvent.HudNoMSAA event) {
        // Hard code in resize arrow to be in bottom opposite of left of the logo.
        resizeX = logoX + (logoWidth - 10);
        resizeY = logoY + (logoHeight - 10);
        if(Hud.alpha.getValue().floatValue() != 100.1F) HudElementRegistry.alpha = Hud.alpha.getValue().floatValue();
        if(client.currentScreen == null) HudElementRegistry.renderModuleList(event.contextStack);
        if(client.currentScreen == null && HudEditorGui.shouldRenderLogo) HudElementRegistry.renderLogo(event.contextStack);
        if(client.currentScreen instanceof HudEditorGui) {
            if (HudEditorGui.selected) HudElementRegistry.highlightLogo(event.contextStack);
            else HudElementRegistry.drawResizeTriangle(event.contextStack);
        }
    }
        // TODO: Code in screen to align elements. ||| UPDATE: 80% Implemented. Still need to save positions to file.
        // TODO: Add module list. ||| UPDATE: DONE
        // TODO: Code toasts system. ||| UPDATE: DONE
}