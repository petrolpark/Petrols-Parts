package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;

public class EncasedSingleShaftAssemblageBlock extends EncasedAssemblageBlock {

    public EncasedSingleShaftAssemblageBlock(BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName) {
        super(properties, casing, casingName);
    };

    @Override
    public boolean hasTopShaft(BlockState state) {
        return true;
    };

    @Override
    public boolean hasBottomShaft(BlockState state) {
        return true;
    };

    @Override
    public BlockState getUnencasedDefaultState() {
        return PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE.getDefaultState();
    };
    
};
