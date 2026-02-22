package io.github.jaffe2718.steroid_planet.client.render.entity.model;

import net.minecraft.client.model.ModelPart;
import org.joml.Vector3f;

public interface BaseEntityModelAccessor {

    Vector3f BASE_SCALE = new Vector3f(1.0F, 1.0F, 1.0F);
    Vector3f STRONG_SCALE = new Vector3f(1.3F, 1.0F, 1.3F);

    ModelPart getPectoralMuscle();
    ModelPart getPointyHead();
    void strengthenArms();
    void resetArms();
}
