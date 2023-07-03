package org.tungsten.client.event;

import net.minecraft.entity.player.PlayerEntity;

public class NoclipQueryEvent extends Event {
    final PlayerEntity player;
    boolean shouldNoclip;

    public NoclipQueryEvent(PlayerEntity player, boolean shouldNoclip) {
        this.player = player;
        this.shouldNoclip = shouldNoclip;
    }
    public void setShouldNoclip(boolean shouldNoclip) {
        this.shouldNoclip = shouldNoclip;
    }
    public boolean getShouldNoclip() { return this.shouldNoclip; }

    public PlayerEntity getPlayer() { return this.player; }
}
