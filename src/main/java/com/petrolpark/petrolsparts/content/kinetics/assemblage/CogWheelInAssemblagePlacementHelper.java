package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.function.Predicate;

import com.simibubi.create.AllBlocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class CogWheelInAssemblagePlacementHelper extends ShaftInAssemblagePlacementHelper {
    
    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return ((Predicate<ItemStack>)(AllBlocks.COGWHEEL::isIn)).or(AllBlocks.LARGE_COGWHEEL::isIn);
    };

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return super.getStatePredicate().and(state -> state.getValue(IAssemblageBlock.MIDDLE_COG).isNone());
    };
};
