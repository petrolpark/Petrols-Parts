package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.IDiagonalBevelCogWheelBlockEntity;

public class SingleDiagonalBevelCogWheelBlockEntity extends KineticBlockEntity implements IDiagonalBevelCogWheelBlockEntity {

    public SingleDiagonalBevelCogWheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public Orientation getOrientation() {
        return getBlockState().getValue(ISingleDiagonalBevelCogWheelBlock.ORIENTATION);
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        //TODO shafts
        return propagateRotationToCogWheel(target, stateTo, diff);
    };
    
};
