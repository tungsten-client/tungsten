package org.tungsten.client.mixin;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerPositionLookS2CPacket.class)
public interface PlayerPositionLookPacketAccessor {
	@Mutable
	@Accessor("yaw")
	void setYaw(float yaw);

	@Mutable
	@Accessor("x")
	void setX(double x);

	@Mutable
	@Accessor("y")
	void setY(double y);

	@Mutable
	@Accessor("z")
	void setZ(double z);

	@Mutable
	@Accessor("pitch")
	void setPitch(float pitch);
}