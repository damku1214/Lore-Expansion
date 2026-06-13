package net.damku1214.loreexpansion.entity.custom;

import net.damku1214.loreexpansion.effect.LEEffects;
import net.damku1214.loreexpansion.entity.LEEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MarkEntity extends Entity {
    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(MarkEntity.class, EntityDataSerializers.INT);
    private UUID targetUUID;

    public static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(MarkEntity.class, EntityDataSerializers.INT);
    public static final int[] DARK_COLOR = {249, 204, 84};
    public static final int[] BRIGHT_COLOR = {255, 247, 131};

    private int age;
    public static final int COLOR_CHANGE_PERIOD = 20;
    public static final int BRIGHTEN_TIME = 5;
    public static final int DARKEN_TIME = COLOR_CHANGE_PERIOD - BRIGHTEN_TIME;

    public MarkEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public MarkEntity(LivingEntity target) {
        this(LEEntities.MARK.get(), target.level());
        this.setTarget(target);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            // Target finding
            if (this.getTarget() == null && this.targetUUID != null && this.level() instanceof ServerLevel serverLevel) {
                Entity potentialTarget = serverLevel.getEntity(this.targetUUID);
                if (potentialTarget == null) {
                    this.discard();
                } else if (potentialTarget instanceof LivingEntity livingEntity) {
                    this.setTarget(livingEntity);
                }
            }

            if (this.getTarget() != null) {
                this.setPos(this.getTarget().getX(), this.getTarget().getY() + this.getTarget().getBbHeight() + 0.25, this.getTarget().getZ());
                if (!this.getTarget().hasEffect(LEEffects.MARKED)) {
                    this.discard();
                }
            }

            // Color changing
            if (this.age < COLOR_CHANGE_PERIOD) {
                this.age++;
            } else {
                this.age = 0;
            }
            this.entityData.set(AGE, this.age);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(TARGET_ID, -1);
        builder.define(AGE, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        if (this.targetUUID != null) {
            compound.putUUID("TargetUUID", this.targetUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        if (compound.hasUUID("TargetUUID")) {
            this.targetUUID = compound.getUUID("TargetUUID");
        }
    }

    public LivingEntity getTarget() {
        int id = this.entityData.get(TARGET_ID);
        if (id == -1) return null;
        return this.level().getEntity(id) instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    public void setTarget(LivingEntity target) {
        this.targetUUID = target.getUUID();
        this.entityData.set(TARGET_ID, target.getId());
    }
}
