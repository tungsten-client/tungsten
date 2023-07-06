package org.tungsten.client.util;

import net.minecraft.util.math.Vec3d;

public interface ClientPlayerEntityInterface {
    void setNoClip(boolean noClip);

    void setMovementMultiplier(Vec3d movementMultiplier);
}