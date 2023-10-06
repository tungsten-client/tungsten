package org.tungsten.client.util.render;

import lombok.Getter;
import me.x150.renderer.render.Renderer2d;
import me.x150.renderer.util.RendererUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

import static org.tungsten.client.Tungsten.client;

public class Line {
    @Getter
    public static double x = client.getWindow().getScaledWidth() / 2D;
    @Getter
    public static double y = client.getWindow().getScaledHeight() / 2D;
    /**
     * <p> Draws a line from your crosshair to a position on the world.</p>
     * <p> You need to get the correct MatrixStack in order for this to work. I used this context stack. </p>
     * <p>         {@link net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback#EVENT#register((MatrixStack, float)} </p>
     * <p> It has to also be on an HUD render event, as we are drawing a 2d line towards a 3d spot. </p>
     *
     * @param stack the current context MatrixStack.
     * @param color color of the line you are drawing to.
     * @param pos the position of the spot you want to draw a line to.
     *
     */
    public static void drawLineToPos(MatrixStack stack, Color color, Vec3d pos) {
        Vec3d j = RendererUtils.worldSpaceToScreenSpace(pos);
        double x = client.getWindow().getScaledWidth() / 2D;
        double y = client.getWindow().getScaledHeight() / 2D;

        Renderer2d.renderLine(
                stack,
                color,
                x,
                y,
                j.getX(),
                j.getY()
        );

    }
    public static void drawLineToPosInverse(MatrixStack stack, Color color, Vec3d pos) {
        Vec3d j = RenderUtils.worldSpaceToScreenSpaceInverse(pos);
        double x = client.getWindow().getScaledWidth() / 2D;
        double y = client.getWindow().getScaledHeight() / 2D;

        Renderer2d.renderLine(
                stack,
                color,
                x,
                y,
                j.getX(),
                j.getY()
        );
    }
}
