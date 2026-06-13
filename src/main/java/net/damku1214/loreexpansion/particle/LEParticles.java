package net.damku1214.loreexpansion.particle;

import net.damku1214.loreexpansion.LoreExpansion;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LEParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, LoreExpansion.MOD_ID);

    public static final Supplier<SimpleParticleType> PET_BEE_SMOKE =
            PARTICLES.register("pet_bee_smoke", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> PET_BEE_SQUARE =
            PARTICLES.register("pet_bee_square", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> CRIT_SKULL =
            PARTICLES.register("crit_skull", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> CRIT_RING =
            PARTICLES.register("crit_ring", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> CRIT_SQUARE =
            PARTICLES.register("crit_square", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> RADIANCE_RING =
            PARTICLES.register("radiance_ring", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> RADIANCE_SQUARE =
            PARTICLES.register("radiance_square", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
}
