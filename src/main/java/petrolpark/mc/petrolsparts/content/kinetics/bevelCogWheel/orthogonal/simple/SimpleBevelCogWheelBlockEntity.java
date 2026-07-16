package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.Objects;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.IOrthogonalBevelCogWheelBlock;

public class SimpleBevelCogWheelBlockEntity extends KineticBlockEntity {

    public SimpleBevelCogWheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        return propagateRotationTo(stateFrom, diff, connectedViaAxes);
    };

    public static final float propagateRotationTo(BlockState stateFrom, BlockPos diff, boolean connectedViaAxes) {
        if (!connectedViaAxes) return 0f;
        final Axis shaftAxis;
        if (stateFrom.getBlock() instanceof IOrthogonalBevelCogWheelBlock block) {
            shaftAxis = block.getShaftAxis(stateFrom);
        } else if (stateFrom.getBlock() instanceof AbstractShaftBlock) {
            shaftAxis = stateFrom.getValue(AbstractShaftBlock.AXIS);
        } else {
            shaftAxis = null;
        };
        final Direction face = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) return 0f; // Should never fail
        if (Objects.equals(face.getAxis(), shaftAxis)) return 1f;
        return face.getAxisDirection() == AxisDirection.POSITIVE ? 1f : -1f; //TODO check
    };
    
};
