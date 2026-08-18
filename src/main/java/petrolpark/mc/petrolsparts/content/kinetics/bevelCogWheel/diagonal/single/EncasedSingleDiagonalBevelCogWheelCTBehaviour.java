package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.util.Orientation;

public class EncasedSingleDiagonalBevelCogWheelCTBehaviour extends EncasedCTBehaviour {

    public EncasedSingleDiagonalBevelCogWheelCTBehaviour(CTSpriteShiftEntry shift) {
        super(shift);
    };

    @Override
    protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Direction result = actualUVDirection(state, face, true);
        if (face.getAxisDirection() == AxisDirection.POSITIVE) result = result.getOpposite();
        if (face == Direction.DOWN) result = result.getOpposite();
        return result;
    };

    @Override
    protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Direction result = actualUVDirection(state, face, false);
        if (face == Direction.DOWN) result = result.getOpposite();
        return result;
    };

    private static Orientation getAppliedOrientation(BlockState state) {
        final Orientation orientation = state.getValue(ISingleDiagonalBevelCogWheelBlock.ORIENTATION);
        return switch (state.getValue(ISingleDiagonalBevelCogWheelBlock.SHAFT)) {
            case NONE -> orientation.asEdgeBlockStateRotation();
            case FIRST_AXIS -> orientation;
            case SECOND_AXIS -> Orientation.fromTopAndFront(orientation.front, orientation.top);
        };
    };

    private static Direction defaultUVDirection(Direction face, boolean horizontal) {
        if (horizontal) {
            Direction result = face.getAxis() == Axis.X ? Direction.SOUTH : Direction.WEST;
            if (face.getAxisDirection() == AxisDirection.POSITIVE) result = result.getOpposite();
            if (face == Direction.DOWN) result = result.getOpposite();
            return result;
        } else {
            Direction result = face.getAxis().isHorizontal() ? Direction.UP : Direction.NORTH;
            if (face == Direction.DOWN) result = result.getOpposite();
            return result;
        }
    };

    private static Direction actualUVDirection(BlockState state, Direction face, boolean horizontal) {
        final Orientation applied = getAppliedOrientation(state);
        return applied.rotateByBlockState(defaultUVDirection(applied.unrotateByBlockState(face), horizontal));
    };

};
