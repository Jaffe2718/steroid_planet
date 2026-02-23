package io.github.jaffe2718.steroid_planet.mixin.entity.effect;

import io.github.jaffe2718.steroid_planet.entity.effect.ModEffects;
import io.github.jaffe2718.steroid_planet.entity.effect.TechFitnessStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffect.class)
public abstract class StatusEffectsMixin {

    /**
     * Use vanilla method to register the status effects.
     * @see io.github.jaffe2718.steroid_planet.entity.effect.ModEffects
     */
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerStatically(CallbackInfo ci) {
        ModEffects.TECH_FITNESS = ModEffects.register("tech_fitness", new TechFitnessStatusEffect(StatusEffectCategory.NEUTRAL, 0xFFD5B4) {});
        ModEffects.CONTEST_PREP = ModEffects.register("contest_prep", new StatusEffect(StatusEffectCategory.BENEFICIAL, 0xFF5300) {});
    }
}
