package io.github.jaffe2718.steroid_planet.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

/**
 * The criterion for checking if the player's liver health is below a certain threshold.
 * <br>
 * <code>steroid_planet:liver_health</code>
 */
public class LiverHealthCriterion extends AbstractCriterion<LiverHealthCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        float playerLiverHealth = ((PlayerAttributeAccessor) player).getLiverHealth();
        this.trigger(player, (conditions) -> conditions.isBelow(playerLiverHealth));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Float> liverHealthThreshold) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                (instance) -> instance
                                .group(
                                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                                        Codec.FLOAT.optionalFieldOf("liver_health").forGetter(Conditions::liverHealthThreshold))
                                .apply(instance, LiverHealthCriterion.Conditions::new));

        /**
         * Check if the player's liver health is below the threshold.
         * The threshold defined in the advancement JSON file with the key <code>liver_health</code> is the maximum liver health value.
         * <br>
         * <code><pre>
         *     // data/{namespace}/advancements/{type}/{your_advancement}.json
         *     {
         *         "criteria": {
         *             "steroid_planet:liver_health": {
         *                 // `player` is optional
         *                 "player": {
         *                     // ...
         *                 },
         *                 // `liver_health` is supported as the threshold
         *                 "liver_health": 15.0
         *             }
         *         },
         *         // other fields are ignored
         *      }
         * </pre></code>
         * <br>
         * @param playerLiverHealth The player's liver health.
         * @see io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor#getLiverHealth()
         * @return True if the player's liver health is below the threshold, false otherwise.
         */
        public boolean isBelow(float playerLiverHealth) {
            return this.liverHealthThreshold.map(threshold -> playerLiverHealth < threshold).orElse(true);
        }
    }
}
