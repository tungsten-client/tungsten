package org.tungsten.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerEntity.class)
public interface ClientPlayerEntityAccessor {

	//can't we just run a check with .hasPermissionLevel? please remove this if it doesn't have a use beyond that
	@Accessor
	int getClientPermissionLevel();
}