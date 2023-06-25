package org.tungsten.client.clickgui;

import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.ModuleType;
import org.tungsten.client.feature.module.ModuleTypeManager;
import org.tungsten.client.feature.module.config.GenericSetting;
import org.tungsten.client.feature.registry.ModuleRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TungstenBridge {

    public void toggleModule(String moduleName){
        ModuleRegistry.getByName(moduleName).toggle();
    }

    public boolean queryEnabled(String moduleName){
        return ModuleRegistry.getByName(moduleName).isEnabled();
    }

    public void setWindowPosition(String windowHandle, double wind_x, double wind_y){
        ModuleType m = ModuleTypeManager.getByName(windowHandle);
        m.setPos(wind_x, wind_y);
    }

    public double getWindowX(String windowHandle){
        return ModuleTypeManager.getByName(windowHandle).getX();
    }

    public double getWindowY(String windowHandle){
        return ModuleTypeManager.getByName(windowHandle).getY();
    }

    public String[] getModuleTypes(){
        return ModuleTypeManager.getModuleTypes().stream().map(ModuleType::getName).toArray(String[]::new);
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


    public String[] getSettingHTML(String module){
        System.out.println("REAL");
        System.out.println(module);
        GenericModule xz = ModuleRegistry.getByName(module);
        if(xz != null) {
            System.out.println("was not null");
            String[] real = xz.getSettings().stream().map(GenericSetting::toHTML).toArray(String[]::new);
            System.out.println(Arrays.toString(real));
            return real;
        }else{
            System.out.println("Was null");
            return null;
        }
    }


}
