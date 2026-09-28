package net.damku1214.loreexpansion.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class ArtifactUseEvent extends Event implements ICancellableEvent {
    private final LivingEntity user;
    private final ItemStack artifact;

    public ArtifactUseEvent(LivingEntity user, ItemStack artifact) {
        this.user = user;
        this.artifact = artifact;
    }

    public LivingEntity getUser() {
        return user;
    }

    public ItemStack getArtifact() {
        return artifact;
    }
}
