package io.github.jaffe2718.steroid_planet.client.render.entity.state;

/**
 * Extension interface for {@link net.minecraft.client.render.entity.state.PlayerEntityRenderState}.
 * @see io.github.jaffe2718.steroid_planet.mixin.client.render.entity.state.PlayerEntityRenderStateMixin
 * @see io.github.jaffe2718.steroid_planet.client.render.entity.model.BipedEntityModelExt
 */
public interface PlayerEntityRenderStateExt {

    void strong(boolean visible);
    boolean strong();

    void pointyHead(boolean visible);
    boolean pointyHead();
}
