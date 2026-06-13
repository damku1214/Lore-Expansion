package net.damku1214.loreexpansion.entity.custom;

import net.damku1214.loreexpansion.entity.LEEntities;
import net.damku1214.loreexpansion.entity.ai.LEFollowOwnerGoal;
import net.damku1214.loreexpansion.particle.LEParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PetBeeEntity extends PetEntity {
    private static final int MAX_STINGS = 3;
    private int stings = 0;
    private String spawnSource = "";
    private int age = 0;
    private static final int MAX_AGE = 600;

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(PetBeeEntity.class, EntityDataSerializers.BYTE);

    private float rollAmount;
    private float rollAmountO;
    private int underWaterTicks;

    public PetBeeEntity(EntityType<? extends PetBeeEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new PetBeeEntityLookControl(this);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    public PetBeeEntity(LivingEntity owner, String spawnSource) {
        this(LEEntities.PET_BEE.get(), owner.level());
        setOwnerUUID(owner.getUUID());
        setSpawnSource(spawnSource);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Stings", this.stings);
        compound.putString("SpawnSource", this.spawnSource);
        compound.putInt("Age", this.age);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.stings = compound.getInt("Stings");
        this.spawnSource = compound.getString("SpawnSource");
        this.age = compound.getInt("Age");
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        DamageSource damagesource = this.damageSources().sting(this);
        boolean flag = entity.hurt(damagesource, (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));

        if (flag) {
            if (this.level() instanceof ServerLevel serverlevel) {
                EnchantmentHelper.doPostAttackEffects(serverlevel, entity, damagesource);
            }

            if (entity instanceof LivingEntity livingentity) {
                livingentity.setStingerCount(livingentity.getStingerCount() + 1);
            }

            this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);

            if (this.stings >= MAX_STINGS - 1) {
                this.kill();
            } else {
                this.stings++;
            }
        }

        return flag;
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        Level level = this.level();
        if (!level.isClientSide) {
            ((ServerLevel) level).sendParticles(LEParticles.PET_BEE_SMOKE.get(), this.getX(), this.getY() + 0.5, this.getZ(), 16, -0.25 + Math.random() * 0.5, -0.125 + Math.random() * 0.25, -0.25 + Math.random() * 0.5, 0.05);
            ((ServerLevel) level).sendParticles(LEParticles.PET_BEE_SQUARE.get(), this.getX(), this.getY() + 0.5, this.getZ(), 10, 0, 0, 0, 0.1);
        }
        super.die(damageSource);
    }

    @Override
    public void tick() {
        super.tick();
        this.updateRollAmount();
        this.setAge(this.getAge() + 1);

        if (this.getAge() >= MAX_AGE) {
            this.kill();
        }
    }

    public float getRollAmount(float partialTick) {
        return Mth.lerp(partialTick, this.rollAmountO, this.rollAmount);
    }

    private void updateRollAmount() {
        this.rollAmountO = this.rollAmount;
        if (this.isRolling()) {
            this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
        } else {
            this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
        }
    }

    @Override
    protected void customServerAiStep() {
        if (this.isInWaterOrBubble()) {
            this.underWaterTicks++;
        } else {
            this.underWaterTicks = 0;
        }

        if (this.underWaterTicks > 20) {
            this.hurt(this.damageSources().drown(), 1.0F);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.setRolling(this.getTarget() != null && this.getTarget().distanceToSqr(this) < 4.0);
        }
    }

    public boolean isRolling() {
        return this.getRollingFlag();
    }

    public void setRolling(boolean isRolling) {
        this.setRollingFlag(isRolling);
    }

    private void setRollingFlag(boolean value) {
        if (value) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | 2));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~2));
        }
    }

    private boolean getRollingFlag() {
        return (this.entityData.get(DATA_FLAGS_ID) & 2) != 0;
    }

    public String getSpawnSource() {
        return this.spawnSource;
    }

    private void setSpawnSource(String source) {
        this.spawnSource = source;
    }

    public int getAge() {
        return this.age;
    }

    private void setAge(int age) {
        this.age = age;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 19.0)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level p_level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_level) {
            @Override
            public boolean isStableDestination(BlockPos p_27947_) {
                return !this.level.getBlockState(p_27947_.below()).isAir();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.BEE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    @Deprecated // FORGE: use jumpInFluid instead
    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluidTag) {
        this.jumpInLiquidInternal();
    }

    private void jumpInLiquidInternal() {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.01, 0.0));
    }

    @Override
    public void jumpInFluid(net.neoforged.neoforge.fluids.@NotNull FluidType type) {
        this.jumpInLiquidInternal();
    }

    @Override
    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
    }

    class PetBeeEntityLookControl extends LookControl {
        PetBeeEntityLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (PetBeeEntity.this.getTarget() != null) {
                super.tick();
            }
        }
    }
}
