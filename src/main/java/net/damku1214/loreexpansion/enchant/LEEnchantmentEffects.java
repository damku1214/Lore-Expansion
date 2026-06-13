package net.damku1214.loreexpansion.enchant;

import com.mojang.serialization.MapCodec;
import net.damku1214.loreexpansion.LoreExpansion;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LEEnchantmentEffects {
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANT_EFFECTS =
            DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, LoreExpansion.MOD_ID);

    //public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> CRITICAL_HIT =
            //ENTITY_ENCHANT_EFFECTS.register("critical_hit", () -> CriticalHitEnchantEffect.CODEC);

    public static void register(IEventBus eventBus) {
        ENTITY_ENCHANT_EFFECTS.register(eventBus);
    }
}
