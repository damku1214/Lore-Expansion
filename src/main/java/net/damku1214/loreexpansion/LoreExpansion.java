package net.damku1214.loreexpansion;

import net.damku1214.loreexpansion.attachment.LEAttachments;
import net.damku1214.loreexpansion.effect.LEEffects;
import net.damku1214.loreexpansion.enchant.LEEnchantmentEffects;
import net.damku1214.loreexpansion.entity.LEEntities;
import net.damku1214.loreexpansion.network.SyncSoulsPacket;
import net.damku1214.loreexpansion.particle.LEParticles;
import net.damku1214.loreexpansion.sound.LESounds;
import net.damku1214.loreexpansion.util.LEAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(LoreExpansion.MOD_ID)
public class LoreExpansion {
    public static final String MOD_ID = "loreexpansion";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LoreExpansion(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        LEEnchantmentEffects.register(modEventBus);
        LEParticles.register(modEventBus);
        LESounds.register(modEventBus);
        LEEntities.register(modEventBus);
        LEEffects.register(modEventBus);
        LEAttachments.register(modEventBus);
        LEAttributes.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}
