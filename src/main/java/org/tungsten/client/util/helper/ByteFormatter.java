package org.tungsten.client.util.helper;

import com.google.common.base.Preconditions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ByteFormatter {
    private static final Map<Long, String> UNIT_MAP = new LinkedHashMap<>() {{
        put(1L, "B");
        put(1L << 10, "KiB");
        put(1L << 10 << 10, "MiB"); // that's about what we're going to expect, anything over that is going to be overkill
    }};
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    private static String format(long size, long div, String unit) {
        return DECIMAL_FORMAT.format((double) size / div) + " " + unit;
    }

    public static String format(long s) {
        Preconditions.checkArgument(s >= 1, "Byte count invalid");
        List<Long> longs1 = UNIT_MAP.keySet().stream().toList();
        for (int i = longs1.size() - 1; i >= 0; i--) { // start from the top, at TiB
            long minUnit = longs1.get(i);
            if (s >= minUnit) {
                return format(s, minUnit, UNIT_MAP.get(minUnit)); // is s bigger than or eq to the min size of this unit? use it
            }
        }
        return null; // this should never happen
    }
}
