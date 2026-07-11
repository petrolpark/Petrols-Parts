package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.util.Orientation;

public class SingleDiagonalBevelCogWheelBlockEntity extends KineticBlockEntity {

    public SingleDiagonalBevelCogWheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        final Orientation orientation = stateFrom.getValue(SingleDiagonalBevelCogWheelBlock.ORIENTATION);
        // TODO shafts
        if (!ICogWheel.isSmallCog(stateTo) || !(stateTo.getBlock() instanceof IRotate rotateTo)) return 0f;
        
        if (orientation.top.getNormal().equals(diff) && rotateTo.getRotationAxis(stateTo) == orientation.front.getAxis())
            return -1f;
        if (orientation.front.getNormal().equals(diff) && rotateTo.getRotationAxis(stateTo) == orientation.top.getAxis())
            return -1f; 
      
        return 0f;
    };
    
};
