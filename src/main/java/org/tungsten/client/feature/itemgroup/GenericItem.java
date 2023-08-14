package org.tungsten.client.feature.itemgroup;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public abstract class GenericItem {
    private final ItemStack item;
    private final String nbt;

    public GenericItem(ItemStack item, String nbt) {
        this.item = item;
        this.nbt = nbt;
    }
    protected void register() {
        Registry.register(Registries.ITEM_GROUP, new Identifier("tungsten", "default"), FabricItemGroup.builder().displayName(Text.of("Test Name")).icon(() -> new ItemStack(Items.COMMAND_BLOCK)).entries((displayContext, entries) -> {
            ItemStack item = this.getItem();
            try {
                item.setNbt(StringNbtReader.parse(nbt));
            } catch (Exception e) {
                e.printStackTrace();
            }
            entries.add(item);
        }).build());
    }
    public ItemStack getItem(){return item;}
    public String getNbt(){return nbt;}

}
