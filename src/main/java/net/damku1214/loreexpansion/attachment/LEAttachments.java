package net.damku1214.loreexpansion.attachment;

import net.damku1214.loreexpansion.LoreExpansion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class LEAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, LoreExpansion.MOD_ID);

    public static final Supplier<AttachmentType<SoulData>> SOUL_DATA =
            ATTACHMENT_TYPES.register("soul_data", () -> AttachmentType
                    .serializable((IAttachmentHolder holder) -> new SoulData())
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
