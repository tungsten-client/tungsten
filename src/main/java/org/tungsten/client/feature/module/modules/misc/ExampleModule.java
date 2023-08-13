package org.tungsten.client.feature.module.modules.misc;

import me.x150.MessageSubscription;
import net.minecraft.SharedConstants;
import net.minecraft.text.Text;
//import org.lwjgl.glfw.GLFW;
import org.tungsten.client.Tungsten;
import org.tungsten.client.event.PacketEvent;
import org.tungsten.client.event.PlayerMoveEvent;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.config.*;

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

    ModeSetting modeSetting = new ModeSetting(Mode.mode1, "ExampleMode", "Example mode setting.");

    public ExampleModule() {
        super("Example", "A really really really really really really long example module description.", "MISC");
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

    public enum Mode {
        mode1,
        mode2,
        mode3
    }

    @MessageSubscription
    void onPlayerMove(PlayerMoveEvent event) {
        Tungsten.LOGGER.info(
                "Move type: %s, Requested Delta: %s%n".formatted(event.getType(),event.getRequestedDelta())
        );
    }
}