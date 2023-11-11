package org.tungsten.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.Tungsten;
import org.tungsten.client.event.GameTickEvent;
import org.tungsten.client.event.WorldChangeEvent;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(method = "tick", at = @At("HEAD"))
	void onGameTick(CallbackInfo ci) {
		Tungsten.EVENT_BUS.post(GameTickEvent.EVENT);
	}

	@Inject(method = "setWorld", at = @At("RETURN"))
	public void injectWorldEvent(final ClientWorld world, final CallbackInfo ci) {
		WorldChangeEvent event = WorldChangeEvent.EVENT;
		Tungsten.EVENT_BUS.post(event);
	}
}
