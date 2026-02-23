package io.github.jaffe2718.steroid_planet.entity.attribute;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public abstract class ModEntityAttributes {

    public static RegistryEntry<EntityAttribute> MAX_LIVER_HEALTH = register("player.max_liver_health", new ClampedEntityAttribute("attribute.name.steroid_planet.player.max_liver_health", 100.0F, 0.0F, 100.0F).setTracked(true));
    public static RegistryEntry<EntityAttribute> MUSCLE_AND_FAT_CAPACITY = register("player.muscle_and_fat_capacity", new ClampedEntityAttribute("attribute.name.steroid_planet.player.muscle_and_fat_capacity", 100.0F, 30.0F, 100.0F).setTracked(true));


    private static RegistryEntry<EntityAttribute> register(final String name, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, SteroidPlanet.id(name), attribute);
    }
}
