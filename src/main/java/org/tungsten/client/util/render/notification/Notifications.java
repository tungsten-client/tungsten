package org.tungsten.client.util.render.notification;

import lombok.Builder;
import lombok.NonNull;
import me.x150.renderer.render.Renderer2d;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.modules.render.Hud;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.tungsten.client.util.render.notification.NotificationUtil.*;

public class Notifications {
    public static List<Notifications> notifications = new CopyOnWriteArrayList<>();
    public static final long fadeTime = 300;
    private String title;
    private String content;
    private long duration;
    private float notiXXX;

    public static void addNotification(Notifications notification) {
        notifications.add(notification);
    }

    public static Notifications.NotificationsBuilder newNotification(String content, long duration) {
        return Notifications.builder().content(content).duration(duration);
    }
    @Builder
    public Notifications(final String title, @NonNull final String content, final long duration) {
        this.title = title;
        this.content = content;
        this.duration = duration;
        NotificationUtil.renderBasicElements();
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
    }
}
