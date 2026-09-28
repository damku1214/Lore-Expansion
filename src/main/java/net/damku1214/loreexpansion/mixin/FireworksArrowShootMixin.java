package net.damku1214.loreexpansion.mixin;

import net.damku1214.loreexpansion.event.ArtifactUseEvent;
import net.damku1214.loreexpansion.item.LEItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ProjectileWeaponItem.class)
public class FireworksArrowShootMixin {
    @Unique
    private static final int COOLDOWN_TICKS = 30 * 20;

    @Inject(method = "shoot", at = @At("HEAD"))
    private void loreexpansion$onFireworksArrowShot(ServerLevel level, LivingEntity shooter, InteractionHand hand,
                                                    ItemStack weapon, List<ItemStack> projectiles, float velocity,
                                                    float inaccuracy, boolean isCrit, LivingEntity target, CallbackInfo ci) {
        if (!(shooter instanceof Player player)) {
            return;
        }

        for (ItemStack projectile : projectiles) {
            if (projectile.is(LEItems.FIREWORKS_ARROW.get())) {
                player.getCooldowns().addCooldown(projectile.getItem(), COOLDOWN_TICKS);
                NeoForge.EVENT_BUS.post(new ArtifactUseEvent(player, projectile));
                break;
            }
        }
    }
}
