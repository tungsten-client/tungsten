package org.tungsten.client.util;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.tungsten.client.Tungsten;

public class MinecraftLogger {
    public static void message(Text t) {
        Tungsten.client.inGameHud.getChatHud().addMessage(t, null, null);
    }

    public static void info(String t) {
        message(Text.literal(t));
    }

    public static void error(String t) {
        message(Text.literal(t).styled(style -> style.withColor(Formatting.RED)));
    }
}
