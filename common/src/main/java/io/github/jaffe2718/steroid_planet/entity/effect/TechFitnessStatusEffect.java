package io.github.jaffe2718.steroid_planet.entity.effect;

import io.github.jaffe2718.steroid_planet.entity.player.PlayerEntityExt;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class TechFitnessStatusEffect extends StatusEffect {

    protected TechFitnessStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (!(entity instanceof PlayerEntity)) return false;
        PlayerEntityExt player = (PlayerEntityExt) entity;
        switch (amplifier) {
            case 0 -> player.lossLiverHealth(0.002F);
            case 1 -> player.lossLiverHealth(0.005F);
            case 2 -> player.lossLiverHealth(0.01F);
        }
        return true;
    }
}
