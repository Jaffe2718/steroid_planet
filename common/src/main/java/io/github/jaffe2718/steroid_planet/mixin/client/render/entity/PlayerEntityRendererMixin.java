package io.github.jaffe2718.steroid_planet.mixin.client.render.entity;

import io.github.jaffe2718.steroid_planet.advancement.criterion.HealthConditionCriterion;
import io.github.jaffe2718.steroid_planet.client.render.entity.model.PlayerEntityModelAccessor;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @Inject(method = "setModelPose", at = @At("RETURN"))
    private void setModelPose(AbstractClientPlayerEntity player, CallbackInfo ci) {
        if (player.isSpectator()) return;
        PlayerEntityModel<AbstractClientPlayerEntity> model = ((PlayerEntityRenderer) (Object) this).getModel();
        PlayerEntityModelAccessor modelAccessor = (PlayerEntityModelAccessor) model;

        if (((PlayerAttributeAccessor) player).getMuscle() >= HealthConditionCriterion.MUSCLE_THRESHOLD) {
            modelAccessor.getPectoralMuscle().visible = true;
            modelAccessor.getChestplate().visible = true;
            modelAccessor.strengthenArms();
        } else {
            modelAccessor.getPectoralMuscle().visible = false;
            modelAccessor.getChestplate().visible = false;
            modelAccessor.resetArms();
        }
        modelAccessor.getPointyHead().visible = ((PlayerAttributeAccessor) player).getLiverHealth() < HealthConditionCriterion.LIVER_HEALTH_THRESHOLD;
        modelAccessor.getPointyHat().visible = ((PlayerAttributeAccessor) player).getMuscle() < HealthConditionCriterion.LIVER_HEALTH_THRESHOLD;
    }
}
