package io.github.jaffe2718.steroid_planet.mixin.client.render.entity.model;

import io.github.jaffe2718.steroid_planet.client.render.entity.model.BipedEntityModelExt;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ArmorEntityModel.class)
public abstract class ArmorEntityModelMixin implements BipedEntityModelExt {

    @Unique
    private ModelPart steroid_planet$pectoralMuscle;

    @Unique
    private ModelPart steroid_planet$pointyHead;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(ModelPart modelPart, CallbackInfo ci) {
        this.steroid_planet$pectoralMuscle = modelPart.getChild("body").getChild("pectoral_muscle");
        this.steroid_planet$pointyHead = modelPart.getChild("head").getChild("pointy_head");
        this.steroid_planet$pectoralMuscle.visible = false;
        this.steroid_planet$pointyHead.visible = false;
    }


    @Inject(method = "getModelData", at = @At("RETURN"))
    private static void getModelData(Dilation dilation, CallbackInfoReturnable<ModelData> cir) {
        ModelData modelData = cir.getReturnValue();
        modelData.getRoot().getChild("body").addChild("pectoral_muscle", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -0.5F, -2.0F, 8.0F, 7.0F, 4.0F, dilation.add(2.0F, 0.0F, 1.5F)), ModelTransform.NONE);
        modelData.getRoot().getChild("head").addChild("pointy_head", ModelPartBuilder.create().uv(8, 2).cuboid(-1.0F, -9.5F, -1.0F, 2.0F, 2.0F, 2.0F, dilation), ModelTransform.NONE);
    }

    @Unique
    @Override
    public ModelPart getSteroid_planet$pectoralMuscle() {
        return this.steroid_planet$pectoralMuscle;
    }

    @Unique
    @Override
    public ModelPart getSteroid_planet$pointyHead() {
        return this.steroid_planet$pointyHead;
    }

    @Unique
    @Override
    public void resetArms() {
        ArmorEntityModel<?> model = (ArmorEntityModel<?>) (Object) this;
        model.leftArm.xScale = BASE_SCALE.x;
        model.leftArm.zScale = BASE_SCALE.z;
        model.rightArm.xScale = BASE_SCALE.x;
        model.rightArm.zScale = BASE_SCALE.z;
    }

    @Unique
    @Override
    public void strengthenArms() {
        ArmorEntityModel<?> model = (ArmorEntityModel<?>) (Object) this;
        model.leftArm.xScale = STRONG_SCALE.x;
        model.leftArm.zScale = STRONG_SCALE.z;
        model.rightArm.xScale = STRONG_SCALE.x;
        model.rightArm.zScale = STRONG_SCALE.z;
        // y scale is not changed
    }
}
