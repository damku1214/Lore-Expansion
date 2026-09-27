package net.damku1214.loreexpansion.entity.custom;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.attachment.LEAttachments;
import net.damku1214.loreexpansion.attachment.SoulData;
import net.damku1214.loreexpansion.entity.LEEntities;
import net.damku1214.loreexpansion.event.LEEnchantEvents;
import net.damku1214.loreexpansion.network.SyncSoulsPacket;
import net.damku1214.loreexpansion.sound.LESounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SoulEntity extends Entity {
    private static final EntityDataAccessor<Integer> OWNER_ID =
            SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> IS_SOUL_SIPHON =
            SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> RISE_X =
            SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RISE_Z =
            SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.FLOAT);
    private static final int DELAY_TICKS = 20;
    private static final int RISE_TICKS = 40;
    private static final float RISE_HEIGHT = 2.5f;

    public static final int TRAIL_LENGTH = 20;
    public final double[] trailX = new double[TRAIL_LENGTH];
    public final double[] trailY = new double[TRAIL_LENGTH];
    public final double[] trailZ = new double[TRAIL_LENGTH];
    private int trailHead = 0;
    public boolean trailReady = false;

    private UUID ownerUUID;

    private static final float DRIFT_PER_TICK = 0.05f;

    public SoulEntity(EntityType<?> type, Level level) { super(type, level); }

    public SoulEntity(Player owner, double x, double y, double z, boolean isSoulSiphon) {
        this(LEEntities.SOUL.get(), owner.level());
        this.ownerUUID = owner.getUUID();
        this.entityData.set(OWNER_ID, owner.getId());
        this.entityData.set(IS_SOUL_SIPHON, isSoulSiphon);

        if (isSoulSiphon) {
            float degrees = (float) (Math.random() * 2 * Math.PI);
            this.entityData.set(RISE_X, (float) -Math.sin(degrees));
            this.entityData.set(RISE_Z, (float) Math.cos(degrees));
        }

        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_ID, -1);
        builder.define(IS_SOUL_SIPHON, false);
        builder.define(RISE_X, 0f);
        builder.define(RISE_Z, 0f);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            if (isVisible()) {
                if (!trailReady) {
                    // Fill all history slots with current position to prevent spawn streak
                    for (int i = 0; i < TRAIL_LENGTH; i++) {
                        trailX[i] = getX();
                        trailY[i] = getY();
                        trailZ[i] = getZ();
                    }
                    trailReady = true;
                }
                trailX[trailHead] = getX();
                trailY[trailHead] = getY();
                trailZ[trailHead] = getZ();
                trailHead = (trailHead + 1) % TRAIL_LENGTH;

                clientTick();
            }
            return;
        }

        Player owner = getOwner();

        // Re-resolve UUID after chunk load
        if (owner == null && ownerUUID != null && level() instanceof ServerLevel serverLevel) {
            if (serverLevel.getPlayerByUUID(ownerUUID) instanceof Player player) {
                owner = player;
                entityData.set(OWNER_ID, player.getId());
            }
        }

        if (owner == null || !owner.isAlive()) { discard(); return; }

        int activeTick = tickCount - DELAY_TICKS;
        if (activeTick <= 0) return;

        if (activeTick <= RISE_TICKS) {
            // Rise phase - move upward steadily
            double dy = RISE_HEIGHT / RISE_TICKS;
            setPos(getX() +  entityData.get(RISE_X) * DRIFT_PER_TICK, getY() + dy, getZ() +  entityData.get(RISE_Z) * DRIFT_PER_TICK);
        } else {
            // Chase phase - accelerate toward owner
            double dx = owner.getX() - getX();
            double dy = owner.getY() + owner.getBbHeight() * 0.5 - getY();
            double dz = owner.getZ() - getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

            // Collect on contact
            if (dist < 1.0) {
                SoulData data = owner.getData(LEAttachments.SOUL_DATA.get());
                data.addSouls(1, owner);
                PacketDistributor.sendToPlayer((ServerPlayer) owner, new SyncSoulsPacket(data.getSouls()));
                discard();

                LEEnchantEvents.applyAnimaConduit(owner);
                this.level().playSound(null, this.blockPosition(), LESounds.SOUL_COLLECT.get(), SoundSource.PLAYERS);

                return;
            }

            double speed = Math.min(0.2 + (activeTick - RISE_TICKS) * 0.015, 1.5);
            setDeltaMovement(dx / dist * speed, dy / dist * speed, dz / dist * speed);
            setPos(getX() + getDeltaMovement().x,
                    getY() + getDeltaMovement().y,
                    getZ() + getDeltaMovement().z);
        }
    }

    private void clientTick() {
        Player owner = getOwner();
        if (owner == null) return;

        int activeTick = tickCount - DELAY_TICKS;
        if (activeTick <= 0) return;

        if (activeTick <= RISE_TICKS) {
            double dy = RISE_HEIGHT / RISE_TICKS;
            setPos(getX() + entityData.get(RISE_X) * DRIFT_PER_TICK, getY() + dy, getZ() + entityData.get(RISE_Z) * DRIFT_PER_TICK);
        } else {
            double dx = owner.getX() - getX();
            double dy = owner.getY() + owner.getBbHeight() * 0.5 - getY();
            double dz = owner.getZ() - getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist < 1.0) return;

            double speed = Math.min(0.2 + (activeTick - RISE_TICKS) * 0.015, 1.5);
            setDeltaMovement(dx / dist * speed, dy / dist * speed, dz / dist * speed);
            setPos(getX() + getDeltaMovement().x,
                    getY() + getDeltaMovement().y,
                    getZ() + getDeltaMovement().z);
        }
    }

    public Player getOwner() {
        int id = entityData.get(OWNER_ID);
        if (id == -1) return null;
        return level().getEntity(id) instanceof Player player ? player : null;
    }

    // Renderer reads this to know whether to draw yet
    public boolean isVisible() { return tickCount > DELAY_TICKS; }

    @Override public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        if (ownerUUID != null) tag.putUUID("OwnerUUID", ownerUUID);
        tag.putBoolean("IsSoulSiphon", entityData.get(IS_SOUL_SIPHON));
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("OwnerUUID")) ownerUUID = tag.getUUID("OwnerUUID");
        if (tag.contains("IsSoulSiphon")) entityData.set(IS_SOUL_SIPHON, tag.getBoolean("IsSoulSiphon"));
    }

    public double getTrailX(int age) { return trailX[(trailHead - 1 - age + TRAIL_LENGTH) % TRAIL_LENGTH]; }
    public double getTrailY(int age) { return trailY[(trailHead - 1 - age + TRAIL_LENGTH) % TRAIL_LENGTH]; }
    public double getTrailZ(int age) { return trailZ[(trailHead - 1 - age + TRAIL_LENGTH) % TRAIL_LENGTH]; }
}
