package org.tungsten.client.feature.module.config;

import net.minecraft.client.util.InputUtil;
import org.tungsten.client.feature.module.GenericModule;

public class KeybindSetting extends GenericSetting<GenericModule> {

    public KeybindSetting(GenericModule defaultValue, String name, String description) {
        super(defaultValue, name, description);
    }

    @Override
    public String toHTML() {
        return this.getDescriptor() + "\n" + "<label for=\"keybind\" class=\"keybind\" style=\"display: flex; align-items: center;\">Bind: <a id=\"keybind\" class=\"keybind-text\" style=\"flex: 1; text-align: center\" tabindex=\"0\">" + (this.getValue().getKeybind() <= 0 ? "None..." : InputUtil.fromKeyCode(this.getValue().getKeybind(), -1).getLocalizedText().getString()) + "</a></label>";
    }
}
