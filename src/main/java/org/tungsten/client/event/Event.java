package org.tungsten.client.event;

import lombok.Getter;
import lombok.Setter;

import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;

public class Event {
	@Getter
	@Setter
	private boolean cancelled;
}
