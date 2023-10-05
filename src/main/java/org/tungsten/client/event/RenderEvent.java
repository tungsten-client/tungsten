package org.tungsten.client.event;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

@Getter
public abstract class RenderEvent extends Event {
	protected MatrixStack contextStack;

	public RenderEvent(MatrixStack contextStack) {
		this.contextStack = contextStack;
	}

	@Getter
	public static class Entity extends RenderEvent {
		private final net.minecraft.entity.Entity entity;

		public Entity(MatrixStack stack, net.minecraft.entity.Entity entity) {
			super(stack);
			this.entity = entity;
		}
	}

	@Getter
	public static class BlockEntity extends RenderEvent {
		private final net.minecraft.block.entity.BlockEntity entity;

		public BlockEntity(MatrixStack stack, net.minecraft.block.entity.BlockEntity entity) {
			super(stack);
			this.entity = entity;
		}
	}

	@Getter
	public static class Block extends RenderEvent {
		private final BlockPos pos;
		private final BlockState state;

		public Block(MatrixStack stack, BlockPos pos, BlockState state) {
			super(stack);
			this.pos = pos;
			this.state = state;
		}
	}

	public static class Hud {
		public static final Hud INSTANCE = new Hud();
	}

	public static class HudNoMSAA {
		public static final HudNoMSAA INSTANCE = new HudNoMSAA();
	}

	public static class World extends RenderEvent {
		public World(MatrixStack stack) {
			super(stack);
		}
	}
}
