package io.github.jaffe2718.steroid_planet.mixin.entity.damage;

import io.github.jaffe2718.steroid_planet.entity.damage.ModDamageTypes;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.Registerable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageTypes.class)
public interface DamageTypesMixin {

    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void bootstrap(Registerable<DamageType> damageTypeRegisterable, CallbackInfo ci) {
        ModDamageTypes.bootstrap(damageTypeRegisterable);
    }
}
