package io.github.jaffe2718.steroid_planet.mixin.entity.player;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import io.github.jaffe2718.steroid_planet.advancement.criterion.HealthConditionCriterion;
import io.github.jaffe2718.steroid_planet.advancement.criterion.ModCriteria;
import io.github.jaffe2718.steroid_planet.entity.attribute.ModEntityAttributes;
import io.github.jaffe2718.steroid_planet.entity.player.PlayerEntityExt;
import io.github.jaffe2718.steroid_planet.entity.damage.DamageTypes;
import io.github.jaffe2718.steroid_planet.entity.effect.ModEffects;
import io.github.jaffe2718.steroid_planet.item.SteroidItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"AddedMixinMembersNamePattern", "DataFlowIssue"})
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityExt {

    @Unique private static final TrackedData<Float> MUSCLE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique private static final TrackedData<Float> LIVER_HEALTH = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique private static final TrackedData<Float> BODY_FAT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);  // default value is 30.0F

    /**
     * Record the steroids the player is using.
     * id -> boolean
     */
    @Unique private static final TrackedData<NbtCompound> STEROID_USING_RECORDS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    @Unique private int liverPoisoningTimer = 0;

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    private void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(MUSCLE, 0.0F);
        builder.add(LIVER_HEALTH, 100.0F);
        builder.add(BODY_FAT, 30.0F);
        builder.add(STEROID_USING_RECORDS, new NbtCompound());
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void createPlayerAttributes(@NotNull CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(ModEntityAttributes.MUSCLE_AND_FAT_CAPACITY)
                .add(ModEntityAttributes.MAX_LIVER_HEALTH)
        );
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("BodyFat", 5)) {               // NbtType.FLOAT
            this.setBodyFat(nbt.getFloat("BodyFat"));
        } else {
            this.setBodyFat(30.0F);
        }
        if (nbt.contains("Muscle", 5)) {                // NbtType.FLOAT
            this.setMuscle(nbt.getFloat("Muscle"));
        } else {
            this.setMuscle(0.0F);
        }
        if (nbt.contains("LiverHealth", 5)) {           // NbtType.FLOAT
            this.setLiverHealth(nbt.getFloat("LiverHealth"));
        }  else {
            this.setLiverHealth(100.0F);
        }
        if (nbt.contains("LiverPoisoningTimer", 4)) {   // NbtType.INT
            this.liverPoisoningTimer = nbt.getInt("LiverPoisoningTimer");
        }
        if (nbt.contains("SteroidUsingRecords", 10)) {   // NbtType.COMPOUND
            ((PlayerEntity) (Object) this).getDataTracker().set(STEROID_USING_RECORDS, nbt.getCompound("SteroidUsingRecords"));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putFloat("Muscle", this.getMuscle());
        nbt.putFloat("LiverHealth", this.getLiverHealth());
        nbt.putFloat("BodyFat", this.getBodyFat());
        nbt.putInt("LiverPoisoningTimer", this.liverPoisoningTimer);
        nbt.put("SteroidUsingRecords", ((PlayerEntity) (Object) this).getDataTracker().get(STEROID_USING_RECORDS));
    }

    /**
     * When the player attacks a living entity, if the player has the Tech Fitness effect, the player will gain muscle.
     */
    @Inject(method = "attack", at = @At("HEAD"))
    private void attack(Entity target, CallbackInfo ci) {
        if (target.isAttackable()) {
            if (((PlayerEntity) (Object) this).getStatusEffect(ModEffects.TECH_FITNESS) instanceof StatusEffectInstance techFitness) {
                this.gainMuscle((techFitness.getAmplifier() + 1.0F) * 3.0F);
            } else {
                this.gainMuscle(1.0F);
            }
            if (((PlayerEntity) (Object) this) instanceof ServerPlayerEntity serverPlayer) {
                ModCriteria.HEALTH_CONDITION.trigger(serverPlayer);
            }
        }
    }

    /**
     * Loss muscle every tick
     * No steroid_planet:tech_fitness effect, loss 0.01F per tick
     * With each amplifier level, the loss rate is halved.
     */
    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        float muscleLoss = 0.01F;
        float fatLoss = 0.01F;
        if (((PlayerEntity) (Object) this).getStatusEffect(ModEffects.TECH_FITNESS) instanceof StatusEffectInstance techFitness) {
            switch (techFitness.getAmplifier()) {
                case 0 -> {
                    muscleLoss *= 0.5F;
                    fatLoss *= 1.5F;
                }
                case 1 -> {
                    muscleLoss *= 0.25F;
                    fatLoss *= 2.0F;
                }
                case 2 -> {
                    muscleLoss *= 0.125F;
                    fatLoss *= 2.5F;
                }
            }
        }
        if (!((PlayerEntity) (Object) this).hasStatusEffect(ModEffects.CONTEST_PREP)) {
            this.lossMuscle(muscleLoss);
        }
        if (((PlayerEntity) (Object) this) instanceof ServerPlayerEntity serverPlayer &&
                (serverPlayer.isSwimming() || serverPlayer.isSprinting())) {
            this.lossBodyFat(fatLoss);
            ModCriteria.HEALTH_CONDITION.trigger(serverPlayer);
        }
        if (((PlayerEntity) (Object) this).getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            this.gainLiverHealth(1.0F);
        }
        this.applyLiverPoisoning();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;wakeUp(ZZ)V", shift = At.Shift.AFTER))
    private void onWakeUpNaturally(CallbackInfo ci) {
        this.gainLiverHealth(10.0F);
    }

// =====================================================================================================================

    @Unique
    private void applyLiverPoisoning() {
        PlayerEntity thiz = (PlayerEntity) (Object) this;
        if (this.getLiverHealth() < HealthConditionCriterion.LIVER_HEALTH_THRESHOLD && this.liverPoisoningTimer > 0) {
            this.liverPoisoningTimer--;
        }
        if (this.liverPoisoningTimer == 0 && this.getLiverHealth() < HealthConditionCriterion.LIVER_HEALTH_THRESHOLD
                && thiz.getWorld() instanceof ServerWorld serverWorld
        ) {
            thiz.damage(
                    serverWorld,
                    new DamageSource(serverWorld.getRegistryManager()
                            .getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(DamageTypes.LIVER_POISONING)
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

    /**
     * Set muscle, clamped between [0.0F, max_muscle - body_fat], because body_fat + muscle <= max_muscle
     * @param muscle muscle value to set
     */
    @Unique
    @Override
    public void setMuscle(float muscle) {
        ((PlayerEntity) (Object) this).getDataTracker().set(
                MUSCLE,
                MathHelper.clamp(muscle,
                        0.0F,
                        (float) ((PlayerEntity) (Object) this).getAttributeValue(ModEntityAttributes.MUSCLE_AND_FAT_CAPACITY) - this.getBodyFat()
                )
        );
        if (muscle > 20.0F) {
            ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).updateModifier(
                    new EntityAttributeModifier(
                            SteroidPlanet.id("muscle_damage"),
                            this.getMuscle() / 100.0F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            );
        } else {
            ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).removeModifier(SteroidPlanet.id("muscle_damage"));
        }
        ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).updateModifier(
                new EntityAttributeModifier(
                        SteroidPlanet.id("muscle_block_break_speed"),
                        this.getMuscle() / 100.0F,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                )
        );
    }

    @Unique
    @Override
    public void setLiverHealth(float liverHealth) {
        ((PlayerEntity) (Object) this).getDataTracker().set(
                LIVER_HEALTH,
                MathHelper.clamp(liverHealth,
                        0.0F,
                        (float) ((PlayerEntity) (Object) this).getAttributeValue(ModEntityAttributes.MAX_LIVER_HEALTH)
                )
        );
    }

    @Unique
    @Override
    public float getBodyFat() {
        return ((PlayerEntity) (Object) this).getDataTracker().get(BODY_FAT);
    }

    @Unique
    @Override
    public void setBodyFat(float bodyFat) {
        ((PlayerEntity) (Object) this).getDataTracker().set(
                BODY_FAT,
                MathHelper.clamp(bodyFat,
                        0.0F,
                        (float) ((PlayerEntity) (Object) this).getAttributeValue(ModEntityAttributes.MUSCLE_AND_FAT_CAPACITY) - this.getMuscle()
                )
        );
        if (bodyFat > 50.0F) {
            ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).updateModifier(
                    new EntityAttributeModifier(
                            SteroidPlanet.id("body_fat"),
                            -0.2F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            );
        } else {
            ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).removeModifier(SteroidPlanet.id("body_fat"));
        }
    }

    @Unique
    @Override
    public Set<Identifier> querySteroids() {
        Set<Identifier> steroidIds = new HashSet<>();
        NbtCompound steroidRecords = ((PlayerEntity) (Object) this).getDataTracker().get(STEROID_USING_RECORDS);
        for (String key : steroidRecords.getKeys()) {
            Identifier sid = Identifier.tryParse(key);
            if (sid != null && steroidRecords.getBoolean(key) && Registries.ITEM.containsId(sid)) {
                steroidIds.add(sid);
            }
        }
        return steroidIds;
    }

    @Unique
    @Override
    public void recordSteroid(SteroidItem steroid) {
        Identifier sid = Registries.ITEM.getId(steroid);
        NbtCompound steroidRecords = ((PlayerEntity) (Object) this).getDataTracker().get(STEROID_USING_RECORDS);
        if (sid != null && !steroidRecords.contains(sid.toString())) {
            steroidRecords.putBoolean(sid.toString(), true);
        }
        ((PlayerEntity) (Object) this).getDataTracker().set(STEROID_USING_RECORDS, steroidRecords);
    }
}
