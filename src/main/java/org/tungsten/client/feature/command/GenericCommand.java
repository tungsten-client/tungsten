package org.tungsten.client.feature.command;

import net.minecraft.client.MinecraftClient;
import org.tungsten.client.Tungsten;

public abstract class GenericCommand {

	protected static final MinecraftClient client = Tungsten.client;


	private final String name;

	public GenericCommand(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	protected abstract void execute();
}
