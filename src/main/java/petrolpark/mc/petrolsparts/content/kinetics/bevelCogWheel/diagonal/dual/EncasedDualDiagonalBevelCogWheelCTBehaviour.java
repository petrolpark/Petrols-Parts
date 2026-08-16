package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual;

import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;

public class EncasedDualDiagonalBevelCogWheelCTBehaviour extends EncasedCTBehaviour {

    public EncasedDualDiagonalBevelCogWheelCTBehaviour(CTSpriteShiftEntry shift) {
        super(shift);
    };

    @Override
	protected boolean reverseUVs(BlockState state, Direction face) {
		final Axis excludedAxis = state.getValue(IDualDiagonalBevelCogWheelBlock.EXCLUDED_AXIS);
		return excludedAxis.isHorizontal()
			&& face.getAxis().isHorizontal()
			&& (face.getAxisDirection() == AxisDirection.POSITIVE) == (excludedAxis == Axis.X);
	};
    
};
