package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;
import java.util.function.Supplier;

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
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;

public class ThreeBevelCogWheelsAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final DirectionProperty EXCLUDED_FACE = BlockStateProperties.FACING;
    /**
     * Axis of the other two cogs
     */
    public static final BooleanProperty OTHER_COGS_ON_FIRST_AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

    public ThreeBevelCogWheelsAndShaftBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(EXCLUDED_FACE, OTHER_COGS_ON_FIRST_AXIS));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        return List.of(
            BlockHelper.copyAll(getSet().threeBlock().getDefaultState(), state),
            getSet().shaftBlock().getDefaultState().setValue(ShaftBlock.AXIS, getShaftAxis(state))
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        if (part instanceof BevelCogWheelPart.Cog cog) {
            return getSet().cornerAndShaftBlock().getDefaultState()
                .setValue(CornerBevelCogWheelsAndShaftBlock.ORIENTATION, Orientation.fromTopAndFront(state.getValue(EXCLUDED_FACE).getOpposite(), cog.face.getOpposite()).asEdge())
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else {
            return BlockHelper.copyAll(getSet().threeBlock().getDefaultState(), state);
        }
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        if (part == getSet().cogParts().get(state.getValue(EXCLUDED_FACE))) return getSet().fourAndShaftBlock().getDefaultState()
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
        return getSet().threeBlock().get().getRotationAxis(state);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face != state.getValue(EXCLUDED_FACE);
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return getSet().threeBlock().get().transform(state, transform);
    };
    
};
