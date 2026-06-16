package net.damku1214.loreexpansion.event;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.effect.LEEffects;
import net.damku1214.loreexpansion.enchant.LEEnchants;
import net.damku1214.loreexpansion.entity.custom.PetBeeEntity;
import net.damku1214.loreexpansion.particle.LEParticles;
import net.damku1214.loreexpansion.sound.LESounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.List;

@EventBusSubscriber(modid = LoreExpansion.MOD_ID)
public class LEEnchantEvents {
    @SubscribeEvent
    public static void procOnHitEnchants(LivingIncomingDamageEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof LivingEntity livingEntity) || attacker.level().isClientSide) return;

        ItemStack stack = livingEntity.getMainHandItem();

        boolean isFullAttack = !(attacker instanceof Player player) || player.getAttackStrengthScale(0.5F) > 0.9f;
        if (event.getSource().is(DamageTypeTags.IS_PLAYER_ATTACK) && isFullAttack) {
            procMeleeEnchants(event, stack);
        } else if (event.getSource().type().msgId().equals("arrow")) {
            procRangedEnchants(event, stack);
        }
    }

    @SubscribeEvent
    public static void procOnKillEnchants(LivingDeathEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof LivingEntity livingEntity) || attacker.level().isClientSide) return;

        ItemStack stack = livingEntity.getMainHandItem();

        attemptBusyBee(event, stack);
        applyLeeching(event, stack);
    }

    public static void procMeleeEnchants(LivingIncomingDamageEvent event, ItemStack stack) {
        attemptAmbush(event, stack);
        attemptChains(event, stack);
        applyCommitted(event, stack);
        attemptCriticalHit(event, stack);
        attemptRadiance(event, stack, true);
    }

    public static void procRangedEnchants(LivingIncomingDamageEvent event, ItemStack stack) {
        attemptRadiance(event, stack, false);
    }

    public static void attemptAmbush(LivingIncomingDamageEvent event, ItemStack stack) {
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        assert attacker != null;
        LivingEntity target = event.getEntity();

        ServerLevel level = ((ServerLevel) attacker.level());
        int enchantLevel = stack.getEnchantmentLevel(attacker.level().holderOrThrow(LEEnchants.AMBUSH));

        boolean isAttackerNotTargeted;
        if (target instanceof Mob mob) {
            isAttackerNotTargeted = mob.getTarget() != attacker;
        } else {
            isAttackerNotTargeted = false;
        }
        boolean isAttackerBehindTarget = attacker.getYRot() >= target.getYRot() - 30 && attacker.getYRot() <= target.getYRot() + 30;
        boolean isAmbushValid = isAttackerNotTargeted || isAttackerBehindTarget;

        if (event.getAmount() > 0 && enchantLevel > 0 && isAmbushValid) {
            event.setAmount(event.getAmount() * (1 + 0.2f * enchantLevel));
            emitCritParticles(level, attacker, target);
            attacker.level().playSound(null, target.blockPosition(), (enchantLevel == 1 ? LESounds.AMBUSH_0 : (enchantLevel == 2 ? LESounds.AMBUSH_1 : LESounds.AMBUSH_2)).get(), SoundSource.PLAYERS);
        }
    }

    public static void attemptBusyBee(LivingDeathEvent event, ItemStack stack) {
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        assert attacker != null;
        LivingEntity target = event.getEntity();

        ServerLevel level = ((ServerLevel) attacker.level());
        int enchantLevel = stack.getEnchantmentLevel(attacker.level().holderOrThrow(LEEnchants.BUSY_BEE));
        boolean isTargetCorrectType = target instanceof Monster || target instanceof Player;

        if (enchantLevel > 0 && Math.random() < enchantLevel * 0.1 + 0.1 && isTargetCorrectType) {
            List<PetBeeEntity> nearbyBees = level.getNearbyEntities(PetBeeEntity.class, TargetingConditions.forNonCombat(), attacker, attacker.getBoundingBox().inflate(100));
            int busyBeeCount = 0;
            for (PetBeeEntity nearbyBee : nearbyBees) {
                if (nearbyBee.getSpawnSource().equals("busy_bee")) {
                    busyBeeCount++;
                }
            }

            if (busyBeeCount >= 3) {
                return;
            }

            PetBeeEntity bee = new PetBeeEntity(attacker, "busy_bee");
            bee.setPos(target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ());
            level.addFreshEntity(bee);

            level.sendParticles(LEParticles.PET_BEE_SMOKE.get(), bee.getX(), bee.getY() + 0.5, bee.getZ(), 16, -0.25 + Math.random() * 0.5, -0.125 + Math.random() * 0.25, -0.25 + Math.random() * 0.5, 0.05);
            level.sendParticles(LEParticles.PET_BEE_SQUARE.get(), bee.getX(), bee.getY() + 0.5, bee.getZ(), 10, 0, 0, 0, 0.1);
        }
    }

    public static void attemptChains(LivingIncomingDamageEvent event, ItemStack stack) {
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        assert attacker != null;
        LivingEntity target = event.getEntity();

        ServerLevel level = (ServerLevel) attacker.level();
        int enchantLevel = stack.getEnchantmentLevel(attacker.level().holderOrThrow(LEEnchants.CHAINS));

        if (enchantLevel == 0 || !(Math.random() < 0.3)) return;

        int durationTicks = enchantLevel * 20;

        AABB area = target.getBoundingBox().inflate(5);
        List<LivingEntity> nearby = level.getNearbyEntities(LivingEntity.class,
                TargetingConditions.forCombat(), attacker, area);

        int chainedCount = 0;
        for (LivingEntity mob : nearby) {
            if (mob == attacker || chainedCount > 3) break;
            mob.addEffect(new MobEffectInstance(LEEffects.CHAINED, durationTicks, 0, false, false));
            level.playSound(null, target.blockPosition(), LESounds.CHAINS_CAST.get(), SoundSource.PLAYERS);
            chainedCount ++;
        }
    }

    public static void applyCommitted(LivingIncomingDamageEvent event, ItemStack stack) {
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        assert attacker != null;
        LivingEntity target = event.getEntity();

        ServerLevel level = ((ServerLevel) attacker.level());
        int enchantLevel = stack.getEnchantmentLevel(attacker.level().holderOrThrow(LEEnchants.COMMITTED));

        float targetHealthPercent = target.getHealth() / target.getMaxHealth();

        if (event.getAmount() > 0 && enchantLevel > 0) {
            event.setAmount(event.getAmount() * (1 + (1 - targetHealthPercent) * (0.25f + 0.25f * enchantLevel)));
            level.sendParticles(LEParticles.CRIT_SQUARE.get(), target.getX(), target.getY() + 0.5, target.getZ(), 12, 0, 0, 0, 0.5);
        }
    }

    public static void attemptCriticalHit(LivingIncomingDamageEvent event, ItemStack stack) {
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        assert attacker != null;
        LivingEntity target = event.getEntity();

        ServerLevel level = ((ServerLevel) attacker.level());
        int enchantLevel = stack.getEnchantmentLevel(attacker.level().holderOrThrow(LEEnchants.CRITICAL_HIT));

        if (event.getAmount() > 0 && enchantLevel > 0 && Math.random() < enchantLevel * 0.05 + 0.05) {
            event.setAmount(event.getAmount() * 3);
            emitCritParticles(level, attacker, target);
            level.playSound(null, target.blockPosition(), LESounds.CRIT.get(), SoundSource.PLAYERS);
        }
    }

    public static void applyLeeching(LivingDeathEvent event, ItemStack stack) {
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        assert attacker != null;
        LivingEntity target = event.getEntity();

        int enchantLevel = stack.getEnchantmentLevel(attacker.level().holderOrThrow(LEEnchants.LEECHING));

        if (enchantLevel > 0) {
            attacker.heal(target.getMaxHealth() * (0.03f + 0.02f * enchantLevel));
        }
    }

    public static void emitCritParticles(ServerLevel level, LivingEntity attacker, Entity target) {
        double theta = Math.atan((attacker.getX() - target.getX()) / (attacker.getZ() - target.getZ()));
        float radius = target.getBbWidth() * 0.75f + 0.25f;
        double xOffset = Math.sin(theta) * radius * (attacker.getZ() < target.getZ() ? -1 : 1);
        double zOffset = Math.cos(theta) * radius * (attacker.getZ() < target.getZ() ? -1 : 1);
        level.sendParticles(LEParticles.CRIT_SKULL.get(), target.getX() + xOffset, target.getY() + target.getBbHeight() * 0.5 + 0.45, target.getZ() + zOffset, 1, 0, 0, 0, 0);
        level.sendParticles(LEParticles.CRIT_RING.get(), target.getX(), target.getY() + target.getBbHeight() * 0.5 + 0.45, target.getZ(), 1, 0, 0, 0, 0);
        level.sendParticles(LEParticles.CRIT_SQUARE.get(), target.getX(), target.getY() + 0.5, target.getZ(), 12, 0, 0, 0, 0.5);
    }

    public static void attemptRadiance(LivingIncomingDamageEvent event, ItemStack stack, boolean isMelee) {
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        assert attacker != null;
        LivingEntity target = event.getEntity();

        ServerLevel level = ((ServerLevel) attacker.level());
        int enchantLevel = stack.getEnchantmentLevel(attacker.level().holderOrThrow(isMelee ? LEEnchants.RADIANCE : LEEnchants.RADIANCE_SHOT));

        if (event.getAmount() > 0 && enchantLevel > 0 && Math.random() < (isMelee ? 0.2 : 0.5)) {
            List<LivingEntity> nearbyEntities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat(), target, new AABB(target.getX() + 3.75, target.getY() + target.getBbHeight() * 0.5 + 2, target.getZ() + 3.75, target.getX() - 3.75, target.getY() + target.getBbHeight() * 0.5 - 2, target.getZ() - 3.75));
            nearbyEntities.forEach(m -> m.heal(1 + enchantLevel * 0.5f));

            level.sendParticles(LEParticles.RADIANCE_RING.get(), target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(), 1, 0, 0, 0, 0);
            level.sendParticles(LEParticles.RADIANCE_SQUARE.get(), target.getX(), target.getY() + 0.5, target.getZ(), 25, 0, 0, 0, 0.1);
            level.playSound(null, target.blockPosition(), LESounds.RADIANCE_0.get(), SoundSource.PLAYERS);
            level.playSound(null, target.blockPosition(), LESounds.RADIANCE_1.get(), SoundSource.PLAYERS);
        }
    }
}
