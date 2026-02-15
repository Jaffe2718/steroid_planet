package io.github.jaffe2718.steroid_planet.registry.tag;

import io.github.jaffe2718.steroid_planet.SteroidPlanet;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;


public interface ItemTags {

    TagKey<Item> LIVER_HEALING_I = of("liver_healing_i");
    TagKey<Item> LIVER_HEALING_II = of("liver_healing_ii");
    TagKey<Item> LIVER_HEALING_III = of("liver_healing_iii");
    TagKey<Item> PROTEIN = of("protein");

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, SteroidPlanet.id(id));
    }
}
