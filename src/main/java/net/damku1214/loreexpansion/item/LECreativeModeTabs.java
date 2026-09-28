package net.damku1214.loreexpansion.item;

import net.damku1214.loreexpansion.LoreExpansion;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LECreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LoreExpansion.MOD_ID);

    public static final Supplier<CreativeModeTab> ARTIFACTS_TAB = CREATIVE_MODE_TAB.register("artifacts_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(LEItems.FIREWORKS_ARROW.get()))
                    .title(Component.translatable("creativetab.loreexpansion.artifacts"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(LEItems.FIREWORKS_ARROW.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
