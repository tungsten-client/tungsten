package org.tungsten.client.feature.module;

import java.util.ArrayList;
import java.util.List;

public class ModuleTypeManager {

	private static final List<ModuleType> moduleTypes = new ArrayList<>();


	public static void subscribeModuleType(String moduleType) {
		if (!moduleTypes.stream().map(ModuleType::getName).toList().contains(moduleType)) {
			moduleTypes.add(new ModuleType(moduleType));
		}
	}

	public static void subscribeModuleType(String moduleType, int x, int y) {
		if (!moduleTypes.stream().map(ModuleType::getName).toList().contains(moduleType)) {
			moduleTypes.add(new ModuleType(moduleType, x, y));
		}
	}

	public static void subscribeModuleType(ModuleType moduleType) {
		if (!moduleTypes.stream().map(ModuleType::getName).toList().contains(moduleType)) {
			moduleTypes.add(moduleType);
		}
	}

	public static ModuleType getByName(String name) {
		return moduleTypes.stream().filter(moduleType -> moduleType.getName().equals(name)).findFirst().orElseThrow();
	}

	public static List<ModuleType> getModuleTypes() {
		return moduleTypes;
	}

}
