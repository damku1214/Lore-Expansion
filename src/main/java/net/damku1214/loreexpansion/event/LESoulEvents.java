package net.damku1214.loreexpansion.event;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.attachment.LEAttachments;
import net.damku1214.loreexpansion.attachment.SoulData;
import net.damku1214.loreexpansion.entity.custom.SoulEntity;
import net.damku1214.loreexpansion.network.SyncSoulsPacket;
import net.damku1214.loreexpansion.util.LEAttributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = LoreExpansion.MOD_ID)
public class LESoulEvents {
    @SubscribeEvent
    public static void onMobKill(LivingDeathEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player player) || player.level().isClientSide) return;

        double gathering = player.getAttributeValue(LEAttributes.SOUL_GATHERING);
        if (gathering <= 0) return;

        LivingEntity dead = event.getEntity();
        ServerLevel level = (ServerLevel) player.level();

        // Slight x/z deviation only, as specified
        double ox = (Math.random() - 0.5) * 0.8;
        double oz = (Math.random() - 0.5) * 0.8;

        SoulEntity soul = new SoulEntity(player, dead.getX() + ox, dead.getY(), dead.getZ() + oz);
        level.addFreshEntity(soul);

        SoulData data = player.getData(LEAttachments.SOUL_DATA.get());
        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncSoulsPacket(data.getSouls()));
    }
}
