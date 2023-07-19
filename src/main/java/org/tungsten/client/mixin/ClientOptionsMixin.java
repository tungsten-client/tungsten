package org.tungsten.client.mixin;

import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;

// FIXME 19.07.23 11:22: this doesn't even fucking work?? this will crash
@Mixin(GameOptions.class)
public class ClientOptionsMixin {
//    public boolean isFullbright;
//    public double setBrightnessLevel;
//
//    @Inject(method="getGamma", at=@At("HEAD"))
//    void onGetGamma(CallbackInfoReturnable<SimpleOption<Double>> cir){
//        if(isFullbright) {
//            SimpleOption<Double> real = cir.getReturnValue();
//            if(real != null) {
//                real.setValue(this.setBrightnessLevel
//                        //default 12?
//                        );
//                cir.setReturnValue(real);
//            }
//        }
//    }
}

