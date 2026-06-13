package net.damku1214.loreexpansion.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CritSkullParticle extends TextureSheetParticle {
    private Vec2 size;
    private final Vec2 startSize;
    private final Vec2 endSize;
    private final float shrinkTicks;
    private final float stayTicks;
    private final float fadeTicks;
    private final double startY;
    private final double yIncrease;

    protected CritSkullParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.startY = y;
        this.yIncrease = 0.1;

        this.startSize = new Vec2(8, 2);
        this.endSize = new Vec2(1, 1);
        this.size = this.startSize;
        this.shrinkTicks = 1.6f;
        this.stayTicks = 0f;
        this.fadeTicks = 13;

        this.lifetime = 20;
        this.quadSize = 0.3f;
        this.setSpriteFromAge(spriteSet);

        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.hasPhysics = false;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        float shrinkProgress = ((float) this.age + partialTicks) / this.shrinkTicks;
        float fadeProgress = ((float) this.age + partialTicks - (this.shrinkTicks + this.stayTicks)) / this.fadeTicks;

        if (shrinkProgress <= 1) {
            this.size = new Vec2(Mth.lerp(shrinkProgress, this.startSize.x, this.endSize.x), Mth.lerp(shrinkProgress, this.startSize.y, this.endSize.y));
        } else {
            this.size = this.endSize;
        }

        if (fadeProgress > 0 && fadeProgress <= 1) {
            this.y = Mth.lerp(fadeProgress, startY, startY + yIncrease);
            this.alpha = 1 - fadeProgress;
        }

        Quaternionf quaternionf = new Quaternionf();
        this.getFacingCameraMode().setRotation(quaternionf, renderInfo, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }

        this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
    }

    @Override
    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float f = this.getQuadSize(partialTicks);
        float f1 = this.getU0();
        float f2 = this.getU1();
        float f3 = this.getV0();
        float f4 = this.getV1();
        int i = this.getLightColor(partialTicks);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, f, f1, f4, i);
    }

    private void renderVertex(
            VertexConsumer buffer,
            Quaternionf quaternion,
            float x,
            float y,
            float z,
            float xOffset,
            float yOffset,
            float quadSize,
            float u,
            float v,
            int packedLight
    ) {
        Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.0F).mul(this.size.x, this.size.y, 1).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z())
                .setUv(u, v)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setLight(packedLight);
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
            return new CritSkullParticle(clientLevel, pX, pY, pZ, this.spriteSet);
        }
    }
}
