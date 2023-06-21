package org.tungsten.client.feature.module;

import net.minecraft.client.MinecraftClient;
import org.tungsten.client.Tungsten;

public abstract class GenericModule {

    protected static final MinecraftClient client = Tungsten.client;

    private final String name;
    private final String description;

    public GenericModule(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    protected abstract void disable();

    protected abstract void enable();

    protected abstract void tickClient();
}
