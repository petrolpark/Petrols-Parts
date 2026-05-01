package com.petrolpark.petrolsparts.mixin.accessor;

import com.simibubi.create.content.kinetics.KineticNetwork;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KineticNetwork.class)
public interface KineticNetworkAccessor {

    @Accessor("currentCapacity")
    float getCurrentCapacity();

    @Accessor("currentStress")
    float getCurrentStress();

}
