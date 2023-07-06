package org.tungsten.client.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.MathHelper;
import org.tungsten.client.util.ClientPlayerInteractionInterface;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionMixin implements ClientPlayerInteractionInterface {
    public float playerReachDistance;
    public boolean isMoreReach;
    public boolean toggleMoreReach;
    public boolean noBlockBreakDelay;
    @Shadow
    private int blockBreakingCooldown;

    @Inject(method = { "getReachDistance()F" }, at = { @At("HEAD") }, cancellable = true)
    private void onReachDistance(CallbackInfoReturnable<Float> cir) {
        if (toggleMoreReach) {
            cir.setReturnValue(playerReachDistance);
        }
    }

    @Inject(method = { "hasExtendedReach()Z" }, at = { @At("HEAD") }, cancellable = true)
    private void onExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        if (isMoreReach) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", opcode = Opcodes.GETFIELD, ordinal = 0))
    public int mocha_overwriteCooldown(ClientPlayerInteractionManager clientPlayerInteractionManager) {
        int cd = this.blockBreakingCooldown;
        return Objects.requireNonNull(noBlockBreakDelay) ? 0 : cd;
    }

    @Override
    public void setBlockHitDelay(int delay) {
        blockBreakingCooldown = delay;
    }


    @Unique
    private float lastBreakingProgress;

    @Shadow
    @Final
    private float currentBreakingProgress;

//    @Override
//    public float getBreakProgress(float tickDelta) {
//        if (this.currentBreakingProgress == 0) {
//            return 0;
//        }
//        return MathHelper.lerp(tickDelta, this.lastBreakingProgress, this.currentBreakingProgress);
//    }

    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;calcBlockBreakingDelta(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"))
    public void updateLastBreakingProgress(CallbackInfoReturnable<Boolean> ci) {
        this.lastBreakingProgress = this.currentBreakingProgress;
    }
}

