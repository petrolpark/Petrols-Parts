package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractCornerShaftBlock extends DirectionalAxisKineticBlock implements IBE<CornerShaftBlockEntity> {

    protected final Supplier<CornerShaftSet> set;

    public AbstractCornerShaftBlock(Supplier<CornerShaftSet> set, BlockBehaviour.Properties properties) {
        super(properties);
        this.set = set;
    };

    public CornerShaftSet getSet() {
        return set.get();
    };

    @Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction1 = context.getClickedFace().getOpposite();
        Direction direction2;
        if (direction1.getAxis() == Axis.Y) {
            direction2 = context.getHorizontalDirection();
        } else {
            direction2 = context.getNearestLookingVerticalDirection();
        };
        return getBlockstateConnectingDirections(direction1, direction2);
	};

    public static boolean hasShaftTowards(BlockState state, Direction face) {
        return Arrays.asList(getDirectionsConnectedByState(state)).contains(face);
    };

    @Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return hasShaftTowards(state, face);
	};

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return transform(originalState, new StructureTransform(new BlockPos(0, 0, 0), targetedFace.getAxis(), Rotation.CLOCKWISE_90, Mirror.NONE));
    };

    /**
     * XYZXYZ
     * Considering the above list, with the Axis of {@code facing} excluded:<ul>
     * <li>If {@code axisAlongFirst} is true, the shaft connects {@code facing} and the Axis to the right in the same direction.</li>
     * <li>If {@code axisAlongFirst} is false, the shaft connects {@code facing} and the Axis to the left in the opposite direction.</li>
     * </ul>
     */
    /*
     * Direction axisAlongFirst Other Direction
     * -Z NORTH  true           -X WEST
     * -Z NORTH  false          +Y UP
     * +Z SOUTH  true           +X EAST
     * +Z SOUTH  false          -Y DOWN
     * +X EAST   true           +Y UP
     * +X EAST   false          -Z NORTH
     * -X WEST   true           -Y DOWN
     * -X WEST   false          +Z SOUTH
     * +Y UP     true           +Z SOUTH
     * +Y UP     false          -X WEST
     * -Y DOWN   true           -Z NORTH
     * -Y DOWN   false          +Z EAST
     */
    public static Direction[] getDirectionsConnectedByState(BlockState state) {
        if (state.getBlock() instanceof AbstractShaftBlock) return new Direction[]{Direction.get(AxisDirection.POSITIVE, state.getValue(ShaftBlock.AXIS)), Direction.get(AxisDirection.NEGATIVE, state.getValue(ShaftBlock.AXIS))};
        if (!(state.getBlock() instanceof AbstractCornerShaftBlock)) return new Direction[]{};
        final Direction facing = state.getValue(DirectionalKineticBlock.FACING);
        final boolean axisAlongFirst = state.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
        final Axis secondDirectionAxis;
        switch (facing.getAxis()) {
            case X:
                secondDirectionAxis = axisAlongFirst ? Axis.Y : Axis.Z;
                break;
            case Y:
                secondDirectionAxis = axisAlongFirst ? Axis.Z : Axis.X;
                break;
            case Z:
                secondDirectionAxis = axisAlongFirst ? Axis.X : Axis.Y;
                break;
            default:
                throw new IllegalStateException("Unknown axis");
        };
        return new Direction[]{facing, Direction.fromAxisAndDirection(secondDirectionAxis, facing.getAxisDirection() == AxisDirection.POSITIVE ^ axisAlongFirst ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE)};
    };

    public BlockState getBlockstateConnectingDirections(Direction direction1, Direction direction2) {
        if (direction1.getAxis() == direction2.getAxis()) return getSet().shaftBlock().getDefaultState().setValue(ShaftBlock.AXIS, direction1.getAxis());
        final boolean axisAlongFirst = (direction1.getAxisDirection() == direction2.getAxisDirection());
        final Map<Axis, Direction> directionsForEachAxis = Map.of(direction1.getAxis(), direction1, direction2.getAxis(), direction2);
        final List<Axis> axes = new ArrayList<>();
        axes.addAll(List.of(Axis.values()));
        axes.remove(direction1.getAxis());
        axes.remove(direction2.getAxis());
        final Axis primaryAxis;
        switch (axes.get(0)) {
            case X:
                primaryAxis = axisAlongFirst ? Axis.Y : Axis.Z;
                break;
            case Y:
                primaryAxis = axisAlongFirst ? Axis.Z : Axis.X;
                break;
            case Z:
                primaryAxis = axisAlongFirst ? Axis.X : Axis.Y;
                break;
            default:
                throw new IllegalStateException("Unknown axis");
        };
        return defaultBlockState().setValue(DirectionalKineticBlock.FACING, directionsForEachAxis.get(primaryAxis)).setValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE, axisAlongFirst);
    };

    public static final boolean isPositiveDirection(Direction direction) {
        return Direction.get(AxisDirection.POSITIVE, direction.getAxis()) == direction;
    };

    @Override
	public BlockState rotate(BlockState state, Rotation rot) {
		Direction[] directions = getDirectionsConnectedByState(state);
        return getBlockstateConnectingDirections(rot.rotate(directions[0]), rot.rotate(directions[1]));
	};

    @Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		Direction[] directions = getDirectionsConnectedByState(state);
        return getBlockstateConnectingDirections(mirror.getRotation(directions[0]).rotate(directions[0]), mirror.getRotation(directions[1]).rotate(directions[1]));
	};

    @Override
	public BlockState transform(BlockState state, StructureTransform transform) {
		Direction[] directions = getDirectionsConnectedByState(state);
        return getBlockstateConnectingDirections(transform.mirrorFacing(transform.rotateFacing(directions[0])), transform.mirrorFacing(transform.rotateFacing(directions[1])));
	};

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return false;
    };

    @Override
    public Class<CornerShaftBlockEntity> getBlockEntityClass() {
        return CornerShaftBlockEntity.class;
    };
    
};
