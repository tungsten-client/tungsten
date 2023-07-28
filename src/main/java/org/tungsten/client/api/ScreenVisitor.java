package org.tungsten.client.api;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ScreenVisitor extends Screen{

    ScreenRenderInterface renderer;
    ScreenInitInterface initializer;

    public ScreenVisitor(){
        super(Text.of(""));
        this.renderer = (context, mouseX, mouseY, delta, ci, sc) -> {

        };

        this.initializer = (ci, sc) -> {

        };
    }

    public void setRenderVisit(ScreenRenderInterface sri){
        this.renderer = sri;
    }

    public void setInitializer(ScreenInitInterface sii){
        this.initializer = sii;
    }


    public ScreenRenderInterface renderer(){
        return this.renderer;
    }

    public ScreenInitInterface initializer(){
        return this.initializer;
    }





}
