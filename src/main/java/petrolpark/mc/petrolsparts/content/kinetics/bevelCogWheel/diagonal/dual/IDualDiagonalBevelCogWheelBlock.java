package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.IDiagonalBevelCogWheelBlock;

public interface IDualDiagonalBevelCogWheelBlock extends IDiagonalBevelCogWheelBlock, IBE<DualDiagonalBevelCogWheelBlockEntity> {
    
    public static final EnumProperty<Axis> EXCLUDED_AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty FACE_PARITY = BooleanProperty.create("face_parity");

    public static Orientation[] getCogOrientations(BlockState state) {
        final Axis axis1 = state.getValue(EXCLUDED_AXIS) == Axis.X ? Axis.Y : Axis.X;
        final Axis axis2 = state.getValue(EXCLUDED_AXIS) == Axis.Z ? Axis.Y : Axis.Z;
        return new Orientation[]{
            Orientation.fromTopAndFront(Direction.get(AxisDirection.POSITIVE, axis1), Direction.get(state.getValue(FACE_PARITY) ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, axis2)),
            Orientation.fromTopAndFront(Direction.get(AxisDirection.NEGATIVE, axis1), Direction.get(state.getValue(FACE_PARITY) ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE, axis2))
        };
    };

    @Override
    public default Axis getCogRotationAxisConnectedToFace(BlockState state, Direction face) {
        final Axis axis = state.getValue(EXCLUDED_AXIS);
        if (face.getAxis() == axis) return null;
        return face.getClockWise(axis).getAxis();
    };

    @Override
    public default BlockState transform(BlockState state, StructureTransform transform) {
        final Direction facing = Direction.fromAxisAndDirection(state.getValue(EXCLUDED_AXIS), AxisDirection.POSITIVE);
        final Direction newFacing = transform.rotateFacing(transform.mirrorFacing(facing));
        if (newFacing.getAxisDirection() == AxisDirection.NEGATIVE) state = state.cycle(FACE_PARITY);
        return state.setValue(EXCLUDED_AXIS, newFacing.getAxis());
    };

    public default BlockState rotateDiagonalBevelCogWheel(BlockState state, Rotation direction) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, direction, Mirror.NONE));
    };

    public default BlockState mirrorDiagonalBevelCogWheel(BlockState state, Mirror mirror) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, Rotation.NONE, mirror));
    };

    @Override
    public default Class<DualDiagonalBevelCogWheelBlockEntity> getBlockEntityClass() {
        return DualDiagonalBevelCogWheelBlockEntity.class;
    };

    @Override
    public default BlockEntityType<? extends DualDiagonalBevelCogWheelBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.DUAL_DIAGONAL_BEVEL_COGWHEEL.get();
    };
};
