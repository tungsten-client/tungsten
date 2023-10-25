package org.tungsten.client.feature.module.config;

public class TextboxSetting extends GenericSetting<String> {


	public TextboxSetting(String defaultValue, String name, String prompt) {
		super(defaultValue, name, prompt);
	}

	@Override
	public String toHTML() {
		return this.getDescriptor() + "\n" + "<label class=\"textbox-container\"><input type=\"text\" class=\"textbox\" placeholder=\"" + this.description + "\" value=\"" + this.getValue() + "\"></label>";
	}
}
