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
}
