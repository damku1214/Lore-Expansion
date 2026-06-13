package net.damku1214.loreexpansion.event;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.entity.LEEntities;
import net.damku1214.loreexpansion.entity.custom.PetBeeEntity;
import net.damku1214.loreexpansion.entity.model.PetBeeModel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = LoreExpansion.MOD_ID)
public class LEEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PetBeeModel.LAYER_LOCATION, PetBeeModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(LEEntities.PET_BEE.get(), PetBeeEntity.createAttributes().build());
    }
}
