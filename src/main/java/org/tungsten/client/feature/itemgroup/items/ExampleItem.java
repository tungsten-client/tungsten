package org.tungsten.client.feature.itemgroup.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.tungsten.client.feature.itemgroup.GenericItem;

public class ExampleItem extends GenericItem {

    public ExampleItem() {
        super(new ItemStack(Items.DIAMOND), "{display:{Name:'{\"text\":\"Test Diamond\"}'}}");
    }
}
