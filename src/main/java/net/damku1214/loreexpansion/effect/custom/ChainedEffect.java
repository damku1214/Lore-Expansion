package net.damku1214.loreexpansion.effect.custom;

import net.damku1214.loreexpansion.LoreExpansion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ChainedEffect extends LEEffect {
    public ChainedEffect(MobEffectCategory category, int color) {
        super(category, color);
        //this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "effect.chained"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
}
