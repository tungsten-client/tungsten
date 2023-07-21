package org.tungsten.client.initializer;

import lombok.SneakyThrows;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.ModuleType;
import org.tungsten.client.feature.module.ModuleTypeManager;
import org.tungsten.client.feature.module.modules.misc.ExampleModule;
import org.tungsten.client.feature.registry.ModuleRegistry;
import org.tungsten.client.util.ModuleClassLoader;
import org.tungsten.client.util.Utils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ModuleInitializer {

	public static final Path MODULES_COMPILED = Tungsten.APPDATA.resolve("mod_tmp");
	private static final Path MODULES = Tungsten.RUNDIR.resolve("modules");

	public static void initModules() {
		Utils.ensureDirectoryIsCreated(MODULES);
		Utils.ensureDirectoryIsCreated(MODULES_COMPILED);
		ModuleRegistry.modules.clear();
		//todo: compile modules into classes and put them in mod_tmp
		searchForModules(MODULES_COMPILED);

		// temp code to force load dummy modules
		ExampleModule exampleModule = new ExampleModule();
		ModuleRegistry.addModule(exampleModule);
		ModuleRegistry.addModule(exampleModule);
		ModuleRegistry.addModule(exampleModule);
		ModuleRegistry.addModule(exampleModule);
		ModuleRegistry.addModule(exampleModule);
		ModuleTypeManager.subscribeModuleType(new ModuleType("Client", 0, 0));
		ModuleTypeManager.subscribeModuleType(new ModuleType("Combat", 210, 0));
		ModuleTypeManager.subscribeModuleType(new ModuleType("Movement", 420, 0));
		ModuleTypeManager.subscribeModuleType(new ModuleType("Player", 630, 0));
		ModuleTypeManager.subscribeModuleType(new ModuleType("Render", 840, 0));
		ModuleTypeManager.subscribeModuleType(new ModuleType("Exploit", 1050, 0));
		ModuleTypeManager.subscribeModuleType(new ModuleType("Misc", 1260, 0));
		Tungsten.LOGGER.info("[TUNGSTEN] Loaded module " + exampleModule.getName());
	}


	/*
		WE ARE LOADING COMPILED CLASSES, NOT COMPILING THEM
	 */
	@SneakyThrows
	private static void searchForModules(Path path) {
		try (Stream<Path> v = Files.walk(path)) {
			v.filter(path1 -> Files.isRegularFile(path1) && path1.toString().endsWith(".class")).forEach(path1 -> {
				try {
					initModule(path1);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
//			v.forEach(path1 -> {
//				if (path1.endsWith(".class")) {
//					try {
//						initModule(path1);
//					} catch (Exception e) {
//						throw new RuntimeException(e);
//					}
//				}
//			});
		}
	}

	public static void initModule(Path path) throws Exception {
		try (InputStream file = Files.newInputStream(path)) {
			byte[] classBytes = file.readAllBytes();
            if (Utils.checkSignature(classBytes)) {
                throw new Exception("invalid class file, did not pass class file signature check");
            }
			Class<?> loadedModule = ModuleClassLoader.getInstance().registerClass(classBytes);
			if (GenericModule.class.isAssignableFrom(loadedModule)) {
				Class<GenericModule> loadedModuleClass = (Class<GenericModule>) loadedModule;
				GenericModule moduleInstance = loadedModuleClass.getDeclaredConstructor().newInstance();
				Tungsten.LOGGER.info("[TUNGSTEN] Loaded module " + moduleInstance.getName());

				ModuleRegistry.addModule(moduleInstance);
				ModuleTypeManager.subscribeModuleType(moduleInstance.getType());
			}
		}
	}
}
