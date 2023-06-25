package org.tungsten.client.clickgui;

import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.ModuleTypeManager;
import org.tungsten.client.feature.registry.ModuleRegistry;

import java.util.ArrayList;
import java.util.List;

public class TungstenBridge {

    public void toggleModule(String moduleName){
        ModuleRegistry.getByName(moduleName).toggle();
    }

    public boolean queryEnabled(String moduleName){
        return ModuleRegistry.getByName(moduleName).isEnabled();
    }

    public String[] getModuleTypes(){
        return ModuleTypeManager.getModuleTypes().toArray(String[]::new);
    }

    public String[] getModulesByType(String moduleType){
        List<String> modules = new ArrayList<String>();
        for(GenericModule mod : ModuleRegistry.modules){
            if(mod.getType().equals(moduleType)){
                modules.add(mod.getName());
            }
        }
        return modules.toArray(String[]::new);
    }

    public void print(String real){
        System.out.println(real);
    }

    public String getDescription(String module){
        return ModuleRegistry.getByName(module).getDescription();
    }

}
