package io.github.jaffe2718.steroid_planet.mixin.client.render.entity.state;

import io.github.jaffe2718.steroid_planet.client.render.entity.state.PlayerEntityRenderStateExt;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements PlayerEntityRenderStateExt {

    @Unique
    private boolean steroid_planet$pectoralMuscleVisible;

    @Unique
    private boolean steroid_planet$pectoralMuscleJecketVisible;

    @Unique
    private boolean steroid_planet$pointyHeadVisible;

    @Unique
    private boolean steroid_planet$pointyHatVisible;

    @Override
    public void pectoralMuscleVisible(boolean visible) {
        this.steroid_planet$pectoralMuscleVisible = visible;
    }

    @Override
    public boolean pectoralMuscleVisible() {
        return this.steroid_planet$pectoralMuscleVisible;
    }

    @Override
    public void pectoralMuscleJecketVisible(boolean visible) {
        this.steroid_planet$pectoralMuscleJecketVisible = visible;
    }

    @Override
    public boolean pectoralMuscleJecketVisible() {
        return this.steroid_planet$pectoralMuscleJecketVisible;
    }

    @Override
    public void pointyHeadVisible(boolean visible) {
        this.steroid_planet$pointyHeadVisible = visible;
    }

    @Override
    public boolean pointyHeadVisible() {
        return this.steroid_planet$pointyHeadVisible;
    }

    @Override
    public void pointyHatVisible(boolean visible) {
        this.steroid_planet$pointyHatVisible = visible;
    }

    @Override
    public boolean pointyHatVisible() {
        return this.steroid_planet$pointyHatVisible;
    }
}
