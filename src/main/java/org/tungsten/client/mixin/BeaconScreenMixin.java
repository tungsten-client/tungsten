package org.tungsten.client.mixin;

import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(BeaconScreen.class)
public abstract class BeaconScreenMixin extends HandledScreen {
    //used for beaconspoofer modules.

    public BeaconScreenMixin(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
                Optional<StatusEffect> i = Optional.of(StatusEffect.byRawId(this.effectID));
                client.player.networkHandler.sendPacket(new UpdateBeaconC2SPacket(i, i));
    }
    public int effectID;
}
