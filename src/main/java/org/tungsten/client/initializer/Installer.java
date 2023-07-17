package org.tungsten.client.initializer;

import org.tungsten.client.util.WebUtils;

import java.io.File;
import java.io.IOException;

public class Installer {
    final static Runtime.Version JavaVersion = Runtime.version();
    final static String TMP = System.getProperty("java.io.tmpdir");

    // likely should change these to work cross-platform but too lazy for now. just want a working installer first
    final static String JDKFileName = String.format("jdk-%s_windows-x64_bin.exe", JavaVersion);
    final static String JDKURL = String.format("https://download.oracle.com/java/%s/latest/%s", JavaVersion, JDKFileName);
    final static String JDKPath = String.format("%s/%s", TMP, JDKFileName);

    public static void run() throws IOException, InterruptedException {
        // TODO check if jdk exists and return if it does

        // if jdk not found, install
        File f = new File(JDKPath);
        WebUtils.downloadURLToPath(JDKURL, f);

        // run installer
        ProcessBuilder pb = new ProcessBuilder(JDKPath);
        Process p = pb.start();
        p.waitFor();
    }
}
