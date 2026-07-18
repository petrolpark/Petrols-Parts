package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;
import java.util.function.Supplier;

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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;

public class OppositeBevelCogWheelsAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty SHAFT_ALONG_FIRST_AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;
    
    public OppositeBevelCogWheelsAndShaftBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS, SHAFT_ALONG_FIRST_AXIS));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        final Axis axis = state.getValue(AXIS);
        return List.of(
            getSet().singleAxisBlock().get().get(Direction.get(AxisDirection.POSITIVE, axis)),
            getSet().singleAxisBlock().get().get(Direction.get(AxisDirection.NEGATIVE, axis)),
            getSet().shaftBlock().getDefaultState().setValue(ShaftBlock.AXIS, getShaftAxis(state))
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        if (part instanceof BevelCogWheelPart.Cog cog) {
            return getSet().singleAndShaftBlock().getDefaultState()
                .setValue(BevelCogWheelAndShaftBlock.FACING, cog.face.getOpposite())
                .setValue(BevelCogWheelAndShaftBlock.SHAFT_ON_FIRST_AXIS, state.getValue(SHAFT_ALONG_FIRST_AXIS))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else {
            return BlockHelper.copyAll(getSet().oppositesBlock().getDefaultState(), state);
        }
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        if (!(part instanceof BevelCogWheelPart.Cog cog)) return null;
        final Axis axis = state.getValue(AXIS);
        if (cog.face.getAxis() == axis || cog.face.getAxis() == getShaftAxis(state)) return null;
        return getSet().threeAndShaftBlock().getDefaultState()
            .setValue(ThreeBevelCogWheelsAndShaftBlock.EXCLUDED_FACE, cog.face.getOpposite())
            .setValue(ThreeBevelCogWheelsAndShaftBlock.OTHER_COGS_ON_FIRST_AXIS, MathsHelper.isSecondaryAxis(cog.face.getAxis(), axis))
            .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return MathsHelper.getSecondaryAxis(state.getValue(AXIS), state.getValue(SHAFT_ALONG_FIRST_AXIS));
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS) || face.getAxis() == getShaftAxis(state);
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        final Axis newAxis = transform.rotateAxis(state.getValue(AXIS));
        return state.setValue(AXIS, newAxis)
            .setValue(SHAFT_ALONG_FIRST_AXIS, MathsHelper.isSecondaryAxis(newAxis, transform.rotateAxis(getShaftAxis(state))));
    };
};
