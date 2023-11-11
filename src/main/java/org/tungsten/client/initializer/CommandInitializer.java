package org.tungsten.client.initializer;

import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.command.GenericCommand;
import org.tungsten.client.feature.command.commands.Settings;
import org.tungsten.client.feature.registry.CommandRegistry;
import org.tungsten.client.util.io.TungstenClassLoader;
import org.tungsten.client.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class CommandInitializer {

	private static final Path COMMANDS = Tungsten.RUNDIR.resolve("commands");
	public static final Path COMMANDS_COMPILED = Tungsten.APPDATA.resolve("cmd_tmp");

	public static void initCommands() {
		Utils.ensureDirectoryIsCreated(COMMANDS);
		Utils.ensureDirectoryIsCreated(COMMANDS_COMPILED);
		//todo: compile modules into classes and put them in mod_tmp
		searchForCommands(COMMANDS_COMPILED);

//		ExampleCommand ex = new ExampleCommand();
//		CommandRegistry.addCommand(ex);
		CommandRegistry.addCommand(new Settings());
	}


	/*
		WE ARE LOADING COMPILED CLASSES, NOT COMPILING THEM
	 */
	private static void searchForCommands(Path path) {
		try (Stream<Path> v = Files.walk(path)) {
			v.filter(path1 -> Files.isRegularFile(path1) && path1.toString().endsWith(".class")).forEach(path1 -> {
				try {
					initCommand(path1);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	private static void initCommand(Path module) throws Exception {
		try (InputStream file = Files.newInputStream(module)) {
			byte[] classBytes = file.readAllBytes();
            if (Utils.checkSignature(classBytes)) {
                throw new IllegalStateException("invalid class file, did not pass class file signature check");
            }
			Class<?> loadedCommand = TungstenClassLoader.getInstance().registerClass(classBytes);
			if (GenericCommand.class.isAssignableFrom(loadedCommand)) {
				Class<GenericCommand> loadedCommandClass = (Class<GenericCommand>) loadedCommand;
				GenericCommand commandInstance = loadedCommandClass.getDeclaredConstructor().newInstance();
				Tungsten.LOGGER.info("Loaded command " + commandInstance.getName());
				CommandRegistry.addCommand(commandInstance);
			}
		}
	}
}
