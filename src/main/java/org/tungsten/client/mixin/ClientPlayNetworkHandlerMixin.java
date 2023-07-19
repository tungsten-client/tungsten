package org.tungsten.client.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.Tungsten;
import org.tungsten.client.event.PacketEvent;


@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListener {
	// TODO 19.07.23 11:31: wht the fuck does this even do
// boolean acknowledgements = true;
//    @ModifyVariable(method = "acknowledge", at = @At("HEAD"), argsOnly = true, index = 2)
//    public boolean acknowledge(boolean value) {
//        if (acknowledgements) {
//            return false;
//        }
//        return value;
//    }
}
