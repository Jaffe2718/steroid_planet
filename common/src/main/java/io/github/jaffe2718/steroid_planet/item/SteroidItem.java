package io.github.jaffe2718.steroid_planet.item;

import io.github.jaffe2718.steroid_planet.advancement.criterion.ModCriteria;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import io.github.jaffe2718.steroid_planet.entity.effect.ModEffects;
import net.minecraft.advancement.criterion.Criteria;
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

public class SteroidItem extends Item {
    private final UseAction useAction;
    private final float liverDamage;
    private final int duration;
    private final int amplifier;

    public SteroidItem(int duration, int amplifier, float liverDamage, UseAction useAction) {
        super(new Settings());
        this.duration = duration;
        this.amplifier = amplifier;
        this.useAction = useAction;
        this.liverDamage = liverDamage;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            ((PlayerAttributeAccessor) player).lossLiverHealth(liverDamage);
            ((PlayerAttributeAccessor) player).recordSteroid(this);
            if (player instanceof ServerPlayerEntity sPlayer) {
                Criteria.CONSUME_ITEM.trigger(sPlayer, stack);
                ModCriteria.HEALTH_CONDITION.trigger(sPlayer);
                ModCriteria.STEROID_RECORDS.trigger(sPlayer);
            }
            player.incrementStat(Stats.USED.getOrCreateStat(this));
            stack.decrementUnlessCreative(1, player);
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
        PotionContentsComponent.buildTooltip(List.of(new StatusEffectInstance(ModEffects.TECH_FITNESS, this.duration, this.amplifier)), tooltip::add, 1.0F, context.getUpdateTickRate());
        tooltip.add(Text.translatable("item.steroid_planet.steroid.liver_damage", liverDamage).withColor(0xFF5555));
    }

    /**
     * Apply the merge effect @{@link ModEffects#TECH_FITNESS} to the target.
     * append the duration to the existing effect if the target already has the effect.
     * Select the better amplifier between the existing effect and the new effect.
     * @param target the target entity to apply the effect to
     */
    private void applyMergeEffect(LivingEntity target) {
        StatusEffectInstance existing = target.getStatusEffect(ModEffects.TECH_FITNESS);
        if (existing == null) {
            target.addStatusEffect(new StatusEffectInstance(ModEffects.TECH_FITNESS, this.duration, this.amplifier));
        } else {
            int duration = (existing.isInfinite() || this.duration == -1) ? -1 : existing.getDuration() + this.duration;
            int amplifier = Math.max(existing.getAmplifier(), this.amplifier);
            target.addStatusEffect(new StatusEffectInstance(ModEffects.TECH_FITNESS, duration, amplifier));
        }
    }
}
