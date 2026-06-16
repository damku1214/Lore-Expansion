package net.damku1214.loreexpansion.event;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.sound.LESounds;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = LoreExpansion.MOD_ID)
public class LEEffectEvents {
    @SubscribeEvent
    public static void onChainedExpired(MobEffectEvent.Expired event) {
        if (!event.getEntity().level().isClientSide) {
            event.getEntity().level().playSound(null, event.getEntity().blockPosition(), LESounds.CHAINS_BREAK.get(), SoundSource.PLAYERS);
        }
    }

    @SubscribeEvent
    public static void onChainedRemoved(MobEffectEvent.Remove event) {
        if (!event.getEntity().level().isClientSide) {
            event.getEntity().level().playSound(null, event.getEntity().blockPosition(), LESounds.CHAINS_BREAK.get(), SoundSource.PLAYERS);
        }
    }
}
