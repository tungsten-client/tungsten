package org.tungsten.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.Tungsten;
import org.tungsten.client.event.RenderEvent;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", shift = At.Shift.BEFORE), method = "render")
    void coffee_postHudRenderNoCheck(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        me.x150.renderer.render.MSAAFramebuffer.use(Math.min(8, me.x150.renderer.render.MSAAFramebuffer.MAX_SAMPLES), () -> Tungsten.eventManager.send(RenderEvent.Hud.INSTANCE));
        Tungsten.eventManager.send(new RenderEvent.HudNoMSAA(RenderSystem.getModelViewStack()));

        //        Events.fireEvent(EventType.HUD_RENDER_NOMSAA, new NonCancellableEvent());
    }
}
