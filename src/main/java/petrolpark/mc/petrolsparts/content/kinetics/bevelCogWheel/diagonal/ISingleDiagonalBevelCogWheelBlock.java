package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import com.simibubi.create.api.contraption.transformable.TransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public interface ISingleDiagonalBevelCogWheelBlock extends IBE<SingleDiagonalBevelCogWheelBlockEntity>, IRotate, TransformableBlock {
    
    public static final EnumProperty<Orientation> ORIENTATION = Orientation.EDGE_ORIENTATION_PROPERTY;
    public static final BooleanProperty FIRST_AXIS_SHAFT = BooleanProperty.create("first_axis_shaft");
    public static final BooleanProperty SECOND_AXIS_SHAFT = BooleanProperty.create("second_axis_shaft");

    @Override
    public default boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        final Orientation orientation = state.getValue(ORIENTATION);
        if (face == orientation.top.getOpposite()) return state.getValue(FIRST_AXIS_SHAFT);
        if (face == orientation.front.getOpposite()) return state.getValue(SECOND_AXIS_SHAFT);
        return false;
    };

    @Override
    public default Axis getRotationAxis(BlockState state) {
        return Axis.Y; // Unused
    };

    @Override
    public default BlockState transform(BlockState state, StructureTransform transform) {
        final Orientation initialOrientation = state.getValue(ORIENTATION);
        final Orientation newOrientation = initialOrientation.rotate(transform.rotationAxis, transform.rotation).mirror(transform.mirror).asEdge();
        final boolean axisOrderInverted = (initialOrientation == initialOrientation.asEdge()) != (newOrientation == newOrientation.asEdge());
        return state.setValue(ORIENTATION, newOrientation)
            .setValue(axisOrderInverted ? FIRST_AXIS_SHAFT : SECOND_AXIS_SHAFT, state.getValue(FIRST_AXIS_SHAFT))
            .setValue(axisOrderInverted ? SECOND_AXIS_SHAFT : FIRST_AXIS_SHAFT, state.getValue(SECOND_AXIS_SHAFT));
    };

    public default BlockState rotateDiagonalBevelCogWheel(BlockState state, Rotation direction) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, direction, Mirror.NONE));
    };

    public default BlockState mirrorDiagonalBevelCogWheel(BlockState state, Mirror mirror) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, Rotation.NONE, mirror));
    };

    @Override
    default Class<SingleDiagonalBevelCogWheelBlockEntity> getBlockEntityClass() {
        return SingleDiagonalBevelCogWheelBlockEntity.class;
    };

    @Override
    default BlockEntityType<? extends SingleDiagonalBevelCogWheelBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.SINGLE_DIAGONAL_BEVEL_COGWHEEL.get();
    };
};
