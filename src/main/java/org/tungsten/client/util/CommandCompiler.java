package org.tungsten.client.util;

import org.tungsten.client.Tungsten;
import org.tungsten.client.initializer.ModuleInitializer;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class CommandCompiler {

    //declare the mapped minecraft sources, the unmapped minecraft sources, and the tungsten jar file, the three things needed to call the java compiler and make everything work
    private static File mapped;
    private static File unmapped;
    private static File self;

    static {
        mapped = new File(Tungsten.LIBS, "minecraft-mapped.jar");
        unmapped = new File(Tungsten.LIBS, "minecraft-unmapped.jar");
        try {
            self = new File(CommandCompiler.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (Exception ignored) {}
    }

    public static void compileModules(){
        searchAndCompileCommands(new File(Tungsten.RUNDIR, "commands"));
    }


    private static void searchAndCompileCommands(File path) {
        Utils.deleteAllFiles(ModuleInitializer.MODULES_COMPILED);
        if(path != null){
            File[] files = path.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isDirectory()){
                        searchAndCompileCommands(file);
                    }else{
                        if(file.getName().endsWith(".java")){
                            try{
                                compileCommand(file);
                            }catch(Exception e){
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void compileCommand(File command) throws IOException {
        Tungsten.LOGGER.info("compileCommand called on " + command.getName());

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        File output = new File(ModuleInitializer.MODULES_COMPILED, command.getName().replace(".java", ".class"));
        PrintStream errorStream = new PrintStream(stream);

        String libraries = mapped.getAbsolutePath() + ";" + unmapped.getAbsolutePath() + ";" + self.getAbsolutePath();
        if(compiler != null){
            int compilerStatus = compiler.run(null, null, errorStream, "-cp", libraries, command.getAbsolutePath());

            if(compilerStatus == 0){
                Files.move(new File(new File(Tungsten.RUNDIR, "commands"), command.getName().replace(".java", ".class")).toPath(), output.toPath());
            }else{
                Tungsten.LOGGER.error("ERROR IN COMPILATION OF COMMAND " + command.getName());
                Tungsten.LOGGER.error(String.valueOf(stream));
                errorStream.close();
            }
        }
    }


    public static void setupCompilerEnvironment() throws IOException, URISyntaxException {
        File mapped = new File(Tungsten.LIBS, "minecraft-mapped.jar");
        File unmapped = new File(Tungsten.LIBS, "minecraft-unmapped.jar");
        File self = new File(CommandCompiler.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        WebUtils.downloadURLToPath("https://cdn.discordapp.com/attachments/1121169365883166790/1121169525522563242/minecraft-mapped.jar", mapped);
        WebUtils.downloadURLToPath("https://cdn.discordapp.com/attachments/1121169365883166790/1121169526021689344/minecraft-unmapped.jar", unmapped);
    }

}
