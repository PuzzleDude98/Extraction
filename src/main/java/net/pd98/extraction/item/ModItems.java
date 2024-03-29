package net.pd98.extraction.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.pd98.extraction.Extraction;

public class ModItems {

    public static final Item EXTRACTOR = registerItem("extractor", new Item(new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Extraction.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Extraction.LOGGER.info("*yawn* Items set to register");
    }
}
