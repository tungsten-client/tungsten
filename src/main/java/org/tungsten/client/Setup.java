package org.tungsten.client;

import org.jetbrains.annotations.NotNull;
import org.tungsten.client.util.io.LibraryDownloader;

import javax.swing.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Setup {

    public static void setup() {
        LibraryDownloader.ensurePresent();
        setupJDK();
        setupIDE();
    }

    static void setupJDK() {
        if(System.getProperty("sun.boot.library.path").contains("jdk")) return;

        Tungsten.LOGGER.info("JDK not found");
        System.setProperty("java.awt.headless", "false");
        JOptionPane.showMessageDialog(null, "Compatible JDK installation not found. Please install JDK '17' (https://www.oracle.com/java/technologies/downloads/#java17)", "Missing JDK", JOptionPane.ERROR_MESSAGE);
        //System.exit(20);
        throw new IllegalStateException("Compatible JDK installation not found. Please install JDK '17'");
    }

    static void setupIDE() {
        if(new File(String.valueOf(Tungsten.APPDATA), "ide").exists() && new File(String.valueOf(Tungsten.APPDATA), "lsp").exists()) return;

        Tungsten.LOGGER.info("Setting up IDE appdata");

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        ZipInputStream ide = new ZipInputStream(cl.getResourceAsStream("ide.zip"));
        ZipInputStream lsp = new ZipInputStream(cl.getResourceAsStream("lsp.zip"));

        File ideDest = new File(String.valueOf(Tungsten.APPDATA), "ide");
        File lspDest = new File(String.valueOf(Tungsten.APPDATA), "lsp");

        try {
            unzip(ide, ideDest);
            unzip(lsp, lspDest);

            ide.close();
            lsp.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // util
    static @NotNull File newFileFromZipEntry(File dest, @NotNull ZipEntry entry) throws IOException {
        File destFile = new File(dest, entry.getName());

        String destDirPath = dest.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + entry.getName());
        }

        return destFile;
    }

    static void unzip(@NotNull ZipInputStream stream, File dest) throws IOException {
        byte[] buffer = new byte[1024];
        ZipEntry entry = stream.getNextEntry();

        while(entry != null) {
            File newf = newFileFromZipEntry(dest, entry);

            if(entry.isDirectory()) {
                if(!newf.isDirectory() && !newf.mkdirs()) {
                    throw new IOException("Failed to create dir " + newf);
                }
            } else {
                File parent = newf.getParentFile();
                if(!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create dir " + parent);
                }

                FileOutputStream out = new FileOutputStream(newf);
                int len;
                while((len = stream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.close();
            }

            entry = stream.getNextEntry();
        }
    }
}
