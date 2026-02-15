package io.github.jaffe2718.steroid_planet.mixin.entity.player;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import io.github.jaffe2718.steroid_planet.advancement.criterion.ModCriteria;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import io.github.jaffe2718.steroid_planet.entity.damage.ModDamageTypes;
import io.github.jaffe2718.steroid_planet.entity.effect.Effects;
import io.github.jaffe2718.steroid_planet.registry.tag.ItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ALL")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerAttributeAccessor {

    @Unique private static final TrackedData<Float> MUSCLE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique private static final TrackedData<Float> LIVER_HEALTH = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique private int liverPoisoningTimer = 0;

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    private void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(MUSCLE, 0.0F);
        builder.add(LIVER_HEALTH, 100.0F);
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void createPlayerAttributes(@NotNull CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(PlayerAttributeAccessor.MAX_MUSCLE)
                .add(PlayerAttributeAccessor.MAX_LIVER_HEALTH)
        );
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("Muscle", 5)) {        // NbtType.FLOAT
            this.setMuscle(nbt.getFloat("Muscle"));
        } else {
            this.setMuscle(0.0F);
        }
        if (nbt.contains("LiverHealth", 5)) {   // NbtType.FLOAT
            this.setLiverHealth(nbt.getFloat("LiverHealth"));
        }  else {
            this.setLiverHealth(100.0F);
        }
        if (nbt.contains("LiverPoisoningTimer", 4)) {   // NbtType.INT
            this.liverPoisoningTimer = nbt.getInt("LiverPoisoningTimer");
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putFloat("Muscle", this.getMuscle());
        nbt.putFloat("LiverHealth", this.getLiverHealth());
    }

    /**
     * When the player attacks a living entity, if the player has the Tech Fitness effect, the player will gain muscle.
     *
     * @param target
     * @param ci
     */
    @Inject(method = "attackLivingEntity", at = @At("RETURN"))
    private void attackLivingEntity(LivingEntity target, CallbackInfo ci) {
        if (target.isAttackable()) {
            if (((PlayerEntity) (Object) this).getStatusEffect(Effects.TECH_FITNESS) instanceof StatusEffectInstance techFitness) {
                this.gainMuscle((techFitness.getAmplifier() + 1.0F) * 3.0F);
            } else {
                this.gainMuscle(1.0F);
            }
            if (((PlayerEntity) (Object) this) instanceof ServerPlayerEntity serverPlayer) {
                ModCriteria.MUSCLE_CRITERION.trigger(serverPlayer);
            }
        }
    }

    /**
     * When the player breaks a block, the speed of breaking the block is increased by the player's muscle.
     *
     * @param block
     * @param cir
     */
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() * (1 + this.getMuscle() / 100F));
    }

    /**
     * Loss muscle every tick
     * No steroid_planet:tech_fitness effect, loss 0.01F per tick
     * With each amplifier level, the loss rate is halved.
     *
     * @param ci
     */
    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        float muscleLoss = 0.01F;
        if (((PlayerEntity) (Object) this).getStatusEffect(Effects.TECH_FITNESS) instanceof StatusEffectInstance techFitness) {
            switch (techFitness.getAmplifier()) {
                case 0 -> {
                    muscleLoss *= 0.5F;
                    this.lossLiverHealth(0.002F);
                }
                case 1 -> {
                    muscleLoss *= 0.25F;
                    this.lossLiverHealth(0.005F);
                }
                case 2 -> {
                    muscleLoss *= 0.125F;
                    this.lossLiverHealth(0.01F);
                }
            }
        }
        if (((PlayerEntity) (Object) this).isSleeping()) {
            this.gainLiverHealth(0.15F);
        }
        if (((PlayerEntity) (Object) this).getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            this.gainLiverHealth(1.0F);
        }
        this.lossMuscle(muscleLoss);
        this.applyLiverPoisoning();
    }

    /**
     * When the player eats food, if the food has the liver healing tag, the player will recover liver health.
     * @see io.github.jaffe2718.steroid_planet.registry.tag.ItemTags
     * @param world
     * @param stack
     * @param foodComponent
     * @param cir
     */
    @Inject(method = "eatFood", at = @At("HEAD"))
    private void eatFood(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isIn(ItemTags.PROTEIN)) {
            if (((PlayerEntity) (Object) this).getStatusEffect(Effects.TECH_FITNESS) instanceof StatusEffectInstance techFitness) {
                this.gainMuscle(8.0F + (techFitness.getAmplifier() + 1.0F) * 3.0F);
            } else {
                this.gainMuscle(8.0F);
            }
            if (((PlayerEntity) (Object) this) instanceof ServerPlayerEntity serverPlayer) {
                ModCriteria.MUSCLE_CRITERION.trigger(serverPlayer);
            }
        }
        if (stack.isIn(ItemTags.LIVER_HEALING_I)) {
            this.gainLiverHealth(0.75F);
        } else if (stack.isIn(ItemTags.LIVER_HEALING_II)) {
            this.gainLiverHealth(8.0F);
        } else if (stack.isIn(ItemTags.LIVER_HEALING_III)) {
            this.gainLiverHealth(100.0F);
        }
    }

// =====================================================================================================================

    @Unique
    private void applyLiverPoisoning() {
        if (this.getLiverHealth() < 15.0F && this.liverPoisoningTimer > 0) {
            this.liverPoisoningTimer--;
        }
        if (this.liverPoisoningTimer == 0 && this.getLiverHealth() < 15.0F) {
            ((PlayerEntity) (Object) this).damage(
                    new DamageSource(((PlayerEntity) (Object) this)
                            .getWorld()
                            .getRegistryManager()
                            .get(RegistryKeys.DAMAGE_TYPE).entryOf(ModDamageTypes.LIVER_POISONING)
                    ),
                    1.0F
            );
            if (this.getLiverHealth() == 0.0F) {
                this.liverPoisoningTimer = 10;
            } else if (this.liverPoisoningTimer < 5) {
                this.liverPoisoningTimer = 15;
            } else if (this.liverPoisoningTimer < 10) {
                this.liverPoisoningTimer = 20;
            }  else {
                this.liverPoisoningTimer = 40;
            }
        }
    }

    @Unique
    @Override
    public float getMuscle() {
        return ((PlayerEntity) (Object) this).getDataTracker().get(MUSCLE);
    }

    @Unique
    @Override
    public float getLiverHealth() {
        return ((PlayerEntity) (Object) this).getDataTracker().get(LIVER_HEALTH);
    }

    @Unique
    @Override
    public void setMuscle(float muscle) {
        ((PlayerEntity) (Object) this).getDataTracker().set(
                MUSCLE,
                MathHelper.clamp(muscle,
                        0.0F,
                        (float) ((PlayerEntity) (Object) this).getAttributeValue(PlayerAttributeAccessor.MAX_MUSCLE)
                )
        );
        if (muscle > 20.0F) {
            ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).updateModifier(
                    new EntityAttributeModifier(
                            SteroidPlanet.id("muscle"),
                            this.getMuscle() / 100.0F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            );
        } else {
            ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).removeModifier(SteroidPlanet.id("muscle"));
        }
    }

    @Unique
    @Override
    public void setLiverHealth(float liverHealth) {
        ((PlayerEntity) (Object) this).getDataTracker().set(
                LIVER_HEALTH,
                MathHelper.clamp(liverHealth,
                        0.0F,
                        (float) ((PlayerEntity) (Object) this).getAttributeValue(PlayerAttributeAccessor.MAX_LIVER_HEALTH)
                )
        );
    }
}
