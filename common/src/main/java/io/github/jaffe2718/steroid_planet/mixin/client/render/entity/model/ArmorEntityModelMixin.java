package io.github.jaffe2718.steroid_planet.mixin.client.render.entity.model;

import io.github.jaffe2718.steroid_planet.client.render.entity.model.BaseEntityModelAccessor;
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
public abstract class ArmorEntityModelMixin implements BaseEntityModelAccessor {

    @Unique
    private ModelPart pectoralMuscle;

    @Unique
    private ModelPart pointyHead;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(ModelPart modelPart, CallbackInfo ci) {
        this.pectoralMuscle = modelPart.getChild("body").getChild("pectoral_muscle");
        this.pointyHead = modelPart.getChild("head").getChild("pointy_head");
        this.pectoralMuscle.visible = false;
        this.pointyHead.visible = false;
    }


    @Inject(method = "getModelData", at = @At("RETURN"))
    private static void getModelData(Dilation dilation, CallbackInfoReturnable<ModelData> cir) {
        ModelData modelData = cir.getReturnValue();
        modelData.getRoot().getChild("body").addChild("pectoral_muscle", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -0.5F, -2.0F, 8.0F, 7.0F, 4.0F, dilation.add(2.0F, 0.0F, 1.5F)), ModelTransform.NONE);
        modelData.getRoot().getChild("head").addChild("pointy_head", ModelPartBuilder.create().uv(8, 2).cuboid(-1.0F, -9.5F, -1.0F, 2.0F, 2.0F, 2.0F, dilation), ModelTransform.NONE);
    }

    @Unique
    @Override
    public ModelPart getPectoralMuscle() {
        return this.pectoralMuscle;
    }

    @Unique
    @Override
    public ModelPart getPointyHead() {
        return this.pointyHead;
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
