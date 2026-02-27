package io.github.jaffe2718.steroid_planet.client.render.entity.state;

/**
 * Extension interface for {@link net.minecraft.client.render.entity.state.PlayerEntityRenderState}.
 * @see io.github.jaffe2718.steroid_planet.mixin.client.render.entity.state.PlayerEntityRenderStateMixin
 * @see io.github.jaffe2718.steroid_planet.client.render.entity.model.BipedEntityModelExt
 */
public interface PlayerEntityRenderStateExt {

    void pectoralMuscleVisible(boolean visible);
    boolean pectoralMuscleVisible();

    void pectoralMuscleJecketVisible(boolean visible);
    boolean pectoralMuscleJecketVisible();

    void pointyHeadVisible(boolean visible);
    boolean pointyHeadVisible();

    void pointyHatVisible(boolean visible);
    boolean pointyHatVisible();
}
