package org.tungsten.client.render.util;

import me.x150.renderer.render.Renderer2d;
import me.x150.renderer.util.RendererUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.tungsten.client.Tungsten;

import java.awt.*;

public class Line {
    public static void drawLineToPos(MatrixStack stack, Color color, Vec3d pos) {
        double x = Tungsten.client.getWindow().getScaledWidth() / 2D;
        double y = Tungsten.client.getWindow().getScaledHeight() / 2D;
        Vec3d j = RendererUtils.worldSpaceToScreenSpace(pos);
        double x2 = j.getX();
        double y2 = j.getY();
        Renderer2d.renderLine(
                stack,
                color,
                x,
                y,
                x2,
                y2
        );
    }
}
