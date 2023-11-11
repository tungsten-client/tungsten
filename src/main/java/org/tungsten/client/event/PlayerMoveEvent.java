package org.tungsten.client.event;

import meteordevelopment.orbit.ICancellable;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class PlayerMoveEvent implements ICancellable {
    private static final PlayerMoveEvent EVENT = new PlayerMoveEvent();
    private boolean isCancelled;
    public MovementType type;
    public Vec3d requestedDelta;

    public static PlayerMoveEvent get(MovementType type, Vec3d requestedDelta) {
        EVENT.isCancelled = false;
        EVENT.type = type;
        EVENT.requestedDelta = requestedDelta;
        return EVENT;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }
}