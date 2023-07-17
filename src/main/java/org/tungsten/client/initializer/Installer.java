package org.tungsten.client.initializer;

import dyorgio.runtime.run.as.root.RootExecutor;
import org.tungsten.client.Tungsten;
import org.tungsten.client.util.WebUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class Installer {
    // prob really unsafe to use; Oracle site only does base part, not 17.0.1 or whatever, so this was the best implementation i saw.
    final static String JavaVersion = Runtime.version().toString().split("\\.")[0];
    final static String TMP = System.getProperty("java.io.tmpdir");

    // likely should change these to work cross-platform but too lazy for now. just want a working installer first
    final static String JDKFileName = String.format("jdk-%s_windows-x64_bin.exe", JavaVersion);
    final static String JDKURL = String.format("https://download.oracle.com/java/%s/latest/%s", JavaVersion, JDKFileName);
    final static String JDKPath = Paths.get(TMP, JDKFileName).toString();

    public static void run() throws Exception { // stupid RootExecutor returns base Exception, had to do this awful throw statement
        Tungsten.LOGGER.info(String.format("Checking for JDK '%s'...", JavaVersion));

        try {
            ProcessBuilder pb = new ProcessBuilder("javac -version");
            Process p = pb.start();
            p.waitFor();
            
            // check if version matches
            if(Arrays.toString(p.getInputStream().readAllBytes()).startsWith(JavaVersion)) return;
        } catch(IOException ignored) {}


        // if jdk not found, install
        Tungsten.LOGGER.info("JDK not found, starting installation process...");

        Tungsten.LOGGER.info(String.format("Downloading JDK '%s' to '%s' from '%s'...", JavaVersion, JDKPath, JDKURL));
        File f = new File(JDKPath);
        WebUtils.downloadURLToPath(JDKURL, f);

        // run installer
        Tungsten.LOGGER.info("Requesting root elevation...");
        RootExecutor re = new RootExecutor(); // elevate to root perms for task

        Tungsten.LOGGER.info("Running JDK installer executable...");
        re.run(() -> {
            ProcessBuilder pb = new ProcessBuilder(JDKPath);
            try {
                Process p = pb.start();
                p.waitFor();
            } catch(InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
