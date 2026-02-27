package io.github.jaffe2718.steroid_planet.mixin.item;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import io.github.jaffe2718.steroid_planet.item.ChampionTrophyItem;
import io.github.jaffe2718.steroid_planet.item.ModItems;
import io.github.jaffe2718.steroid_planet.item.SteroidItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(Items.class)
public abstract class ItemsMixin {

    /**
     * Register steroids statically in vanilla's way.
     */
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerStatically(CallbackInfo ci) {
        ModItems.STANOZOLOL = steroid_planet$registerSteroid("stanozolol", 3600, 0, 12.0F, UseAction.EAT);
        ModItems.OXYMETHOLONE = steroid_planet$registerSteroid("oxymetholone", 3600, 1, 16.0F, UseAction.EAT);
        ModItems.OXANDROLONE = steroid_planet$registerSteroid("oxandrolone", 3600, 0, 7.0F, UseAction.EAT);
        ModItems.METHENOLONE = steroid_planet$registerSteroid("methenolone", 3600, 0, 5.0F, UseAction.EAT);

        ModItems.NANDROLONE = steroid_planet$registerSteroid("nandrolone", 3600, 0, 5.0F, UseAction.CROSSBOW);
        ModItems.TRENBOLOONE = steroid_planet$registerSteroid("trenbolone", 3600, 2, 18.0F, UseAction.CROSSBOW);
        ModItems.TRESTOLONE = steroid_planet$registerSteroid("trestolone", 9600, 2, 20.0F, UseAction.CROSSBOW);
        ModItems.CIS_TRENBOLONE = steroid_planet$registerSteroid("cis_trenbolone", 3600, 1, 15.0F, UseAction.CROSSBOW);
        ModItems.STENBOLONE = steroid_planet$registerSteroid("stenbolone", 3600, 0, 13.0F, UseAction.CROSSBOW);
        ModItems.TRENBOLOONE_ACETATE = steroid_planet$registerSteroid("trenbolone_acetate", 3600, 1, 19.0F, UseAction.CROSSBOW);
        ModItems.NANDROLONE_DECANOATE = steroid_planet$registerSteroid("nandrolone_decanoate", 3600, 0, 6.0F, UseAction.CROSSBOW);
        ModItems.NANDROLONE_PHENYLPROPIONATE = steroid_planet$registerSteroid("nandrolone_phenylpropionate", 9600, 0, 7.0F, UseAction.CROSSBOW);
        ModItems.SYNTHOL = steroid_planet$registerSteroid("synthol", 9600, 1, 17.0F, UseAction.CROSSBOW);

        ModItems.CHAMPION_TROPHY = steroid_planet$register("champion_trophy", settings -> new ChampionTrophyItem(settings.rarity(Rarity.UNCOMMON)));
    }

    @Unique
    private static Item steroid_planet$registerSteroid(String id, int duration, int amplifier, float liverDamage, UseAction useAction) {
        return steroid_planet$register(id, (settings) -> new SteroidItem(settings, duration, amplifier, liverDamage, useAction));
    }

    @Unique
    private static Item steroid_planet$register(String id, Function<Item.Settings, Item> factory) {
        return Items.register(steroid_planet$keyOf(id), factory);
    }

    @Unique
    private static RegistryKey<Item> steroid_planet$keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, SteroidPlanet.id(id));
    }

}
