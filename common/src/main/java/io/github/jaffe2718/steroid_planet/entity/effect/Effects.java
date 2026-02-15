package io.github.jaffe2718.steroid_planet.entity.effect;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public interface Effects {
    RegistryEntry<StatusEffect> TECH_FITNESS = Registry.registerReference(Registries.STATUS_EFFECT, SteroidPlanet.id("tech_fitness"), new TechFitnessStatusEffect());
}
