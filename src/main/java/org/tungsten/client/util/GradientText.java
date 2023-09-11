package org.tungsten.client.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
public final class GradientText {
    // @formatter:off
    public static @NotNull MutableText get(final @NotNull String string,
                                           final @NotNull String hexStart,
                                           final @NotNull String hexEnd) { return get(Text.literal(string), hexStart, hexEnd); }
    // @formatter:on

    public static @NotNull <T extends MutableText> MutableText get(final @NotNull T text,
                                                                   final @NotNull String hexStart,
                                                                   final @NotNull String hexEnd) {
        final Color startColor, endColor;
        try {
            startColor = Color.decode(hexStart);
            endColor = Color.decode(hexEnd);
        } catch (final NumberFormatException ignored) {
            return text.copy();
        }

        final int length = text.getString().length();
        final Color[] colors = IntStream.range(0, length).mapToObj(i -> {
            final float t = (float) i / ((float) length - 1.0F);
            return new Color(// @formatter:off
                    (int) (startColor.getRed   () + t * (endColor.getRed   () - startColor.getRed  ())),
                    (int) (startColor.getGreen () + t * (endColor.getGreen () - startColor.getGreen())),
                    (int) (startColor.getBlue  () + t * (endColor.getBlue  () - startColor.getBlue ()))
            );// @formatter:on
        }).toArray(Color[]::new);

        final String[] charsAsStrings = text.getString()
                .chars()
                .mapToObj(c -> String.valueOf((char) c))
                .toArray(String[]::new);
        final MutableText returnText = Text.empty();
        for (int i = 0; i < length; i++) {
            final MutableText appendText = Text.literal(charsAsStrings[i]);
            appendText.setStyle(appendText.getStyle().withColor(colors[i].hashCode()));
            returnText.append(appendText);
        }
        return returnText;
    }
}