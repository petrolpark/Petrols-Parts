package com.petrolpark.petrolsparts.content.kinetics.movement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.util.CodecHelper;

import net.minecraft.resources.ResourceLocation;

public record MovementWeightData(float stressCapacity, ResourceLocation model) {
    
    public static final Codec<MovementWeightData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CodecHelper.POS_FLOAT.fieldOf("stress_capacity").forGetter(MovementWeightData::stressCapacity),
        ResourceLocation.CODEC.fieldOf("model").forGetter(MovementWeightData::model)
    ).apply(instance, MovementWeightData::new));
};
