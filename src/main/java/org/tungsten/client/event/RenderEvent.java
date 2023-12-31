package org.tungsten.client.event;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

public abstract class RenderEvent {
	public final MatrixStack contextStack;

	public RenderEvent(MatrixStack contextStack) {
		this.contextStack = contextStack;
	}

	public static class Entity extends RenderEvent {
		public final net.minecraft.entity.Entity entity;

		public Entity(MatrixStack stack, net.minecraft.entity.Entity entity) {
			super(stack);
			this.entity = entity;
		}
	}

	public static class BlockEntity extends RenderEvent {
		public final net.minecraft.block.entity.BlockEntity entity;

		public BlockEntity(MatrixStack stack, net.minecraft.block.entity.BlockEntity entity) {
			super(stack);
			this.entity = entity;
		}
	}

	public static class Block extends RenderEvent {
		public final BlockPos pos;
		public final BlockState state;

		public Block(MatrixStack stack, BlockPos pos, BlockState state) {
			super(stack);
			this.pos = pos;
			this.state = state;
		}
	}

	public static class Hud {
		public static final Hud INSTANCE = new Hud();
	}

	public static class HudNoMSAA extends RenderEvent {
		public HudNoMSAA(MatrixStack stack) {
			super(stack);
		}
	}

	public static class World extends RenderEvent {
		public World(MatrixStack stack) {
			super(stack);
		}
	}
}
