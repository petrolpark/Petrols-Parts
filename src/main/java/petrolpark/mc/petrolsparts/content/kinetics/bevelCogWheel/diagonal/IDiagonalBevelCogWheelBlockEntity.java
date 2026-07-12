package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.util.Orientation;

public interface IDiagonalBevelCogWheelBlockEntity {
    
    public Orientation getOrientation();

    public default float propagateRotationToCogWheel(KineticBlockEntity target, BlockState stateTo, BlockPos diff) {
        final Orientation orientation = getOrientation();

        if (!ICogWheel.isSmallCog(stateTo) || !(stateTo.getBlock() instanceof IRotate rotateTo)) return 0f;
        
        final boolean flips = orientation.top.getAxisDirection() == orientation.front.getAxisDirection();

        if (orientation.top.getNormal().equals(diff) && rotateTo.getRotationAxis(stateTo) == orientation.front.getAxis())
            return orientation.top.getAxisDirection() == AxisDirection.POSITIVE ^ flips ? 1f : -1f;
        if (orientation.front.getNormal().equals(diff) && rotateTo.getRotationAxis(stateTo) == orientation.top.getAxis())
            return orientation.front.getAxisDirection() == AxisDirection.POSITIVE ^ flips ? 1f : -1f;
      
        return 0f;
    };
};
