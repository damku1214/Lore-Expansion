package net.damku1214.loreexpansion.util;

import net.damku1214.loreexpansion.LoreExpansion;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LEAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, LoreExpansion.MOD_ID);

    public static final Holder<Attribute> MAX_SOULS = ATTRIBUTES.register("max_souls",
            () -> new RangedAttribute("attribute.loreexpansion.max_souls", 100.0, 0.0, 10000.0)
                    .setSyncable(true));

    // Multiplier on souls gained per kill; 0 = no gathering, 1 = default
    public static final Holder<Attribute> SOUL_GATHERING = ATTRIBUTES.register("soul_gathering",
            () -> new RangedAttribute("attribute.loreexpansion.soul_gathering", 0.0, 0.0, 100.0)
                    .setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}