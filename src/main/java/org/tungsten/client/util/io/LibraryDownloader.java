package org.tungsten.client.util.io;

import org.tungsten.client.Tungsten;
import org.tungsten.client.util.WebUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class LibraryDownloader {

    public static HashMap<String, Path> libraries = new HashMap<>();

    static {
        libraries.put("https://cdn.discordapp.com/attachments/1121169365883166790/1121169526021689344/minecraft-unmapped.jar", Tungsten.LIBS.resolve("minecraft-unmapped.jar"));
        libraries.put("https://cdn.discordapp.com/attachments/1121169365883166790/1121169525522563242/minecraft-mapped.jar", Tungsten.LIBS.resolve("minecraft-mapped.jar"));
        libraries.put("https://github.com/tungsten-client/dependencies/releases/download/DevJars/Tungsten-dev.jar", Tungsten.LIBS.resolve("Tungsten-dev.jar"));
        libraries.put("https://libraries.minecraft.net/com/mojang/authlib/3.18.38/authlib-3.18.38-sources.jar", Tungsten.LIBS.resolve("authlib.jar"));
        libraries.put("https://libraries.minecraft.net/com/mojang/blocklist/1.0.10/blocklist-1.0.10-sources.jar", Tungsten.LIBS.resolve("blocklist.jar"));
        libraries.put("https://libraries.minecraft.net/com/mojang/brigadier/1.0.18/brigadier-1.0.18-sources.jar", Tungsten.LIBS.resolve("brigadier.jar"));
        libraries.put("https://libraries.minecraft.net/com/mojang/datafixerupper/6.0.8/datafixerupper-6.0.8-sources.jar", Tungsten.LIBS.resolve("datafixerupper.jar"));
        libraries.put("https://libraries.minecraft.net/com/mojang/logging/1.1.1/logging-1.1.1-sources.jar", Tungsten.LIBS.resolve("logging.jar"));
        libraries.put("https://libraries.minecraft.net/com/mojang/text2speech/1.13.9/text2speech-1.13.9-sources.jar", Tungsten.LIBS.resolve("text2speech.jar"));
        libraries.put("https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/0.89.3%2B1.20.2/fabric-api-0.89.3%2B1.20.2.jar", Tungsten.LIBS.resolve("fabric-api-0.89.3+1.20.2.jar"));
        libraries.put("https://maven.fabricmc.net/net/fabricmc/fabric-loader/0.14.23/fabric-loader-0.14.23.jar", Tungsten.LIBS.resolve("fabric-loader-0.14.23.jar"));
        libraries.put("https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.7/slf4j-simple-2.0.7.jar", Tungsten.LIBS.resolve("slf4j-simple-2.0.7.jar"));
        libraries.put("https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.7/slf4j-api-2.0.7.jar", Tungsten.LIBS.resolve("slf4j-api-2.0.7.jar"));
        libraries.put("https://cdn.discordapp.com/attachments/402585927135789077/1159351512837922876/Renderer-master-SNAPSHOT.jar", Tungsten.LIBS.resolve("Renderer-master-SNAPSHOT.jar"));
        //add future libraries here as see fit
    }

    public static void ensurePresent(){
        for(String url : libraries.keySet()){
            Path p = libraries.get(url);
            if(!p.toFile().exists()){
                WebUtils.downloadURLToPath(url, p);
            }
        }
    }

    public static String generateClasspath(){
        StringBuilder classPath = new StringBuilder();
        for(Path p : libraries.values()){
            classPath.append(p.toAbsolutePath()).append(File.pathSeparator);
        }
        return classPath.toString();
    }

}
