package io.github.jaffe2718.steroid_planet.entity.damage;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public interface ModDamageTypes {
    RegistryKey<DamageType> LIVER_POISONING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, SteroidPlanet.id("liver_poisoning"));

    static void bootstrap(Registerable<DamageType> damageTypeRegisterable) {
        damageTypeRegisterable.register(LIVER_POISONING, new DamageType("liver_poisoning", 0.0F));
    }
}
