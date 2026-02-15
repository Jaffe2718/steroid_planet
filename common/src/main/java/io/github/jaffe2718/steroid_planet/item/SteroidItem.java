package io.github.jaffe2718.steroid_planet.item;

import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import io.github.jaffe2718.steroid_planet.entity.effect.Effects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Objects;

public class SteroidItem extends Item {
    private final UseAction useAction;
    private final float liverDamage;

    public SteroidItem(int duration, int amplifier, float liverDamage, UseAction useAction) {
        super(new Settings().component(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT.with(new StatusEffectInstance(Effects.TECH_FITNESS, duration, amplifier))));
        this.useAction = useAction;
        this.liverDamage = liverDamage;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            if (player instanceof ServerPlayerEntity sPlayer) {
                Criteria.CONSUME_ITEM.trigger(sPlayer, stack);
            }
            player.incrementStat(Stats.USED.getOrCreateStat(this));
            stack.decrementUnlessCreative(1, player);
            ((PlayerAttributeAccessor) player).lossLiverHealth(liverDamage);
            this.applyMergeEffect(player);
            player.emitGameEvent(GameEvent.EAT);
        }
        return stack;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return this.useAction;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            potionContentsComponent.buildTooltip(tooltip::add, 1.0F, context.getUpdateTickRate());
        }
        tooltip.add(Text.translatable("item.steroid_planet.steroid.liver_damage", liverDamage).withColor(0xFF5555));
    }

    /**
     * Apply the merge effect @{@link Effects#TECH_FITNESS} to the target.
     * append the duration to the existing effect if the target already has the effect.
     * Select the better amplifier between the existing effect and the new effect.
     * @param target the target entity to apply the effect to
     */
    private void applyMergeEffect(LivingEntity target) {
        StatusEffectInstance existing = target.getStatusEffect(Effects.TECH_FITNESS);
        StatusEffectInstance add = Objects.requireNonNull(this.getComponents().get(DataComponentTypes.POTION_CONTENTS)).getEffects().iterator().next();
        if (existing == null) {
            target.addStatusEffect(add);
        } else {
            int duration = (existing.isInfinite() || add.isInfinite()) ? -1 : existing.getDuration() + add.getDuration();
            int amplifier = Math.max(existing.getAmplifier(), add.getAmplifier());
            target.addStatusEffect(new StatusEffectInstance(Effects.TECH_FITNESS, duration, amplifier));
        }
    }
}
