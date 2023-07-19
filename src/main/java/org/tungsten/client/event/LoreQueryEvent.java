package org.tungsten.client.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

@AllArgsConstructor
@Getter
public class LoreQueryEvent extends Event {
	private ItemStack source;
	private List<Text> existingLore;

	public void addLore(String v) {
		existingLore.add(Text.of(v));
	}

	public void addClientLore(String v) {
		addLore("§7" + v + "§r");
	}
}
