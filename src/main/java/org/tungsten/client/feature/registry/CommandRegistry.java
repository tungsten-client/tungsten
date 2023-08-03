package org.tungsten.client.feature.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.BuiltinRegistries;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.command.GenericCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry {

	public static final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
	public static final CommandSource COMMAND_SOURCE = new ClientCommandSource(null, Tungsten.client);
	public static List<GenericCommand> commands = new ArrayList<>();

	private static final Map<Class<? extends GenericCommand>, GenericCommand> commandInstances = new HashMap<>();

	public static final CommandRegistryAccess REGISTRY_ACCESS = net.minecraft.server.command.CommandManager.createRegistryAccess(BuiltinRegistries.createWrapperLookup());

	public static void dispatch(String message) throws CommandSyntaxException {
		dispatch(message, new ChatCommandSource(Tungsten.client));
	}

	public static void dispatch(String message, CommandSource source) throws CommandSyntaxException {
		ParseResults<CommandSource> results = DISPATCHER.parse(message, source);
		DISPATCHER.execute(results);
	}

	private final static class ChatCommandSource extends ClientCommandSource {
		public ChatCommandSource(MinecraftClient client) {
			super(null, client);
		}
	}

	public static void addCommand(GenericCommand command) {
		commands.removeIf(command1 -> command1.getName().equals(command.getName()));
		commandInstances.values().removeIf(command1 -> command1.getName().equals(command.getName()));

		command.registerTo(DISPATCHER);
		commands.add(command);
		commandInstances.put(command.getClass(), command);
	}

	@SuppressWarnings("unchecked")
	public <T extends GenericCommand> T get(Class<T> klass) {
		return (T) commandInstances.get(klass);
	}

}
