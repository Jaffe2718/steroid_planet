package io.github.jaffe2718.steroid_planet.mixin.client.render.entity.feature;

import io.github.jaffe2718.steroid_planet.advancement.criterion.HealthConditionCriterion;
import io.github.jaffe2718.steroid_planet.client.render.entity.model.BaseEntityModelAccessor;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, A extends BipedEntityModel<T>> {

    @Inject(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;setVisible(Lnet/minecraft/client/render/entity/model/BipedEntityModel;Lnet/minecraft/entity/EquipmentSlot;)V", shift = At.Shift.AFTER))
    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
        if (model instanceof ArmorEntityModel<?>) {
            BaseEntityModelAccessor armorModel = (BaseEntityModelAccessor) model;
            this.steroid_planet$setVisible(entity, armorModel, armorSlot);
            if (entity instanceof PlayerEntity
                    && ((PlayerAttributeAccessor) entity).getMuscle() >= HealthConditionCriterion.MUSCLE_THRESHOLD) {
                armorModel.strengthenArms();
            } else {
                armorModel.resetArms();
            }
        }
    }

    @Unique
    private void steroid_planet$setVisible(T entity, BaseEntityModelAccessor model, EquipmentSlot armorSlot) {
        switch (armorSlot) {
            case HEAD -> model.getPointyHead().visible = entity instanceof PlayerEntity player
                    && ((PlayerAttributeAccessor) player).getLiverHealth() < HealthConditionCriterion.LIVER_HEALTH_THRESHOLD;
            case CHEST -> model.getPectoralMuscle().visible = entity instanceof PlayerEntity player
                    && ((PlayerAttributeAccessor) player).getMuscle() >= HealthConditionCriterion.MUSCLE_THRESHOLD;
        }
    }

}