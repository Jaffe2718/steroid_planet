package io.github.jaffe2718.steroid_planet.fabric;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;


public final class SteroidPlanetFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        // Run our common setup.
        SteroidPlanet.init();
    }
}
