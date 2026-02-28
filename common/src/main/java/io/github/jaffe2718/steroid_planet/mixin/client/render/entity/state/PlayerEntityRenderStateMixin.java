package io.github.jaffe2718.steroid_planet.mixin.client.render.entity.state;

import io.github.jaffe2718.steroid_planet.client.render.entity.state.PlayerEntityRenderStateExt;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements PlayerEntityRenderStateExt {

    @Unique
    private boolean steroid_planet$strong;

    @Unique
    private boolean steroid_planet$pointyHead;


    @Override
    public void strong(boolean strong) {
        this.steroid_planet$strong = strong;
    }

    @Override
    public boolean strong() {
        return this.steroid_planet$strong;
    }

    @Override
    public void pointyHead(boolean pointy) {
        this.steroid_planet$pointyHead = pointy;
    }

    @Override
    public boolean pointyHead() {
        return this.steroid_planet$pointyHead;
    }
}
