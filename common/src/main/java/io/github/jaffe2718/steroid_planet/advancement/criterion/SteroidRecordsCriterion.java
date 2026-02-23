package io.github.jaffe2718.steroid_planet.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import io.github.jaffe2718.steroid_planet.item.SteroidItem;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.Item;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Optional;

public class SteroidRecordsCriterion extends AbstractCriterion<SteroidRecordsCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        List<Item> records = ((PlayerAttributeAccessor) player).querySteroids()
                .stream()
                .map(Registries.ITEM::get)
                .filter(item -> item instanceof SteroidItem)
                .toList();
        int steroidTypes = records.size();
        int steroidGrade = records.stream().map(item -> ((SteroidItem) item).amplifier + 1).reduce(0, Math::max);
        this.trigger(player, (conditions) -> conditions.enough(steroidTypes, steroidGrade));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Integer> steroidTypes, Optional<Integer> steroidGrade) implements AbstractCriterion.Conditions {
        public static final Codec<SteroidRecordsCriterion.Conditions> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(SteroidRecordsCriterion.Conditions::player),
                        Codec.INT.optionalFieldOf("steroid_types").forGetter(SteroidRecordsCriterion.Conditions::steroidTypes),
                        Codec.INT.optionalFieldOf("steroid_grade").forGetter(SteroidRecordsCriterion.Conditions::steroidGrade)
                ).apply(instance, SteroidRecordsCriterion.Conditions::new)
        );

        /**
         * Check if the player has enough steroid types and grade.
         * @param types the number of steroid types the player has
         * @param grade the maximum grade of the steroid types the player has
         * @return true if the player has enough steroid types, false otherwise
         */
        public boolean enough(int types, int grade) {
            return steroidTypes.map(requirement -> requirement <= types).orElse(true)
                     && steroidGrade.map(requirement -> requirement <= grade).orElse(true);
        }
    }
}
