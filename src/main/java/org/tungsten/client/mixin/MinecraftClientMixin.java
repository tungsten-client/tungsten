package org.tungsten.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method="tick", at=@At("HEAD"), cancellable = false)
    void onGameTick(CallbackInfo ci){
        //maybe we could implement an org.tungsten.client.event for this
    }
}
