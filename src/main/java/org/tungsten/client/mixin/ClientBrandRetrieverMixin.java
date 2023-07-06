package org.tungsten.client.mixin;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {
    private static String setClientBrand;
    @Inject(method = "getClientModName", at = @At("HEAD"), cancellable = true, remap = false)
    private static void spoofBrandName(CallbackInfoReturnable<String> callback) {
        callback.setReturnValue(setClientBrand);
    }
    public String setClientBrandName = setClientBrand;
}

