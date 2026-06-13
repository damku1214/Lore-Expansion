package net.damku1214.loreexpansion.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.entity.custom.MarkEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class MarkRenderer extends EntityRenderer<MarkEntity> {
    public MarkRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull MarkEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        Camera camera = this.entityRenderDispatcher.camera;

        float w = 0.5f;
        float h = 0.5f;

        poseStack.translate(0, h, 0);
        poseStack.mulPose(camera.rotation());

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
        Matrix4f matrix = poseStack.last().pose();

        int red, green, blue;
        float realAge = entity.getEntityData().get(MarkEntity.AGE) + partialTick;

        if (realAge < MarkEntity.BRIGHTEN_TIME) {
            red = (int)(MarkEntity.DARK_COLOR[0] + (MarkEntity.BRIGHT_COLOR[0] - MarkEntity.DARK_COLOR[0]) * (realAge / MarkEntity.BRIGHTEN_TIME));
            green = (int)(MarkEntity.DARK_COLOR[1] + (MarkEntity.BRIGHT_COLOR[1] - MarkEntity.DARK_COLOR[1]) * (realAge / MarkEntity.BRIGHTEN_TIME));
            blue = (int)(MarkEntity.DARK_COLOR[2] + (MarkEntity.BRIGHT_COLOR[2] - MarkEntity.DARK_COLOR[2]) * (realAge / MarkEntity.BRIGHTEN_TIME));
        } else {
            red = (int)(MarkEntity.BRIGHT_COLOR[0] + (MarkEntity.DARK_COLOR[0] - MarkEntity.BRIGHT_COLOR[0]) * (realAge - MarkEntity.BRIGHTEN_TIME) / MarkEntity.DARKEN_TIME);
            green = (int)(MarkEntity.BRIGHT_COLOR[1] + (MarkEntity.DARK_COLOR[1] - MarkEntity.BRIGHT_COLOR[1]) * (realAge - MarkEntity.BRIGHTEN_TIME) / MarkEntity.DARKEN_TIME);
            blue = (int)(MarkEntity.BRIGHT_COLOR[2] + (MarkEntity.DARK_COLOR[2] - MarkEntity.BRIGHT_COLOR[2]) * (realAge - MarkEntity.BRIGHTEN_TIME) / MarkEntity.DARKEN_TIME);
        }

        consumer.addVertex(matrix, -w, -h, 0).setColor(red, green, blue, 255)
                .setUv(0f, 1f).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
        consumer.addVertex(matrix, w, -h, 0).setColor(red, green, blue, 255)
                .setUv(1f, 1f).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
        consumer.addVertex(matrix, w, h, 0).setColor(red, green, blue, 255)
                .setUv(1f, 0f).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
        consumer.addVertex(matrix, -w, h, 0).setColor(red, green, blue, 255)
                .setUv(0f, 0f).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull Vec3 getRenderOffset(@NotNull MarkEntity entity, float partialTicks) {
        if (entity.getTarget() == null) return Vec3.ZERO;

        double ox = Mth.lerp(partialTicks, entity.getTarget().xOld, entity.getTarget().getX())
                - Mth.lerp(partialTicks, entity.xOld, entity.getX());
        double oy = Mth.lerp(partialTicks, entity.getTarget().yOld, entity.getTarget().getY())
                - Mth.lerp(partialTicks, entity.yOld, entity.getY());
        double oz = Mth.lerp(partialTicks, entity.getTarget().zOld, entity.getTarget().getZ())
                - Mth.lerp(partialTicks, entity.zOld, entity.getZ());

        return new Vec3(ox, oy + entity.getTarget().getBbHeight() + 0.25, oz);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MarkEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(LoreExpansion.MOD_ID, "textures/entity/mark.png");
    }
}
