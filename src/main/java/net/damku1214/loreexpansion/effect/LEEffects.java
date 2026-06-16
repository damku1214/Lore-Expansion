package net.damku1214.loreexpansion.effect;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.effect.custom.ChainedEffect;
import net.damku1214.loreexpansion.effect.custom.MarkedEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LEEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, LoreExpansion.MOD_ID);

    public static final Holder<MobEffect> MARKED =
            MOB_EFFECTS.register("marked", () -> new MarkedEffect(MobEffectCategory.HARMFUL, 16776003));

    public static final Holder<MobEffect> CHAINED =
            MOB_EFFECTS.register("chained", () -> new ChainedEffect(MobEffectCategory.HARMFUL, 16535799));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
