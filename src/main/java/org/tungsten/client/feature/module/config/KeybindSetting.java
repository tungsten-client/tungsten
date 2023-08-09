package org.tungsten.client.feature.module.config;

import net.minecraft.client.util.InputUtil;

public class KeybindSetting extends GenericSetting<Integer> {

    public KeybindSetting(Integer defaultValue, String name, String description) {
        super(defaultValue, name, description);
    }

    @Override
    public String toHTML() {
        return this.getDescriptor() + "\n" + "<label for=\"keybind\" class=\"keybind\" style=\"display: flex; align-items: center;\">Bind: <a id=\"keybind\" class=\"keybind-text\" style=\"flex: 1; text-align: center\"> " + (this.getValue() <= 0 ? "None..." : InputUtil.fromKeyCode(this.getValue(), -1).getLocalizedText().getString()) + "</a></label>";
    }
}
