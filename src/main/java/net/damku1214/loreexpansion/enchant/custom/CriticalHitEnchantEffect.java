package net.damku1214.loreexpansion.enchant.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public record CriticalHitEnchantEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<CriticalHitEnchantEffect> CODEC = MapCodec.unit(CriticalHitEnchantEffect::new);

    @Override
    @ParametersAreNonnullByDefault
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if (Math.random() < 0.1 + enchantmentLevel * 0.05) {

        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
