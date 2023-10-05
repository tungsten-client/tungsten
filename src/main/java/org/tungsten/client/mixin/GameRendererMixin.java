package org.tungsten.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;

    /**
     * I take out the part of code that moves the hand. Essentially separating that piece of code into it's own option.
     */
    @Inject(at = @At("HEAD"), method = "bobView", cancellable = true)
    private void bobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getBobView()Lnet/minecraft/client/option/SimpleOption;", shift = At.Shift.AFTER))
    private void inject(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        if (client.getCameraEntity() instanceof PlayerEntity playerEntity) {
            float f = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
            float g = -(playerEntity.horizontalSpeed + f * tickDelta);
            float h = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance);
            if (client.options.getPerspective().isFirstPerson() && client.options.getBobView().getValue()) {
                matrices.translate(MathHelper.sin(g * MathHelper.PI) * h * 0.5F, -Math.abs(MathHelper.cos(g * MathHelper.PI) * h), 0.0);
            }
        }
    }
}
