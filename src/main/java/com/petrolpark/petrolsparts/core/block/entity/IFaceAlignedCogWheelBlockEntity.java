package com.petrolpark.petrolsparts.core.block.entity;

import com.petrolpark.petrolsparts.PetrolsPartsTags;
import com.petrolpark.petrolsparts.core.block.CogType;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
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

    public static float propagateFaceAlignedCogwheels(KineticBlockEntity from, KineticBlockEntity to, BlockPos diff) {

        for (Direction face : Iterate.directions) {
            final CogType fromCog = getCogType(from, face);
            if (fromCog.isNone()) continue;
            final CogType toCog = getCogType(to, face);
            if (toCog.isNone()) continue;

            // Large -> Small (same axis)
            if (isLargeToSmallCog(face.getAxis(), diff)) {
                if (fromCog.isLarge() && toCog.isSmall()) return -2f;
                if (fromCog.isSmall() && toCog.isLarge()) return -0.5f;
            };

            // Small -> Small
            if (diff.distManhattan(Vec3i.ZERO) == 1 && Direction.getNearest(diff.getX(), diff.getY(), diff.getZ()).getAxis() != face.getAxis()) {
                return fromCog.isSmall() && toCog.isSmall() ? -1f : 0f;
            };

        };

        return 0f;
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
