package org.tungsten.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.tungsten.client.initializer.CommandInitializer;
import org.tungsten.client.initializer.ModuleInitializer;
import org.tungsten.client.util.ModuleCompiler;
import org.tungsten.client.util.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Environment(EnvType.CLIENT)
public class Tungsten implements ClientModInitializer {

    public static final MinecraftClient client = MinecraftClient.getInstance();
    public static final File RUNDIR = new File(MinecraftClient.getInstance().runDirectory, "tungsten"); //PLEASE USE THIS DIRECTORY TO SAVE ALL CONFIGURATION FILES, EVERYTHING
    public static final File APPDATA = new File(RUNDIR, "appdata"); //use this for the temporary creation and storing of files that are needed for the launch of the client, e.g. compiled class files for modules, etc...
    public static final File LIBS = new File(Tungsten.APPDATA, "libs"); //used to store downloaded libraries / the jdk

    static {
        Utils.ensureDirectoryIsCreated(RUNDIR);
        Utils.ensureDirectoryIsCreated(APPDATA);
        Utils.ensureDirectoryIsCreated(LIBS);
    }

    @Override
    public void onInitializeClient() {
        try {
            ModuleCompiler.setupCompilerEnvironment();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ModuleCompiler.compileModules();
        ModuleInitializer.initModules();
    }
}
