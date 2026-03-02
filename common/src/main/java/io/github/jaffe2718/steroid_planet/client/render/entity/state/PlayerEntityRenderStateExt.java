package io.github.jaffe2718.steroid_planet.client.render.entity.state;

import io.github.jaffe2718.steroid_planet.client.render.entity.model.PlayerEntityModelExt;

/**
 * Extension interface for {@link net.minecraft.client.render.entity.state.PlayerEntityRenderState}.
 * @see io.github.jaffe2718.steroid_planet.mixin.client.render.entity.state.PlayerEntityRenderStateMixin
 * @see PlayerEntityModelExt
 */
public interface PlayerEntityRenderStateExt {

    void strong(boolean strong);
    boolean strong();

    void pointyHead(boolean pointy);
    boolean pointyHead();
}
