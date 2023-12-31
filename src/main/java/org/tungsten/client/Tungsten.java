package org.tungsten.client;

import com.labymedia.ultralight.UltralightJava;
import meteordevelopment.orbit.EventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tungsten.client.config.Config;
import org.tungsten.client.gui.clickgui.TungstenBridge;
import org.tungsten.client.initializer.CommandInitializer;
import org.tungsten.client.initializer.ItemInitializer;
import org.tungsten.client.initializer.ModuleInitializer;
import org.tungsten.client.languageserver.LanguageServer;
import org.tungsten.client.util.Texture;
import org.tungsten.client.util.Utils;
import org.tungsten.client.util.WebUtils;
import org.tungsten.client.util.io.CommandCompiler;
import org.tungsten.client.util.io.ItemCompiler;
import org.tungsten.client.util.io.ModuleCompiler;
import org.tungsten.client.util.render.notification.Notifications;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class Tungsten implements ClientModInitializer {

	public static final MinecraftClient client = MinecraftClient.getInstance();
	public static final Path RUNDIR = Path.of(MinecraftClient.getInstance().runDirectory.toURI())
			.resolve("tungsten"); //PLEASE USE THIS DIRECTORY TO SAVE ALL CONFIGURATION FILES, EVERYTHING
	public static final Path APPDATA = RUNDIR.resolve(
			"appdata"); //use this for the temporary creation and storing of files that are needed for the launch of the client, e.g. compiled class files for modules, etc...
	public static final Path LIBS = APPDATA.resolve("libs"); //used to store downloaded libraries / the jdk
	public static final Path ULTRALIGHT = APPDATA.resolve("ultralight");
	public final static Logger LOGGER = LoggerFactory.getLogger("Tungsten");
	private static final String NATIVES_URL = "https://cdn.discordapp.com/attachments/1121169365883166790/1123366568903061634/natives.zip";
	public static final EventBus EVENT_BUS = new EventBus();
	public static Path ulNatives;
	public static Path ulResources;

	public static Config config = new Config();

	public static Identifier logo = new Texture("icon/tungsten.png");
	public static TungstenBridge tungstenBridge;


	static {
		Utils.ensureDirectoryIsCreated(RUNDIR);
		Utils.ensureDirectoryIsCreated(APPDATA);
		Utils.ensureDirectoryIsCreated(LIBS);
		Utils.ensureDirectoryIsCreated(ULTRALIGHT);
	}

	public static void onShutdownClient() {
		LOGGER.info("Shutting down client");
		LanguageServer.kill();
	}

	private static void cleanupUL() {
		Tungsten.LOGGER.info("Cleaning up UL");
		try {
			Thread.sleep(1000 * 10); //sleep for 10 seconds to ensure the locks are released before cleaning up files.
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		Utils.rmDirectoryTree(ULTRALIGHT); //cleanup ultralight files
	}

	@Override
	public void onInitializeClient() {
		Setup.setup();

		EVENT_BUS.registerLambdaFactory("org.tungsten.client", ((lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup())));

		ModuleCompiler.compileModules();
		ModuleInitializer.initModules();

		ItemCompiler.compileItems();
		ItemInitializer.initItems();

		CommandCompiler.compileCommands();
		CommandInitializer.initCommands();

		LanguageServer.instance();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			//Tungsten.cleanupUL();
		}
		));

		try {
			startUltralight();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ClientLifecycleEvents.CLIENT_STOPPING.register(_c -> {
			onShutdownClient();
		});

		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			Notifications.render(drawContext.getMatrices());
		});

		tungstenBridge = new TungstenBridge();
	}

	private void setupUltraliteGUISystem() {
		try {
			Utils.ensureDirectoryIsCreated(Tungsten.APPDATA.resolve("gui"));
			Iterator<Path> iterator = getGUIComponents().iterator();
			while (iterator.hasNext()) {
				Path path = iterator.next();
                if (Files.isDirectory(path)) {
                    continue; // dont need
                }
				String s = path.getFileName().toString();
				Path tf = APPDATA.resolve("gui").resolve(s);
				Tungsten.LOGGER.info(path + " -> " + tf);
				if (!Files.exists(tf)) {
					Files.createDirectories(tf.getParent());
					Files.createFile(tf);
				}
				try (InputStream fis = Files.newInputStream(path); OutputStream fos = Files.newOutputStream(tf)) {
					byte[] buffer = new byte[512];
					int r;
					while ((r = fis.read(buffer, 0, buffer.length)) != -1) {
						fos.write(buffer, 0, r);
					}
				}
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private Stream<Path> getGUIComponents() throws IOException, URISyntaxException {
		URI uri = Objects.requireNonNull(Tungsten.class.getClassLoader().getResource("gui")).toURI();
		Path myPath;
		if (uri.getScheme().equals("jar")) {
			FileSystem c;
			try {
				c = FileSystems.newFileSystem(uri, Collections.emptyMap());
				myPath = c.getPath("gui");
				c.close();
			} catch (FileSystemAlreadyExistsException e) {
				c = FileSystems.getFileSystem(uri);
				myPath = c.getPath("gui");
			}
		} else {
			myPath = Paths.get(uri);
		}

		return Files.walk(myPath);
	}

	private Stream<Path> findUlResources() throws IOException, URISyntaxException {
		URI uri = Objects.requireNonNull(Tungsten.class.getClassLoader().getResource("ul-resources")).toURI();
		Path myPath;
		if (uri.getScheme().equals("jar")) {
			FileSystem c;
			try {
				c = FileSystems.newFileSystem(uri, Collections.emptyMap());
				myPath = c.getPath("ul-resources");
				c.close();
			} catch (FileSystemAlreadyExistsException e) {
				c = FileSystems.getFileSystem(uri);
				myPath = c.getPath("ul-resources");
			}
		} else {
			myPath = Paths.get(uri);
		}

		return Files.walk(myPath);
	}


	public void startUltralight() throws Exception {
		Tungsten.LOGGER.info("Initializing Ultralight");
		setupUltraliteGUISystem();
		ulNatives = ULTRALIGHT.resolve("natives");
		Utils.ensureDirectoryIsCreated(ulNatives);
		ulResources = ULTRALIGHT.resolve("resources");
		Utils.ensureDirectoryIsCreated(ulResources);

		String libpath = System.getProperty("java.library.path");

		if (libpath != null) {
			libpath += File.pathSeparator + ulNatives.toAbsolutePath();
		} else {
			libpath = ulNatives.toAbsolutePath().toString();
		}
		System.setProperty("java.library.path", libpath);

		if (Utils.isDirectoryEmpty(ulNatives)) {
			Path resolve = ulNatives.resolve("natives.zip");
			WebUtils.downloadURLToPath(NATIVES_URL, resolve);
			Utils.unzip(resolve, ulNatives);
		}

		Iterator<Path> iterator = findUlResources().iterator();
		while (iterator.hasNext()) {
			Path path = iterator.next();
            if (Files.isDirectory(path)) {
                continue; // dont need
            }
			String s = path.getFileName().toString();
			Path tf = ulResources.resolve(s);
			Tungsten.LOGGER.info(path + " -> " + tf);
			try (InputStream fis = Files.newInputStream(path); OutputStream fos = Files.newOutputStream(tf)) {
				byte[] buffer = new byte[512];
				int r;
				while ((r = fis.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, r);
				}
			}
		}

		Tungsten.LOGGER.info("Extracting UltralightJava");
		UltralightJava.extractNativeLibrary(ulNatives);

		Tungsten.LOGGER.info("Loading");
		UltralightJava.load(ulNatives);

		Tungsten.LOGGER.info("OK");
	}
	public static String getOS() {
		return System.getProperty("os.name");
	}
}
