package org.tungsten.client.feature.module.config;

public class CheckboxSetting extends GenericSetting<Boolean> {

	public CheckboxSetting(Boolean defaultValue, String name, String description) {
		super(defaultValue, name, description);
	}

	@Override
	public String toHTML() {
		return this.getDescriptor() + "\n" + "<label class=\"checkbox-container\">" + this.getName() + "<input type=\"checkbox\" " + (this.getValue() ? "checked" : "") + "><span class=\"checkmark\"></span></label>";
	}


}
