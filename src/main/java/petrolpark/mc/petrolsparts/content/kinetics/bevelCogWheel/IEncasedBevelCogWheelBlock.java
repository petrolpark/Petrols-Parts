package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import petrolpark.mc.petrolsparts.PetrolsParts;

public interface IEncasedBevelCogWheelBlock extends EncasedBlock {

    public static <T extends IEncasedBevelCogWheelBlock> NonNullFunction<BlockBehaviour.Properties, T> andesite(IEncasedBevelCogWheelBlock.Factory<T> factory) {
        return p -> factory.create(p, AllBlocks.ANDESITE_CASING::get, Util.makeDescriptionId("block", PetrolsParts.asResource("andesite_encased_bevel_cogwheel")));
    };

    public static <T extends IEncasedBevelCogWheelBlock> NonNullFunction<BlockBehaviour.Properties, T> brass(IEncasedBevelCogWheelBlock.Factory<T> factory) {
        return p -> factory.create(p, AllBlocks.BRASS_CASING::get, Util.makeDescriptionId("block", PetrolsParts.asResource("brass_encased_bevel_cogwheel")));
    };
    
    @FunctionalInterface
    public interface Factory<T extends IEncasedBevelCogWheelBlock> {

        public T create(BlockBehaviour.Properties properties, Supplier<Block> casing, String translationKey);
    };
};
