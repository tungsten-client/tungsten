package org.tungsten.client.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.event.PacketEvent;
import org.tungsten.client.event.Event;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void tungsten_onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        PacketEvent.Received event = new PacketEvent.Received(packet);

        if (event.getCancelled()) {
            //to cancel specified packets ig?
            ci.cancel();
        }
    }

    @Inject(at = { @At(value = "HEAD") }, method = "send(Lnet/minecraft/network/packet/Packet;)V", cancellable = true)
    private void onSendPacket(Packet<?> pack, CallbackInfo ci) {

    }
}