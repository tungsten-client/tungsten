package org.tungsten.client.mixin;

import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DisplayEntity.class)
public class DisplayEntityMixin {
    public boolean antiCrashing = false;
    // todo: Anticrash boolean thingy

    @Inject(method="getShadowRadius", at=@At("HEAD"), cancellable = true)
    public void shouldRender(CallbackInfoReturnable<Float> cir){
        if(cir.getReturnValue() == null) return; //thumbs_up
        if(cir.getReturnValue() > 10F && antiCrashing){
            cir.setReturnValue(1F);
        }
    }

    @Inject(method="setShadowRadius", at=@At("HEAD"), cancellable = true)
    public void setShadowRadius(float shadowRadius, CallbackInfo ci){
        if(shadowRadius > 10F && antiCrashing){
            ci.cancel();
        }
    }
}

