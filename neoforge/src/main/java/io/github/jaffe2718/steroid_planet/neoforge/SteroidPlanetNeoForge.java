package io.github.jaffe2718.steroid_planet.neoforge;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(SteroidPlanet.MOD_ID)
public final class SteroidPlanetNeoForge {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SteroidPlanet.MOD_ID);

    public SteroidPlanetNeoForge(IEventBus modEventBus) {
        // Run our common setup.
        ATTACHMENT_TYPES.register(modEventBus);
        SteroidPlanet.init();
    }
}
