package net.damku1214.loreexpansion.item;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.item.custom.FireworksArrowItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LEItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(LoreExpansion.MOD_ID);

    public static final DeferredItem<Item> FIREWORKS_ARROW =
            ITEMS.register("fireworks_arrow", () -> new FireworksArrowItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
