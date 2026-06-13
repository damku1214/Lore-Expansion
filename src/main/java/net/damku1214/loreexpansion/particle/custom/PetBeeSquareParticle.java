package net.damku1214.loreexpansion.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PetBeeSquareParticle extends TextureSheetParticle {
    protected PetBeeSquareParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0, 0.0, 0.0);

        this.xd += xSpeed;
        this.xd *= 0.5f;
        this.yd += ySpeed;
        this.zd += zSpeed;
        this.zd *= 0.5f;

        this.lifetime = 13;
        this.quadSize = 0.1f;
        this.alpha = 1;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        if (this.age + partialTicks >= 8) this.alpha = 1 - Math.min(((float) this.age + partialTicks - 8) / (this.lifetime - 8), 1);
        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel,
                                       double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new PetBeeSquareParticle(clientLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.spriteSet);
        }
    }
}
