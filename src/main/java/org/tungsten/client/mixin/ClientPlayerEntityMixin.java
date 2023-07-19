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
import org.tungsten.client.Tungsten;
import org.tungsten.client.event.PlayerMoveEvent;
import org.tungsten.client.event.PushOutOfBlockEvent;
import org.tungsten.client.util.Rotations;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Redirect(method = "sendMovementPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F"))
	float tungsten_replaceMovementPacketYaw(ClientPlayerEntity instance) {

		Rotations.Goal currentGoal = Rotations.getCurrentGoal();
		if (currentGoal != null) {
			Rotations.Rotation compute = currentGoal.compute();
			return compute == null ? instance.getYaw() : compute.yaw();
		} else {
			return instance.getYaw();
		}
	}

	@Redirect(method = "sendMovementPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F"))
	float tungsten_replaceMovementPacketPitch(ClientPlayerEntity instance) {
		Rotations.Goal currentGoal = Rotations.getCurrentGoal();
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
		Tungsten.eventManager.send(pob);
        if (pob.isCancelled()) {
            ci.cancel();
        }

	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "move")
	public void onMove(MovementType mov, Vec3d delta, CallbackInfo ci) {
		PlayerMoveEvent pm = new PlayerMoveEvent(mov, delta);
		Tungsten.eventManager.send(pm);
		if (pm.isCancelled()) ci.cancel();
	}
}