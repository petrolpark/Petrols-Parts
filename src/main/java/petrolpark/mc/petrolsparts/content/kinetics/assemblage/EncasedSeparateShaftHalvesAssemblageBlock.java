package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class EncasedSeparateShaftHalvesAssemblageBlock extends EncasedAssemblageBlock {

    public EncasedSeparateShaftHalvesAssemblageBlock(Supplier<AssemblageSet> set, BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName) {
        super(set, properties, casing, casingName);
            registerDefaultState(defaultBlockState()
            .setValue(TOP_SHAFT_HALF, false)
            .setValue(BOTTOM_SHAFT_HALF, false)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TOP_SHAFT_HALF, BOTTOM_SHAFT_HALF);
    };

    @Override
    public boolean hasTopShaft(BlockState state) {
        return state.getValue(TOP_SHAFT_HALF);
    };

    @Override
    public boolean hasBottomShaft(BlockState state) {
        return state.getValue(BOTTOM_SHAFT_HALF);
    };

    @Override
    public BlockState getUnencasedDefaultState() {
        return getSet().separateShaftsAssemblage().getDefaultState();
    };
    
};
