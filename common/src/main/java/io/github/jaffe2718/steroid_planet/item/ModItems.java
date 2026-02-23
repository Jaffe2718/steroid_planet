package io.github.jaffe2718.steroid_planet.item;

import com.google.common.base.Supplier;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.UseAction;

@SuppressWarnings("SameParameterValue")
public abstract class ModItems {

    private static final Registrar<Item> ITEM_REGISTRAR = SteroidPlanet.REGISTRAR_MANAGER.get().get(RegistryKeys.ITEM);

    // oral steroids
    public static final RegistrySupplier<Item> STANOZOLOL = registerSteroid("stanozolol", 3600, 0, 12.0F, UseAction.EAT);
    public static final RegistrySupplier<Item> OXYMETHOLONE = registerSteroid("oxymetholone", 3600, 1, 18.0F, UseAction.EAT);
    public static final RegistrySupplier<Item> OXANDROLONE = registerSteroid("oxandrolone", 3600, 0, 7.0F, UseAction.EAT);
    public static final RegistrySupplier<Item> METHENOLONE = registerSteroid("methenolone", 3600, 0, 5.0F, UseAction.EAT);

    // Injectable steroids
    public static final RegistrySupplier<Item> NANDROLONE = registerSteroid("nandrolone", 3600, 0, 5.0F, UseAction.CROSSBOW);
    public static final RegistrySupplier<Item> TRENBOLOONE = registerSteroid("trenbolone", 3600, 2, 18.0F, UseAction.CROSSBOW);
    public static final RegistrySupplier<Item> TRESTOLONE = registerSteroid("trestolone", 9600, 2, 20.0F, UseAction.CROSSBOW);
    public static final RegistrySupplier<Item> CIS_TRENBOLONE = registerSteroid("cis_trenbolone", 3600, 1, 15.0F, UseAction.CROSSBOW);
    public static final RegistrySupplier<Item> STENBOLONE = registerSteroid("stenbolone", 3600, 0, 13.0F, UseAction.CROSSBOW);
    public static final RegistrySupplier<Item> TRENBOLOONE_ACETATE = registerSteroid("trenbolone_acetate", 3600, 1, 19.0F, UseAction.CROSSBOW);
    public static final RegistrySupplier<Item> NANDROLONE_DECANOATE = registerSteroid("nandrolone_decanoate", 3600, 0, 6.0F, UseAction.CROSSBOW);
    public static final RegistrySupplier<Item> NANDROLONE_PHENYLPROPIONATE = registerSteroid("nandrolone_phenylpropionate", 9600, 0, 7.0F, UseAction.CROSSBOW);

    // Champion Trophy
    public static final RegistrySupplier<Item> CHAMPION_TROPHY = register("champion_trophy", ChampionTrophyItem::new);

    /**
     * Register a steroid item with the given potion, liver damage, and use action.
     * @param id           The ID of the steroid item.
     * @param duration     The duration of the potion effect.
     * @param amplifier    The amplifier of the potion effect.
     * @param liverDamage  The liver damage caused by the steroid.
     * @param useAction    The use action of the steroid item.
     * @return             The registered steroid item.
     */
    private static RegistrySupplier<Item> registerSteroid(String id, int duration, int amplifier, float liverDamage, UseAction useAction) {
        return register(id, () -> new SteroidItem(duration, amplifier, liverDamage, useAction));
    }

    /**
     * Register an item with the given ID.
     * @param id   The ID of the item.
     * @param itemSupplier The supplier of the item.
     * @return             The registered item.
     */
    private static RegistrySupplier<Item> register(String id, Supplier<Item> itemSupplier) {
        return ITEM_REGISTRAR.register(SteroidPlanet.id(id), itemSupplier);
    }

    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
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
        CreativeTabRegistry.append(ItemGroups.INGREDIENTS, CHAMPION_TROPHY);
    }
}