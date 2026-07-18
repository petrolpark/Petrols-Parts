package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.OppositeBevelCogWheelsAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.OppositeBevelCogWheelsBlock;

public class ThreeBevelCogWheelsBlock extends SimpleBevelCogWheelBlock implements IBE<SimpleBevelCogWheelBlockEntity> {
  
    public static final DirectionProperty EXCLUDED_FACE = BlockStateProperties.FACING;
    /**
     * Axis of the other two cogs
     */
    public static final BooleanProperty OTHER_COGS_ON_FIRST_AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;
    public static final BooleanProperty SHAFT = BooleanProperty.create("shaft");

    public ThreeBevelCogWheelsBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
        registerDefaultState(defaultBlockState()
            .setValue(SHAFT, false)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(EXCLUDED_FACE, OTHER_COGS_ON_FIRST_AXIS, SHAFT));
    };

    @Override
    public Collection<BevelCogWheelPart> getParts(BlockState state) {
        final List<BevelCogWheelPart> parts = new ArrayList<>(4);
        final Direction facing = state.getValue(EXCLUDED_FACE);
        parts.add(getSet().cogParts().get(facing.getOpposite()));
        if (state.getValue(SHAFT)) parts.add(getSet().shaftParts().get(facing.getAxis()));
        final Axis otherAxis = getRotationAxis(state);
        parts.add(getSet().cogParts().get(Direction.get(AxisDirection.POSITIVE, otherAxis)));
        parts.add(getSet().cogParts().get(Direction.get(AxisDirection.NEGATIVE, otherAxis)));
        return parts;
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        if (state.getValue(SHAFT)) return null;
        final Direction facing = state.getValue(EXCLUDED_FACE);
        final boolean firstAxis = state.getValue(OTHER_COGS_ON_FIRST_AXIS);
        final Axis perpendicularAxis = MathsHelper.getTertiaryAxis(facing.getAxis(), firstAxis);
        if (part == getSet().cogParts().get(facing))
            return getSet().fourBlock().getDefaultState().setValue(FourBevelCogWheelsBlock.EXCLUDED_AXIS, perpendicularAxis);
        else if (part == getSet().shaftParts().get(perpendicularAxis))
            return BlockHelper.copyAll(getSet().threeAndShaftBlock().getDefaultState(), state);
        else return null;
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        final Direction facing = state.getValue(EXCLUDED_FACE);
        final boolean hasShaft = state.getValue(SHAFT);

        return switch (part) {
            case BevelCogWheelPart.Cog cog -> {
                if (cog.face == facing.getOpposite()) {
                    yield (hasShaft ? getSet().oppositesAndShaftBlock().getDefaultState().setValue(OppositeBevelCogWheelsAndShaftBlock.SHAFT_ALONG_FIRST_AXIS, MathsHelper.isSecondaryAxis(getRotationAxis(state), facing.getAxis())) : getSet().oppositesBlock().getDefaultState())
                        .setValue(OppositeBevelCogWheelsBlock.AXIS, getRotationAxis(state))
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                } else {
                    final Axis otherAxis = getRotationAxis(state);
                    final Direction remainingCogFace = cog.face.getOpposite();
                    yield (switch (otherAxis) {
                        case X -> getSet().cornerBlock().getDefaultState()
                            .setValue(CornerBevelCogWheelsBlock.ORIENTATION, Orientation.fromTopAndFront(remainingCogFace, facing.getOpposite()))
                            .setValue(CornerBevelCogWheelsBlock.SHAFT, hasShaft ? CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS : CornerBevelCogWheelsBlock.ShaftType.NONE);
                        case Y -> getSet().cornerBlock().getDefaultState()
                            .setValue(CornerBevelCogWheelsBlock.ORIENTATION, Orientation.fromTopAndFront(facing.getOpposite(), remainingCogFace).asEdge())
                            .setValue(CornerBevelCogWheelsBlock.SHAFT, hasShaft ? facing.getAxis() == Axis.X ? CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS : CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS : CornerBevelCogWheelsBlock.ShaftType.NONE);
                        case Z -> getSet().cornerBlock().getDefaultState()
                            .setValue(CornerBevelCogWheelsBlock.ORIENTATION, Orientation.fromTopAndFront(facing.getOpposite(), remainingCogFace))
                            .setValue(CornerBevelCogWheelsBlock.SHAFT, hasShaft ? CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS : CornerBevelCogWheelsBlock.ShaftType.NONE);
                    }).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                }
            } case BevelCogWheelPart.Shaft shaft -> state.setValue(SHAFT, false);
        };
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return state.getValue(SHAFT) ? state.getValue(EXCLUDED_FACE).getAxis() : null;
    };

    /**
     * Axis perpendicular to the missing face
     */
    @Override
    public Axis getRotationAxis(BlockState state) {
        return MathsHelper.getSecondaryAxis(state.getValue(EXCLUDED_FACE).getAxis(), state.getValue(OTHER_COGS_ON_FIRST_AXIS));
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        final Direction facing = state.getValue(EXCLUDED_FACE);
        if (face == facing.getOpposite()) return true;
        if (face == facing) return state.getValue(SHAFT);
        return face.getAxis() == getRotationAxis(state);
    };

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return false;
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        final Direction side = Direction.get(AxisDirection.POSITIVE, getRotationAxis(state));
        if (side.getAxis() != transform.rotateFacing(transform.mirrorFacing(side)).getAxis()) state = state.cycle(OTHER_COGS_ON_FIRST_AXIS);
        return state.setValue(EXCLUDED_FACE, transform.rotateFacing(transform.mirrorFacing(state.getValue(EXCLUDED_FACE))));
    };

    @Override
    public Class<SimpleBevelCogWheelBlockEntity> getBlockEntityClass() {
        return SimpleBevelCogWheelBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends SimpleBevelCogWheelBlockEntity> getBlockEntityType() {
        return getSet().simpleBE().get();
    };
};