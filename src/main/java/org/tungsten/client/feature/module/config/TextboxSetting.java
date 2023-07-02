package org.tungsten.client.feature.module.config;

public class TextboxSetting extends GenericSetting<String>{




    public TextboxSetting(String defaultValue, String name, String description) {
        super(defaultValue, name, description);
    }

    @Override
    public String toHTML() {
        return this.getDescriptor() + "\n" + "<input type=\"text\" class=\"textbox\" placeholder=\"" + this.defaultValue+ "\">";
    }
}
