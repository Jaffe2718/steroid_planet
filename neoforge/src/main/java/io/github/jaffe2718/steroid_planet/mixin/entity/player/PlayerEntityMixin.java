package io.github.jaffe2718.steroid_planet.mixin.entity.player;

import com.mojang.serialization.Codec;
import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import io.github.jaffe2718.steroid_planet.advancement.criterion.HealthConditionCriterion;
import io.github.jaffe2718.steroid_planet.advancement.criterion.ModCriteria;
import io.github.jaffe2718.steroid_planet.entity.attribute.ModEntityAttributes;
import io.github.jaffe2718.steroid_planet.entity.player.PlayerEntityExt;
import io.github.jaffe2718.steroid_planet.entity.damage.DamageTypes;
import io.github.jaffe2718.steroid_planet.entity.effect.ModEffects;
import io.github.jaffe2718.steroid_planet.item.SteroidItem;
import io.github.jaffe2718.steroid_planet.neoforge.SteroidPlanetNeoForge;
import io.github.jaffe2718.steroid_planet.neoforge.attachment.FloatSyncHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings({"DataFlowIssue", "AddedMixinMembersNamePattern"})
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityExt {

    @Unique
    private static final Supplier<AttachmentType<Float>> steroid_planet$MUSCLE = SteroidPlanetNeoForge.ATTACHMENT_TYPES.register(
            "muscle", () -> AttachmentType.builder(() -> 0.0F)
                    .serialize(Codec.FLOAT.fieldOf("Muscle"))
                    .sync(new FloatSyncHandler())
                    .build()
    );

    @Unique
    private static final Supplier<AttachmentType<Float>> steroid_planet$LIVER_HEALTH = SteroidPlanetNeoForge.ATTACHMENT_TYPES.register(
            "liver_health", () -> AttachmentType.builder(() -> 100.0F)
                    .serialize(Codec.FLOAT.fieldOf("LiverHealth"))
                    .sync(new FloatSyncHandler())
                    .build()
    );

    @Unique
    private static final Supplier<AttachmentType<Float>> steroid_planet$BODY_FAT = SteroidPlanetNeoForge.ATTACHMENT_TYPES.register(
            "body_fat", () -> AttachmentType.builder(() -> 30.0F)
                    .serialize(Codec.FLOAT.fieldOf("BodyFat"))
                    .sync(new FloatSyncHandler())
                    .build()
    );

    @Unique   // id -> boolean
    @OnlyIn(Dist.DEDICATED_SERVER)
    private NbtCompound steroid_planet$steroidUsingRecords = new NbtCompound();

    @Unique
    @OnlyIn(Dist.DEDICATED_SERVER)
    private int steroid_planet$liverPoisoningTimer = 0;

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void createPlayerAttributes(@NotNull CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(ModEntityAttributes.MUSCLE_AND_FAT_CAPACITY)
                .add(ModEntityAttributes.MAX_LIVER_HEALTH)
        );
    }

    @Inject(method = "readCustomData", at = @At("RETURN"))
    private void readCustomData(ReadView view, CallbackInfo ci) {
        this.setBodyFat(0.0F);   // reset first
        this.setMuscle(0.0F);    // reset first
        this.setMuscle(view.getFloat("Muscle", 0.0F));
        this.setBodyFat(view.getFloat("BodyFat", 30.0F));
        this.setLiverHealth(view.getFloat("LiverHealth", 100.0F));
        this.steroid_planet$liverPoisoningTimer = view.getInt("LiverPoisoningTimer", 0);
        this.steroid_planet$steroidUsingRecords = view.read("SteroidUsingRecords", NbtCompound.CODEC).orElse(new NbtCompound());
    }

    @Inject(method = "writeCustomData", at = @At("RETURN"))
    private void writeCustomData(WriteView view, CallbackInfo ci) {
        view.putFloat("Muscle", this.getMuscle());
        view.putFloat("LiverHealth", this.getLiverHealth());
        view.putFloat("BodyFat", this.getBodyFat());
        view.putInt("LiverPoisoningTimer", this.steroid_planet$liverPoisoningTimer);
        view.put("SteroidUsingRecords", NbtCompound.CODEC, this.steroid_planet$steroidUsingRecords);
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
        this.steroid_planet$applyLiverPoisoning();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;wakeUp(ZZ)V", shift = At.Shift.AFTER))
    private void onWakeUpNaturally(CallbackInfo ci) {
        this.gainLiverHealth(10.0F);
    }

// =====================================================================================================================

    @Unique
    private <T> T steroid_planet$getDataOrElse(Supplier<AttachmentType<T>> type, T defaultValue) {
        return ((PlayerEntity) (Object) this).getExistingData(type).orElse(defaultValue);
    }

    @Unique
    private void steroid_planet$applyLiverPoisoning() {
        PlayerEntity thiz = (PlayerEntity) (Object) this;
        if (this.getLiverHealth() < HealthConditionCriterion.LIVER_HEALTH_THRESHOLD && this.steroid_planet$liverPoisoningTimer > 0) {
            this.steroid_planet$liverPoisoningTimer--;
        }
        if (this.steroid_planet$liverPoisoningTimer == 0 && this.getLiverHealth() < HealthConditionCriterion.LIVER_HEALTH_THRESHOLD
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
                this.steroid_planet$liverPoisoningTimer = 10;
            } else if (this.steroid_planet$liverPoisoningTimer < 5) {
                this.steroid_planet$liverPoisoningTimer = 15;
            } else if (this.steroid_planet$liverPoisoningTimer < 10) {
                this.steroid_planet$liverPoisoningTimer = 20;
            }  else {
                this.steroid_planet$liverPoisoningTimer = 40;
            }
        }
    }

    @Unique
    @Override
    public float getMuscle() {
        return this.steroid_planet$getDataOrElse(steroid_planet$MUSCLE, 0.0F);
    }

    @Unique
    @Override
    public float getLiverHealth() {
        return this.steroid_planet$getDataOrElse(steroid_planet$LIVER_HEALTH, 100.0F);
    }

    /**
     * Set muscle, clamped between [0.0F, max_muscle - body_fat], because body_fat + muscle <= max_muscle
     * @param muscle muscle value to set
     */
    @Unique
    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    public void setMuscle(float muscle) {
        muscle = MathHelper.clamp(muscle, 0.0F,
                (float) ((PlayerEntity) (Object) this).getAttributeValue(ModEntityAttributes.MUSCLE_AND_FAT_CAPACITY) - this.getBodyFat()
        );
        ((PlayerEntity) (Object) this).setData(steroid_planet$MUSCLE, muscle);
        if (muscle > 20.0F) {
            ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).updateModifier(
                    new EntityAttributeModifier(
                            SteroidPlanet.id("muscle_damage"),
                            muscle / 100.0F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            );
        } else {
            ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).removeModifier(SteroidPlanet.id("muscle_damage"));
        }
        ((PlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).updateModifier(
                new EntityAttributeModifier(
                        SteroidPlanet.id("muscle_block_break_speed"),
                        muscle / 100.0F,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                )
        );
    }

    @Unique
    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    public void setLiverHealth(float liverHealth) {
        ((PlayerEntity) (Object) this).setData(
                steroid_planet$LIVER_HEALTH,
                MathHelper.clamp(liverHealth,
                        0.0F,
                        (float) ((PlayerEntity) (Object) this).getAttributeValue(ModEntityAttributes.MAX_LIVER_HEALTH)
                )
        );
    }

    @Unique
    @Override
    public float getBodyFat() {
        return this.steroid_planet$getDataOrElse(steroid_planet$BODY_FAT, 0.0F);
    }

    @Unique
    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    public void setBodyFat(float bodyFat) {
        bodyFat = MathHelper.clamp(bodyFat, 0.0F,
                (float) ((PlayerEntity) (Object) this).getAttributeValue(ModEntityAttributes.MUSCLE_AND_FAT_CAPACITY) - this.getMuscle()
        );
        ((PlayerEntity) (Object) this).setData(steroid_planet$BODY_FAT, bodyFat);
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
    @OnlyIn(Dist.DEDICATED_SERVER)
    public Set<Identifier> querySteroids() {
        Set<Identifier> steroidIds = new HashSet<>();
        for (String key : this.steroid_planet$steroidUsingRecords.getKeys()) {
            Identifier sid = Identifier.tryParse(key);
            if (sid != null && this.steroid_planet$steroidUsingRecords.getBoolean(key).orElse(false) && Registries.ITEM.containsId(sid)) {
                steroidIds.add(sid);
            }
        }
        return steroidIds;
    }

    @Unique
    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    public void recordSteroid(SteroidItem steroid) {
        this.steroid_planet$steroidUsingRecords.putBoolean(Registries.ITEM.getId(steroid).toString(), true);
    }
}
