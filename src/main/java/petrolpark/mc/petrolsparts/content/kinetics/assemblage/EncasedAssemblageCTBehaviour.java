package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import javax.annotation.Nullable;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import net.createmod.catnip.data.Couple;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.PetrolsPartsSpriteShifts;
import petrolpark.mc.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;

/**
 * Largely copied from {@link EncasedCogCTBehaviour}
 */
public class EncasedAssemblageCTBehaviour extends EncasedCTBehaviour {

    public static final EncasedAssemblageCTBehaviour ANDESITE = new EncasedAssemblageCTBehaviour(AllSpriteShifts.ANDESITE_CASING, PetrolsPartsSpriteShifts.CLOSED_ANDESITE_ENCASED_COGWHEEL_SIDE_VERTICAL, PetrolsPartsSpriteShifts.CLOSED_ANDESITE_ENCASED_COGWHEEL_SIDE_HORIZONTAL, PetrolsPartsSpriteShifts.OPEN_ANDESITE_ENCASED_COGWHEEL_SIDE_VERTICAL, PetrolsPartsSpriteShifts.OPEN_ANDESITE_ENCASED_COGWHEEL_SIDE_HORIZONTAL);

    protected final CTSpriteShiftEntry endShift;
    protected final Couple<CTSpriteShiftEntry> closedSideShifts;
    protected final Couple<CTSpriteShiftEntry> openSideShifts;
    
    public EncasedAssemblageCTBehaviour(
        CTSpriteShiftEntry endShift,
        CTSpriteShiftEntry closedSideVerticalShift, CTSpriteShiftEntry closedSideHorizontalShift,
        CTSpriteShiftEntry openSideVerticalShift, CTSpriteShiftEntry openSideHorizontalShift
    ) {
        super(endShift);
        this.endShift = endShift;
        this.closedSideShifts = Couple.create(closedSideVerticalShift, closedSideHorizontalShift);
        this.openSideShifts = Couple.create(openSideVerticalShift, openSideHorizontalShift);
    };

    @Override
	public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
		final Axis axis = state.getValue(IAssemblageBlock.AXIS);
		if (axis == face.getAxis()) return super.connectsTo(state, other, reader, pos, otherPos, face);

		if (
            state.getBlock() instanceof EncasedAssemblageBlock encasedAssemblage && ( // Should always pass
                state.getValue(IAssemblageBlock.TOP_COG).isLarge() ||
                state.getValue(IAssemblageBlock.MIDDLE_COG).isLarge() ||
                state.getValue(IAssemblageBlock.BOTTOM_COG).isLarge() || (
                    other.getBlock() instanceof EncasedAssemblageBlock otherEncasedAssemblage &&
                    encasedAssemblage.getCasing() == otherEncasedAssemblage.getCasing() && 
                    axis == other.getValue(IAssemblageBlock.AXIS)
                )
            ) 
        ) return true;

		final BlockState diagonalState = reader.getBlockState(otherPos.relative(face));
        final BlockEntity diagonalBE = reader.getBlockEntity(otherPos.relative(face));
		if (
            diagonalState.getBlock() instanceof IRotate rotate && rotate.getRotationAxis(diagonalState) == axis && (
                (ICogWheel.isLargeCog(diagonalState) && ICogWheel.isSmallCog(state)) ||
                (IFaceAlignedCogWheelBlockEntity.getPossibleCogType(diagonalBE, Direction.get(AxisDirection.POSITIVE, axis)).isLarge() && state.getValue(IAssemblageBlock.TOP_COG).isSmall()) ||
                (IFaceAlignedCogWheelBlockEntity.getPossibleCogType(diagonalBE, Direction.get(AxisDirection.NEGATIVE, axis)).isLarge() && state.getValue(IAssemblageBlock.BOTTOM_COG).isSmall())
            )
        )  return true;

        return false;
	};

	@Override
	protected boolean reverseUVs(BlockState state, Direction face) {
		return 
            state.getValue(IAssemblageBlock.AXIS).isHorizontal() &&
            face.getAxis().isHorizontal() &&
            face.getAxisDirection() == AxisDirection.POSITIVE;
	};

	@Override
	protected boolean reverseUVsVertically(BlockState state, Direction face) {
		if (state.getValue(IAssemblageBlock.AXIS) == Axis.X && face.getAxis() == Axis.Z) return face != Direction.SOUTH;
		return super.reverseUVsVertically(state, face);
	};

	@Override
	protected boolean reverseUVsHorizontally(BlockState state, Direction face) {
		if (state.getValue(IAssemblageBlock.AXIS).isVertical() && face.getAxis().isHorizontal()) return true;
		if (state.getValue(IAssemblageBlock.AXIS) == Axis.Z && face == Direction.DOWN) return true;
		return super.reverseUVsHorizontally(state, face);
	};

	@Override
	public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
		final Axis axis = state.getValue(IAssemblageBlock.AXIS);
		if (axis == direction.getAxis()) {
			if (axis == direction.getAxis() && state.getBlock() instanceof EncasedAssemblageBlock assemblage && assemblage.hasShaftExposed(state, direction.getAxisDirection() == AxisDirection.POSITIVE)) return null;
			return super.getShift(state, direction, sprite);
		};
		return (closedSideShifts.either(shift -> shift.getOriginal().equals(sprite)) ? closedSideShifts : openSideShifts).get(axis == Axis.X || axis == Axis.Z && direction.getAxis() == Axis.X);
	};

    public CTSpriteShiftEntry getEndShift() {
        return endShift;
    };
};
