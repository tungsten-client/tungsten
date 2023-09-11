package org.tungsten.client.feature.registry;

import net.minecraft.item.ItemStack;
import org.tungsten.client.feature.itemgroup.GenericItem;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {

    public static List<GenericItem> items = new ArrayList<>();

    public static int id = 0;

    public static GenericItem getByName(ItemStack item) {
        for (GenericItem i : items) {
            if (i.getItem().equals(item)) { return i; }
        }
        return null;
    }

    public static GenericItem getByID(int id) {
        return items.get(id);
    }

    public static GenericItem instanceFromClass(Class<?> clazz) {
        for (GenericItem i : items) {
            if (i.getClass().equals(clazz)) { return i; }
        }
        return null;
    }

    public static void addItem(GenericItem item) {
        items.add(item);
        id += 1;
    }
}