package net.damku1214.loreexpansion.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.entity.custom.ChainsEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ChainsRenderer extends EntityRenderer<ChainsEntity> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "textures/entity/chains.png");

    public ChainsRenderer(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(ChainsEntity entity, float yaw, float partialTick,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        LivingEntity from = entity.getFrom();
        LivingEntity to   = entity.getTo();
        if (from == null || to == null) return;

        // Interpolated positions of both endpoints
        double fx = Mth.lerp(partialTick, from.xOld, from.getX());
        double fy = Mth.lerp(partialTick, from.yOld, from.getY()) + from.getBbHeight() * 0.5;
        double fz = Mth.lerp(partialTick, from.zOld, from.getZ());

        double tx = Mth.lerp(partialTick, to.xOld, to.getX());
        double ty = Mth.lerp(partialTick, to.yOld, to.getY()) + to.getBbHeight() * 0.5;
        double tz = Mth.lerp(partialTick, to.zOld, to.getZ());

        // Entity render origin is at 'from', so 'to' is a relative vector
        float dx = (float)(tx - fx);
        float dy = (float)(ty - fy);
        float dz = (float)(tz - fz);

        float beamLength = Mth.sqrt(dx * dx + dy * dy + dz * dz);
        if (beamLength < 0.01f) return;

        // Scroll UV over time for a moving chain effect
        //float uScroll = (entity.tickCount + partialTick) * 0.05f;
        float uScroll = 0;

        poseStack.pushPose();

        // Rotate posestack to align +X axis with the beam direction
        float yawAngle   = (float)(Math.atan2(dz, dx));
        float pitchAngle = (float)(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
        poseStack.mulPose(Axis.YP.rotation(-yawAngle));
        poseStack.mulPose(Axis.ZP.rotation(pitchAngle));

        float beamW = entity.getAnimatedWidth(partialTick, 0.5f); // half-width of the chain beam

        VertexConsumer vc = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE));
        Matrix4f m = poseStack.last().pose();

        // Draw two crossed quads (like guardian laser cross-section)
        //renderBeamQuad(m, vc, beamLength, beamW, uScroll, packedLight, 0);
        renderBeamQuad(m, vc, beamLength, beamW, uScroll, packedLight, (float)(Math.PI / 2));

        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void renderBeamQuad(Matrix4f m, VertexConsumer vc,
                                float length, float w, float uScroll,
                                int light, float rollAngle) {
        float sin = Mth.sin(rollAngle) * w;
        float cos = Mth.cos(rollAngle) * w;

        float uTiles = length * 0.5f; // how many texture tiles along the beam length

        vc.addVertex(m, 0,      sin,  cos).setColor(255,255,255,255)
                .setUv(uScroll,          0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0,1,0);
        vc.addVertex(m, 0,     -sin, -cos).setColor(255,255,255,255)
                .setUv(uScroll,          1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0,1,0);
        vc.addVertex(m, length,-sin, -cos).setColor(255,255,255,255)
                .setUv(uScroll + uTiles, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0,1,0);
        vc.addVertex(m, length, sin,  cos).setColor(255,255,255,255)
                .setUv(uScroll + uTiles, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0,1,0);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ChainsEntity entity) { return TEXTURE; }

    @Override
    public @NotNull Vec3 getRenderOffset(@NotNull ChainsEntity entity, float partialTick) { return Vec3.ZERO; }

    @Override
    public boolean shouldRender(ChainsEntity entity, @NotNull Frustum frustum, double x, double y, double z) {
        LivingEntity from = entity.getFrom();
        LivingEntity to = entity.getTo();
        if (from == null || to == null) return false;

        // Build an AABB that wraps both endpoints and check that against the frustum
        AABB box = from.getBoundingBox().minmax(to.getBoundingBox());
        return frustum.isVisible(box);
    }
}
