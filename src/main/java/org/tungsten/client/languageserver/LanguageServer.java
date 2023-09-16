package org.tungsten.client.languageserver;

import org.jetbrains.annotations.NotNull;
import org.tungsten.client.Tungsten;

import java.io.IOException;
import java.io.*;
import java.lang.*;
import java.nio.file.Path;

public class LanguageServer {

    // should I download the lsp on client init?
    private static LanguageServer inst = null;

    public static LanguageServer instance() {
        if(inst == null) {
            Tungsten.LOGGER.info("Starting language sever");
            Runnable runnable = () -> {
                try {
                    inst = new LanguageServer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

            Thread thread = new Thread(runnable);

            thread.start();
        }
        return inst;
    }

    public LanguageServer() throws IOException {
        Path LSPDir = Tungsten.APPDATA.resolve("lsp");
        Path JDTLangServ = LSPDir.resolve("jdt-lang-serv");

        String exe = String.valueOf(LSPDir.resolve("lsp-ws-proxy/lsp-ws-proxy.exe"));
        final ProcessBuilder pb = getProcessBuilder(JDTLangServ, LSPDir, exe);
        pb.directory(new File(Tungsten.RUNDIR +"\\appdata\\lsp\\"));
        // starting the process
        Process process = pb.start();

        // for reading the output from stream
        BufferedReader stdInput
                = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String s;
        while ((s = stdInput.readLine()) != null) {
            Tungsten.LOGGER.debug(s);
        }
    }

    @NotNull
    private static ProcessBuilder getProcessBuilder(@NotNull Path JDTLangServ, @NotNull Path LSPDir, String exe) {
        String jar = String.valueOf(JDTLangServ.resolve("plugins/org.eclipse.equinox.launcher_1.6.400.v20210924-0641.jar"));
        String configWin = String.valueOf(JDTLangServ.resolve("config_win"));
        String work = String.valueOf(LSPDir.resolve("work"));

        return new ProcessBuilder(
                exe,
                "-r",
                "-l 9999", // might want to make this autoadjust if port taken
                "--",
                "java",
                "-Declipse.application=org.eclipse.jdt.ls.core.id1",
                "-Dosgi.bundles.defaultStartLevel=4",
                "-Declipse.product=org.eclipse.jdt.ls.core.product",
                "-Dlog.protocol=true",
                "-Dlog.level=ALL",
                "-Xmx1G",
                "--add-module=ALL-SYSTEM",
                "--add-opens java.base/java.util=ALL-UNNAMED",
                "--add-opens java.base/java.lang=ALL-UNNAMED",
                "-jar " + jar,
                "-configuration " + configWin,
                "-data " + work
        );
    }


}
