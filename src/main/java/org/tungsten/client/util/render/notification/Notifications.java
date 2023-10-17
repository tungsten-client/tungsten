package org.tungsten.client.util.render.notification;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Builder;
import lombok.NonNull;
import me.x150.renderer.font.FontRenderer;
import me.x150.renderer.render.Renderer2d;
import net.minecraft.util.math.MathHelper;
import org.tungsten.client.Tungsten;
import org.tungsten.client.util.Texture;
import org.tungsten.client.util.render.Animation;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.tungsten.client.Tungsten.stack;
import static org.tungsten.client.util.render.FontUtils.primaryFont;

public class Notifications {
    public static List<Notifications> notifications = new CopyOnWriteArrayList<>();
    private static final long fadeTime = 300;
    private static String title;
    private static String content;
    private static long duration;
    private static long createdAt;
    private static int notiX;
    private static int notiY;
    private static int notiX2;
    private static int notiY2;
    private static final Color j = new Color(225, 104, 12);
    private static final Color mainColor = new Color(38, 34, 72);
    private static final Color outlineColor = new Color(22, 19, 46);
    public static int screenWidth;
    public static int screenHeight;
    public static FontRenderer notiRenderer;
    public static FontRenderer textRender;
    private int notiWidth = Notifications.notiX2 - Notifications.notiX;
    private int notiHeight = Notifications.notiY2 - Notifications.notiY;

    public int getNotiX() {
        return notiX;
    }

    public int getNotiX2() {
        return notiX2;
    }

    public int getNotiWidth() {
        return this.notiWidth;
    }

    public int getNotiHeight() {
        return this.notiHeight;
    }

    public int getNotiY() {
        return notiY;
    }
    public int getNotiY2() {
        return notiY2;
    }

    public void setNotiX(int newPos) {
        notiX = newPos;
    }

    public void setNotiX2(int newPos) {
        notiX2 = newPos;
    }

    public void setPosX(int newPos) {
        notiX -=  newPos;
        notiX2 -= newPos;
    }

    public void setPosY(int newPos) {
        notiY -=  newPos;
        notiY2 -= newPos;
    }

    public static void addNotification(Notifications notification) {
        notifications.add(notification);
    }

    public static Notifications.NotificationsBuilder newNotification(String content, long duration) {
        return Notifications.builder().content(content).duration(duration);
    }

    public static void removeInactive() {
        notifications.removeIf(notification -> notification.createdAt + notification.duration <= System.currentTimeMillis());
    }

    @Builder
    public Notifications(final String title, @NonNull final String content, final long duration) {
        this.title = title;
        this.content = content;
        this.duration = duration;
        this.createdAt = System.currentTimeMillis();
        Notifications.addNotification(this);
    }

    public double getFade() {
        long l = System.currentTimeMillis();
        long existed = l - createdAt;
        long end = createdAt + duration;
        long remaining = end - l;

        float fadeIn = Math.min(existed / (float) fadeTime, 1);
        float fadeOut = Math.min(remaining / (float) fadeTime, 1);
        return Animation.Easing.CIRC_IN_OUT.apply(Math.min(fadeIn, fadeOut));
    }

    public static void render() {
        Notifications.removeInactive();
        int y = 0;
        screenWidth = Tungsten.client.getWindow().getScaledWidth();
        screenHeight = Tungsten.client.getWindow().getScaledHeight();
        notiX = screenWidth - 105;
        notiY = screenHeight - 45;
        notiX2 = screenWidth - 8;
        notiY2 = screenHeight - 8;
        for (Notifications notification : Notifications.notifications) {
//            notification.setPosX(notification.getNotiX() - notification.getNotiWidth());
            notification.setPosY((notification.getNotiY() - y - notification.getNotiHeight()) - (screenHeight - 80));
            y -= (notification.getNotiHeight() + 5) * notification.getFade();

            if (notiRenderer == null || textRender == null) {
                try {
                    Notifications.notiRenderer = new FontRenderer(new Font[] { primaryFont }, 14F);
                    Notifications.textRender = new FontRenderer(new Font[] { primaryFont }, 10F);
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

            // Render noti text
            notiRenderer.drawString(
                    stack,
                    title,
                    notiX + 24,
                    notiY - 3,
                    0.968F,
                    0.964F,
                    0.788F,
                    1F
            );

            // Render toggle message
            textRender.drawString(
                    stack,
                    content,
                    notiX + 4F,
                    notiY + 15F,
                    0.968F,
                    0.964F,
                    0.788F,
                    1F
            );

            // Render progress bar
            // I couldn't figure out how to do this so shoutouts 0x, again LMAO
            long l = System.currentTimeMillis();
            float par = ((l - createdAt) - fadeTime) / (float) (duration - fadeTime * 2);
            par = MathHelper.clamp(par, 0, 1);

            double v = par * ((notiX2 - notiX) - 5D * 2D);
            if (v > 1) {
                Renderer2d.renderRoundedQuad(
                        stack,
                        j,
                        notiX + 4F,
                        notiY2 - 6F,
                        (notiX + 5) + v,
                        notiY2 - 4F,
                        .75F,
                        4F
                );
            }
        }
    }
}