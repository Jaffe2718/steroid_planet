package io.github.jaffe2718.steroid_planet.mixin.server.network;

import io.github.jaffe2718.steroid_planet.advancement.criterion.ModCriteria;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import io.github.jaffe2718.steroid_planet.entity.effect.ModEffects;
import io.github.jaffe2718.steroid_planet.registry.tag.ModItemTags;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    /**
     * When the player eats food, if the food has the liver healing tag, the player will recover liver health.
     * @see ModItemTags
     */
    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "consumeItem", at = @At("HEAD"))
    private void consumeItem(CallbackInfo ci) {
        ServerPlayerEntity thiz = (ServerPlayerEntity) (Object) this;
        PlayerAttributeAccessor thisAccessor = (PlayerAttributeAccessor) thiz;
        ItemStack activeStack = thiz.getActiveItem();
        if (thiz.isUsingItem()) {
            if (activeStack.isIn(ModItemTags.PROTEIN)) {
                if (((PlayerEntity) (Object) this).getStatusEffect(ModEffects.TECH_FITNESS) instanceof StatusEffectInstance techFitness) {
                    thisAccessor.gainMuscle(8.0F + (techFitness.getAmplifier() + 1.0F) * 3.0F);
                } else {
                    thisAccessor.gainMuscle(8.0F);
                }
                if (((PlayerEntity) (Object) this) instanceof ServerPlayerEntity serverPlayer) {
                    ModCriteria.HEALTH_CONDITION.trigger(serverPlayer);
                }
            }
            if (activeStack.isIn(ModItemTags.LIVER_HEALING_I)) {
                thisAccessor.gainLiverHealth(0.75F);
            } else if (activeStack.isIn(ModItemTags.LIVER_HEALING_II)) {
                thisAccessor.gainLiverHealth(8.0F);
            } else if (activeStack.isIn(ModItemTags.LIVER_HEALING_III)) {
                thisAccessor.gainLiverHealth(100.0F);
            }
            if (activeStack.isIn(ModItemTags.FAT_I)) {
                thisAccessor.gainBodyFat(2.0F);
            } else if (activeStack.isIn(ModItemTags.FAT_II)) {
                thisAccessor.gainBodyFat(5.0F);
            } else if (activeStack.isIn(ModItemTags.FAT_III)) {
                thisAccessor.gainBodyFat(9.0F);
            }
        }
    }
}
