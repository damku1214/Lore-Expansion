package net.damku1214.loreexpansion.effect.custom;

import net.damku1214.loreexpansion.entity.custom.MarkEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class MarkedEffect extends LEEffect {
    public MarkedEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(@NotNull LivingEntity livingEntity, int amplifier) {
        super.onEffectStarted(livingEntity, amplifier);
        MarkEntity markEntity = new MarkEntity(livingEntity);
        markEntity.setPos(livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() + 0.25, livingEntity.getZ());
        livingEntity.level().addFreshEntity(markEntity);
    }
}
