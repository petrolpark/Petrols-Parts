package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;

public class EncasedSeparateShaftHalvesAssemblageBlock extends EncasedAssemblageBlock {

    public EncasedSeparateShaftHalvesAssemblageBlock(BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName) {
        super(properties, casing, casingName);
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
        return PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE.getDefaultState();
    };
    
};
