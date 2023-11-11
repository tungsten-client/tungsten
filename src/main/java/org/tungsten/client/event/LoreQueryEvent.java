package org.tungsten.client.event;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public record LoreQueryEvent(ItemStack source, List<Text> existingLore) {
	public void addLore(String v) {
		existingLore.add(Text.of(v));
	}

	public void addClientLore(String v) {
		addLore("§7" + v + "§r");
	}
}
