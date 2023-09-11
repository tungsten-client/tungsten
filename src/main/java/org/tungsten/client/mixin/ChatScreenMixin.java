package org.tungsten.client.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.registry.CommandRegistry;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    @Unique
    private boolean ignoreChatMessage;

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, boolean addToHistory, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ignoreChatMessage) return;

        String prefix = Tungsten.config.getConfig().Prefix.getValue();

        if (message.startsWith(prefix)) {
            try {
                CommandRegistry.dispatch(message.substring(prefix.length()));
            } catch (CommandSyntaxException e) {
                Tungsten.client.inGameHud.getChatHud().addMessage(Text.literal(e.getMessage()));
            }
            Tungsten.client.inGameHud.getChatHud().addToMessageHistory(message);
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}