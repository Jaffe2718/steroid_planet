package io.github.jaffe2718.steroid_planet.entity.damage;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public interface DamageTypes {
    RegistryKey<DamageType> LIVER_POISONING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, SteroidPlanet.id("liver_poisoning"));
}
