package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.IDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.CornerBevelCogWheelsBlock;

public interface ISingleDiagonalBevelCogWheelBlock extends IDiagonalBevelCogWheelBlock, IBE<SingleDiagonalBevelCogWheelBlockEntity> {
    
    public static final EnumProperty<Orientation> ORIENTATION = Orientation.EDGE_ORIENTATION_PROPERTY;
    public static final EnumProperty<CornerBevelCogWheelsBlock.ShaftType> SHAFT = CornerBevelCogWheelsBlock.SHAFT;
    public BevelCogWheelSet getSet();

    @Override
    public default boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        final Orientation orientation = state.getValue(ORIENTATION);
        return switch (state.getValue(SHAFT)) {
            case NONE -> false;
            case FIRST_AXIS -> face == orientation.top.getOpposite();
            case SECOND_AXIS -> face == orientation.front.getOpposite();
        };
    };

    @Override
    public default Axis getCogRotationAxisConnectedToFace(BlockState state, Direction face) {
        final Orientation orientation = state.getValue(ORIENTATION);
        if (face == orientation.top) return orientation.front.getAxis();
        if (face == orientation.front) return orientation.top.getAxis();
        return null;
    };

    @Override
    public default Axis getRotationAxis(BlockState state) {
        return Axis.Y; // Unused
    };

    @Override
    public default BlockState transform(BlockState state, StructureTransform transform) {
        final Orientation initialOrientation = state.getValue(ORIENTATION);
        final Orientation newOrientation = initialOrientation.mirror(transform.mirror).rotate(transform.rotationAxis, transform.rotation);
        final boolean axisOrderInverted = (newOrientation != newOrientation.asEdge());
        return state.setValue(ORIENTATION, newOrientation.asEdge())
            .setValue(SHAFT, switch (state.getValue(SHAFT)) {
                case NONE -> CornerBevelCogWheelsBlock.ShaftType.NONE;
                case FIRST_AXIS -> axisOrderInverted ? CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS : CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS;
                case SECOND_AXIS -> axisOrderInverted ? CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS : CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS;
            });
    };

    public default BlockState rotateDiagonalBevelCogWheel(BlockState state, Rotation direction) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, direction, Mirror.NONE));
    };

    public default BlockState mirrorDiagonalBevelCogWheel(BlockState state, Mirror mirror) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, Rotation.NONE, mirror));
    };

    @Override
    public default Class<SingleDiagonalBevelCogWheelBlockEntity> getBlockEntityClass() {
        return SingleDiagonalBevelCogWheelBlockEntity.class;
    };

    @Override
    public default BlockEntityType<? extends SingleDiagonalBevelCogWheelBlockEntity> getBlockEntityType() {
        return getSet().singleDiagonalBE().get();
    };
};
