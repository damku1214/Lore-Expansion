package net.damku1214.loreexpansion.enchant;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.util.LETags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.Tags;

public class LEEnchants {
    public static final ResourceKey<Enchantment> AMBUSH = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "ambush"));
    public static final ResourceKey<Enchantment> BUSY_BEE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "busy_bee"));
    public static final ResourceKey<Enchantment> COMMITTED = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "committed"));
    public static final ResourceKey<Enchantment> CRITICAL_HIT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "critical_hit"));
    public static final ResourceKey<Enchantment> LEECHING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "leeching"));
    public static final ResourceKey<Enchantment> RADIANCE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "radiance"));

    public static final ResourceKey<Enchantment> RADIANCE_SHOT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "radiance_shot"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchants = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        register(
                context, AMBUSH, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.dynamicCost(15, 9),
                                Enchantment.dynamicCost(65, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );

        register(
                context, BUSY_BEE, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.dynamicCost(15, 9),
                                Enchantment.dynamicCost(65, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );

        register(
                context, COMMITTED, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.dynamicCost(15, 9),
                                Enchantment.dynamicCost(65, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).exclusiveWith(enchants.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
        );

        register(
                context, CRITICAL_HIT, Enchantment.enchantment(
                    Enchantment.definition(
                            items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                            2,
                            3,
                            Enchantment.dynamicCost(36, 2),
                            Enchantment.dynamicCost(37, 2),
                            8,
                            EquipmentSlotGroup.MAINHAND
                    )
                ).exclusiveWith(enchants.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
        );

        register(
                context, LEECHING, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.dynamicCost(15, 9),
                                Enchantment.dynamicCost(65, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).exclusiveWith(enchants.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
        );

        register(
                context, RADIANCE, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.dynamicCost(36, 2),
                                Enchantment.dynamicCost(37, 2),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );

        register(
                context, RADIANCE_SHOT, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(Tags.Items.RANGED_WEAPON_TOOLS),
                                2,
                                3,
                                Enchantment.dynamicCost(15, 9),
                                Enchantment.dynamicCost(65, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }
}
