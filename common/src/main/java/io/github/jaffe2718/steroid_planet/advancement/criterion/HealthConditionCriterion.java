package io.github.jaffe2718.steroid_planet.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jaffe2718.steroid_planet.entity.player.PlayerEntityExt;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class HealthConditionCriterion extends AbstractCriterion<HealthConditionCriterion.Conditions> {

    public static final float LIVER_HEALTH_THRESHOLD = 15.0F;
    public static final float MUSCLE_THRESHOLD = 90.0F;

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        float playerLiverHealth = ((PlayerEntityExt) player).getLiverHealth();
        float playerMuscle = ((PlayerEntityExt) player).getMuscle();
        float playerBodyFat = ((PlayerEntityExt) player).getBodyFat();
        boolean useSteroid = !((PlayerEntityExt) player).querySteroids().isEmpty();
        this.trigger(player, (conditions) -> conditions.matches(playerLiverHealth, playerMuscle, playerBodyFat, useSteroid));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Float> minLiverHealth, Optional<Float> maxMuscle, Optional<Float> maxBodyFat, Optional<Boolean> useSteroid) implements AbstractCriterion.Conditions {
        public static final Codec<HealthConditionCriterion.Conditions> CODEC = RecordCodecBuilder.create(
                (instance) -> instance
                        .group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(HealthConditionCriterion.Conditions::player),
                                Codec.FLOAT.optionalFieldOf("liver_health").forGetter(HealthConditionCriterion.Conditions::minLiverHealth),
                                Codec.FLOAT.optionalFieldOf("muscle").forGetter(HealthConditionCriterion.Conditions::maxMuscle),
                                Codec.FLOAT.optionalFieldOf("body_fat").forGetter(HealthConditionCriterion.Conditions::maxBodyFat),
                                Codec.BOOL.optionalFieldOf("use_steroid").forGetter(HealthConditionCriterion.Conditions::useSteroid)
                        )
                        .apply(instance, HealthConditionCriterion.Conditions::new)
        );

        public boolean matches(float liverHealth, float muscle, float bodyFat, boolean useSteroid) {
            return minLiverHealth.map(threshold -> liverHealth < threshold).orElse(true)
                    && this.maxMuscle.map(threshold -> muscle >= threshold).orElse(true)
                    && this.maxBodyFat.map(threshold -> bodyFat < threshold).orElse(true)
                    && this.useSteroid.map(use -> use == useSteroid).orElse(true);
        }
    }
}
