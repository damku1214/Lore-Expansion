package net.damku1214.loreexpansion.entity.renderer;

import net.damku1214.loreexpansion.entity.custom.PetBeeEntity;
import net.damku1214.loreexpansion.entity.model.PetBeeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PetBeeRenderer extends MobRenderer<PetBeeEntity, PetBeeModel<PetBeeEntity>> {
    private static final ResourceLocation ANGRY_BEE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/bee/bee_angry.png");
    private static final ResourceLocation BEE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/bee/bee.png");

    public PetBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new PetBeeModel<>(context.bakeLayer(PetBeeModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PetBeeEntity entity) {
        return entity.isRolling() ? ANGRY_BEE_TEXTURE : BEE_TEXTURE;
    }
}
