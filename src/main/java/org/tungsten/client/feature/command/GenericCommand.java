package org.tungsten.client.feature.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.registry.CommandRegistry;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericCommand {

	protected static final MinecraftClient client = Tungsten.client;


	private final String name;
	private final List<ArgumentEntry> arguments;
	protected LiteralArgumentBuilder<CommandSource> builder;

	public LiteralArgumentBuilder<CommandSource> getBuilder(){
		return this.builder;
	}

	public GenericCommand(String name, List<ArgumentEntry> arguments) {
		this.name = name;
		this.arguments = arguments;

		//todo: rewrite ENTIRELY. this shit is straight ass. i also had and still have no idea how specific parts make it work/if they are even needed to work lol. saturn you could prolly do this.
		//also this is super inconsistent with the this.name & name (param).
		//dont bully me for this shit i js got back into modding when i wrote this :waa:

		LiteralArgumentBuilder<CommandSource> command = LiteralArgumentBuilder.literal(this.name);
		builder = command;
		if(arguments != null) {
			if(arguments.size()==1){
				ArgumentEntry entry = arguments.get(0);

				command.then(RequiredArgumentBuilder.argument(entry.name(), entry.type()).executes(context -> 1));
			} else if (!arguments.isEmpty()) {
				ArgumentBuilder ab = null;
				ArgumentBuilder previous = ab;
				for (int i = arguments.size() - 1; i >= 0; i--) {
					ArgumentEntry entry = arguments.get(i);
					ArgumentBuilder new_arg = RequiredArgumentBuilder.argument(entry.name(), entry.type()).executes(context -> 1);
					if (previous != null)
						new_arg = new_arg.then(previous);
					previous = new_arg;
					ab = new_arg;
				}
				command.then(ab);
			}
		}
		command.executes(context -> 1);
		CommandRegistry.DISPATCHER.register(command);
		Tungsten.LOGGER.info("constructor registered " + this.name);
	}

	public String getName() {
		return this.name;
	}
	public List<ArgumentEntry> getTypes(){
		return this.arguments;
	}

	protected void registerWithBuilder(LiteralArgumentBuilder<CommandSource> builder){
		builder.executes(c -> 1);
		boolean removed = CommandRegistry.DISPATCHER.getRoot().getChildren().removeIf(node -> node.getName().equals(builder.getLiteral()));
		Tungsten.LOGGER.info("removed matching node from root?: " + removed);
		CommandRegistry.DISPATCHER.register(builder);
	}
	public void onRegistered(LiteralArgumentBuilder<CommandSource> builder) {}
	public abstract void execute(String[] args);

}
