package org.tungsten.client.languageserver;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.tungsten.client.Tungsten;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;

public class LanguageServer {

    // should I download the lsp on client init?
    private static LanguageServer inst = null;
    private Process process;

    @Getter
    private static int port = 0;

    @Getter
    private static String addr;

    public static LanguageServer instance() {
        if(inst == null) {
            Tungsten.LOGGER.info("Starting language sever");
            new Thread(() -> {
                try { inst = new LanguageServer(); }
                catch (IOException e) { throw new RuntimeException(e); }
            }).start();
        }
        return inst;
    }

    /**
     *  Returns the respective LanguageServer config for the current os.
     */
    public static String getConfigForOS(@NotNull Path langServ) {
        Path win = langServ.resolve("config_win");
        Path linux = langServ.resolve("config_linux");
        Path macos = langServ.resolve("config_mac");
        if(Tungsten.getOS().startsWith("Win")) return String.valueOf(win);
        if(Tungsten.getOS().startsWith("Lin")) return String.valueOf(linux);
        if(Tungsten.getOS().startsWith("Mac")) return String.valueOf(macos);
        return "";
    }

    public LanguageServer() throws IOException {
        // find available port
        ServerSocket s = new ServerSocket(0);
        port = s.getLocalPort();
        addr = "127.0.0.1:" + port;
        s.close();

        Path LSPDir = Tungsten.APPDATA.resolve("lsp");
        Path JDTLangServ = LSPDir.resolve("jdt-lang-serv");

        String exe = String.valueOf(LSPDir.resolve("lsp-ws-proxy/lsp-ws-proxy.exe"));
        final ProcessBuilder pb = getProcessBuilder(JDTLangServ, LSPDir, exe);

        pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        // starting the process
        process = pb.start();
    }

    @NotNull
    private static ProcessBuilder getProcessBuilder(@NotNull Path JDTLangServ, @NotNull Path LSPDir, String exe) {
        String jar = String.valueOf(JDTLangServ.resolve("plugins/org.eclipse.equinox.launcher_1.6.400.v20210924-0641.jar"));
        String configOS = getConfigForOS(JDTLangServ);
        String work = String.valueOf(LSPDir.resolve("work"));

        // https://github.com/qualified/lsp-ws-proxy#usage
        return new ProcessBuilder(
                exe,
                "-r",
                "-l",
                addr,
                "--",
                "java",
                "-Declipse.application=org.eclipse.jdt.ls.core.id1",
                "-Dosgi.bundles.defaultStartLevel=4",
                "-Declipse.product=org.eclipse.jdt.ls.core.product",
                "-Dlog.protocol=true",
                "-Dlog.level=ALL",
                "-Xmx1G",
                "--add-module=ALL-SYSTEM",
                "--add-opens",
                "java.base/java.util=ALL-UNNAMED",
                "--add-opens",
                "java.base/java.lang=ALL-UNNAMED",
                "-jar",
                jar,
                "-configuration",
                configOS,
                "-data",
                work
        );
    }

    public static void kill() {
        Tungsten.LOGGER.info("Killing language server");
        inst.process.destroy();
        inst = null;
    }
}
