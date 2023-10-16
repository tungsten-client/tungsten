package org.tungsten.client.util.render.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.x150.renderer.render.Renderer2d;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.modules.render.Hud;
import org.tungsten.client.util.render.Animation;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.tungsten.client.util.render.notification.NotificationUtil.notiRenderer;
import static org.tungsten.client.util.render.notification.NotificationUtil.textRender;

public class Notifications {
    public static List<Notifications> notifications = new CopyOnWriteArrayList<>();
    public static final long fadeTime = 300;
    private String title;
    private String content;
    private long duration;
    private float notiXXX;
    private long createdAt;
    private static int notiX;
    private static int notiY;
    private static int notiX2;
    private static int notiY2;
    @Getter @Setter
    private int notiWidth = NotificationUtil.notiX - NotificationUtil.notiX2;
    @Getter @Setter
    private int notiHeight = NotificationUtil.notiY - NotificationUtil.notiY2;

    public int getNotiX() {
        return notiX;
    }

    public int getNotiY() {
        return notiY;
    }

    public void setNotiX(int newPos) {
        notiX = newPos;
    }

    public void setNotiY(int newPos) {
        notiY = newPos;
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

        NotificationUtil.renderBasicElements();
        Notifications.notiX = NotificationUtil.notiX;
        Notifications.notiY = NotificationUtil.notiY;
        Notifications.notiX2 = NotificationUtil.notiX2;
        Notifications.notiY2 = NotificationUtil.notiY2;

        // Render noti text
        notiRenderer.drawString(
                Tungsten.stack,
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
                Tungsten.stack,
                content,
                notiX + 4F,
                notiY + 15F,
                0.968F,
                0.964F,
                0.788F,
                1F
        );

        // Render funny progress bar
        // Duration Goes here
        // TODO: Calculate math for duration in ms.
        notiXXX += Hud.daVal;
        if(-90F + notiXXX >= -4F) notiXXX = 0;
        Color c = new Color(225, 104, 12);
        Renderer2d.renderRoundedQuad(
                Tungsten.stack,
                c,
                notiX + 4F,
                notiY2 - 6F,
                notiX2 + -90F + notiXXX,
                notiY2 - 4F,
                2,
                10
        );

        Notifications.addNotification(this);
//        Notifications.removeInactive();

//        int y = 0;
//        for (Notifications notification : Notifications.notifications) {
//            notification.setNotiX(notification.getNotiX() - notification.getNotiWidth());
//            notification.setNotiY((notification.getNotiY() + y - notification.getNotiHeight()));
//            y -= (notification.getNotiHeight() + 5) * notification.getFade();
//        }
    }

//    public double getFade() {
//        long l = System.currentTimeMillis();
//        long existed = l - createdAt;
//        long end = createdAt + duration;
//        long remaining = end - l;
//
//        float fadeIn = Math.min(existed / (float) fadeTime, 1);
//        float fadeOut = Math.min(remaining / (float) fadeTime, 1);
//        return Animation.Easing.CIRC_IN_OUT.apply(Math.min(fadeIn, fadeOut));
//    }


}
