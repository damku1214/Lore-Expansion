package net.damku1214.loreexpansion.effect.custom;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.particle.LEParticles;
import net.damku1214.loreexpansion.sound.LESounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class ChainedEffect extends LEEffect {
    public ChainedEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "effect.chained"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().isClientSide && livingEntity.tickCount % 3 == 0) {
            livingEntity.level().addParticle(LEParticles.CHAINED_RING.get(), true, livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() * 0.5 + 0.45, livingEntity.getZ(), 0, 0, 0);
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }
}
