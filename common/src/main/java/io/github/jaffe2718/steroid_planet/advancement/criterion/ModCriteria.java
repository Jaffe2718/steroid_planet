package io.github.jaffe2718.steroid_planet.advancement.criterion;

import dev.architectury.registry.registries.Registrar;
import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.RegistryKeys;

public abstract class ModCriteria {

    private static final Registrar<Criterion<?>> CRITERION_REGISTRAR = SteroidPlanet.REGISTRAR_MANAGER.get().get(RegistryKeys.CRITERION);

    public static final HealthConditionCriterion HEALTH_CONDITION = new HealthConditionCriterion();
    public static final SteroidRecordsCriterion STEROID_RECORDS = new SteroidRecordsCriterion();

    static <T extends Criterion<?>> void register(String id, T criterion) {
        CRITERION_REGISTRAR.register(SteroidPlanet.id(id), () -> criterion);
    }

    public static void init() {
         register("health_condition", HEALTH_CONDITION);
         register("steroid_records", STEROID_RECORDS);
    }
}
