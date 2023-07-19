package org.tungsten.client.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.packet.Packet;

@AllArgsConstructor
@Getter
public abstract class PacketEvent extends Event {
	protected Packet<?> packet;

	public static class Received extends PacketEvent {
		public Received(Packet<?> packet) {
			super(packet);
		}
	}

	public static class Sent extends PacketEvent {
		public Sent(Packet<?> packet) {
			super(packet);
		}
	}
}
