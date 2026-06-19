package net.damku1214.loreexpansion.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.damku1214.loreexpansion.particle.LEParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public record CommittedSquareParticleOptions(float length) implements ParticleOptions {
    public static final MapCodec<CommittedSquareParticleOptions> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.fieldOf("length").forGetter(o -> o.length)
    ).apply(inst, CommittedSquareParticleOptions::new));

    public static final StreamCodec<ByteBuf, CommittedSquareParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, o -> o.length,
            CommittedSquareParticleOptions::new
    );

    @Override
    public @NotNull ParticleType<CommittedSquareParticleOptions> getType() {
        return LEParticles.COMMITTED_SQUARE.get();
    }
}
