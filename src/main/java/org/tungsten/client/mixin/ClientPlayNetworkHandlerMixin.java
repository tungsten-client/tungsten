package org.tungsten.client.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.Tungsten;
import org.tungsten.client.event.PacketEvent;
import org.tungsten.client.event.Event;


@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListener {

    boolean acknowledgements = true;
    @Inject(at = { @At("HEAD") }, method = { "sendPacket(Lnet/minecraft/network/packet/Packet;)V" }, cancellable = true)
    private void matcha_onSendPacket(Packet<?> packet, CallbackInfo ci) {
        PacketEvent.Sent event = new PacketEvent.Sent(packet);
        Tungsten.eventManager.send(event);
        if (event.getCancelled()) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "acknowledge", at = @At("HEAD"), argsOnly = true, index = 2)
    public boolean acknowledge(boolean value) {
        if (acknowledgements) {
            return false;
        }
        return value;
    }
}
