package org.tungsten.client.mixin;

import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;

// TODO 19.07.23 11:33: proper module impl to make this work
@Mixin(DisplayEntity.class)
public class DisplayEntityMixin {
//    public boolean antiCrashing = false;

//    @Inject(method="getShadowRadius", at=@At("HEAD"), cancellable = true)
//    public void shouldRender(CallbackInfoReturnable<Float> cir){
//        if(cir.getReturnValue() > 10F && antiCrashing){
//            cir.setReturnValue(1F);
//        }
//    }

//    @Inject(method="setShadowRadius", at=@At("HEAD"), cancellable = true)
//    public void setShadowRadius(float shadowRadius, CallbackInfo ci){
//        if(shadowRadius > 10F && antiCrashing){
//            ci.cancel();
//        }
//    }
}

