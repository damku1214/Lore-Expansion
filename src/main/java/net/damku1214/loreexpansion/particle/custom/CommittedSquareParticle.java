package net.damku1214.loreexpansion.particle.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CommittedSquareParticle extends TextureSheetParticle {
    private final float length;
    private final float width;

    protected CommittedSquareParticle(ClientLevel level, double x, double y, double z,
                                      double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime = 4;
        this.alpha = 1f;
        this.hasPhysics = false;
        this.gravity = 0f;
        this.setSpriteFromAge(spriteSet);

        float baseLength = (float) Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed + zSpeed * zSpeed);
        float deviation = (float)(Math.random() * 0.1 - 0.05);
        this.length = Math.max(0.05f, baseLength + deviation);
        this.width = 0.15f * this.length;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera camera, float partialTicks) {
        float t = (this.age + partialTicks) / this.lifetime;

        float currentLength = this.length * (1f - t);
        if (currentLength < 1e-4f) return;

        // Interpolated world position
        float px = (float) Mth.lerp(partialTicks, this.xo, this.x);
        float py = (float) Mth.lerp(partialTicks, this.yo, this.y);
        float pz = (float) Mth.lerp(partialTicks, this.zo, this.z);

        // Long axis — velocity direction
        Vector3f dir = new Vector3f((float)xd, (float)yd, (float)zd);
        if (dir.lengthSquared() < 1e-6f) dir.set(1, 0, 0);
        dir.normalize();

        // Short axis — velocity cross worldUp
        Vector3f shortAxis = new Vector3f(dir).cross(new Vector3f(0, 1, 0));
        if (shortAxis.lengthSquared() < 1e-6f) shortAxis.set(1, 0, 0);
        shortAxis.normalize().mul(this.width);

        Vector3f longAxis = new Vector3f(dir).mul(currentLength);

        // Camera position
        Vec3 cam = camera.getPosition();
        float camX = (float)cam.x;
        float camY = (float)cam.y;
        float camZ = (float)cam.z;

        // Corners in world space
        Vector3f v0 = new Vector3f(px, py, pz).add(new Vector3f(longAxis).add(shortAxis));
        Vector3f v1 = new Vector3f(px, py, pz).add(new Vector3f(longAxis).sub(shortAxis));
        Vector3f v2 = new Vector3f(px, py, pz).add(new Vector3f(longAxis).negate().sub(shortAxis));
        Vector3f v3 = new Vector3f(px, py, pz).add(new Vector3f(longAxis).negate().add(shortAxis));

        // Submit to buffer (transformed to view space by subtracting camera position)
        float u0 = this.getU0(), u1 = this.getU1();
        float v0t = this.getV0(), v1t = this.getV1();
        int light = this.getLightColor(partialTicks);

        buffer.addVertex(v0.x - camX, v0.y - camY, v0.z - camZ).setColor(1f, 1f, 1f, alpha).setUv(u1, v0t).setLight(light);
        buffer.addVertex(v1.x - camX, v1.y - camY, v1.z - camZ).setColor(1f, 1f, 1f, alpha).setUv(u1, v1t).setLight(light);
        buffer.addVertex(v2.x - camX, v2.y - camY, v2.z - camZ).setColor(1f, 1f, 1f, alpha).setUv(u0, v1t).setLight(light);
        buffer.addVertex(v3.x - camX, v3.y - camY, v3.z - camZ).setColor(1f, 1f, 1f, alpha).setUv(u0, v0t).setLight(light);
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
            return new CommittedSquareParticle(level, x, y, z, dx, dy, dz, spriteSet);
        }
    }
}