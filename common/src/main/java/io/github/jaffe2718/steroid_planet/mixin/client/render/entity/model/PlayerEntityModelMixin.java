package io.github.jaffe2718.steroid_planet.mixin.client.render.entity.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.jaffe2718.steroid_planet.client.render.entity.model.PlayerEntityModelAccessor;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
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
public abstract class PlayerEntityModelMixin implements PlayerEntityModelAccessor {

    @Unique
    private ModelPart pectoralMuscle;

    @Unique
    private ModelPart pectoral_muscle_jecket;

    @Unique
    private ModelPart pointyHead;

    @Unique
    private ModelPart pointyHat;


    @Final
    @Shadow
    private boolean thinArms;


    @Inject(method = "<init>", at = @At("CTOR_HEAD"))
    private void constructor(ModelPart root, boolean thinArms, CallbackInfo ci) {
        this.pectoralMuscle = root.getChild("body").getChild("pectoral_muscle");
        this.pectoral_muscle_jecket = root.getChild("jacket").getChild("pectoral_muscle_jecket");
        this.pointyHead = root.getChild("head").getChild("pointy_head");
        this.pointyHat = root.getChild("hat").getChild("pointy_hat");
    }

    @Inject(method = "getTexturedModelData", at = @At("RETURN"), cancellable = true)
    private static void getTexturedModelData(Dilation dilation, boolean slim, CallbackInfoReturnable<ModelData> cir) {
        ModelData modelData = cir.getReturnValue();
        modelData.getRoot().getChild("body").addChild("pectoral_muscle", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -0.5F, -2.0F, 8.0F, 7.0F, 4.0F, dilation.add(2.0F, 0.0F, 1.5F)), ModelTransform.NONE);
        modelData.getRoot().getChild("jacket").addChild("pectoral_muscle_jecket", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, -0.5F, -2.0F, 8.0F, 7.0F, 4.0F, dilation.add(2.0F, 0.0F, 1.5F).add(0.25F)), ModelTransform.NONE);
        modelData.getRoot().getChild("head").addChild("pointy_head", ModelPartBuilder.create().uv(8, 2).cuboid(-1.0F, -9.5F, -1.0F, 2.0F, 2.0F, 2.0F, dilation), ModelTransform.NONE);
        modelData.getRoot().getChild("hat").addChild("pointy_hat", ModelPartBuilder.create().uv(40, 2).cuboid(-1.0F, -9.5F, -1.0F, 2.0F, 2.0F, 2.0F, dilation.add(0.25F)), ModelTransform.NONE);
        cir.setReturnValue(modelData);
    }

    @Inject(method = "getBodyParts", at = @At("RETURN"), cancellable = true)
    private void getBodyParts(CallbackInfoReturnable<Iterable<ModelPart>> cir) {
        cir.setReturnValue(Iterables.concat(cir.getReturnValue(), ImmutableList.of(this.pectoralMuscle, this.pectoral_muscle_jecket)));
    }

    @Inject(method = "setVisible", at = @At("RETURN"))
    private void setVisible(boolean visible, CallbackInfo ci) {
        this.pectoralMuscle.visible = visible;
        this.pectoral_muscle_jecket.visible = visible;
        this.pointyHead.visible = visible;
        this.pointyHat.visible = visible;
    }

    @Unique
    @Override
    public ModelPart getPectoralMuscle() {
        return this.pectoralMuscle;
    }

    @Unique
    @Override
    public ModelPart getPectoral_muscle_jecket() {
        return this.pectoral_muscle_jecket;
    }

    @Unique
    @Override
    public ModelPart getPointyHead() {
        return this.pointyHead;
    }

    @Unique
    @Override
    public ModelPart getPointyHat() {
        return this.pointyHat;
    }

    @Unique
    @Override
    public void resetArms() {
        PlayerEntityModel<?> model = (PlayerEntityModel<?>) (Object) this;
        model.leftArm.xScale = BASE_SCALE.x;
        model.leftArm.zScale = BASE_SCALE.z;
        model.leftSleeve.xScale = BASE_SCALE.x;
        model.leftSleeve.zScale = BASE_SCALE.z;

        model.rightArm.xScale = BASE_SCALE.x;
        model.rightArm.zScale = BASE_SCALE.z;
        model.rightSleeve.xScale = BASE_SCALE.x;
        model.rightSleeve.zScale = BASE_SCALE.z;
    }

    @Unique
    @Override
    public void strengthenArms() {
        PlayerEntityModel<?> model = (PlayerEntityModel<?>) (Object) this;
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
