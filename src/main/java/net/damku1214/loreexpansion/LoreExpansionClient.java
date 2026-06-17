package net.damku1214.loreexpansion;

import net.damku1214.loreexpansion.entity.LEEntities;
import net.damku1214.loreexpansion.entity.renderer.ChainsRenderer;
import net.damku1214.loreexpansion.entity.renderer.MarkRenderer;
import net.damku1214.loreexpansion.entity.renderer.PetBeeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = LoreExpansion.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = LoreExpansion.MOD_ID, value = Dist.CLIENT)
public class LoreExpansionClient {
    public LoreExpansionClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(LEEntities.PET_BEE.get(), PetBeeRenderer::new);
        EntityRenderers.register(LEEntities.MARK.get(), MarkRenderer::new);
        EntityRenderers.register(LEEntities.CHAINS.get(), ChainsRenderer::new);
    }
}
