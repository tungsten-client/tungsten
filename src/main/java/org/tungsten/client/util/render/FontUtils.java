package org.tungsten.client.util.render;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontUtils {

    public static Font primaryFont;
    public static Font secondaryFont;
    public static Font backupFont;

    static {
        try {
            primaryFont = Font.createFont(Font.PLAIN, new File("C:\\Users\\burke\\IdeaProjects\\tongueson\\src\\main\\resources\\assets\\tungsten\\font\\TitilliumWeb-SemiBold.ttf").getAbsoluteFile());
            secondaryFont = Font.createFont(Font.PLAIN, new File("C:\\Users\\burke\\IdeaProjects\\tongueson\\src\\main\\resources\\assets\\tungsten\\font\\Tajawal-Regular.ttf").getAbsoluteFile());
            backupFont = Font.createFont(Font.PLAIN, new File("C:\\Users\\burke\\IdeaProjects\\tongueson\\src\\main\\resources\\assets\\tungsten\\font\\OpenSans-Regular.ttf").getAbsoluteFile());
        } catch (FontFormatException | IOException e) { throw new RuntimeException(e); }
    }

}
