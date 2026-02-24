package io.github.jaffe2718.steroid_planet.entity.attribute;

import io.github.jaffe2718.steroid_planet.item.SteroidItem;
import net.minecraft.util.Identifier;

import java.util.Set;

public interface PlayerAttributeAccessor {

    float getMuscle();
    void setMuscle(float muscle);

    float getLiverHealth();
    void setLiverHealth(float liverHealth);

    float getBodyFat();
    void setBodyFat(float bodyFat);

    Set<Identifier> querySteroids();
    void recordSteroid(SteroidItem steroid);



    default void lossMuscle(float loss) {
        setMuscle(getMuscle() - loss);
    }

    default void lossLiverHealth(float loss) {
        setLiverHealth(getLiverHealth() - loss);
    }

    default void lossBodyFat(float loss) {
        setBodyFat(getBodyFat() - loss);
    }

    default void gainMuscle(float gain) {
        setMuscle(getMuscle() + gain);
    }

    default void gainLiverHealth(float gain) {
        setLiverHealth(getLiverHealth() + gain);
    }

    default void gainBodyFat(float gain) {
        setBodyFat(getBodyFat() + gain);
    }
}
