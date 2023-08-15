package org.tungsten.client.feature.itemgroup;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;


public abstract class GenericItem {
    private final ItemStack item;
    private final String nbt;

    public GenericItem(ItemStack item, String nbt) {
        this.item = item;
        this.nbt = nbt;
    }

    public ItemStack create() {
        try {
            item.setNbt(StringNbtReader.parse(nbt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getNbt() {
        return nbt;
    }
}
