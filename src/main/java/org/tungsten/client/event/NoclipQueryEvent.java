package org.tungsten.client.event;

import net.minecraft.entity.player.PlayerEntity;

public class NoclipQueryEvent {
	public final PlayerEntity player;
	public boolean shouldNoclip;

	public NoclipQueryEvent(PlayerEntity player, boolean shouldNoclip) {
		this.player = player;
		this.shouldNoclip = shouldNoclip;
	}
}
