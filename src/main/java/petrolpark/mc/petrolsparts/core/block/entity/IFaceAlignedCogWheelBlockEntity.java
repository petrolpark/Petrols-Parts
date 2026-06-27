package petrolpark.mc.petrolsparts.core.block.entity;

import com.google.common.util.concurrent.AtomicDouble;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity.CompositeKineticBlockEntityPart;
import petrolpark.mc.petrolsparts.PetrolsPartsTags;
import petrolpark.mc.petrolsparts.core.block.CogType;

public interface IFaceAlignedCogWheelBlockEntity {

    public static CogType getPossibleCogType(BlockEntity be, Direction face) {
        if (be instanceof KineticBlockEntity kbe) return getCogType(kbe, face);
        if (be instanceof CompositeKineticBlockEntity ckbe) {
            for (CompositeKineticBlockEntityPart part : ckbe.getParts()) {
                final CogType cogType = getCogType(part, face);
                if (!cogType.isNone()) return cogType;
            };
        };
        return CogType.NONE;
    };

    public static CogType getCogType(KineticBlockEntity kbe, Direction face) {
        if (kbe instanceof IFaceAlignedCogWheelBlockEntity cogwheel) return cogwheel.getCogType(face);

        if (kbe instanceof MillstoneBlockEntity) return face == Direction.UP ? CogType.SMALL : CogType.NONE;

        // Pumps and some Gears 'n' Kinetics blocks
        if (
            kbe.getBlockState().getBlock() instanceof IRotate rotate &&
            face.getAxis() == rotate.getRotationAxis(kbe.getBlockState())
        ) {
            if (kbe.getBlockState().is(PetrolsPartsTags.THICK_SMALL_COGWHEELS)) return CogType.SMALL;
            if (kbe.getBlockState().is(PetrolsPartsTags.THICK_LARGE_COGWHEELS)) return CogType.LARGE;
        };

        return CogType.NONE;
    };

    public static boolean isValidFaceAlignedCogwheelPosition(boolean large, LevelReader worldIn, BlockPos pos, Direction cogFace) {
		for (Direction perpFace : Iterate.directions) {

            final BlockPos offsetPos = pos.relative(perpFace);
			final BlockState offsetState = worldIn.getBlockState(offsetPos);

            if (large && CompositeKineticBlockEntity.streamAny(worldIn, offsetPos.relative(cogFace)).anyMatch(kbe -> getCogType(kbe, perpFace.getOpposite()) == CogType.LARGE)) return false;

			if (!(offsetState.getBlock() instanceof IRotate rotate)) continue;
            final Axis axis = rotate.getRotationAxis(offsetState);
            
			if (perpFace.getAxis() == cogFace.getAxis()) {

                if (perpFace == cogFace &&
                    axis != cogFace.getAxis() && (
                        ICogWheel.isLargeCog(offsetState) ||
                        CompositeKineticBlockEntity.streamAny(worldIn, offsetPos).anyMatch(kbe -> 
                            getCogType(kbe, Direction.get(AxisDirection.POSITIVE, axis)) != CogType.NONE ||
                            getCogType(kbe, Direction.get(AxisDirection.NEGATIVE, axis)) != CogType.NONE
                        )
                    )
                ) return false;

                continue;
            };

            if (CompositeKineticBlockEntity.streamAny(worldIn, offsetPos).anyMatch(kbe -> {
                
                if (axis == cogFace.getAxis()) { // Same axis, large Cogwheel directly next to another
                    final CogType cogType = getCogType(kbe, cogFace);
                    if (cogType.isLarge() || (large && cogType.isSmall())) return true;
                } else if (axis == perpFace.getAxis()) {
                    if (large && getCogType(kbe, perpFace.getOpposite()) != CogType.NONE) return true;
                } else {
                    final CogType rightAngleUpperCogType = getCogType(kbe, Direction.get(AxisDirection.POSITIVE, axis));
                    final CogType rightAngleLowerCogType = getCogType(kbe, Direction.get(AxisDirection.NEGATIVE, axis));
                    if (rightAngleUpperCogType.isLarge() || rightAngleLowerCogType.isLarge() || ICogWheel.isLargeCog(offsetState)) return true;
                    if (large && rightAngleUpperCogType.isSmall() || rightAngleLowerCogType.isSmall() || ICogWheel.isSmallCog(offsetState)) return true;
                };

                return false;
            })) return false;
		};
		return true;
	};
    
    public CogType getCogType(Direction face);

    public static float propagateFaceAlignedCogwheels(KineticBlockEntity from, KineticBlockEntity to, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {

        final AtomicDouble ratio = new AtomicDouble();

        for (Direction face : Iterate.directions) {
            final CogType fromCog = getCogType(from, face);
            if (fromCog.isNone()) continue;
            final CogType toCog = getCogType(to, face);
            if (toCog.isNone()) continue;

            // Large -> Small (same axis)
            if (isLargeToSmallCog(face.getAxis(), diff)) {
                if (fromCog.isLarge() && toCog.isSmall()) if (cannotSetRatio(from, ratio, -2d)) return 0f;
                if (fromCog.isSmall() && toCog.isLarge()) if (cannotSetRatio(from, ratio, -0.5d)) return 0f;
            };

            // Small -> Small
            if (diff.distManhattan(Vec3i.ZERO) == 1 && Direction.getNearest(diff.getX(), diff.getY(), diff.getZ()).getAxis() != face.getAxis() && fromCog.isSmall() && toCog.isSmall()) {
                if (cannotSetRatio(from, ratio, -1d)) return 0f;
            };

        };

        // Also check for non-face aligned Cogwheels in case there is a conflict
        if (stateFrom.getBlock() instanceof IRotate rotateFrom && stateTo.getBlock() instanceof IRotate rotateTo && rotateFrom.getRotationAxis(stateFrom) == rotateTo.getRotationAxis(stateTo)) {
            final Axis axis = rotateFrom.getRotationAxis(stateFrom);

            // Large -> Small (same axis)
            if (isLargeToSmallCog(axis, diff)) {
                if (ICogWheel.isLargeCog(stateFrom) && ICogWheel.isSmallCog(stateTo)) if (cannotSetRatio(from, ratio, -2d)) return 0f;
                if (ICogWheel.isSmallCog(stateFrom) && ICogWheel.isLargeCog(stateTo)) if (cannotSetRatio(from, ratio, -0.5d)) return 0f;
            };

            // Small -> Small
            if (diff.distManhattan(Vec3i.ZERO) == 1 && Direction.getNearest(diff.getX(), diff.getY(), diff.getZ()).getAxis() != axis && connectedViaCogs) {
                if (cannotSetRatio(from, ratio, -1d)) return 0f;
            };
        };

        return ratio.floatValue();
    };

    /**
     * @param from
     * @param ratio
     * @param newRatio
     * @return {@code true} if different Cogwheel components of this block rotate at different speeds
     */
    static boolean cannotSetRatio(KineticBlockEntity from, AtomicDouble ratio, double newRatio) {
        double set = ratio.getAndSet(newRatio);
        if (set != 0d && set != newRatio) {
            from.getLevel().destroyBlock(from.getBlockPos(), true); // Incompatible
            return true;
        };
        return false;
    };

    public static boolean isLargeToSmallCog(Axis axisFrom, BlockPos diff) {
		if (axisFrom.choose(diff.getX(), diff.getY(), diff.getZ()) != 0) return false;
		for (Axis axis : Axis.values()) {
			if (axis == axisFrom) continue;
			if (Math.abs(axis.choose(diff.getX(), diff.getY(), diff.getZ())) != 1) return false;
		}
		return true;
	}
};
