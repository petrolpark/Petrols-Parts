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
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.CornerBevelCogWheelsBlock;

public class BevelCogWheelAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty SHAFT_ON_FIRST_AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

    public BevelCogWheelAndShaftBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
    };
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, SHAFT_ON_FIRST_AXIS));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        return List.of(
            getSet().singleAxisBlock().get().get(state.getValue(FACING)),
            getSet().shaftBlock().getDefaultState().setValue(ShaftBlock.AXIS, getShaftAxis(state))
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        return (part instanceof BevelCogWheelPart.Cog
            ? getSet().shaftBlock().getDefaultState().setValue(ShaftBlock.AXIS, getShaftAxis(state))
            : getSet().singleAxisBlock().get().get(state.getValue(FACING))
        )
            .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        if (!(part instanceof BevelCogWheelPart.Cog cog)) return null;
        final Direction facing = state.getValue(FACING);
        if (cog.face == facing.getOpposite()) {
            return getSet().oppositesAndShaftBlock().getDefaultState()
                .setValue(OppositeBevelCogWheelsAndShaftBlock.AXIS, facing.getAxis())
                .setValue(OppositeBevelCogWheelsAndShaftBlock.SHAFT_ALONG_FIRST_AXIS, state.getValue(SHAFT_ON_FIRST_AXIS))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else if (cog.face.getAxis() == getShaftAxis(state)) {
            return getSet().cornerBlock().getDefaultState()
                .setValue(CornerBevelCogWheelsBlock.ORIENTATION, Orientation.fromTopAndFront(facing, cog.face).asEdge())
                .setValue(CornerBevelCogWheelsBlock.SHAFT, cog.face.getAxis().ordinal() < facing.getAxis().ordinal() ? CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS : CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS)
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else {
            return getSet().cornerAndShaftBlock().getDefaultState()
                .setValue(CornerBevelCogWheelsAndShaftBlock.ORIENTATION, Orientation.fromTopAndFront(facing, cog.face).asEdge())
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        }
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
        final Direction newFacing = transform.rotateFacing(transform.mirrorFacing(state.getValue(FACING)));
        return state.setValue(FACING, newFacing)
            .setValue(SHAFT_ON_FIRST_AXIS, MathsHelper.isSecondaryAxis(newFacing.getAxis(), transform.rotateAxis(getShaftAxis(state))));
    };
    
};
