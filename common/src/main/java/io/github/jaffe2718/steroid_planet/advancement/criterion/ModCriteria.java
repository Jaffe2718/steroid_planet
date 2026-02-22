package io.github.jaffe2718.steroid_planet.advancement.criterion;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public abstract class ModCriteria {


    public static final HealthConditionCriterion HEALTH_CONDITION = new HealthConditionCriterion();
    public static final SteroidRecordsCriterion STEROID_RECORDS = new SteroidRecordsCriterion();

    static <T extends Criterion<?>> void register(String id, T criterion) {
        Registry.register(Registries.CRITERION, SteroidPlanet.id(id), criterion);
    }

    public static void init() {
         register("health_condition", HEALTH_CONDITION);
         register("steroid_records", STEROID_RECORDS);
    }
}
