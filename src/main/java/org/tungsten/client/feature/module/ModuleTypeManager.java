package org.tungsten.client.feature.module;

import java.util.ArrayList;
import java.util.List;

public class ModuleTypeManager {

    private static List<String> moduleTypes = new ArrayList<String>();


    public static void subscribeModuleType(String moduleType){
        if(!moduleTypes.contains(moduleType)) moduleTypes.add(moduleType);
    }

    public static List<String> getModuleTypes(){
        return moduleTypes;
    }

}
