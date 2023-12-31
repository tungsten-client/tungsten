package org.tungsten.client.initializer;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Unique;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.itemgroup.GenericItem;
import org.tungsten.client.feature.itemgroup.items.ExampleItem;
import org.tungsten.client.feature.registry.ItemRegistry;
import org.tungsten.client.util.render.GradientText;
import org.tungsten.client.util.io.TungstenClassLoader;
import org.tungsten.client.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ItemInitializer {
    private static final @Unique MutableText CreativeTabName = GradientText.get(Text.literal("Tungsten Items"), 0x585D5B, 0xEF712A);

    private static final Path ITEMS = Tungsten.RUNDIR.resolve("items");
    public static final Path ITEMS_COMPILED = Tungsten.APPDATA.resolve("item_tmp");

    public static void initItems() {

        Utils.ensureDirectoryIsCreated(ITEMS);
        Utils.ensureDirectoryIsCreated(ITEMS_COMPILED);
        searchForItems(ITEMS_COMPILED);
        ItemRegistry.addItem(new ExampleItem());

        Registry.register(Registries.ITEM_GROUP, new Identifier("tungsten", "default"), FabricItemGroup.builder().displayName(CreativeTabName.setStyle(CreativeTabName.getStyle().withBold(true))).icon(() -> new ItemStack(Items.IRON_INGOT)).entries((displayContext, entries) -> {
            for (GenericItem item : ItemRegistry.items) {
                entries.add(item.create());
            }
        }).build());
    }


    /*
        WE ARE LOADING COMPILED CLASSES, NOT COMPILING THEM
     */
    private static void searchForItems(Path path) {
        try (Stream<Path> v = Files.walk(path)) {
            v.filter(path1 -> Files.isRegularFile(path1) && path1.toString().endsWith(".class")).forEach(path1 -> {
                try {
                    initItem(path1);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initItem(Path item) throws Exception {
        try (InputStream file = Files.newInputStream(item)) {
            byte[] classBytes = file.readAllBytes();
            if (Utils.checkSignature(classBytes)) {
                throw new IllegalStateException("invalid class file, did not pass class file signature check");
            }
            Class<?> loadedItem = TungstenClassLoader.getInstance().registerClass(classBytes);
            if (GenericItem.class.isAssignableFrom(loadedItem)) {
                Class<GenericItem> loadedItemClass = (Class<GenericItem>) loadedItem;
                GenericItem itemInstance = loadedItemClass.getDeclaredConstructor().newInstance();
                Tungsten.LOGGER.info("Loaded item " + itemInstance.getItem());
                ItemRegistry.addItem(itemInstance);
            }
        }
    }
}
