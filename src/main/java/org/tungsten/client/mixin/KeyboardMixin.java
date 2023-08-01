package org.tungsten.client.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.Tungsten;
import org.tungsten.client.event.KeyboardEvent;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.registry.ModuleRegistry;


@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "onKey", at = @At("RETURN"))
	void onOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		KeyboardEvent ke = new KeyboardEvent(key, modifiers, action);
		Tungsten.eventManager.send(ke);
		if (action == GLFW.GLFW_PRESS && key != -1) { // key = -1 means invalid key mapping, aka a special key like a volume wheel
			if (client.player != null) {
				if(client.currentScreen == null){
					for (GenericModule m : ModuleRegistry.modules) {
						if (m.getKeybind() == key) {
							m.toggle();
						}
					}
				}
			}
		}
	}
}
