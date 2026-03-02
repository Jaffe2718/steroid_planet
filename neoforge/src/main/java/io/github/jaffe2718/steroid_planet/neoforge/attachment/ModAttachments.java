package io.github.jaffe2718.steroid_planet.neoforge.attachment;

import com.mojang.serialization.Codec;
import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public interface ModAttachments  {

    DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SteroidPlanet.MOD_ID);


    Supplier<AttachmentType<Float>> MUSCLE = ATTACHMENT_TYPES.register(
            "muscle", () -> AttachmentType.builder(() -> 0.0F)
                    .serialize(Codec.FLOAT.fieldOf("Muscle"))
                    .sync(new FloatSyncHandler())
                    .build()
    );

    Supplier<AttachmentType<Float>> LIVER_HEALTH = ATTACHMENT_TYPES.register(
            "liver_health", () -> AttachmentType.builder(() -> 100.0F)
                    .serialize(Codec.FLOAT.fieldOf("LiverHealth"))
                    .sync(new FloatSyncHandler())
                    .build()
    );

    Supplier<AttachmentType<Float>> BODY_FAT = ATTACHMENT_TYPES.register(
            "body_fat", () -> AttachmentType.builder(() -> 30.0F)
                    .serialize(Codec.FLOAT.fieldOf("BodyFat"))
                    .sync(new FloatSyncHandler())
                    .build()
    );

}
