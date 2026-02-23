package io.github.jaffe2718.steroid_planet;


import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;
import io.github.jaffe2718.steroid_planet.advancement.criterion.ModCriteria;
import io.github.jaffe2718.steroid_planet.entity.effect.ModEffects;
import io.github.jaffe2718.steroid_planet.item.ModItems;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SteroidPlanet {
    public static final String MOD_ID = "steroid_planet";
    public static final Supplier<RegistrarManager> REGISTRAR_MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    @Contract("_ -> new")
    public static @NotNull Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static void init() {
        // Write common init code here.
        ModCriteria.init();
        ModItems.init();
    }
}
