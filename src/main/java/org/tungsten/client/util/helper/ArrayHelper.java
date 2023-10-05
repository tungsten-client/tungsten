package org.tungsten.client.util.helper;

import java.util.Random;

public class ArrayHelper {
    private static final Random r = new Random();

    public static <T> T random(T[] a) {
        if (a == null) {
            return null;
        }
        int len = a.length;
        return a[r.nextInt(len)];
    }
}
