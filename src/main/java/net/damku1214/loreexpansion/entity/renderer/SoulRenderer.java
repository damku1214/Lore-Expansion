package net.damku1214.loreexpansion.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.entity.custom.SoulEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class SoulRenderer extends EntityRenderer<SoulEntity> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "textures/entity/soul.png");

    public SoulRenderer(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(SoulEntity entity, float yaw, float partialTick,
                       @NotNull PoseStack pose, @NotNull MultiBufferSource buffers, int light) {
        if (!entity.isVisible()) return;

        renderTrail(entity, partialTick, pose, buffers, light);

        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(180f - entityRenderDispatcher.camera.getYRot()));
        pose.mulPose(Axis.XP.rotationDegrees(-entityRenderDispatcher.camera.getXRot()));
        pose.scale(0.5f, 0.5f, 0.5f);

        VertexConsumer vc = buffers.getBuffer(RenderType.entityTranslucentCull(TEXTURE));
        Matrix4f m = pose.last().pose();

        float u0 = 0f, u1 = 1f, v0 = 0f, v1 = 1f;
        vc.addVertex(m, -0.5f,  0.5f, 0f).setColor(1f,1f,1f,1f).setUv(u0,v0).setLight(light);
        vc.addVertex(m, -0.5f, -0.5f, 0f).setColor(1f,1f,1f,1f).setUv(u0,v1).setLight(light);
        vc.addVertex(m,  0.5f, -0.5f, 0f).setColor(1f,1f,1f,1f).setUv(u1,v1).setLight(light);
        vc.addVertex(m,  0.5f,  0.5f, 0f).setColor(1f,1f,1f,1f).setUv(u1,v0).setLight(light);

        pose.popPose();
        super.render(entity, yaw, partialTick, pose, buffers, light);
    }

    private void renderTrail(SoulEntity entity, float partialTick,
                             PoseStack pose, MultiBufferSource buffers, int light) {
        if (!entity.trailReady) return;

        VertexConsumer vc = buffers.getBuffer(RenderType.entityTranslucentCull(TEXTURE));

        // Head position — interpolated with partialTick for smoothness
        double hx = Mth.lerp(partialTick, entity.xOld, entity.getX());
        double hy = Mth.lerp(partialTick, entity.yOld, entity.getY());
        double hz = Mth.lerp(partialTick, entity.zOld, entity.getZ());

        Vec3 cam = entityRenderDispatcher.camera.getPosition();

        for (int i = 0; i < SoulEntity.TRAIL_LENGTH - 1; i++) {
            // Segment: from point [i] to point [i+1], where 0 is newest
            double ax, ay, az, bx, by, bz;

            if (i == 0) {
                // Head segment: interpolated current pos → first history point
                ax = hx; ay = hy; az = hz;
                bx = entity.getTrailX(0);
                by = entity.getTrailY(0);
                bz = entity.getTrailZ(0);
            } else {
                ax = entity.getTrailX(i - 1);
                ay = entity.getTrailY(i - 1);
                az = entity.getTrailZ(i - 1);
                bx = entity.getTrailX(i);
                by = entity.getTrailY(i);
                bz = entity.getTrailZ(i);
            }

            // Fade and narrow toward tail
            float tA = (float) i       / SoulEntity.TRAIL_LENGTH;
            float tB = (float)(i + 1)  / SoulEntity.TRAIL_LENGTH;
            float alphaA = (1f - tA) * 200; // 0-200 range for byte alpha
            float alphaB = (1f - tB) * 200;
            float widthA = (1f - tA) * 0.08f;
            float widthB = (1f - tB) * 0.08f;

            // Segment direction
            float dx = (float)(bx - ax);
            float dy = (float)(by - ay);
            float dz = (float)(bz - az);
            float len = Mth.sqrt(dx*dx + dy*dy + dz*dz);
            if (len < 1e-4f) continue;

            // Perpendicular axis: segment direction × camera direction
            float cdx = (float)(ax - cam.x);
            float cdy = (float)(ay - cam.y);
            float cdz = (float)(az - cam.z);

            // Cross product: dir × toCamera gives a perpendicular in view space
            float px = dy * cdz - dz * cdy;
            float py = dz * cdx - dx * cdz;
            float pz = dx * cdy - dy * cdx;
            float pLen = Mth.sqrt(px*px + py*py + pz*pz);
            if (pLen < 1e-4f) continue;
            px /= pLen; py /= pLen; pz /= pLen;

            // Build quad relative to camera
            float a1x = (float)(ax - cam.x) + px * widthA;
            float a1y = (float)(ay - cam.y) + py * widthA;
            float a1z = (float)(az - cam.z) + pz * widthA;
            float a2x = (float)(ax - cam.x) - px * widthA;
            float a2y = (float)(ay - cam.y) - py * widthA;
            float a2z = (float)(az - cam.z) - pz * widthA;
            float b1x = (float)(bx - cam.x) + px * widthB;
            float b1y = (float)(by - cam.y) + py * widthB;
            float b1z = (float)(bz - cam.z) + pz * widthB;
            float b2x = (float)(bx - cam.x) - px * widthB;
            float b2y = (float)(by - cam.y) - py * widthB;
            float b2z = (float)(bz - cam.z) - pz * widthB;

            Matrix4f m = pose.last().pose();
            int aAlpha = (int) alphaA;
            int bAlpha = (int) alphaB;

            // Blue tint: r=80, g=140, b=255
            vc.addVertex(m, a1x, a1y, a1z).setColor(80, 140, 255, aAlpha).setUv(0f, 0f).setLight(light);
            vc.addVertex(m, a2x, a2y, a2z).setColor(80, 140, 255, aAlpha).setUv(1f, 0f).setLight(light);
            vc.addVertex(m, b2x, b2y, b2z).setColor(80, 140, 255, bAlpha).setUv(1f, 1f).setLight(light);
            vc.addVertex(m, b1x, b1y, b1z).setColor(80, 140, 255, bAlpha).setUv(0f, 1f).setLight(light);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SoulEntity entity) { return TEXTURE; }
}