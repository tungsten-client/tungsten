package org.tungsten.client.initializer;

import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.registry.ModuleRegistry;
import org.tungsten.client.util.ModuleClassLoader;
import org.tungsten.client.util.Utils;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class ModuleInitializer {

    private static final File MODULES = new File(Tungsten.RUNDIR, "modules");
    private static final File MODULES_COMPILED = new File(Tungsten.APPDATA, "mod_tmp");

    static {
        Utils.ensureDirectoryIsCreated(MODULES);
        Utils.ensureDirectoryIsCreated(MODULES_COMPILED);
    }

    public static void initModules(){
        //todo: compile modules into classes and put them in mod_tmp
        searchForModules(MODULES_COMPILED);
    }


    /*
        this method is to be called ON THE MODULES TEMP DIRECTORY, NOT THE MODULES DIRECTORY, WE ARE LOADING COMPILED CLASSES, NOT COMPILING THEM
     */
    public static void searchForModules(File path){
        if(path != null){
            File[] files = path.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isDirectory()){
                        searchForModules(file);
                    }else{
                        if(file.getName().endsWith(".class")){
                            try{
                                initModule(file);
                            }catch(Exception e){
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void initModule(File module) throws Exception{
        InputStream file = new FileInputStream(module);
        byte[] classBytes = file.readAllBytes();
        if(Utils.checkSignature(classBytes)) throw new Exception("invalid class file, did not pass class file signature check");
        Class<?> loadedModule = ModuleClassLoader.getInstance().registerClass(classBytes);
        if(GenericModule.class.isAssignableFrom(loadedModule)){
            Class<GenericModule> loadedModuleClass = (Class<GenericModule>) loadedModule;
            GenericModule moduleInstance = loadedModuleClass.getDeclaredConstructor().newInstance();
            System.out.println("[YIPPIE] Loaded module " + moduleInstance.getName());
            System.out.println("[YIPPIE] Loaded module " + moduleInstance.getName());
            System.out.println("[YIPPIE] Loaded module " + moduleInstance.getName());
            System.out.println("[YIPPIE] Loaded module " + moduleInstance.getName());
            System.out.println("[YIPPIE] Loaded module " + moduleInstance.getName());
            System.out.println("[YIPPIE] Loaded module " + moduleInstance.getName());
            System.out.println("[YIPPIE] Loaded module " + moduleInstance.getName());
            System.out.println("[YIPPIE] Loaded module " + moduleInstance.getName());

            ModuleRegistry.addModule(moduleInstance);
        }
    }
}
