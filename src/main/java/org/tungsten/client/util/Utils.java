package org.tungsten.client.util;

import java.io.File;
import java.util.Arrays;

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

    public static void ensureDirectoryIsCreated(File directory){
        if (!directory.isDirectory()) directory.delete();
        if (!directory.exists()) directory.mkdir();
    }

    public static void deleteFilesExcept(File directory, String... filenames) {

        if (!directory.isDirectory()) {
            System.out.println("Invalid directory path: " + directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            System.out.println("An error occurred while retrieving files from the directory.");
            return;
        }

        for (File file : files) {
            if (file.isFile() && !containsFilename(filenames, file.getName())) {
                file.delete();
            }
        }
    }

    public static void deleteAllFiles(File directory) {

        if (!directory.isDirectory()) {
            System.out.println("Invalid directory path: " + directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            System.out.println("An error occurred while retrieving files from the directory.");
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



    private static boolean containsFilename(String[] filenames, String filename) {
        for (String name : filenames) {
            if (name.equals(filename)) {
                return true;
            }
        }
        return false;
    }
}
