package net.damku1214.loreexpansion.gui;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.attachment.LEAttachments;
import net.damku1214.loreexpansion.attachment.SoulData;
import net.damku1214.loreexpansion.util.LEAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = LoreExpansion.MOD_ID, value = Dist.CLIENT)
public class SoulHudRenderer {
    private static final ResourceLocation EMPTY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "textures/gui/soul_bar_empty.png");
    private static final ResourceLocation FULL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "textures/gui/soul_bar_full.png");

    // Full texture is 182x10 (matches XP bar dimensions), split: left=filled, right=empty

    @SubscribeEvent
    public static void renderSoulBar(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.player instanceof Player player)) return;

        SoulData data = player.getData(LEAttachments.SOUL_DATA.get());
        int souls    = data.getSouls();
        int maxSouls = (int) player.getAttributeValue(LEAttributes.MAX_SOULS);
        if (maxSouls <= 0) return;

        GuiGraphics gui   = event.getGuiGraphics();
        int screenWidth   = mc.getWindow().getGuiScaledWidth();
        int screenHeight  = mc.getWindow().getGuiScaledHeight();

        int barWidth  = 182;
        int barHeight = 5;
        int x = screenWidth  / 2 - barWidth / 2;
        // Anchor above health bar — health bar sits at screenHeight - 49
        // Soul bar goes 8px above it
        int y = screenHeight - 57;

        float fill = (float) souls / maxSouls;
        int filledPx = (int)(fill * barWidth);

        // Empty bar
        gui.blit(EMPTY_TEXTURE, x, y, 0, 0, barWidth, barHeight, 182, barHeight);
        // Filled portion
        if (filledPx > 0) {
            gui.blit(FULL_TEXTURE, x, y, 0, 0, filledPx, barHeight, 182, barHeight);
        }
    }
}