package net.damku1214.loreexpansion.sound;

import net.damku1214.loreexpansion.LoreExpansion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LESounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, LoreExpansion.MOD_ID);

    public static final Supplier<SoundEvent> AMBUSH_0 = registerSoundEvent("ambush_0");
    public static final Supplier<SoundEvent> AMBUSH_1 = registerSoundEvent("ambush_1");
    public static final Supplier<SoundEvent> AMBUSH_2 = registerSoundEvent("ambush_2");
    public static final Supplier<SoundEvent> CHAINS_CAST = registerSoundEvent("chains_cast");
    public static final Supplier<SoundEvent> CHAINS_BREAK = registerSoundEvent("chains_break");
    public static final Supplier<SoundEvent> CRIT = registerSoundEvent("crit");
    public static final Supplier<SoundEvent> LEECHING = registerSoundEvent("leeching");
    public static final Supplier<SoundEvent> RADIANCE_0 = registerSoundEvent("radiance_0");
    public static final Supplier<SoundEvent> RADIANCE_1 = registerSoundEvent("radiance_1");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
