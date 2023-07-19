package org.tungsten.client.feature.module.config;

public class ButtonSetting extends GenericSetting<Void> {
	Runnable vfuture;

	public ButtonSetting(String name, String innerText, Runnable vfuture) {
		super(null, name, innerText);
		this.vfuture = vfuture;
	}

	public void run() {
		vfuture.run();
	}

	@Override
	public String toHTML() {
		return this.getDescriptor() + "\n" + "<button class=\"button\">" + this.getDescription() + "</button>";
	}
}
