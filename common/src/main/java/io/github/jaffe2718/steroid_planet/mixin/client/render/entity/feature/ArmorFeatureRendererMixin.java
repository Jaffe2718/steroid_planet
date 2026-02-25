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
import net.minecraft.item.Equipment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, A extends BipedEntityModel<T>> {

    @Shadow
    protected abstract A getModel(EquipmentSlot slot);

    @SuppressWarnings("AmbiguousMixinReference")
    @Inject(method = "render", at = @At("HEAD"))
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (this.getModel(EquipmentSlot.CHEST) instanceof ArmorEntityModel<?>
                && livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof Equipment equipment
                && equipment.getSlotType() == EquipmentSlot.CHEST) {
            BaseEntityModelAccessor chestplateModel = (BaseEntityModelAccessor) this.getModel(EquipmentSlot.CHEST);
            chestplateModel.getPectoralMuscle().visible = livingEntity instanceof PlayerEntity player
                    && ((PlayerAttributeAccessor) player).getMuscle() >= HealthConditionCriterion.MUSCLE_THRESHOLD;
        }
        if (this.getModel(EquipmentSlot.HEAD) instanceof ArmorEntityModel<?>
                && livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof Equipment equipment
                && equipment.getSlotType() == EquipmentSlot.HEAD) {
            BaseEntityModelAccessor helmetModel = (BaseEntityModelAccessor) this.getModel(EquipmentSlot.HEAD);
            helmetModel.getPointyHead().visible = livingEntity instanceof PlayerEntity player
                    && ((PlayerAttributeAccessor) player).getLiverHealth() < HealthConditionCriterion.LIVER_HEALTH_THRESHOLD;
        }
    }

}