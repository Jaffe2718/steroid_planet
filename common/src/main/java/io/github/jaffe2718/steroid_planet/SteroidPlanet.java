package io.github.jaffe2718.steroid_planet;

import io.github.jaffe2718.steroid_planet.advancement.criterion.ModCriteria;
import io.github.jaffe2718.steroid_planet.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SteroidPlanet {
    public static final String MOD_ID = "steroid_planet";

    @Contract("_ -> new")
    public static @NotNull Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static void init() {
        // Write common init code here.
        ModCriteria.init();
        Items.init();
    }
}
