package org.tungsten.client.feature.module;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.config.GenericSetting;
import org.tungsten.client.feature.module.config.KeybindSetting;
import org.tungsten.client.feature.registry.ModuleRegistry;
import org.tungsten.client.util.render.notification.Notifications;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericModule {

	protected static final MinecraftClient client = Tungsten.client;
	final List<GenericSetting<?>> settings = new ArrayList<>();
	private final String name;
	private final String description;
	private final String moduleType;
	private int keybind;
	private boolean enabled = false;

	protected Logger logger;

	private int id;


	public GenericModule(String name, String description, @NotNull String moduleType) {
		this.name = name;
		this.description = description;
		this.moduleType = moduleType.toUpperCase();
		this.id = ModuleRegistry.id;
		this.logger = LoggerFactory.getLogger("Module '" + name + "'");
	}

	public GenericSetting<?> getSettingByName(String name) {
		for (GenericSetting<?> gs : settings) {
			if (gs.getName().equals(name)) {
				return gs;
			}
		}
		return null;
	}

	public int getKeybind() {
		return this.keybind;
	}

	public void updateKeybind(int keybind) {
		this.keybind = keybind;
	}

	public List<GenericSetting<?>> getSettings() {
		return this.settings;
	}

	public void registerSettings() {
		for (Field f : this.getClass().getDeclaredFields()) {
			Tungsten.LOGGER.info(f.getName());
			Tungsten.LOGGER.info(String.valueOf(f.getType()));
			if (GenericSetting.class.isAssignableFrom(f.getType())) {
				try {
					if (f.trySetAccessible()) {
						f.setAccessible(true);
						GenericSetting<?> setting = (GenericSetting<?>) f.get(this);
						if (setting != null) {
							Tungsten.LOGGER.info("Setup setting " + setting.getName());
							settings.add(setting);
						}
					} else {
						throw new RuntimeException(
								"Please do not make the settings inside your module private, we need to see them");
					}
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getType() {
		return this.moduleType;
	}

	public int getID() { return this.id; }

	protected abstract void disable();

	protected abstract void enable();

	protected abstract void tickClient();

	public void toggle() {
		enabled = !enabled;
		setEnabled(enabled);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean state) {
		this.enabled = state;
		if (state) {
			Tungsten.eventManager.registerSubscribers(this);
			this.enable();
			Notifications.newNotification("Enabled " + this.name, 3_000L).title("Toggle").build();
		} else {
			Tungsten.eventManager.unregister(this);
			this.disable();
			Notifications.newNotification("Disabled " + this.name, 3_000L).title("Toggle").build();
		}
	}

	public String toHTML() {
		return "";
	}
}
