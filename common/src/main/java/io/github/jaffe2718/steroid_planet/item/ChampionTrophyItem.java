package io.github.jaffe2718.steroid_planet.item;

import io.github.jaffe2718.steroid_planet.advancement.criterion.ModCriteria;
import io.github.jaffe2718.steroid_planet.entity.player.PlayerEntityExt;
import io.github.jaffe2718.steroid_planet.entity.effect.ModEffects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

public class ChampionTrophyItem extends Item {

    private static final float LIVER_HEALING = 20.0F;

    public ChampionTrophyItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            ((PlayerEntityExt) player).gainLiverHealth(LIVER_HEALING);
            if (user instanceof ServerPlayerEntity sPlayer) {
                Criteria.CONSUME_ITEM.trigger(sPlayer, stack);
                ModCriteria.HEALTH_CONDITION.trigger(sPlayer);
            }
            player.incrementStat(Stats.USED.getOrCreateStat(this));
            stack.decrementUnlessCreative(1, player);
        }
        user.addStatusEffect(new StatusEffectInstance(ModEffects.CONTEST_PREP, 9600, 0, false, false, true));
        return stack;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        PotionContentsComponent.buildTooltip(List.of(new StatusEffectInstance(ModEffects.CONTEST_PREP, 9600, 0, false, false, true)), textConsumer, 1.0F, context.getUpdateTickRate());
        textConsumer.accept(Text.translatable("tooltip.item.steroid_planet.steroid.liver_healing", LIVER_HEALING).withColor(0x55FF55));
    }
}
