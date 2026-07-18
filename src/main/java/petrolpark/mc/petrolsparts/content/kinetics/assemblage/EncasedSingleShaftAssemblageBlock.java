package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class EncasedSingleShaftAssemblageBlock extends EncasedAssemblageBlock {

    public EncasedSingleShaftAssemblageBlock(Supplier<AssemblageSet> set, BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName) {
        super(set, properties, casing, casingName);
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
        return getSet().singleShaftAssemblage().getDefaultState();
    };
    
};
