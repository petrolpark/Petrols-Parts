package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.entity.ISplitShaftKineticBlockEntity;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.IDiagonalBevelCogWheelBlockEntity;

public class SingleDiagonalBevelCogWheelBlockEntity extends KineticBlockEntity implements IDiagonalBevelCogWheelBlockEntity, ISplitShaftKineticBlockEntity {

    public SingleDiagonalBevelCogWheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public Orientation getOrientation() {
        return getBlockState().getValue(ISingleDiagonalBevelCogWheelBlock.ORIENTATION);
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        return propagateRotationToCogWheel(target, stateTo, diff);
    };

    @Override
    public float getRotationSpeedModifier(Direction face) {
        final Orientation orientation = getOrientation();
        final boolean flips = orientation.top.getAxisDirection() == orientation.front.getAxisDirection();

        if (face == orientation.top.getOpposite()) // First Axis Shaft: coaxial with the Cog mesh at the front face
            return orientation.front.getAxisDirection() == AxisDirection.POSITIVE ^ flips ? -2f : 2f;
        if (face == orientation.front.getOpposite()) // Second Axis Shaft: coaxial with the Cog mesh at the top face
            return orientation.top.getAxisDirection() == AxisDirection.POSITIVE ^ flips ? -2f : 2f;

        return 1f;
    };

};
