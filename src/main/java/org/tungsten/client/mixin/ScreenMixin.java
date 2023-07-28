package org.tungsten.client.mixin;


import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.api.ScreenManager;
import org.tungsten.client.api.ScreenVisitor;

@Mixin(Screen.class)
public class ScreenMixin{

    @Inject(method="render", at=@At("HEAD"), cancellable = true)
    void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        for(ScreenVisitor visit : ScreenManager.getVisitors(this.getClass())){
            visit.renderer().onRender(context, mouseX, mouseY, delta, ci, (Screen)(Object) this); //weird casting trickery should be fine since we are in screen, and only subclasses of screen
        }
    }


    @Inject(method="init()V", at=@At("HEAD"), cancellable = false)
    void onInit(CallbackInfo ci){
        for(ScreenVisitor visit : ScreenManager.getVisitors(this.getClass())){
            visit.initializer().onInitialize(ci, (Screen)(Object) this);
        }
    }
}
