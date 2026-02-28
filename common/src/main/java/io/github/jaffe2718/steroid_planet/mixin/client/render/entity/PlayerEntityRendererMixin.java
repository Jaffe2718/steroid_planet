package io.github.jaffe2718.steroid_planet.mixin.client.render.entity;

import io.github.jaffe2718.steroid_planet.advancement.criterion.HealthConditionCriterion;
import io.github.jaffe2718.steroid_planet.client.render.entity.state.PlayerEntityRenderStateExt;
import io.github.jaffe2718.steroid_planet.entity.player.PlayerEntityExt;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @SuppressWarnings("AmbiguousMixinReference")
    @Inject(method = "updateRenderState", at = @At("RETURN"))
    private void updateRenderState(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        PlayerEntityExt player = (PlayerEntityExt) abstractClientPlayerEntity;
        PlayerEntityRenderStateExt stateExt = (PlayerEntityRenderStateExt) playerEntityRenderState;
        stateExt.strong(player.getMuscle() >= HealthConditionCriterion.MUSCLE_THRESHOLD);
        stateExt.pointyHead(player.getLiverHealth() < HealthConditionCriterion.LIVER_HEALTH_THRESHOLD);
    }
}
