package net.damku1214.loreexpansion.event;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.entity.model.PetBeeModel;
import net.damku1214.loreexpansion.particle.LEParticles;
import net.damku1214.loreexpansion.particle.custom.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = LoreExpansion.MOD_ID, value = Dist.CLIENT)
public class LEClientEvents {
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(LEParticles.PET_BEE_SMOKE.get(), PetBeeSmokeParticle.Provider::new);
        event.registerSpriteSet(LEParticles.PET_BEE_SQUARE.get(), PetBeeSquareParticle.Provider::new);
        event.registerSpriteSet(LEParticles.COMMITTED_SQUARE.get(), CommittedSquareParticle.Provider::new);
        event.registerSpriteSet(LEParticles.CHAINS_RING.get(), ChainsRingParticle.Provider::new);
        event.registerSpriteSet(LEParticles.CHAINS_SQUARE.get(), ChainsSquareParticle.Provider::new);
        event.registerSpriteSet(LEParticles.CRIT_SKULL.get(), CritSkullParticle.Provider::new);
        event.registerSpriteSet(LEParticles.CRIT_RING.get(), CritRingParticle.Provider::new);
        event.registerSpriteSet(LEParticles.CRIT_SQUARE.get(), CritSquareParticle.Provider::new);
        event.registerSpriteSet(LEParticles.RADIANCE_RING.get(), RadianceRingParticle.Provider::new);
        event.registerSpriteSet(LEParticles.RADIANCE_SQUARE.get(), RadianceSquareParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PetBeeModel.LAYER_LOCATION, PetBeeModel::createBodyLayer);
    }
}
