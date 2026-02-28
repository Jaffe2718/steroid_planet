package io.github.jaffe2718.steroid_planet.mixin.client.render.entity.feature;

import io.github.jaffe2718.steroid_planet.client.render.entity.model.BipedEntityModelExt;
import io.github.jaffe2718.steroid_planet.client.render.entity.state.PlayerEntityRenderStateExt;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<S extends BipedEntityRenderState, A extends BipedEntityModel<S>> {

    @Shadow
    protected abstract A getModel(S state, EquipmentSlot slot);

    @SuppressWarnings("AmbiguousMixinReference")
    @Inject(method = "render", at = @At("HEAD"))
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, S bipedEntityRenderState, float f, float g, CallbackInfo ci) {
        if (bipedEntityRenderState instanceof PlayerEntityRenderState playerState) {
            PlayerEntityRenderStateExt stateExt = (PlayerEntityRenderStateExt) playerState;
            if (this.getModel(bipedEntityRenderState, EquipmentSlot.HEAD) instanceof ArmorEntityModel<?> armorModel) {
                BipedEntityModelExt helmetModel = (BipedEntityModelExt) armorModel;
                EquippableComponent equippableComponent = bipedEntityRenderState.equippedHeadStack.get(DataComponentTypes.EQUIPPABLE);
                helmetModel.getSteroid_planet$pointyHead().visible = equippableComponent != null
                        && steroid_planet$hasModel(equippableComponent, EquipmentSlot.HEAD) && stateExt.pointyHead();
            }
            if (this.getModel(bipedEntityRenderState, EquipmentSlot.CHEST) instanceof ArmorEntityModel<?> armorModel) {
                BipedEntityModelExt chestplateModel = (BipedEntityModelExt) armorModel;
                EquippableComponent equippableComponent = bipedEntityRenderState.equippedChestStack.get(DataComponentTypes.EQUIPPABLE);
                chestplateModel.getSteroid_planet$pectoralMuscle().visible = equippableComponent != null
                        && steroid_planet$hasModel(equippableComponent, EquipmentSlot.CHEST) && stateExt.strong();
                if (chestplateModel.getSteroid_planet$pectoralMuscle().visible) {
                    chestplateModel.strengthenArms();
                } else {
                    chestplateModel.resetArms();
                }
            }
        }
    }

    @Unique
    private static boolean steroid_planet$hasModel(EquippableComponent component, EquipmentSlot slot) {
        return component.assetId().isPresent() && component.slot() == slot;
    }

}