package org.tungsten.client.util.render;

import org.tungsten.client.Tungsten;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

public class FontUtils {

    public static Font primaryFont;
    public static Font secondaryFont;
    public static Font backupFont;
    static {
        try {
            primaryFont = Font.createFont(Font.TRUETYPE_FONT, new File(Tungsten.class.getResource("/assets/tungsten/fonts/TitilliumWeb-SemiBold.ttf").toURI()));
            secondaryFont = Font.createFont(Font.PLAIN, new File(Tungsten.class.getResource("/assets/tungsten/fonts/Tajawal-Regular.ttf").toURI()));
            backupFont = Font.createFont(Font.PLAIN, new File(Tungsten.class.getResource("/assets/tungsten/fonts/OpenSans-Regular.ttf").toURI()));
        } catch (FontFormatException | IOException | URISyntaxException e) { throw new RuntimeException(e); }
    }

}
