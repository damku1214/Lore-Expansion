package net.damku1214.loreexpansion.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class ChainsSquareParticle extends TextureSheetParticle {
    private final float initialRoll;

    protected ChainsSquareParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.lifetime = 13;
        this.quadSize = 0.1f;
        this.alpha = 1f;
        this.hasPhysics = false;
        this.initialRoll = (float)(Math.random() * Math.PI * 2);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        float t = (this.age + partialTicks) / this.lifetime;

        // Shrink in the last 4 ticks
        if (t >= (this.lifetime - 4f) / this.lifetime) {
            float shrinkProgress = (this.age + partialTicks - (this.lifetime - 4f)) / 4f;
            this.quadSize = 0.1f * (1f - shrinkProgress);
        }

        // Clockwise slow rotation — oRoll is the roll angle used by super.render()
        this.oRoll = this.roll;
        this.roll = initialRoll + (this.age + partialTicks) * 0.04f;

        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;
        public Provider(SpriteSet spriteSet) { this.spriteSet = spriteSet; }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level,
                                       double x, double y, double z, double dx, double dy, double dz) {
            return new ChainsSquareParticle(level, x, y, z, spriteSet);
        }
    }
}
