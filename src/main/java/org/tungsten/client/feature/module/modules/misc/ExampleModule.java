package org.tungsten.client.feature.module.modules.misc;

import net.minecraft.SharedConstants;
import net.minecraft.text.Text;
//import org.lwjgl.glfw.GLFW;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.config.ButtonSetting;
import org.tungsten.client.feature.module.config.CheckboxSetting;
import org.tungsten.client.feature.module.config.SliderSetting;
import org.tungsten.client.feature.module.config.TextboxSetting;

public class ExampleModule extends GenericModule {

        CheckboxSetting checkboxSetting = new CheckboxSetting(true,"CheckboxSetting","Example checkbox.");

        SliderSetting sliderSetting = new SliderSetting(5,1,10,"SliderSetting","Example slider.");

        TextboxSetting textboxSetting = new TextboxSetting("Example", "TextboxSetting","Example textbox.");

        ButtonSetting buttonSetting = new ButtonSetting("ExampleButton","Example Button", () -> {
                if(Tungsten.client.player != null) {
                        Tungsten.client.player.sendMessage(Text.of("CheckBox: "
                                + checkboxSetting.getValue().toString() + "Slider: "
                                + sliderSetting.getValue().toString() + "Text: " + SharedConstants.stripInvalidChars(textboxSetting.getValue())));
                }
        });

        public ExampleModule() {
                super("Example", "A really really really really really really long example module description.", "Misc");
                // -- Add default GLFW keybind.
                //this.updateKeybind(GLFW.GLFW_KEY_M);
                this.registerSettings();
        }

        @Override
        protected void enable() {
                System.out.println("Module Enabled.");
        }

        @Override
        protected void disable() {
                System.out.println("Module Disabled.");
        }

        @Override
        protected void tickClient() {

        }
}
