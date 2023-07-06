package org.tungsten.client.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.event.PushOutOfBlockEvent;
import org.tungsten.client.util.ClientPlayerEntityInterface;
import org.tungsten.client.util.Goal;
import org.tungsten.client.util.Rotations;
import org.tungsten.client.event.PlayerMoveEvent;

import static org.tungsten.client.Tungsten.client;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements ClientPlayerEntityInterface {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Redirect(method = "sendMovementPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F"))
    float tungsten_replaceMovementPacketYaw(ClientPlayerEntity instance) {

        Goal currentGoal = Rotations.getCurrentGoal();
        if (currentGoal != null) {
            Rotations.Rotation compute = currentGoal.compute();
            return compute == null ? instance.getYaw() : compute.yaw();
        } else {
            return instance.getYaw();
        }
    }

    @Redirect(method = "sendMovementPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F"))
    float tungsten_replaceMovementPacketPitch(ClientPlayerEntity instance) {
        Goal currentGoal = Rotations.getCurrentGoal();
        if (currentGoal != null) {
            Rotations.Rotation compute = currentGoal.compute();
            return compute == null ? instance.getPitch() : compute.pitch();
        } else {
            return instance.getPitch();
        }
    }


    @Inject(at = @At("HEAD"), cancellable = true, method = "pushOutOfBlocks")
    public void onPushOutOfBlocks(double x, double y, CallbackInfo ci) {
        PushOutOfBlockEvent pob = new PushOutOfBlockEvent();
//        Events.emitEvent(pob);

//        if (pob.isCancelled()) {
//            ci.cancel();
//        }
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void sendMovementPackets(CallbackInfo ci) {
//        if(ModuleRegistry.getByClass(PacketFly.class).isEnabled()){
//            client.player.setVelocity(Vec3d.ZERO);
//            ci.cancel();
//        }
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void move(MovementType type, Vec3d movement, CallbackInfo ci) {
//        if(ModuleRegistry.getByClass(PacketFly.class).isEnabled()){
//            ci.cancel();
//        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "move")
    public void onMove(MovementType mov, Vec3d pos, CallbackInfo ci) {
//        PlayerMoveEvent pm = new PlayerMoveEvent();
//        Events.emitEvent(pm);

//        if (pm.getCancelled()) {
//            ci.cancel();
//        }
    }


    @Override
    public void setNoClip(boolean noClip) {
        this.noClip = noClip;
    }

    @Override
    public void setMovementMultiplier(Vec3d movementMultiplier) {

    }
}