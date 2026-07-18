package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface IEncasedBevelCogWheelBlock extends EncasedBlock {

    public static <T extends IEncasedBevelCogWheelBlock> NonNullFunction<BlockBehaviour.Properties, T> andesite(Supplier<BevelCogWheelSet> set, IEncasedBevelCogWheelBlock.Factory<T> factory) {
        return p -> factory.create(set, p, AllBlocks.ANDESITE_CASING::get, "andesite");
    };

    public static <T extends IEncasedBevelCogWheelBlock> NonNullFunction<BlockBehaviour.Properties, T> brass(Supplier<BevelCogWheelSet> set, IEncasedBevelCogWheelBlock.Factory<T> factory) {
        return p -> factory.create(set, p, AllBlocks.BRASS_CASING::get, "brass");
    };
    
    @FunctionalInterface
    public interface Factory<T extends IEncasedBevelCogWheelBlock> {

        public T create(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName);
    };
};
