package net.damku1214.loreexpansion.event;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.effect.LEEffects;
import net.damku1214.loreexpansion.particle.LEParticles;
import net.damku1214.loreexpansion.sound.LESounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = LoreExpansion.MOD_ID)
public class LEEffectEvents {
    @SubscribeEvent
    public static void sendChainedParticles(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity) || event.getEntity().level().isClientSide) return;
        ServerLevel level = (ServerLevel) livingEntity.level();

        if (livingEntity.hasEffect(LEEffects.CHAINED) && livingEntity.tickCount % 3 == 0) {
            level.sendParticles(LEParticles.CHAINS_RING.get(), livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() * 0.5, livingEntity.getZ(), 1, 0, 0, 0, 0);
        }
    }

    @SubscribeEvent
    public static void onChainedExpired(MobEffectEvent.Expired event) {
        assert event.getEffectInstance() != null;
        if (!event.getEntity().level().isClientSide && event.getEffectInstance().getEffect().equals(LEEffects.CHAINED)) {
            event.getEntity().level().playSound(null, event.getEntity().blockPosition(), LESounds.CHAINS_BREAK.get(), SoundSource.PLAYERS);
        }
    }

    @SubscribeEvent
    public static void onChainedRemoved(MobEffectEvent.Remove event) {
        assert event.getEffectInstance() != null;
        if (!event.getEntity().level().isClientSide && event.getEffectInstance().getEffect().equals(LEEffects.CHAINED)) {
            event.getEntity().level().playSound(null, event.getEntity().blockPosition(), LESounds.CHAINS_BREAK.get(), SoundSource.PLAYERS);
        }
    }
}
