package io.github.jaffe2718.steroid_planet.neoforge;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.neoforged.fml.common.Mod;

@Mod(SteroidPlanet.MOD_ID)
public final class SteroidPlanetNeoForge {
    public SteroidPlanetNeoForge() {
        // Run our common setup.
        SteroidPlanet.init();
    }
}
