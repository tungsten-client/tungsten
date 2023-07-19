package org.tungsten.client.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

@AllArgsConstructor
@Getter
public class PlayerMoveEvent extends Event {
	MovementType type;
	Vec3d requestedDelta;
}