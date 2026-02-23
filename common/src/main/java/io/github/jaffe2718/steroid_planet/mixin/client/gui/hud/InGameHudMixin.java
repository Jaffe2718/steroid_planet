package io.github.jaffe2718.steroid_planet.mixin.client.gui.hud;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import io.github.jaffe2718.steroid_planet.entity.attribute.PlayerAttributeAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    private static final Identifier LIVER_HEALTH_FULL_TEXTURE = SteroidPlanet.id("hud/liver_health_full");

    @Unique
    private static final Identifier LIVER_HEALTH_HALF_TEXTURE = SteroidPlanet.id("hud/liver_health_half");

    @Unique
    private static final Identifier MUSCLE_FULL_TEXTURE = SteroidPlanet.id("hud/muscle_full");

    @Unique
    private static final Identifier MUSCLE_HALF_TEXTURE = SteroidPlanet.id("hud/muscle_half");

    @Unique
    private static final Identifier BODY_FAT_TEXTURE = SteroidPlanet.id("hud/body_fat");

    @Unique
    private static int steroid_planet$muscleBarY = 0;

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    protected abstract int getHeartCount(LivingEntity entity);

    @Shadow
    protected abstract int getHeartRows(int heartCount);

    @Shadow
    protected abstract LivingEntity getRiddenEntity();

    @Inject(method = "renderArmor", at = @At("RETURN"))
    private static void renderArmor(DrawContext context, PlayerEntity player, int i, int j, int k, int x, CallbackInfo ci) {
        if (player.getArmor() > 0) {
            steroid_planet$muscleBarY = i - (j - 1) * k - 20;
        } else  {
            steroid_planet$muscleBarY = i - (j - 1) * k - 10;
        }
    }

    @Inject(method = "renderStatusBars", at = @At("RETURN"))
    private void renderStatusBars(DrawContext context,CallbackInfo ci) {
        PlayerEntity player = this.getCameraPlayer();
        if (player != null) {
            steroid_planet$renderMuscleBar(context, player, context.getScaledWindowWidth() / 2 - 91, steroid_planet$muscleBarY);
            int liverBarY = context.getScaledWindowHeight() - Math.max(this.getHeartRows(this.getHeartCount(this.getRiddenEntity())) * 10, 10) - 39;
            if (player.isSubmergedInWater() || player.getAir() < player.getMaxAir()) {
                liverBarY -= 10;
            }
            steroid_planet$renderLiverHealthBar(context, player, context.getScaledWindowWidth() / 2 + 81, liverBarY);
        }
    }

    /**
     * Render the muscle bar.
     * @param context The draw context.
     * @param player The player entity.
     * @param x The x position.
     * @param y The y position.
     */
    @Unique
    private void steroid_planet$renderMuscleBar(DrawContext context, PlayerEntity player, int x, int y) {
        float muscleValue = ((PlayerAttributeAccessor) player).getMuscle();
        float bodyFatValue = ((PlayerAttributeAccessor) player).getBodyFat();
        int bodyFatIcons = (int) bodyFatValue / 10;
        int fullMuscleIcons = Math.round(muscleValue / 10);
        boolean halfMuscle = 0 < (muscleValue % 10) && (muscleValue % 10) < 5;
        for (int i = 0; i < bodyFatIcons; i++) {
            context.drawGuiTexture(BODY_FAT_TEXTURE, x + i * 8, y, 9, 9);
        }
        for (int i = bodyFatIcons; i < bodyFatIcons + fullMuscleIcons; i++) {
            context.drawGuiTexture(MUSCLE_FULL_TEXTURE, x + i * 8, y, 9, 9);
        }
        if (halfMuscle) {
            context.drawGuiTexture(MUSCLE_HALF_TEXTURE, x + (bodyFatIcons + fullMuscleIcons) * 8, y, 9, 9);
        }
    }

    @Unique
    private void steroid_planet$renderLiverHealthBar(DrawContext context, PlayerEntity player, int x, int y) {
        float liverValue = ((PlayerAttributeAccessor) player).getLiverHealth();
        int fullLiver = Math.round(liverValue / 10);
        boolean halfLiver = 1E-3 < (liverValue % 10) && (liverValue % 10) < 5;
        for (int i = 0; i < fullLiver; i++) {
            context.drawGuiTexture(LIVER_HEALTH_FULL_TEXTURE, x - i * 8, y, 9, 9);
        }
        if (halfLiver) {
            context.drawGuiTexture(LIVER_HEALTH_HALF_TEXTURE, x - fullLiver * 8, y, 9, 9);
        }
    }
}
