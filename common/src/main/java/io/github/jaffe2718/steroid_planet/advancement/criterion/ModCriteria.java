package io.github.jaffe2718.steroid_planet.advancement.criterion;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public abstract class ModCriteria {

    public static LiverHealthCriterion LIVER_HEALTH_CRITERION;
    public static MuscleCriterion MUSCLE_CRITERION;

    static <T extends Criterion<?>> T register(String id, T criterion) {
        return Registry.register(Registries.CRITERION, SteroidPlanet.id(id), criterion);
    }

    public static void init() {
         LIVER_HEALTH_CRITERION = register("liver_health", new LiverHealthCriterion());
         MUSCLE_CRITERION = register("muscle", new MuscleCriterion());
    }
}
