package org.tungsten.client.util.render;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.eclipse.jgit.annotations.NonNull;
import org.jetbrains.annotations.Contract;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.tungsten.client.Tungsten.client;

import static me.x150.renderer.util.RendererUtils.*;

@SuppressWarnings("unused")
public class RenderUtils {

    public static boolean screenSpaceCoordinateIsVisible(Vec3d pos) {
        return pos != null && pos.z > -1 && pos.z < 1;
    }

    @Contract(value = "_ -> new", pure = true)
    public static Vec3d worldSpaceToScreenSpaceInverse(@NonNull Vec3d pos) {
        Camera camera = client.getEntityRenderDispatcher().camera;
        double scaleFactor = client.getWindow().getScaleFactor();
        int displayHeight = client.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        Vector3f target = new Vector3f();

        double deltaX = pos.x - camera.getPos().x;
        double deltaY = pos.y - camera.getPos().y;
        double deltaZ = pos.z - camera.getPos().z;

        Vector4f transformedCoordinates = new Vector4f(
                (float) deltaX,
                (float) deltaY,
                (float) deltaZ,
                1F
        ).mul(lastWorldSpaceMatrix);

        Matrix4f matrixProj = new Matrix4f(lastProjMat);
        Matrix4f matrixModel = new Matrix4f(lastModMat);

        matrixProj.mul(matrixModel).project(
                transformedCoordinates.x(),
                transformedCoordinates.y(),
                transformedCoordinates.z(),
                viewport,
                target
        );

        return new Vec3d(
                -target.x / scaleFactor,
                (displayHeight + target.y) / scaleFactor,
                target.z
        );

    }

    public static boolean inBounds(int mouseX, int mouseY, int elementX, int elementY, int elementWidth, int elementHeight) {
        return !((mouseX >= elementX && mouseX <= elementX + elementWidth) && (mouseY >= elementY && mouseY <= elementY + elementHeight));
    }

    public static Color lerp(Color a, Color b, float delta) {
        return new Color(MathHelper.lerp(delta, a.getRed(), b.getRed()),
                MathHelper.lerp(delta, a.getGreen(), b.getGreen()),
                MathHelper.lerp(delta, a.getBlue(), b.getBlue()),
                MathHelper.lerp(delta, a.getAlpha(), b.getAlpha()));
    }
}
