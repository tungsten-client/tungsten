package org.tungsten.client;

import me.x150.MessageManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tungsten.client.initializer.CommandInitializer;
import com.labymedia.ultralight.UltralightJava;
import org.tungsten.client.initializer.Installer;
import org.tungsten.client.initializer.ModuleInitializer;
import org.tungsten.client.util.ModuleCompiler;
import org.tungsten.client.util.Utils;
import org.tungsten.client.util.WebUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class Tungsten implements ClientModInitializer {

    public static MessageManager eventManager = new MessageManager();

    public static final MinecraftClient client = MinecraftClient.getInstance();
    public static final File RUNDIR = new File(MinecraftClient.getInstance().runDirectory, "tungsten"); //PLEASE USE THIS DIRECTORY TO SAVE ALL CONFIGURATION FILES, EVERYTHING
    public static final File APPDATA = new File(RUNDIR, "appdata"); //use this for the temporary creation and storing of files that are needed for the launch of the client, e.g. compiled class files for modules, etc...
    public static final File LIBS = new File(Tungsten.APPDATA, "libs"); //used to store downloaded libraries / the jdk

    public final static Logger LOGGER = LoggerFactory.getLogger("Tungsten");

    public static final File ULTRALIGHT = new File(Tungsten.APPDATA, "ultralight");

    private static final String NATIVES_URL = "https://cdn.discordapp.com/attachments/1121169365883166790/1123366568903061634/natives.zip";


    public static Path ulNatives;
    public static Path ulResources;

    static {
        Utils.ensureDirectoryIsCreated(RUNDIR);
        Utils.ensureDirectoryIsCreated(APPDATA);
        Utils.ensureDirectoryIsCreated(LIBS);
        Utils.ensureDirectoryIsCreated(ULTRALIGHT);
    }

    public static void onShutdownClient(){
        Utils.deleteAllFiles(ULTRALIGHT); //cleanup ultralight files
    }

    @Override
    public void onInitializeClient() {
        try {
            Installer.run();
            ModuleCompiler.setupCompilerEnvironment();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ModuleCompiler.compileModules();
        ModuleInitializer.initModules();

        Runtime.getRuntime().addShutdownHook(new Thread(Tungsten::onShutdownClient));
        try {
            startUltralight();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupUltraliteGUISystem() {
        try {
            Utils.ensureDirectoryIsCreated(new File(Tungsten.APPDATA, "gui"));
            //FileUtils.copyDirectory(getGUI(), new File(Tungsten.APPDATA, "gui"));
            Iterator<Path> iterator = getGUIComponents().iterator();
            while (iterator.hasNext()) {
                Path path = iterator.next();
                if (Files.isDirectory(path)) continue; // dont need
                String s = path.getFileName().toString();
                Path tf = Path.of(new File(Tungsten.APPDATA, "gui").toURI()).resolve(s);
                System.out.println(path + " -> " + tf);
                new File(tf.toUri()).createNewFile();
                try (InputStream fis = Files.newInputStream(path); OutputStream fos = Files.newOutputStream(tf)) {
                    byte[] buffer = new byte[512];
                    int r;
                    while ((r = fis.read(buffer, 0, buffer.length)) != -1) {
                        fos.write(buffer, 0, r);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Path> getGUIComponents() throws IOException, URISyntaxException {
        URI uri = Tungsten.class.getClassLoader().getResource("gui").toURI();
        Path myPath;
        FileSystem c = null;
        if (uri.getScheme().equals("jar")) {
            try {
                c = FileSystems.newFileSystem(uri, Collections.emptyMap());
            } catch (FileSystemAlreadyExistsException e) {
                c = FileSystems.getFileSystem(uri);
            }
            myPath = c.getPath("gui");
        } else {
            myPath = Paths.get(uri);
        }

        return Files.walk(myPath);
    }

    private Stream<Path> findUlResources() throws IOException, URISyntaxException {
        URI uri = Tungsten.class.getClassLoader().getResource("ul-resources").toURI();
        Path myPath;
        FileSystem c = null;
        if (uri.getScheme().equals("jar")) {
            try {
                c = FileSystems.newFileSystem(uri, Collections.emptyMap());
            } catch (FileSystemAlreadyExistsException e) {
                c = FileSystems.getFileSystem(uri);
            }
            myPath = c.getPath("ul-resources");
        } else {
            myPath = Paths.get(uri);
        }

        return Files.walk(myPath);
    }


    public void startUltralight() throws Exception{
        System.out.println("Initializing Ultralight");
        setupUltraliteGUISystem();
        ulNatives = new File(ULTRALIGHT, "natives").toPath();
        Utils.ensureDirectoryIsCreated(new File(ULTRALIGHT, "natives"));
        ulResources = new File(ULTRALIGHT, "resources").toPath();
        Utils.ensureDirectoryIsCreated(new File(ULTRALIGHT, "resources"));

        String libpath = System.getProperty("java.library.path");


        if (libpath != null) {
            libpath += File.pathSeparator + ulNatives.toAbsolutePath();
        } else {
            libpath = ulNatives.toAbsolutePath().toString();
        }
        System.setProperty("java.library.path", libpath);

        File natives = new File(ulNatives.toUri());
        if(Utils.isDirectoryEmpty(natives)){
            File natives_zip = new File(natives, "natives.zip");
            WebUtils.downloadURLToPath(NATIVES_URL, natives_zip);
            Utils.unzip(natives_zip, natives);
            natives_zip.delete();
        }



        Iterator<Path> iterator = findUlResources().iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            if (Files.isDirectory(path)) continue; // dont need
            String s = path.getFileName().toString();
            Path tf = ulResources.resolve(s);
            System.out.println(path + " -> " + tf);
            try (InputStream fis = Files.newInputStream(path); OutputStream fos = Files.newOutputStream(tf)) {
                byte[] buffer = new byte[512];
                int r;
                while ((r = fis.read(buffer, 0, buffer.length)) != -1) {
                    fos.write(buffer, 0, r);
                }
            }
        }

        System.out.println("Extracting UltralightJava");
        UltralightJava.extractNativeLibrary(ulNatives);

        System.out.println("Loading");
        UltralightJava.load(ulNatives);

        System.out.println("OK");
    }
}
