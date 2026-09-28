package net.damku1214.loreexpansion.datagen;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.item.LEItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class LEItemModelProvider extends ItemModelProvider {
    public LEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LoreExpansion.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //basicItem(LEItems.FIREWORKS_ARROW.get());
    }
}
