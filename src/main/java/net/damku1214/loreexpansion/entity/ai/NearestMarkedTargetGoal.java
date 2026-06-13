package net.damku1214.loreexpansion.entity.ai;

import net.damku1214.loreexpansion.effect.LEEffects;
import net.damku1214.loreexpansion.entity.custom.PetEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class NearestMarkedTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public NearestMarkedTargetGoal(PetEntity mob, Class<T> targetType, boolean mustSee) {
        super(mob, targetType, mustSee);
    }

    @Override
    protected void findTarget() {
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
            List<T> entities = this.mob.level().getNearbyEntities(this.targetType, this.targetConditions, this.mob, this.getTargetSearchArea(this.getFollowDistance()));
            for (LivingEntity entity : entities) {
                if (entity.hasEffect(LEEffects.MARKED) && entity.getUUID() != this.mob.getUUID()) {
                    this.target = entity;
                }
            }
        } else {
            LivingEntity player = this.mob.level().getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            if (player != null && player.hasEffect(LEEffects.MARKED) && player.getUUID() != this.mob.getUUID()) {
                this.target = player;
            }
        }
    }
}
