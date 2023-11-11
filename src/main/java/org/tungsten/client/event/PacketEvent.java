package org.tungsten.client.event;

import lombok.Getter;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.network.packet.Packet;

@Getter
public abstract class PacketEvent implements ICancellable {
	private boolean isCancelled;
	protected Packet<?> packet;

	protected PacketEvent(Packet<?> packet) {
		this.packet = packet;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

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
