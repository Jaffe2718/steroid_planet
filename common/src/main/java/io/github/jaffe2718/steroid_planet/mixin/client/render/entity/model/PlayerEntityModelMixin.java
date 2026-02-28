package io.github.jaffe2718.steroid_planet.mixin.client.render.entity.model;

import io.github.jaffe2718.steroid_planet.client.render.entity.model.BipedEntityModelExt;
import io.github.jaffe2718.steroid_planet.client.render.entity.state.PlayerEntityRenderStateExt;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin implements BipedEntityModelExt {

    @Unique
    private ModelPart steroid_planet$pectoralMuscle;

    @Unique
    private ModelPart steroid_planet$pectoralMuscleJacket;

    @Unique
    private ModelPart steroid_planet$pointyHead;

    @Unique
    private ModelPart steroid_planet$pointyHat;


    @Final
    @Shadow
    private boolean thinArms;


    @Inject(method = "<init>", at = @At("CTOR_HEAD"))
    private void constructor(ModelPart root, boolean thinArms, CallbackInfo ci) {
        this.steroid_planet$pectoralMuscle = root.getChild("body").getChild("pectoral_muscle");
        this.steroid_planet$pectoralMuscleJacket = root.getChild("body").getChild("jacket").getChild("pectoral_muscle_jecket");
        this.steroid_planet$pointyHead = root.getChild("head").getChild("pointy_head");
        this.steroid_planet$pointyHat = root.getChild("head").getChild("hat").getChild("pointy_hat");
    }

    @Inject(method = "getTexturedModelData", at = @At("RETURN"), cancellable = true)
    private static void getTexturedModelData(Dilation dilation, boolean slim, CallbackInfoReturnable<ModelData> cir) {
        ModelData modelData = cir.getReturnValue();
        modelData.getRoot().getChild("body").addChild("pectoral_muscle", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -0.5F, -2.0F, 8.0F, 7.0F, 4.0F, dilation.add(2.0F, 0.0F, 1.5F)), ModelTransform.NONE);
        modelData.getRoot().getChild("body").getChild("jacket").addChild("pectoral_muscle_jecket", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, -0.5F, -2.0F, 8.0F, 7.0F, 4.0F, dilation.add(2.0F, 0.0F, 1.5F).add(0.25F)), ModelTransform.NONE);
        modelData.getRoot().getChild("head").addChild("pointy_head", ModelPartBuilder.create().uv(8, 2).cuboid(-1.0F, -9.5F, -1.0F, 2.0F, 2.0F, 2.0F, dilation), ModelTransform.NONE);
        modelData.getRoot().getChild("head").getChild("hat").addChild("pointy_hat", ModelPartBuilder.create().uv(40, 2).cuboid(-1.0F, -9.5F, -1.0F, 2.0F, 2.0F, 2.0F, dilation.add(0.25F)), ModelTransform.NONE);
        cir.setReturnValue(modelData);
    }

    @Inject(method = "setVisible", at = @At("RETURN"))
    private void setVisible(boolean visible, CallbackInfo ci) {
        this.steroid_planet$pectoralMuscle.visible = visible;
        this.steroid_planet$pectoralMuscleJacket.visible = visible;
        this.steroid_planet$pointyHead.visible = visible;
        this.steroid_planet$pointyHat.visible = visible;
    }

    /**
     * Apply visible state to the model parts based on the render state.
     * @param playerEntityRenderState the render state of the player entity
     * @param ci the callback info
     */
    @SuppressWarnings("AmbiguousMixinReference")
    @Inject(method = "setAngles", at = @At("RETURN"))
    private void setAngles(PlayerEntityRenderState playerEntityRenderState, CallbackInfo ci) {
        PlayerEntityRenderStateExt stateExt = (PlayerEntityRenderStateExt) playerEntityRenderState;
        this.steroid_planet$pectoralMuscle.visible = stateExt.strong();
        this.steroid_planet$pectoralMuscleJacket.visible = stateExt.strong();
        this.steroid_planet$pointyHead.visible = stateExt.pointyHead();
        this.steroid_planet$pointyHat.visible = stateExt.pointyHead();
        if (stateExt.strong()) {
            this.strengthenArms();
        } else {
            this.resetArms();
        }
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
        PlayerEntityModel thiz = (PlayerEntityModel) (Object) this;
        thiz.leftArm.xScale = BASE_SCALE.x;
        thiz.leftArm.zScale = BASE_SCALE.z;
        thiz.leftSleeve.xScale = BASE_SCALE.x;
        thiz.leftSleeve.zScale = BASE_SCALE.z;

        thiz.rightArm.xScale = BASE_SCALE.x;
        thiz.rightArm.zScale = BASE_SCALE.z;
        thiz.rightSleeve.xScale = BASE_SCALE.x;
        thiz.rightSleeve.zScale = BASE_SCALE.z;
    }

    @Unique
    @Override
    public void strengthenArms() {
        PlayerEntityModel model = (PlayerEntityModel) (Object) this;
        model.leftArm.xScale = STRONG_SCALE.x + (this.thinArms ? 0.2F : 0.0F);
        model.leftArm.zScale = STRONG_SCALE.z;
        model.leftSleeve.xScale = STRONG_SCALE.x + (this.thinArms ? 0.2F : 0.0F);
        model.leftSleeve.zScale = STRONG_SCALE.z;

        model.rightArm.xScale = STRONG_SCALE.x + (this.thinArms ? 0.2F : 0.0F);
        model.rightArm.zScale = STRONG_SCALE.z;
        model.rightSleeve.xScale = STRONG_SCALE.x + (this.thinArms ? 0.2F : 0.0F);
        model.rightSleeve.zScale = STRONG_SCALE.z;

        // y scale is not changed
    }
}
