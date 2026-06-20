package net.damku1214.loreexpansion.attachment;

import net.damku1214.loreexpansion.LoreExpansion;
import net.damku1214.loreexpansion.util.LEAttributes;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public class SoulData implements INBTSerializable<CompoundTag> {
    private int souls = 0;

    public int getSouls() { return this.souls; }

    public void setRawSouls(int souls) {
        this.souls = souls;
    }

    public void setSouls(int souls, Player player) {
        int max = (int) player.getAttributeValue(LEAttributes.MAX_SOULS);
        this.souls = Mth.clamp(souls, 0, max);
        LoreExpansion.LOGGER.info("Souls: {}", this.souls);
    }

    public void addSouls(int amount, Player player) {
        setSouls((int) (this.souls + (amount * player.getAttributeValue(LEAttributes.SOUL_GATHERING))), player);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("souls", souls);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag tag) {
        souls = tag.getInt("souls");
    }
}