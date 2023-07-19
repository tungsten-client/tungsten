package org.tungsten.client.feature.registry;

import org.tungsten.client.feature.command.GenericCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {

	public static List<GenericCommand> commands = new ArrayList<>();

	public static void addCommand(GenericCommand command) {
		commands.add(command);
	}

	public GenericCommand getByName(String name) {
		for (GenericCommand c : commands) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public GenericCommand instanceFromClass(Class<?> clazz) {
		for (GenericCommand c : commands) {
			if (c.getClass().equals(clazz)) {
				return c;
			}
		}
		return null;
	}

}
