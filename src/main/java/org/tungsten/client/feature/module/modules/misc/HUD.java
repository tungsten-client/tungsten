package org.tungsten.client.feature.module.modules.misc;

import me.x150.MessageSubscription;
import org.tungsten.client.Tungsten;
import org.tungsten.client.event.RenderEvent;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.config.ButtonSetting;
import org.tungsten.client.feature.module.config.SliderSetting;
import org.tungsten.client.gui.HudEditorGui;
import org.tungsten.client.gui.HudElementRegistry;

public class HUD extends GenericModule {
    public HUD() {
        super("HUD", "Enable default HUD elements.", "RENDER");
        this.registerSettings();
    }
    static SliderSetting alpha = new SliderSetting(100D, 1D, 100D, "Opacity", "Change the opacity of the Tungsten logo. \n100 = No transparency\n1 = Barely visible");
    ButtonSetting edit = new ButtonSetting("Edit HUD", "Edit Elements", () -> client.setScreen(new HudEditorGui(client.currentScreen)));
    ButtonSetting reset = new ButtonSetting("Reset HUD", "Reset Sizes", HudElementRegistry::resetLogoSize);
    @Override
    protected void disable() {

    }
    public static int logoX = -5; // In HudEditorGui change pos when dragging these
    public static int logoY = -15;
    public static int resizeX = logoX + 90;
    public static int resizeY = logoY + 90;
    public static int resizeWidth = 10;
    public static int resizeHeight = 10;
    public static int logoWidth = 100;
    public static int logoHeight = 100;
    public static float imgAlpha = alpha.getValue().floatValue();
    @Override
    protected void enable() {

    }

    @Override
    protected void tickClient() {

    }

    @MessageSubscription
    void onRender(RenderEvent.HudNoMSAA event) {
        // Hard code in resize arrow to be in bottom opposite of left of the logo.
        resizeX = logoX + (logoWidth - 10);
        resizeY = logoY + (logoHeight - 10);

        if(client.currentScreen == null ) HudElementRegistry.renderLogo();
        if(client.currentScreen instanceof HudEditorGui) {
            if (HudEditorGui.selected) HudElementRegistry.highlightLogo();
            else HudElementRegistry.drawResizeTriangle();
        }
    }
        // TODO: Code in screen to align elements. Prio: 3/10 ||| UPDATE: 50% Implemented correctly. Still need to save positions to file.
        // TODO: Add module list. Prio: 4/10
        // TODO: Code toasts system. Prio: 4/10
}
