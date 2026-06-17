package net.damku1214.loreexpansion.entity.custom;

import net.damku1214.loreexpansion.effect.LEEffects;
import net.damku1214.loreexpansion.entity.LEEntities;
import net.damku1214.loreexpansion.particle.LEParticles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChainsEntity extends Entity {
    private static final EntityDataAccessor<Integer> FROM_ID =
            SynchedEntityData.defineId(ChainsEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TO_ID =
            SynchedEntityData.defineId(ChainsEntity.class, EntityDataSerializers.INT);
    private static final int GROW_TICKS = 2;

    private UUID fromUUID, toUUID;

    public ChainsEntity(EntityType<?> type, Level level) { super(type, level); }

    public ChainsEntity(LivingEntity from, LivingEntity to) {
        this(LEEntities.CHAINS.get(), from.level());
        this.fromUUID = from.getUUID();
        this.toUUID   = to.getUUID();
        this.entityData.set(FROM_ID, from.getId());
        this.entityData.set(TO_ID,   to.getId());
        this.setPos(from.getX(), from.getY() + from.getBbHeight() * 0.5, from.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FROM_ID, -1);
        builder.define(TO_ID,   -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) return;

        LivingEntity from = getFrom();
        LivingEntity to   = getTo();

        // Re-resolve UUIDs after chunk load
        if ((from == null || to == null) && level() instanceof ServerLevel serverLevel) {
            if (from == null && fromUUID != null)
                from = serverLevel.getEntity(fromUUID) instanceof LivingEntity le ? le : null;
            if (to == null && toUUID != null)
                to = serverLevel.getEntity(toUUID) instanceof LivingEntity le ? le : null;

            if (from != null) entityData.set(FROM_ID, from.getId());
            if (to   != null) entityData.set(TO_ID,   to.getId());
        }

        // Discard if either end is gone or no longer chained
        if (from == null || to == null
                || !from.hasEffect(LEEffects.CHAINED)
                || !to.hasEffect(LEEffects.CHAINED)
                || !from.isAlive() || !to.isAlive()) {
            this.discard();
            return;
        }

        this.setPos(from.getX(), from.getY() + from.getBbHeight() * 0.5, from.getZ());
    }

    public LivingEntity getFrom() {
        int id = entityData.get(FROM_ID);
        return id == -1 ? null : level().getEntity(id) instanceof LivingEntity le ? le : null;
    }

    public LivingEntity getTo() {
        int id = entityData.get(TO_ID);
        return id == -1 ? null : level().getEntity(id) instanceof LivingEntity le ? le : null;
    }

    @Override public void addAdditionalSaveData(CompoundTag tag) {
        if (fromUUID != null) tag.putUUID("FromUUID", fromUUID);
        if (toUUID   != null) tag.putUUID("ToUUID",   toUUID);
    }

    @Override public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("FromUUID")) fromUUID = tag.getUUID("FromUUID");
        if (tag.hasUUID("ToUUID"))   toUUID   = tag.getUUID("ToUUID");
    }

    public float getAnimatedWidth(float partialTick, float baseWidth) {
        if (tickCount >= GROW_TICKS) return baseWidth;
        float t = (tickCount + partialTick) / GROW_TICKS;
        // Overshoot easing: grows to 1.3x then settles at 1.0x
        float eased = t < 0.6f
                ? t / 0.6f * 1.3f
                : 1.3f - (t - 0.6f) / 0.4f * 0.3f;
        return baseWidth * eased;
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        super.remove(reason);
        if (level().isClientSide) return;

        LivingEntity from = getFrom();
        LivingEntity to   = getTo();
        if (from == null || to == null) return;

        float dx = (float)(to.getX() - from.getX());
        float dy = (float)(to.getY() - from.getY());
        float dz = (float)(to.getZ() - from.getZ());

        float beamLength = Mth.sqrt(dx * dx + dy * dy + dz * dz);

        for (float i = 0.0f; i < beamLength; i += 1.5f) {
            double t = (i + 0.5 + (Math.random() - 0.5) * 0.4) / beamLength;
            double px = Mth.lerp(t, from.getX(), to.getX());
            double py = Mth.lerp(t, from.getY() + from.getBbHeight() * 0.5, to.getY() + to.getBbHeight() * 0.5);
            double pz = Mth.lerp(t, from.getZ(), to.getZ());
            ((ServerLevel)level()).sendParticles(LEParticles.CHAINS_SQUARE.get(), px, py, pz, 1, 0.5, 0, 0.5, 0);
        }
    }
}
