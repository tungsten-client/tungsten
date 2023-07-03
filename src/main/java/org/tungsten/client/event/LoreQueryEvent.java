package org.tungsten.client.event;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class LoreQueryEvent extends Event {
    private ItemStack source;
    private List<Text> existingLore;

    public LoreQueryEvent(ItemStack source, List<Text> existingLore) {
        this.source = source;
        this.existingLore = existingLore;
    }

    public void addLore(String v) {
        existingLore.add(Text.of(v));
    }

    public void addClientLore(String v) {
        addLore("§7" + v + "§r");
    }

    public ItemStack getSource() {
        return this.source;
    }

    public List<Text> getExistingLore() {
        return this.existingLore;
    }
}
