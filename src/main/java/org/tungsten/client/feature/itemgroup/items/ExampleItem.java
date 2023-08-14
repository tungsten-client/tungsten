package org.tungsten.client.feature.itemgroup.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import org.tungsten.client.feature.itemgroup.GenericItem;

public class ExampleItem extends GenericItem {

    public ExampleItem(ItemStack item, String nbt) {
        super(item, nbt);
    }
    public ItemStack itemStack = getItem();
    String nbt = getNbt();
    @Override
    protected void register() {
        nbt = "{display:{Name:'{\"text\":\"Test Dirt Block\"}'}}";
        itemStack = new ItemStack(Items.DIRT);
        try {
            getItem().setNbt(StringNbtReader.parse(getNbt()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Registering " + itemStack + "...");
    }
}
