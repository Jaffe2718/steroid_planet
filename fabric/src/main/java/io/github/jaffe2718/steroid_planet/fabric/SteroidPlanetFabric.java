package io.github.jaffe2718.steroid_planet.fabric;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.fabricmc.api.ModInitializer;


public final class SteroidPlanetFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SteroidPlanet.init();
    }
}
