package org.tungsten.client.mixin;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlockState.class)
public class BlockStateMixin {
	//Many block states to hook into. May take some time.
	@Inject(at = @At("TAIL"), method = {"isFullCube"}, cancellable = true)
	void onFullCube(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		//Yet to add codes here, sorry. -Lefty
	}
}

