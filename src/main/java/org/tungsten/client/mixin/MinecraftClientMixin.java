package org.tungsten.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.Tungsten;
import org.tungsten.client.event.GameTickEvent;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method="tick", at=@At("HEAD"), cancellable = false)
    void onGameTick(CallbackInfo ci){
        Tungsten.eventManager.send(new GameTickEvent());
    }
}
