package org.tungsten.client.util;

import net.minecraft.item.ItemStack;

public interface ClientPlayerInteractionInterface {
    float getCurrentBreakingProgress();

    void setBreakingBlock(boolean breakingBlock);

    ItemStack windowClick_PICKUP(int slot);

    ItemStack windowClick_QUICK_MOVE(int slot);

    ItemStack windowClick_THROW(int slot);

    void setBlockHitDelay(int delay);

    void setOverrideReach(boolean overrideReach);
}

