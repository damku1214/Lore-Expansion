package net.damku1214.loreexpansion.network;

import io.netty.buffer.ByteBuf;
import net.damku1214.loreexpansion.LoreExpansion;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record SyncSoulsPacket(int souls) implements CustomPacketPayload {
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "sync_souls");

    public static final StreamCodec<ByteBuf, SyncSoulsPacket> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.INT, SyncSoulsPacket::souls, SyncSoulsPacket::new);

    public static final Type<SyncSoulsPacket> TYPE = new Type<>(ID);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }
}
