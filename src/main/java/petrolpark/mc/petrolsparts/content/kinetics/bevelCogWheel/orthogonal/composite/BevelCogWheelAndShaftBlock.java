package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;

public class BevelCogWheelAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty SHAFT_ON_FIRST_AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

    public BevelCogWheelAndShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, SHAFT_ON_FIRST_AXIS));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        return List.of(
            PetrolsPartsBlocks.SINGLE_AXIS_BEVEL_COGWHEEL.get().get(state.getValue(FACING)),
            AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, getShaftAxis(state))
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        return (part.isCog
            ? AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, getShaftAxis(state))
            : PetrolsPartsBlocks.SINGLE_AXIS_BEVEL_COGWHEEL.get().get(state.getValue(FACING)))
            .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withPart'");
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return MathsHelper.getSecondaryAxis(state.getValue(FACING).getAxis(), state.getValue(SHAFT_ON_FIRST_AXIS));
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING) || face.getAxis() == getShaftAxis(state);
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'transform'");
    };
    
};
