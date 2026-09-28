package net.damku1214.loreexpansion.mixin;

import net.damku1214.loreexpansion.item.LEItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class FireworksArrowAmmoMixin {
    @Inject(method = "getProjectile", at = @At("HEAD"), cancellable = true)
    private void loreexpansion$fireworksArrowAsAmmo(ItemStack weapon, CallbackInfoReturnable<ItemStack> cir) {
        if (!(weapon.getItem() instanceof ProjectileWeaponItem)) {
            return;
        }

        Player player = (Player) (Object) this;
        ItemStack offhand = player.getOffhandItem();

        if (offhand.is(LEItems.FIREWORKS_ARROW.get()) && !player.getCooldowns().isOnCooldown(offhand.getItem())) {
            cir.setReturnValue(offhand.copy());
        }
    }
}
