package org.tungsten.client.util;

import org.tungsten.client.Tungsten;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class LibraryDownloader {

    public static HashMap<String, Path> libraries = new HashMap<>();

    static {
        libraries.put("https://cdn.discordapp.com/attachments/1121169365883166790/1121169526021689344/minecraft-unmapped.jar", Tungsten.LIBS.resolve("minecraft-unmapped.jar"));
        libraries.put("https://cdn.discordapp.com/attachments/1121169365883166790/1121169525522563242/minecraft-mapped.jar", Tungsten.LIBS.resolve("minecraft-mapped.jar"));
        libraries.put("https://cdn.discordapp.com/attachments/1121169365883166790/1130395140884791296/Tungsten-dev.jar", Tungsten.LIBS.resolve("Tungsten-dev.jar"));
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
