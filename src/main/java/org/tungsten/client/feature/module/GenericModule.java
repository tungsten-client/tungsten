package org.tungsten.client.feature.module;

import net.minecraft.client.MinecraftClient;
import org.tungsten.client.Tungsten;

public abstract class GenericModule {

    protected static final MinecraftClient client = Tungsten.client;

    private final String name;
    private final String description;

    private final String moduleType;

    private boolean enabled = false;

    public GenericModule(String name, String description, String moduleType){
        this.name = name;
        this.description = description;
        this.moduleType = moduleType;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public String getType(){
        return this.moduleType;
    }

    protected abstract void disable();

    protected abstract void enable();

    protected abstract void tickClient();

    public void toggle(){
        enabled = !enabled;
    }

    public void setEnabled(boolean state){
        enabled = state;
    }

    public boolean isEnabled(){
        return enabled;
    }
}
