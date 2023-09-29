package org.tungsten.client.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public final class GradientText {
    public static @NotNull MutableText get(final @NotNull String string,
                                           final int hexStart,
                                           final int hexEnd) {
        final int r1 = ColorHelper.Argb.getRed(hexStart), g1 = ColorHelper.Argb.getGreen(hexStart), b1 = ColorHelper.Argb.getBlue(hexStart);
        final int r2 = ColorHelper.Argb.getRed(hexEnd), g2 = ColorHelper.Argb.getGreen(hexEnd), b2 = ColorHelper.Argb.getBlue(hexEnd);
        final int length = string.length();

        final MutableText returnText = Text.empty();
        for(int i = 0; i < length; i++) {
            final float delta = i / (length - 1f);
            final int color = ColorHelper.Argb.getArgb(0xFF,
                    MathHelper.lerp(delta, r1, r2),
                    MathHelper.lerp(delta, g1, g2),
                    MathHelper.lerp(delta, b1, b2)
            );
            returnText.append(Text.literal(Character.toString(string.codePointAt(i))).setStyle(Style.EMPTY.withColor(color)));
        }
        return returnText;
    }

    public static @NotNull <T extends MutableText> MutableText get(final @NotNull T text,
                                                                   final int hexStart,
                                                                   final int hexEnd) {
        final int r1 = ColorHelper.Argb.getRed(hexStart), g1 = ColorHelper.Argb.getGreen(hexStart), b1 = ColorHelper.Argb.getBlue(hexStart);
        final int r2 = ColorHelper.Argb.getRed(hexEnd), g2 = ColorHelper.Argb.getGreen(hexEnd), b2 = ColorHelper.Argb.getBlue(hexEnd);
        final int length = text.getString().length();

        final MutableText returnText = Text.empty();
        text.asOrderedText().accept((index, style, codePoint) -> {
            final float delta = index / (length - 1f);
            final int color = ColorHelper.Argb.getArgb(0xFF,
                    MathHelper.lerp(delta, r1, r2),
                    MathHelper.lerp(delta, g1, g2),
                    MathHelper.lerp(delta, b1, b2)
            );
            returnText.append(Text.literal(Character.toString(codePoint)).setStyle(style.withColor(color)));
            return true;
        });
        return returnText;
    }
}
