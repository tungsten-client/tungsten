package org.tungsten.client.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.tungsten.client.Tungsten;
import org.tungsten.client.gui.clickgui.ClickGUI;

import java.util.HashMap;
import java.util.function.Consumer;

public class VanillaKeybindManager {
    public static final HashMap<KeyBinding, Consumer<MinecraftClient>> keys = new HashMap<>();

    public static void init() {
        Tungsten.LOGGER.info("Registering keybind event");
        // can prob migrate to mixin at some point
        ClientTickEvents.END_CLIENT_TICK.register(client -> keys.forEach((key, event) -> {while(key.wasPressed()) event.accept(client);}));
    }

    public static void createBinds() {
        Tungsten.LOGGER.info("Initializing individual keybinds");
        // individual binds
        initClickGUIBind();
    }

    public static KeyBinding registerKey(String translation, int keyCode, String category, Consumer<MinecraftClient> event) {
        KeyBinding key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                translation,
                InputUtil.Type.KEYSYM,
                keyCode,
                category
        ));

        keys.put(key, event);

        return key;
    }

    // individual binds
    private static void initClickGUIBind() {
        registerKey("Open ClickGUI", GLFW.GLFW_KEY_RIGHT_SHIFT, "Tungsten Client", ClickGUI::setScreen);
    }
}
