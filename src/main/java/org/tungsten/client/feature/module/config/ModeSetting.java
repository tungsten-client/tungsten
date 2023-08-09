package org.tungsten.client.feature.module.config;

import org.tungsten.client.Tungsten;

import java.util.HashMap;
import java.util.Map;

public class ModeSetting<T extends Enum<?>> extends GenericSetting<T> {
    public T[] modes;
    public Map<String, T> modesMap = new HashMap<>();

    public ModeSetting(T defaultValue, String name, String description) {
        super(defaultValue, name, description);
        try {
            modes = (T[]) defaultValue.getClass().getMethod("values").invoke(null);
            for(T mode : modes) {
                modesMap.put(mode.name(), mode);
            }
        } catch (Exception e) {
            Tungsten.LOGGER.info(e.toString());
        }
    }

    @Override
    public String toHTML() {
        String html = this.getDescriptor() + "<label for=\"modes\" class=\"modes\">Mode: <select name=\"modes\" id=\"modes\">";
        for(T mode : modes) {
            if(this.getValue() == mode) {
                html += "<option value=\"" + mode.name() + "\" selected>" + mode.name() + "</option>";
            }
            else {
                html += "<option value=\"" + mode.name() + "\">" + mode.name() + "</option>";
            }
        }
        html += "</select></label>";
        return html;
    }
}
