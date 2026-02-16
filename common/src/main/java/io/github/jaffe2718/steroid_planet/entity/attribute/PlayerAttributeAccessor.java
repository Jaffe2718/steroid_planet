package io.github.jaffe2718.steroid_planet.entity.attribute;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import io.github.jaffe2718.steroid_planet.item.SteroidItem;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Set;

public interface PlayerAttributeAccessor {
    RegistryEntry<EntityAttribute> MAX_MUSCLE = register("player.max_muscle", (new ClampedEntityAttribute("attribute.name.player.muscle", 100.0F, 0.0F, 100.0F).setTracked(true)));
    RegistryEntry<EntityAttribute> MAX_LIVER_HEALTH = register("player.max_liver_health", (new ClampedEntityAttribute("attribute.name.player.liver_health", 100.0F, 0.0F, 100.0F).setTracked(true)));

    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, SteroidPlanet.id(id), attribute);
    }

    float getMuscle();

    void setMuscle(float muscle);

    float getLiverHealth();

    void setLiverHealth(float liverHealth);

    Set<Identifier> querySteroids();

    void recordSteroid(SteroidItem steroid);



    default void lossMuscle(float loss) {
        setMuscle(getMuscle() - loss);
    }

    default void lossLiverHealth(float loss) {
        setLiverHealth(getLiverHealth() - loss);
    }

    default void gainMuscle(float gain) {
        setMuscle(getMuscle() + gain);
    }

    default void gainLiverHealth(float gain) {
        setLiverHealth(getLiverHealth() + gain);
    }
}
