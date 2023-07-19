package org.tungsten.client.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;

@Getter
@AllArgsConstructor
public class NoclipQueryEvent extends Event {
	final PlayerEntity player;
	@Setter
	boolean shouldNoclip;
}
