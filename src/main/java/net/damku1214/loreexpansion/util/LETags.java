package net.damku1214.loreexpansion.util;

import net.damku1214.loreexpansion.LoreExpansion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class LETags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> SOUL_WEAPON = createTag("enchantable/soul_weapon");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, name));
        }
    }

    public static class Enchants {
        public static final TagKey<Enchantment> POWERFUL_ENCHANTS = createTag("powerful_enchants");

        public static final TagKey<Enchantment> HEALING_EXCLUSIVE_MELEE = createTag("exclusive_set/healing_melee");
        public static final TagKey<Enchantment> HEALING_EXCLUSIVE_RANGED = createTag("exclusive_set/healing_ranged");

        private static TagKey<Enchantment> createTag(String name) {
            return TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, name));
        }
    }
}
