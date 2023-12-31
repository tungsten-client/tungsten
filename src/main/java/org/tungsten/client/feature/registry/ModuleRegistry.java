package org.tungsten.client.feature.registry;

import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.config.KeybindSetting;

import java.util.ArrayList;
import java.util.List;

public class ModuleRegistry {

	public static List<GenericModule> modules = new ArrayList<>();

	public static int id = 0;

	public static GenericModule getByName(String name) {
		for (GenericModule m : modules) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

	public static GenericModule getByID(int id) {
		return modules.get(id);
	}

	public static GenericModule instanceFromClass(Class<?> clazz) {
		for (GenericModule m : modules) {
			if (m.getClass().equals(clazz)) {
				return m;
			}
		}
		return null;
	}

	public static void addModule(GenericModule module) {
		modules.add(module);
		module.getSettings().add(new KeybindSetting(module, "Keybind", "Keybind for module"));
		id += 1;
	}


}
