package org.tungsten.client.util.render.notification;

import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.font.FontRenderer;
import me.x150.renderer.render.Renderer2d;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.modules.render.Hud;
import org.tungsten.client.util.Texture;

import java.awt.*;

import static org.tungsten.client.util.render.FontUtils.primaryFont;
import static org.tungsten.client.util.render.notification.Notifications.*;

public class NotificationUtil {

    private static final Color mainColor = new Color(38, 34, 72);
    private static final Color outlineColor = new Color(22, 19, 46);
    public static int screenWidth;
    public static int screenHeight;
    public static int notiX;
    public static int notiY;
    public static int notiX2;
    public static int notiY2;
    public static FontRenderer notiRenderer;
    public static FontRenderer textRender;

    public static void renderBasicElements() {
        screenWidth = Tungsten.client.getWindow().getScaledWidth();
        screenHeight = Tungsten.client.getWindow().getScaledHeight();
        notiX = screenWidth - 105;
        notiY = screenHeight - 50;
        notiX2 = screenWidth - 8;
        notiY2 = screenHeight - 8;

        if (notiRenderer == null || textRender == null) {
            try {
                NotificationUtil.notiRenderer = new FontRenderer(new Font[] { primaryFont }, 14F);
                NotificationUtil.textRender = new FontRenderer(new Font[] { primaryFont }, 10F);
            } catch (Exception e) { throw new RuntimeException(e); }
    }
        // Render box bottom opposite of left side

        Renderer2d.renderRoundedQuad(
                Tungsten.stack,
                mainColor,
                notiX,
                notiY,
                notiX2,
                notiY2,
                3,
                10
        );
        // Box border
        Renderer2d.renderRoundedOutline(
                Tungsten.stack,
                outlineColor,
                notiX,
                notiY,
                notiX2 - 1,
                notiY2 - 1,
                3,
                2,
                10
        );

        // Render logo top left of noti
        RenderSystem.setShaderTexture(0, new Texture("icon/TungstenOnlyW.png"));
        RenderSystem.setShaderColor(1, 1, 1, 0.8F);
        Renderer2d.renderTexture(
                Tungsten.stack,
                notiX - 4,
                notiY - 4,
                30,
                30
        );

//        // Render noti text
//        notiRenderer.drawString(
//                Tungsten.stack,
//                "Toggle",
//                notiX + 24,
//                notiY - 3,
//                0.968F,
//                0.964F,
//                0.788F,
//                1F
//        );
//
//        // Render toggle message
//        textRender.drawString(
//                Tungsten.stack,
//                "Enabled Hud",
//                notiX + 4F,
//                notiY + 15F,
//                0.968F,
//                0.964F,
//                0.788F,
//                1F
//        );
//        // Render funny progress bar
//        notiXXX += Hud.daVal;
//        if(-90F + notiXXX >= -4F) notiXXX = 0;
//        Color c = new Color(225, 104, 12);
//        Renderer2d.renderRoundedQuad(
//                Tungsten.stack,
//                c,
//                notiX + 4F,
//                notiY2 - 6F,
//                notiX2 + -90F + notiXXX,
//                notiY2 - 4F,
//                2,
//                10
//                );
    }

}
