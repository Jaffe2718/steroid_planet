package io.github.jaffe2718.steroid_planet.item;

import dev.architectury.registry.CreativeTabRegistry;
import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.util.UseAction;

public abstract class Items {

    // oral steroids
    public static final Item STANOZOLOL = register("stanozolol", 3600, 0, 12.0F, UseAction.EAT);
    public static final Item OXYMETHOLONE = register("oxymetholone", 3600, 1, 18.0F, UseAction.EAT);
    public static final Item OXANDROLONE = register("oxandrolone", 3600, 0, 7.0F, UseAction.EAT);
    public static final Item METHENOLONE = register("methenolone", 3600, 0, 5.0F, UseAction.EAT);

    // Injectable steroids
    public static final Item NANDROLONE = register("nandrolone", 3600, 0, 5.0F, UseAction.CROSSBOW);
    public static final Item TRENBOLOONE = register("trenbolone", 3600, 2, 18.0F, UseAction.CROSSBOW);
    public static final Item TRESTOLONE = register("trestolone", 9600, 2, 20.0F, UseAction.CROSSBOW);
    public static final Item CIS_TRENBOLONE = register("cis_trenbolone", 3600, 1, 15.0F, UseAction.CROSSBOW);
    public static final Item STENBOLONE = register("stenbolone", 3600, 0, 13.0F, UseAction.CROSSBOW);
    public static final Item TRENBOLOONE_ACETATE = register("trenbolone_acetate", 3600, 1, 19.0F, UseAction.CROSSBOW);
    public static final Item NANDROLONE_DECANOATE = register("nandrolone_decanoate", 3600, 0, 6.0F, UseAction.CROSSBOW);
    public static final Item NANDROLONE_PHENYLPROPIONATE = register("nandrolone_phenylpropionate", 9600, 0, 7.0F, UseAction.CROSSBOW);

    /**
     * Register a steroid item with the given potion, liver damage, and use action.
     * @param id           The ID of the steroid item.
     * @param duration     The duration of the potion effect.
     * @param amplifier    The amplifier of the potion effect.
     * @param liverDamage  The liver damage caused by the steroid.
     * @param useAction    The use action of the steroid item.
     * @return             The registered steroid item.
     */
    private static Item register(String id, int duration, int amplifier, float liverDamage, UseAction useAction) {
        return net.minecraft.item.Items.register(SteroidPlanet.id(id), new SteroidItem(duration, amplifier, liverDamage, useAction));
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void init() {
        CreativeTabRegistry.append(ItemGroups.FOOD_AND_DRINK,
            STANOZOLOL,
            OXYMETHOLONE,
            OXANDROLONE,
            METHENOLONE,
            NANDROLONE,
            TRENBOLOONE,
            TRESTOLONE,
            CIS_TRENBOLONE,
            STENBOLONE,
            TRENBOLOONE_ACETATE,
            NANDROLONE_DECANOATE,
            NANDROLONE_PHENYLPROPIONATE
        );
    }
}