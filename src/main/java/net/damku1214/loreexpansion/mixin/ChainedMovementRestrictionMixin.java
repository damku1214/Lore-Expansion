package net.damku1214.loreexpansion.mixin;

import net.damku1214.loreexpansion.effect.LEEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class ChainedMovementRestrictionMixin {
    @ModifyVariable(method = "travel", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Vec3 modifiedVector(Vec3 travelVector) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasEffect(LEEffects.CHAINED)) {
            return new Vec3(0, travelVector.y, 0);
        } else {
            return travelVector;
        }
    }
}
