package net.damku1214.loreexpansion.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class RadianceRingParticle extends TextureSheetParticle {
    protected RadianceRingParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z);

        this.lifetime = 12;
        this.quadSize = 0f;
        this.setSpriteFromAge(spriteSet);
        this.alpha = 1;

        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.hasPhysics = false;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        this.quadSize = (float) (-4 * Math.pow(0.8f, this.age + partialTicks) + 4);
        if (this.age + partialTicks >= 5) {
            this.alpha = 1 - (this.age + partialTicks - 5) / this.lifetime;
        }

        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotationX((float) -Math.PI / 2);
        this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
        quaternionf.rotationYXZ((float) -Math.PI, (float) Math.PI / 2, 0.0f);
        this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
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
            return new RadianceRingParticle(clientLevel, pX, pY, pZ, this.spriteSet);
        }
    }
}
