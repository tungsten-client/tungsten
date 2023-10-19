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
    private static int port = 9999;

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
    private static String getConfigForOS(@NotNull Path langServ) {
        Path win = langServ.resolve("config_win");
        Path linux = langServ.resolve("config_linux");
        Path macos = langServ.resolve("config_mac");
        if(Tungsten.getOS().startsWith("Win")) return win.toAbsolutePath().toString();
        if(Tungsten.getOS().startsWith("Lin")) return linux.toAbsolutePath().toString();
        if(Tungsten.getOS().startsWith("Mac")) return macos.toAbsolutePath().toString();
        return win.toAbsolutePath().toString();
    }

    public LanguageServer() throws IOException {
        // find available port
        ServerSocket s = new ServerSocket(0);
        port = s.getLocalPort();
        addr = "127.0.0.1:" + 9999;
        s.close();

        Path LSPDir = Tungsten.APPDATA.resolve("lsp");
        Path JDTLangServ = LSPDir.resolve("jdt-lang-serv");

        String exe = LSPDir.resolve("lsp-ws-proxy/lsp-ws-proxy.exe").toAbsolutePath().toString();
        final ProcessBuilder pb = getProcessBuilder(JDTLangServ, LSPDir, exe);

        pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        // starting the process
        process = pb.start();
    }

    @NotNull
    private static ProcessBuilder getProcessBuilder(@NotNull Path JDTLangServ, @NotNull Path LSPDir, String exe) {
        String jar = JDTLangServ.resolve("plugins/org.eclipse.equinox.launcher_1.6.400.v20210924-0641.jar").toAbsolutePath().toString();
        String configOS = getConfigForOS(JDTLangServ);
        String work = LSPDir.resolve("work").toAbsolutePath().toString();

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
                "--add-modules=ALL-SYSTEM",
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
