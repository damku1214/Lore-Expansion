package net.damku1214.loreexpansion.entity.ai;

import net.damku1214.loreexpansion.entity.custom.PetEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.PathType;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class LEFollowOwnerGoal extends Goal {
    private final PetEntity pet;
    @Nullable
    private LivingEntity owner;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;

    public LEFollowOwnerGoal(PetEntity pet, double speedModifier, float startDistance, float stopDistance) {
        this.pet = pet;
        this.speedModifier = speedModifier;
        this.navigation = pet.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(pet.getNavigation() instanceof GroundPathNavigation) && !(pet.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.pet.getOwner();
        if (livingentity == null) {
            return false;
        } else if (this.pet.unableToMoveToOwner()) {
            return false;
        } else if (this.pet.distanceToSqr(livingentity) < (double)(this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else {
            if (this.pet.unableToMoveToOwner()) return false;
            assert this.owner != null;
            return !(this.pet.distanceToSqr(this.owner) <= (double) (this.stopDistance * this.stopDistance));
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.pet.getPathfindingMalus(PathType.WATER);
        this.pet.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.pet.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        boolean flag = this.pet.shouldTryTeleportToOwner();
        if (!flag) {
            assert this.owner != null;
            this.pet.getLookControl().setLookAt(this.owner, 10.0F, (float)this.pet.getMaxHeadXRot());
        }

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (flag) {
                this.pet.tryToTeleportToOwner();
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
            }
        }
    }
}
