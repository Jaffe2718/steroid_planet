package io.github.jaffe2718.steroid_planet.item;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

public abstract class ModItems {

    // oral steroids
    public static Item STANOZOLOL;
    public static Item OXYMETHOLONE;
    public static Item OXANDROLONE;
    public static Item METHENOLONE;

    // Injectable steroids
    public static Item NANDROLONE;
    public static Item TRENBOLOONE;
    public static Item TRESTOLONE;
    public static Item CIS_TRENBOLONE;
    public static Item STENBOLONE;
    public static Item TRENBOLOONE_ACETATE;
    public static Item NANDROLONE_DECANOATE;
    public static Item NANDROLONE_PHENYLPROPIONATE;
    public static Item SYNTHOL;

    // Champion Trophy
    public static Item CHAMPION_TROPHY;

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
                NANDROLONE_PHENYLPROPIONATE,
                SYNTHOL
        );
        CreativeTabRegistry.append(ItemGroups.INGREDIENTS, CHAMPION_TROPHY);
    }
}
