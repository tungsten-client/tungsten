package org.tungsten.client.mixin;

import me.x150.ul.HtmlScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tungsten.client.Tungsten;
import org.tungsten.client.clickgui.ClickGUI;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    void onInit(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("GUI"), button -> {
            ClickGUI gui = ClickGUI.create();
            this.client.setScreen(gui);
            gui.reload();
        }).dimensions(5, 5, 100, 20).build());
    }
}