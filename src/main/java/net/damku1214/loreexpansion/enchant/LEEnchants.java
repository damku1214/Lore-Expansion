package net.damku1214.loreexpansion.enchant;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.util.LEAttributes;
import net.damku1214.loreexpansion.util.LETags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class LEEnchants {
    private static final int COMMON_WEIGHT = 5;
    private static final int POWERFUL_WEIGHT = 3;
    private static final int COMMON_ANVIL_COST = 4;
    private static final int POWERFUL_ANVIL_COST = 8;

    public static final ResourceKey<Enchantment> AMBUSH = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "ambush"));
    public static final ResourceKey<Enchantment> ANIMA_CONDUIT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "anima_conduit"));
    public static final ResourceKey<Enchantment> BUSY_BEE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "busy_bee"));
    public static final ResourceKey<Enchantment> CHAINS = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "chains"));
    public static final ResourceKey<Enchantment> COMMITTED = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "committed"));
    public static final ResourceKey<Enchantment> CRITICAL_HIT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "critical_hit"));
    public static final ResourceKey<Enchantment> ENIGMA_RESONATOR = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "enigma_resonator"));
    public static final ResourceKey<Enchantment> LEECHING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "leeching"));
    public static final ResourceKey<Enchantment> RADIANCE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "radiance"));
    public static final ResourceKey<Enchantment> SOUL_SIPHON = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "soul_siphon"));

    public static final ResourceKey<Enchantment> RADIANCE_SHOT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "radiance_shot"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchants = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        register(
                context, AMBUSH, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                COMMON_WEIGHT,
                                2,
                                minCost(2, false),
                                maxCost(2, false),
                                COMMON_ANVIL_COST,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
        register(
                context, ANIMA_CONDUIT, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(LETags.Items.SOUL_WEAPON),
                                COMMON_WEIGHT,
                                3,
                                minCost(3, false),
                                maxCost(3, false),
                                COMMON_ANVIL_COST,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).exclusiveWith(enchants.getOrThrow(LETags.Enchants.HEALING_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "enchantment.anima_conduit"),
                                LEAttributes.SOUL_GATHERING,
                                LevelBasedValue.constant(1.0F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
        );
        register(
                context, BUSY_BEE, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                COMMON_WEIGHT,
                                3,
                                minCost(3, false),
                                maxCost(3, false),
                                COMMON_ANVIL_COST,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
        register(
                context, CHAINS, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                COMMON_WEIGHT,
                                3,
                                minCost(3, false),
                                maxCost(3, false),
                                COMMON_ANVIL_COST,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
        register(
                context, COMMITTED, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(LETags.Items.WEAPON),
                                COMMON_WEIGHT,
                                4,
                                minCost(4, false),
                                maxCost(4, false),
                                COMMON_ANVIL_COST,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).exclusiveWith(enchants.getOrThrow(LETags.Enchants.DAMAGE_EXCLUSIVE))
        );
        register(
                context, CRITICAL_HIT, Enchantment.enchantment(
                    Enchantment.definition(
                            items.getOrThrow(LETags.Items.WEAPON),
                            POWERFUL_WEIGHT,
                            5,
                            minCost(5, true),
                            maxCost(5, true),
                            POWERFUL_ANVIL_COST,
                            EquipmentSlotGroup.MAINHAND
                    )
                ).exclusiveWith(enchants.getOrThrow(LETags.Enchants.DAMAGE_EXCLUSIVE))
        );
        register(
                context, ENIGMA_RESONATOR, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(LETags.Items.SOUL_WEAPON),
                                COMMON_WEIGHT,
                                5,
                                minCost(5, false),
                                maxCost(5, false),
                                COMMON_ANVIL_COST,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).exclusiveWith(enchants.getOrThrow(LETags.Enchants.DAMAGE_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "enchantment.enigma_resonator"),
                                LEAttributes.SOUL_GATHERING,
                                LevelBasedValue.constant(1.0F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
        );
        register(
                context, LEECHING, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                COMMON_WEIGHT,
                                2,
                                minCost(2, false),
                                maxCost(2, false),
                                COMMON_ANVIL_COST,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).exclusiveWith(enchants.getOrThrow(LETags.Enchants.HEALING_EXCLUSIVE_MELEE))
        );
        register(
                context, RADIANCE, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                POWERFUL_WEIGHT,
                                3,
                                minCost(3, true),
                                maxCost(3, true),
                                POWERFUL_ANVIL_COST,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).exclusiveWith(enchants.getOrThrow(LETags.Enchants.HEALING_EXCLUSIVE_MELEE))
        );
        register(
                context, RADIANCE_SHOT, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(LETags.Items.RANGED_WEAPON),
                                COMMON_WEIGHT,
                                3,
                                minCost(3, false),
                                maxCost(3, false),
                                COMMON_ANVIL_COST,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).exclusiveWith(enchants.getOrThrow(LETags.Enchants.HEALING_EXCLUSIVE_RANGED))
        );
        register(
                context, SOUL_SIPHON, Enchantment.enchantment(
                                Enchantment.definition(
                                        items.getOrThrow(LETags.Items.SOUL_WEAPON),
                                        COMMON_WEIGHT,
                                        5,
                                        minCost(5, false),
                                        maxCost(5, false),
                                        COMMON_ANVIL_COST,
                                        EquipmentSlotGroup.MAINHAND
                                )
                        ).withEffect(
                                EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(
                                        ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "enchantment.soul_siphon"),
                                        LEAttributes.SOUL_GATHERING,
                                        LevelBasedValue.constant(1.0F),
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        )
        );
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }

    private static Enchantment.Cost minCost(int maxLevel, boolean isPowerful) {
        int target = isPowerful ? 39 : 34;
        int step = 1;
        return Enchantment.dynamicCost(target - (maxLevel - 1) * step, step);
    }

    private static Enchantment.Cost maxCost(int maxLevel, boolean isPowerful) {
        int target = isPowerful ? 40 : 36;
        int step = 1;
        return Enchantment.dynamicCost(target - (maxLevel - 1) * step, step);
    }
}
