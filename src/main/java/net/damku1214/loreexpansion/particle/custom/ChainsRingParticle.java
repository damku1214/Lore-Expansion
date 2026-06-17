package net.damku1214.loreexpansion.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class ChainsRingParticle extends TextureSheetParticle {
    protected ChainsRingParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.lifetime = 6;
        this.quadSize = 0.2f;
        this.setSpriteFromAge(spriteSet);
        this.alpha = 1;
        this.hasPhysics = false;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        float progress = (this.age + partialTicks) / this.lifetime;
        this.quadSize = 0.2f + progress * 0.5f;
        if (this.age + partialTicks >= 3) {
            this.alpha = 1 - (this.age + partialTicks - 3) / this.lifetime;
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
        public Provider(SpriteSet spriteSet) { this.spriteSet = spriteSet; }
        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ChainsRingParticle(level, x, y, z, this.spriteSet);
        }
    }
}
