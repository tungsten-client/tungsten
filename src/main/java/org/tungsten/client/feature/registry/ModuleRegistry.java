package org.tungsten.client.feature.registry;

import org.tungsten.client.feature.command.GenericCommand;
import org.tungsten.client.feature.module.GenericModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleRegistry {

    public static List<GenericModule> modules = new ArrayList<>();

    public GenericModule getByName(String name) {
        return null; //todo: implement genericcommand and genericmodule
    }

    public static void addModule(GenericModule module){
        modules.add(module);
    }



}
