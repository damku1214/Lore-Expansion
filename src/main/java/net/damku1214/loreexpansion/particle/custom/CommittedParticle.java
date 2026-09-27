package net.damku1214.loreexpansion.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class CommittedParticle extends CritParticle {
    protected CommittedParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(
                @NotNull SimpleParticleType type,
                @NotNull ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            CommittedParticle committedParticle = new CommittedParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            committedParticle.rCol = 1F;
            committedParticle.gCol = 0.67F;
            committedParticle.bCol = 0.15F;

            float f = (float)(Math.random() * 0.2F + 0.8F);
            committedParticle.rCol *= f;
            committedParticle.gCol *= f;
            committedParticle.bCol *= f;

            committedParticle.pickSprite(this.sprite);
            return committedParticle;
        }
    }
}
