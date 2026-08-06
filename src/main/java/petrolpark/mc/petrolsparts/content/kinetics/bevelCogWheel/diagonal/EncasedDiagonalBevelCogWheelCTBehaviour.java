package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;

public class EncasedDiagonalBevelCogWheelCTBehaviour extends EncasedCTBehaviour {

    public EncasedDiagonalBevelCogWheelCTBehaviour(CTSpriteShiftEntry shift) {
        super(shift);
    };

    public boolean isX(BlockState state) {
        final IDiagonalBevelCogWheelBlock block = (IDiagonalBevelCogWheelBlock)(state.getBlock());
        return block.getCogRotationAxisConnectedToFace(state, Direction.EAST) != null
            || block.getCogRotationAxisConnectedToFace(state, Direction.WEST) != null;
    };

    @Override
	protected boolean reverseUVs(BlockState state, Direction face) {
		return isX(state)
			&& face.getAxis().isHorizontal()
			&& face.getAxisDirection() == AxisDirection.POSITIVE;
	};

	@Override
	protected boolean reverseUVsVertically(BlockState state, Direction face) {
		if (isX(state) && face.getAxis() == Axis.Z)
			return face != Direction.SOUTH;
		return super.reverseUVsVertically(state, face);
	};
    
};
