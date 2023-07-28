package org.tungsten.client.api;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface ScreenRenderInterface {
    void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci, Screen screenInstance);

}
