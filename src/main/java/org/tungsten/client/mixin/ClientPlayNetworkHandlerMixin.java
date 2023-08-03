package org.tungsten.client.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.command.GenericCommand;
import org.tungsten.client.feature.registry.CommandRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        String prefix = Tungsten.config.getConfig().Prefix.getValue();
        String[] split = message.split("(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\\s+");

        if(!message.startsWith(prefix)) return;

        ci.cancel();

        GenericCommand instance = CommandRegistry.getByName(split[0].replaceFirst(prefix,""));

        if(instance==null) return;

        String[] args = new String[split.length - 1];
        try{
            System.arraycopy(split, 1, args, 0, args.length);
        } catch(Exception ex) {
            args = new String[0];
        }

        instance.execute(args);
    }
}
