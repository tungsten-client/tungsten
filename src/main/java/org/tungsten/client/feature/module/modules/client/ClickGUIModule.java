package org.tungsten.client.feature.module.modules.client;

import org.lwjgl.glfw.GLFW;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.gui.clickgui.ClickGUI;

public class ClickGUIModule extends GenericModule {
    public ClickGUIModule() {
        super("ClickGUI", "Enables the ClickGUI", "CLIENT");
        this.updateKeybind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    protected void disable() {

    }

    @Override
    protected void enable() {
        ClickGUI gui = ClickGUI.create();
        this.client.setScreen(gui);
        gui.reload();
        setEnabled(false);
    }

    @Override
    protected void tickClient() {

    }
}
