package io.github.jaffe2718.steroid_planet.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class MuscleCriterion extends AbstractCriterion<MuscleCriterion.Conditions> {

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        float playerMuscle = ((PlayerAttributeAccessor) player).getMuscle();
        this.trigger(player, (conditions) -> conditions.notBelow(playerMuscle));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Float> muscleThreshold) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                        Codec.FLOAT.optionalFieldOf("muscle").forGetter(Conditions::muscleThreshold)).apply(instance, Conditions::new)
        );

        /**
         * Check if the player's muscle is above the threshold.
         * The threshold defined in the JSON file with the key "muscle" is the minimum muscle value.
         * <br>
         * <code><pre>
         *     // data/{namespace}/advancements/{type}/{your_advancement}.json
         *     {
         *         "criteria": {
         *             "steroid_planet:muscle": {
         *                 // `player` is optional
         *                 "player": {
         *                     // ...
         *                 },
         *                 // `muscle` is supported as the threshold
         *                 "muscle": 100.0
         *             }
         *         },
         *         // other fields are ignored
         *      }
         * </pre></code>
         * <br>
         * @param muscle The player's muscle.
         * @see io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor#getMuscle()
         * @return True if the player's muscle is above the threshold, false otherwise.
         */
        public boolean notBelow(float muscle) {

            return this.muscleThreshold.map(threshold -> muscle >= threshold).orElse(true);
        }
    }
}
