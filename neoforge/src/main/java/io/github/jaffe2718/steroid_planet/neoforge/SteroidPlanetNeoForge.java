package io.github.jaffe2718.steroid_planet.neoforge;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import io.github.jaffe2718.steroid_planet.neoforge.attachment.ModAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SteroidPlanet.MOD_ID)
public final class SteroidPlanetNeoForge {


    public SteroidPlanetNeoForge(IEventBus modEventBus) {
        // Run our common setup.
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        SteroidPlanet.init();
    }
}
