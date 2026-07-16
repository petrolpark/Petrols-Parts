package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;

public class ThreeBevelCogWheelsAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final DirectionProperty EXCLUDED_FACE = BlockStateProperties.FACING;
    /**
     * Axis of the other two cogs
     */
    public static final BooleanProperty OTHER_COGS_ON_FIRST_AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

    public ThreeBevelCogWheelsAndShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(EXCLUDED_FACE, OTHER_COGS_ON_FIRST_AXIS));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        return List.of(
            BlockHelper.copyAll(PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.getDefaultState(), state),
            AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, getShaftAxis(state))
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        if (part.isCog) {
            return PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS_AND_SHAFT.getDefaultState()
                .setValue(CornerBevelCogWheelsAndShaftBlock.ORIENTATION, Orientation.fromTopAndFront(state.getValue(EXCLUDED_FACE).getOpposite(), Direction.get(part.isTopCog() ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE, getRotationAxis(state))).asEdge())
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else {
            return BlockHelper.copyAll(PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.getDefaultState(), state);
        }
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        if (part == BevelCogWheelPart.COGS.get(state.getValue(EXCLUDED_FACE))) return PetrolsPartsBlocks.FOUR_BEVEL_COGWHEELS_AND_SHAFT.getDefaultState()
            .setValue(FourBevelCogWheelsAndShaftBlock.SHAFT_AXIS, getShaftAxis(state))
            .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        else return null; 
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return MathsHelper.getTertiaryAxis(state.getValue(EXCLUDED_FACE).getAxis(), state.getValue(OTHER_COGS_ON_FIRST_AXIS));
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.get().getRotationAxis(state);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face != state.getValue(EXCLUDED_FACE);
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.get().transform(state, transform);
    };
    
};
