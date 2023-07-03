package org.tungsten.client.event;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

public abstract class RenderEvent extends Event {
    protected MatrixStack contextStack;

    public RenderEvent(MatrixStack contextStack) {
        this.contextStack = contextStack;
    }

    public static class Entity extends RenderEvent {
        private net.minecraft.entity.Entity entity;

        public Entity(MatrixStack stack, net.minecraft.entity.Entity entity) {
            super(stack);
            this.entity = entity;
        }

        public net.minecraft.entity.Entity getEntity() {
            return this.entity;
        }
    }

    public static class BlockEntity extends RenderEvent {
        private net.minecraft.block.entity.BlockEntity entity;

        public BlockEntity(MatrixStack stack, net.minecraft.block.entity.BlockEntity entity) {
            super(stack);
            this.entity = entity;
        }

        public net.minecraft.block.entity.BlockEntity getBlockEntity() {
            return this.entity;
        }
    }

    public static class Block extends RenderEvent {
        private BlockPos pos;
        private BlockState state;

        public Block(MatrixStack stack, BlockPos pos, BlockState state) {
            super(stack);
            this.pos = pos;
            this.state = state;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public BlockState getState() {
            return this.state;
        }
    }

    public static class World extends RenderEvent {

        public World(MatrixStack stack) {
            super(stack);
        }
    }

    public MatrixStack getContextStack() {
        return this.contextStack;
    }
}
