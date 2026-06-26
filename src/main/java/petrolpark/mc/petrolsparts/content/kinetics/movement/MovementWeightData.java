package petrolpark.mc.petrolsparts.content.kinetics.movement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import petrolpark.mc.library.util.codec.CodecHelper;

public record MovementWeightData(float stressCapacity, ResourceLocation model) {
    
    public static final Codec<MovementWeightData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CodecHelper.POS_FLOAT.fieldOf("stress_capacity").forGetter(MovementWeightData::stressCapacity),
        ResourceLocation.CODEC.fieldOf("model").forGetter(MovementWeightData::model)
    ).apply(instance, MovementWeightData::new));
};
