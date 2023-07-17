package org.tungsten.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
public boolean fullbright = false;
    @Inject(method = "render", at = @At("RETURN"))
    public void changeGamma(DrawContext context, float tickDelta, CallbackInfo ci) {
        if(fullbright) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.getGamma().setValue(69420.0);
        }else {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.getGamma().setValue(1.0);
        }
    }
}
