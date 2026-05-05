package com.petrolpark.petrolsparts.core.block.entity;

import com.google.common.util.concurrent.AtomicDouble;
import com.petrolpark.petrolsparts.PetrolsPartsTags;
import com.petrolpark.petrolsparts.core.block.CogType;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public interface IFaceAlignedCogWheelBlockEntity {

    public static CogType getCogType(KineticBlockEntity kbe, Direction face) {
        if (kbe instanceof IFaceAlignedCogWheelBlockEntity cogwheel) return cogwheel.getCogType(face);
        if (kbe.getBlockState().is(PetrolsPartsTags.THICK_SMALL_COGWHEELS) &&
            kbe.getBlockState().hasProperty(BlockStateProperties.AXIS) &&
            face.getAxis() == kbe.getBlockState().getValue(BlockStateProperties.AXIS)
        ) return CogType.SMALL;
        return CogType.NONE;
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
                if (fromCog.isLarge() && toCog.isSmall()) if (trySetRatio(from, ratio, -2d)) return 0f;
                if (fromCog.isSmall() && toCog.isLarge()) if (trySetRatio(from, ratio, -0.5d)) return 0f;
            };

            // Small -> Small
            if (diff.distManhattan(Vec3i.ZERO) == 1 && Direction.getNearest(diff.getX(), diff.getY(), diff.getZ()).getAxis() != face.getAxis() && fromCog.isSmall() && toCog.isSmall()) {
                if (trySetRatio(from, ratio, -1d)) return 0f;
            };

        };

        // Also check for non-face aligned Cogwheels in case there is a conflict
        if (stateFrom.getBlock() instanceof IRotate rotateFrom && stateTo.getBlock() instanceof IRotate rotateTo && rotateFrom.getRotationAxis(stateFrom) == rotateTo.getRotationAxis(stateTo)) {
            final Axis axis = rotateFrom.getRotationAxis(stateFrom);

            // Large -> Small (same axis)
            if (isLargeToSmallCog(axis, diff)) {
                if (ICogWheel.isLargeCog(stateFrom) && ICogWheel.isSmallCog(stateTo)) if (trySetRatio(from, ratio, -2d)) return 0f;
                if (ICogWheel.isSmallCog(stateFrom) && ICogWheel.isLargeCog(stateTo)) if (trySetRatio(from, ratio, -0.5d)) return 0f;
            };

            // Small -> Small
            if (diff.distManhattan(Vec3i.ZERO) == 1 && Direction.getNearest(diff.getX(), diff.getY(), diff.getZ()).getAxis() != axis && connectedViaCogs) {
                if (trySetRatio(from, ratio, -1d)) return 0f;
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
    static boolean trySetRatio(KineticBlockEntity from, AtomicDouble ratio, double newRatio) {
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
