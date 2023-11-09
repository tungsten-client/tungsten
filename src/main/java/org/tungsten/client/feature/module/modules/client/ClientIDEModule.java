package org.tungsten.client.feature.module.modules.client;

import com.mojang.text2speech.OperatingSystem;
import org.jetbrains.annotations.Nullable;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.config.CheckboxSetting;
import org.tungsten.client.feature.module.config.TextboxSetting;
import org.tungsten.client.gui.ide.ClientIDE;
import org.tungsten.client.util.io.FileTraveller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ClientIDEModule extends GenericModule {
    public ClientIDEModule() {
        super("ClientIDE", "Enables the ClientIDE", "CLIENT");
        this.registerSettings();
    }

    public static final CheckboxSetting useNativeIde = new CheckboxSetting(false, "Use Native IDE", "");
    public static final TextboxSetting nativeIdeLocation = new TextboxSetting(tryFindNativeIde(), "Native IDE Location", "");

    @Override
    protected void disable() {
    }

    @Override
    protected void enable() {
        ClientIDE.openIde();
        setEnabled(false);
    }

    @Override
    protected void tickClient() {
    }

    private static String tryFindNativeIde() {
        try {
            String path = System.getenv("Path");
            Path intellijExecutable;
            if (path != null && path.contains("JetBrains")) {
                intellijExecutable = findInPath(path, "JetBrains").resolve("idea64.exe");
            } else {
                intellijExecutable = switch (OperatingSystem.get()) {
                    case LINUX -> FileTraveller.fromHome().then(".local", "share", "JetBrains", "ToolBox", "apps").find().then("bin").find(p -> p.getFileName().startsWith("idea64")).get();
                    case WINDOWS -> FileTraveller.from("C:", "Program Files", "JetBrains").find().then("bin", "idea64.exe").get();
                    case MAC_OS -> FileTraveller.fromHome().then("Library", "Application Support", "JetBrains").find().then("bin", "idea64.exe").get();
                    case UNSUPPORTED -> null;
                };
            }
            if (intellijExecutable != null && Files.isExecutable(intellijExecutable)) return intellijExecutable.toAbsolutePath().toString();
        } catch (Throwable ignored) {}
        return "";
    }

    @Nullable
    private static Path findInPath(String path, String token) {
        return Stream.of(path.split(";")).filter(str -> str.contains(token)).findAny().map(Paths::get).orElse(null);
    }
}
