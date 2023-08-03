package org.tungsten.client.feature.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.command.GenericCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {

	public static final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
	public static final CommandSource COMMAND_SOURCE = new ClientCommandSource(null, Tungsten.client);
	public static List<GenericCommand> commands = new ArrayList<>();

	public static void addCommand(GenericCommand command) {
		commands.add(command);
	}

	public static GenericCommand getByName(String name) {
		for (GenericCommand c : commands) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public static GenericCommand instanceFromClass(Class<?> clazz) {
		for (GenericCommand c : commands) {
			if (c.getClass().equals(clazz)) {
				return c;
			}
		}
		return null;
	}

}
