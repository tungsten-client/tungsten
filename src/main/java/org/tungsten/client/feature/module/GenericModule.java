package org.tungsten.client.feature.module;

import net.minecraft.client.MinecraftClient;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.config.GenericSetting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericModule {

    protected static final MinecraftClient client = Tungsten.client;

    private final String name;
    private final String description;

    private final String moduleType;

    private boolean enabled = false;

    final List<GenericSetting<?>> settings = new ArrayList<>();


    public GenericModule(String name, String description, String moduleType){
        this.name = name;
        this.description = description;
        this.moduleType = moduleType;
    }

    public List<GenericSetting<?>> getSettings(){
        return this.settings;
    }

    public void registerSettings(){
        for(Field f : this.getClass().getDeclaredFields()){
            System.out.println(f.getName());
            System.out.println(f.getType());
            if(GenericSetting.class.isAssignableFrom(f.getType())){
                try {
                    if(f.trySetAccessible()){
                        f.setAccessible(true);
                        GenericSetting<?> setting = (GenericSetting<?>) f.get(this);
                        if(setting != null){
                            System.out.println("Setup setting " + setting.getName()) ;
                            settings.add(setting);
                        }
                    }else{
                        throw new RuntimeException("Please do not make the settings inside your module private, we need to see them");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
        setEnabled(enabled);
    }

    public void setEnabled(boolean state){
        if(state){
            Tungsten.eventManager.registerSubscribers(this);
            this.enable();
        }else{
            Tungsten.eventManager.unregister(this);
            this.disable();
        }
        enabled = state;

    }

    public boolean isEnabled(){
        return enabled;
    }

    public String toHTML(){
        return "";
    }
}
