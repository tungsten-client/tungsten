package org.tungsten.client.mixin;

import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

//please mark this mixin as soon to be unused, this will be overriden and replaced by the screens api in a future update
@Mixin(BeaconScreen.class)
public abstract class BeaconScreenMixin extends HandledScreen<BeaconScreenHandler> {
	//used for beaconspoofer modules.

	@Unique
	public int effectID;

	public BeaconScreenMixin(BeaconScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Inject(method = "init", at = @At("TAIL"))
	protected void init(CallbackInfo ci) {
		Optional<StatusEffect> i = Optional.ofNullable(StatusEffect.byRawId(this.effectID));
		client.player.networkHandler.sendPacket(new UpdateBeaconC2SPacket(i, i));
	}
}
