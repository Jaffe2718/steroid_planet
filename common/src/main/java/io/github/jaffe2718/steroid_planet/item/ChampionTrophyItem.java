package io.github.jaffe2718.steroid_planet.item;

import io.github.jaffe2718.steroid_planet.advancement.criterion.ModCriteria;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import io.github.jaffe2718.steroid_planet.entity.effect.ModEffects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ChampionTrophyItem extends Item {

    public ChampionTrophyItem() {
        super(new Settings().rarity(Rarity.UNCOMMON));
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
            ((PlayerAttributeAccessor) player).gainLiverHealth(20.0F);
            if (user instanceof ServerPlayerEntity sPlayer) {
                Criteria.CONSUME_ITEM.trigger(sPlayer, stack);
                ModCriteria.HEALTH_CONDITION.trigger(sPlayer);
            }
            player.incrementStat(Stats.USED.getOrCreateStat(this));
            stack.decrementUnlessCreative(1, player);
        }
        user.addStatusEffect(new StatusEffectInstance(ModEffects.BODYBUILDING_PREPARATION_PERIOD, 9600));
        return stack;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
