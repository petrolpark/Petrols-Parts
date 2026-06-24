package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.List;

import com.simibubi.create.AllBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public non-sealed class SingleShaftAssemblageBlock extends AssemblageBlock {

    public SingleShaftAssemblageBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    public List<AssemblagePart> getParts(BlockState state) {
        final List<AssemblagePart> parts = super.getParts(state);
        parts.add(AssemblagePart.SHAFTS.get(state.getValue(AXIS)));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, AssemblagePart part) {
        state = super.withoutPart(state, part);
        if (state.getValue(TOP_COG).isNone() && state.getValue(BOTTOM_COG).isNone()) {
            final Axis axis = state.getValue(AXIS);
            state = (switch (state.getValue(MIDDLE_COG)) {
                // case SMALL -> AllBlocks.COGWHEEL.getDefaultState();
                // case LARGE -> AllBlocks.LARGE_COGWHEEL.getDefaultState();
                case NONE -> AllBlocks.SHAFT.getDefaultState();
                default -> state;
            }).setValue(BlockStateProperties.AXIS, axis);
        };
        return state;
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
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS);
    };

    @Override
    public BlockEntityType<? extends AssemblageBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.ASSEMBLAGE.get();
    };
    
};
