package org.tungsten.client.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public class ClientOptionsMixin {
    public boolean isFullbright;
    double setBrightnessLevel;

    @Inject(method="getGamma", at=@At("HEAD"))
    void onGetGamma(CallbackInfoReturnable<SimpleOption<Double>> cir){
        if(isFullbright) {
            SimpleOption<Double> real = cir.getReturnValue();
            if(real != null) {
                real.setValue(this.setBrightnessLevel
                        //default 12?
                        );
                cir.setReturnValue(real);
            }
        }
    }
}

