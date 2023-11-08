package org.tungsten.client.util.render;

import org.tungsten.client.Tungsten;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class FontUtils {
    public static final Font primaryFont;
    public static final Font secondaryFont;
    public static final Font backupFont;

    static {
        try {
            primaryFont = Font.createFont(Font.TRUETYPE_FONT, new File(Tungsten.class.getResource("/assets/tungsten/fonts/TitilliumWeb-SemiBold.ttf").toURI()));
            secondaryFont = Font.createFont(Font.PLAIN, new File(Tungsten.class.getResource("/assets/tungsten/fonts/Tajawal-Regular.ttf").toURI()));
            backupFont = Font.createFont(Font.PLAIN, new File(Tungsten.class.getResource("/assets/tungsten/fonts/OpenSans-Regular.ttf").toURI()));
        } catch (FontFormatException | IOException | URISyntaxException e) { throw new RuntimeException(e); }
    }
}
