package org.tungsten.client.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
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
        //Tungsten.LOGGER.info(key + " pressed");
        Tungsten.eventManager.send(ke);
        if(action == 1){
            if(client.player != null && client != null){
                for(GenericModule m : ModuleRegistry.modules){
                    if(m.getKeybind() == key){
                        m.toggle();
                    }
                }
            }
        }
    }
}
