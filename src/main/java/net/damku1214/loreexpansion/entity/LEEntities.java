package net.damku1214.loreexpansion.entity;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.entity.custom.ChainsEntity;
import net.damku1214.loreexpansion.entity.custom.MarkEntity;
import net.damku1214.loreexpansion.entity.custom.PetBeeEntity;
import net.damku1214.loreexpansion.entity.custom.SoulEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LEEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, LoreExpansion.MOD_ID);

    public static final Supplier<EntityType<MarkEntity>> MARK =
            ENTITY_TYPES.register("mark", () -> EntityType.Builder.<MarkEntity>of(MarkEntity::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F).clientTrackingRange(64).build("mark"));

    public static final Supplier<EntityType<PetBeeEntity>> PET_BEE =
            ENTITY_TYPES.register("pet_bee", () -> EntityType.Builder.<PetBeeEntity>of(PetBeeEntity::new, MobCategory.CREATURE)
                    .sized(0.7F, 0.6F).eyeHeight(0.3F).clientTrackingRange(64).build("pet_bee"));

    public static final Supplier<EntityType<ChainsEntity>> CHAINS =
            ENTITY_TYPES.register("chains", () -> EntityType.Builder.<ChainsEntity>of(ChainsEntity::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F).clientTrackingRange(64).noSummon().build("chains"));

    public static final Supplier<EntityType<SoulEntity>> SOUL =
            ENTITY_TYPES.register("soul", () -> EntityType.Builder.<SoulEntity>of(SoulEntity::new, MobCategory.MISC)
                    .sized(0.3F, 0.3F).clientTrackingRange(64).noSummon().build("soul"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
