package io.github.jaffe2718.steroid_planet.entity.effect;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public abstract class ModEffects {

    public static RegistryEntry<StatusEffect> TECH_FITNESS;
    public static RegistryEntry<StatusEffect> CONTEST_PREP;

    /**
     * Use vanilla registry to register a status effect.
     * @see io.github.jaffe2718.steroid_planet.mixin.entity.effect.StatusEffectsMixin
     * @param id          The id of the status effect.
     * @param statusEffect The status effect to register.
     * @return The registered status effect.
     */
    public static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, SteroidPlanet.id(id), statusEffect);
    }
}
