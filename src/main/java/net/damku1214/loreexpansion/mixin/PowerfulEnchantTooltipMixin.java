package net.damku1214.loreexpansion.mixin;

import net.damku1214.loreexpansion.util.LETags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class PowerfulEnchantTooltipMixin {
    @Inject(method = "getFullname", at = @At("RETURN"), cancellable = true)
    private static void boldPowerfulEnchants(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<Component> cir) {
        if (enchantment.is(LETags.Enchants.POWERFUL_ENCHANTS)) {
            Component original = cir.getReturnValue();
            cir.setReturnValue(
                    original.copy().withStyle(ChatFormatting.BOLD)
            );
        }
    }
}
