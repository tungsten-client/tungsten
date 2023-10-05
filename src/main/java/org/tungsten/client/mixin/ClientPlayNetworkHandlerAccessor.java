package org.tungsten.client.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.message.LastSeenMessagesCollector;
import net.minecraft.network.message.MessageChain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayNetworkHandler.class)
public interface ClientPlayNetworkHandlerAccessor {
    @Accessor("lastSeenMessagesCollector")
    LastSeenMessagesCollector getLastSeenMessages();

    @Accessor("messagePacker")
    MessageChain.Packer getMessagePacker();
}

