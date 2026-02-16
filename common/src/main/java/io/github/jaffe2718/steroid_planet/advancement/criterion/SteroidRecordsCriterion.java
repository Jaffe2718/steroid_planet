package io.github.jaffe2718.steroid_planet.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class SteroidRecordsCriterion extends AbstractCriterion<SteroidRecordsCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        int steroidTypes = ((PlayerAttributeAccessor) player).querySteroids().size();
        this.trigger(player, (conditions) -> conditions.enough(steroidTypes));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Integer> steroidTypes) implements AbstractCriterion.Conditions {
        public static final Codec<SteroidRecordsCriterion.Conditions> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(SteroidRecordsCriterion.Conditions::player),
                        Codec.INT.optionalFieldOf("steroid_types").forGetter(SteroidRecordsCriterion.Conditions::steroidTypes)).apply(instance, SteroidRecordsCriterion.Conditions::new)
        );

        /**
         * Check if the player has enough steroid types.
         *
         * @param types the number of steroid types the player has
         * @return true if the player has enough steroid types, false otherwise
         */
        public boolean enough(int types) {
            return steroidTypes.map(requirement -> requirement <= types).orElse(true);
        }
    }
}
