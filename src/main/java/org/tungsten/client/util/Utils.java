package org.tungsten.client.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.tungsten.client.Tungsten;

import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    private static final int[] EXPECTED_CLASS_SIGNATURE = new int[] { 0xCA, 0xFE, 0xBA, 0xBE };
    public static boolean checkSignature(byte[] classFile){
        byte[] cSig = Arrays.copyOfRange(classFile, 0, 4);
        int[] cSigP = new int[4];
        for (int i = 0; i < cSig.length; i++) {
            cSigP[i] = Byte.toUnsignedInt(cSig[i]);
        }
        return !Arrays.equals(cSigP, EXPECTED_CLASS_SIGNATURE);
    }

    public static boolean isDirectoryEmpty(@NotNull File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory.");
        }

        String[] files = directory.list();
        return files == null || files.length == 0;
    }

    public static void ensureDirectoryIsCreated(@NotNull File directory){
        if (!directory.isDirectory()) directory.delete();
        if (!directory.exists()) directory.mkdir();
    }

    public static void deleteFilesExcept(@NotNull File directory, String... filenames) {

        if (!directory.isDirectory()) {
            Tungsten.LOGGER.warn("Invalid directory path: " + directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            Tungsten.LOGGER.error("An error occurred while retrieving files from the directory.");
            return;
        }

        for (File file : files) {
            if (file.isFile() && !containsFilename(filenames, file.getName())) {
                file.delete();
            }
        }
    }

    public static void deleteAllFiles(@NotNull File directory) {

        if (!directory.isDirectory()) {
            Tungsten.LOGGER.warn("Invalid directory path: " + directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            Tungsten.LOGGER.error("An error occurred while retrieving files from the directory.");
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            }else{
                if(file.isDirectory()){
                    deleteAllFiles(file);
                }
            }
        }
    }

    public static void unzip(File zipFile, @NotNull File folder) throws IOException {
        byte[] buffer = new byte[1024];
        if (!folder.exists()) {
            folder.mkdir();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(folder, fileName);
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();

                    try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(newFile))) {
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }

                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }



    @Contract(pure = true)
    private static boolean containsFilename(String @NotNull [] filenames, String filename) {
        for (String name : filenames) {
            if (name.equals(filename)) {
                return true;
            }
        }
        return false;
    }
}
