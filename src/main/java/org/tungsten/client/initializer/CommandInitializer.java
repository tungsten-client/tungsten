package org.tungsten.client.initializer;

import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.command.GenericCommand;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.registry.CommandRegistry;
import org.tungsten.client.feature.registry.ModuleRegistry;
import org.tungsten.client.util.ModuleClassLoader;
import org.tungsten.client.util.Utils;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class CommandInitializer {

    private static final File COMMANDS = new File(Tungsten.RUNDIR, "commands");
    private static final File COMMANDS_COMPILED = new File(Tungsten.APPDATA, "cmd_tmp");

    static {
        Utils.ensureDirectoryIsCreated(COMMANDS);
        Utils.ensureDirectoryIsCreated(COMMANDS_COMPILED);
    }

    public static void initCommands(){
        //todo: compile modules into classes and put them in mod_tmp
        searchForCommands(COMMANDS_COMPILED);
    }


    /*
        WE ARE LOADING COMPILED CLASSES, NOT COMPILING THEM
     */
    private static void searchForCommands(File path){
        if(path != null){
            File[] files = path.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isDirectory()){
                        searchForCommands(file);
                    }else{
                        if(file.getName().endsWith(".class")){
                            try{
                                initCommand(file);
                            }catch(Exception e){
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
    }




    private static void initCommand(File module) throws Exception {
        InputStream file = new FileInputStream(module);
        byte[] classBytes = file.readAllBytes();
        if(Utils.checkSignature(classBytes)) throw new Exception("invalid class file, did not pass class file signature check");
        Class<?> loadedCommand = ModuleClassLoader.getInstance().registerClass(classBytes);
        if(GenericCommand.class.isAssignableFrom(loadedCommand)){
            Class<GenericCommand> loadedCommandClass = (Class<GenericCommand>) loadedCommand;
            GenericCommand commandInstance = loadedCommandClass.getDeclaredConstructor().newInstance();
            Tungsten.LOGGER.info("Loaded module " + commandInstance.getName());
            CommandRegistry.addCommand(commandInstance);
        }
    }
}
