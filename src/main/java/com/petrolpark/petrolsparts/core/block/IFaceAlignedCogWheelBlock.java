package com.petrolpark.petrolsparts.core.block;

import com.petrolpark.petrolsparts.PetrolsPartsTags;
import com.petrolpark.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlock;

import net.createmod.catnip.placement.IPlacementHelper;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Used in {@link IPlacementHelper}s only. Actually functionality is in {@link IFaceAlignedCogWheelBlockEntity}.
 */
public interface IFaceAlignedCogWheelBlock {
    
    public static boolean canHaveFaceAlignedCogWheels(BlockState state) {
        return (state.getBlock() instanceof IFaceAlignedCogWheelBlock)
            || state.getBlock() instanceof MillstoneBlock
            || state.is(PetrolsPartsTags.THICK_SMALL_COGWHEELS)
            || state.is(PetrolsPartsTags.THICK_LARGE_COGWHEELS);
    };
};
