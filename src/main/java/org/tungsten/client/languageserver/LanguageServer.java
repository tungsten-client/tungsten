package org.tungsten.client.languageserver;

import org.tungsten.client.Tungsten;

import java.io.IOException;
import java.io.*;
import java.lang.*;
public class LanguageServer {

    // should I download the lsp on client init?
    private static LanguageServer inst = null;

    public static LanguageServer instance(){
        if(inst == null) {
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
        ProcessBuilder pb = new ProcessBuilder(Tungsten.RUNDIR + "\\appdata\\lsp\\lsp.bat");
        pb.directory(new File(Tungsten.RUNDIR +"\\appdata\\lsp\\"));
        // starting the process
        Process process = pb.start();

        // for reading the output from stream
        BufferedReader stdInput
                = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String s;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
    }


}
