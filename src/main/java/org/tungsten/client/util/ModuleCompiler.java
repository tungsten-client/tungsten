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

public class ModuleCompiler {

    private static final String MAPPED_FILE_NAME = "sources-mapped-1.20.1.jar";
    private static final String UNMAPPED_FILE_NAME = "sources-unmapped-1.20.1.jar";
    private static final String MAPPED_FILE_URL = "https://cdn.discordapp.com/attachments/1121169365883166790/1121169525522563242/minecraft-mapped.jar";
    private static final String UNMAPPED_FILE_URL = "https://cdn.discordapp.com/attachments/1121169365883166790/1121169526021689344/minecraft-unmapped.jar";

    //declare the mapped minecraft sources, the unmapped minecraft sources, and the tungsten jar file, the three things needed to call the java compiler and make everything work
    private static File mapped;
    private static File unmapped;
    private static File self;

    static {
        mapped = new File(Tungsten.LIBS, MAPPED_FILE_NAME);
        unmapped = new File(Tungsten.LIBS, UNMAPPED_FILE_NAME);
        try {
            self = new File(ModuleCompiler.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void compileModules(){
        searchAndCompileModules(new File(Tungsten.RUNDIR, "modules"));
    }


    private static void searchAndCompileModules(File path){
        if(path != null){
            File[] files = path.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isDirectory()){
                        searchAndCompileModules(file);
                    }else{
                        if(file.getName().endsWith(".java")){
                            try{
                                compileModule(file);
                            }catch(Exception e){
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void compileModule(File module) throws IOException {
        System.out.println("compileModule called on " + module.getName());

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        File output = new File(ModuleInitializer.MODULES_COMPILED, module.getName().replace(".java", ".class"));
        PrintStream errorStream = new PrintStream(stream);

        String libraries = mapped.getAbsolutePath() + ";" + unmapped.getAbsolutePath() + ";" + self.getAbsolutePath();
        if(compiler != null){
            int compilerStatus = compiler.run(null, null, errorStream, "-cp", libraries, module.getAbsolutePath());

            if(compilerStatus == 0){
                Files.move(new File(new File(Tungsten.RUNDIR, "modules"), module.getName().replace(".java", ".class")).toPath(), output.toPath());
            }else{
                System.out.println("ERROR IN COMPILATION OF MODULE " + module.getName());
                System.out.println(stream);
                errorStream.close();
            }
        }
    }




    public static void setupCompilerEnvironment() throws IOException, URISyntaxException {
        Utils.deleteFilesExcept(Tungsten.LIBS, UNMAPPED_FILE_NAME, MAPPED_FILE_NAME); //delete everything that isnt required in the libs folder
        Utils.deleteAllFiles(ModuleInitializer.MODULES_COMPILED); //make sure we've cleaned up the compiled class files from last time, or that might cause problems trying to uninstall modules
        if(!mapped.exists())
            WebUtils.downloadURLToPath(MAPPED_FILE_URL, mapped);
        if(!unmapped.exists())
            WebUtils.downloadURLToPath(UNMAPPED_FILE_URL, unmapped);
    }

}
