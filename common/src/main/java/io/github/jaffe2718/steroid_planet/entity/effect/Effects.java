package io.github.jaffe2718.steroid_planet.entity.effect;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public interface Effects {
    RegistryEntry<StatusEffect> TECH_FITNESS = Registry.registerReference(Registries.STATUS_EFFECT, SteroidPlanet.id("tech_fitness"), new TechFitnessStatusEffect(StatusEffectCategory.NEUTRAL, 0xFFD5B4));
    RegistryEntry<StatusEffect> BODYBUILDING_PREPARATION_PERIOD = Registry.registerReference(Registries.STATUS_EFFECT, SteroidPlanet.id("bodybuilding_preparation_period"), new StatusEffect(StatusEffectCategory.BENEFICIAL, 0xFF5300) {});

}
