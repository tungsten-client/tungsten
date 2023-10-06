package org.tungsten.client.render.util;

import lombok.Getter;
import me.x150.renderer.font.FontRenderer;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontUtils {

    @Getter
    public static Font[] primaryFont;

    static {
        try {
            primaryFont = Font.createFonts(new File("assets/tungsten/TitilliumWeb-SemiBold.ttf"));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    @Getter
    public static Font[] secondaryFont;

    static {
        try {
            secondaryFont = Font.createFonts(new File("assets/tungsten/Tajawal-Regular.ttf"));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public static FontRenderer defaultFontRenderer = new FontRenderer(primaryFont, 16);
}
