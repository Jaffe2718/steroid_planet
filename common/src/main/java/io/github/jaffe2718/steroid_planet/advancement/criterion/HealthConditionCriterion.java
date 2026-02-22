package io.github.jaffe2718.steroid_planet.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
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
        float playerLiverHealth = ((PlayerAttributeAccessor) player).getLiverHealth();
        float playerMuscle = ((PlayerAttributeAccessor) player).getMuscle();
        this.trigger(player, (conditions) -> conditions.matches(playerLiverHealth, playerMuscle));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Float> minLiverHealth, Optional<Float> maxMuscle) implements AbstractCriterion.Conditions {
        public static final Codec<HealthConditionCriterion.Conditions> CODEC = RecordCodecBuilder.create(
                (instance) -> instance
                        .group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(HealthConditionCriterion.Conditions::player),
                                Codec.FLOAT.optionalFieldOf("liver_health").forGetter(HealthConditionCriterion.Conditions::minLiverHealth),
                                Codec.FLOAT.optionalFieldOf("muscle").forGetter(HealthConditionCriterion.Conditions::maxMuscle)
                        )
                        .apply(instance, HealthConditionCriterion.Conditions::new)
        );
        
        public boolean matches(float liverHealth, float muscle) {
            return minLiverHealth.map(threshold -> threshold <= liverHealth).orElse(true) &&
                    this.maxMuscle.map(threshold -> muscle >= threshold).orElse(true);
        }
    }
}
