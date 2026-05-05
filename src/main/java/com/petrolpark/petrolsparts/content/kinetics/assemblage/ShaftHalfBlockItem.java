package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class ShaftHalfBlockItem extends AssemblageBlockItem {

    public ShaftHalfBlockItem(Item.Properties properties) {
        super(properties);
    };

    @Override
    public AssemblageBlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        final AssemblageBlockPlaceContext assemblageContext = super.updatePlacementContext(context);
        final BlockState replacingState = assemblageContext.getLevel().getBlockState(assemblageContext.getClickedPos());
        if (assemblageContext.replacingClickedOnBlock() && replacingState.getBlock() instanceof AssemblageBlock && replacingState.getValue(IAssemblageBlock.AXIS) != assemblageContext.getClickedFace().getAxis())
            assemblageContext.dontReplaceClicked(); // Shafts will never be placed on the side of another part within the same block (only front or back)
        return assemblageContext;
    };

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        final BlockState existingState = AssemblageBlock.getEquivalent(context.getLevel().getBlockState(context.getClickedPos()));
        BlockState state = getBlock().defaultBlockState();
        boolean topShaft = context.getClickedFace().getAxisDirection() == AxisDirection.POSITIVE;
        if (existingState.getBlock() instanceof AssemblageBlock) {
            final Axis axis = existingState.getValue(IAssemblageBlock.AXIS);
            state = state.setValue(IAssemblageBlock.AXIS, axis);
            topShaft ^= getTargetedPart(context).isMiddleCog(axis); // If targeting the middle cog, place on the other side
        } else {
            state = state.setValue(IAssemblageBlock.AXIS, context.getNearestLookingDirection().getAxis());
        };
        return ProperWaterloggedBlock.withWater(context.getLevel(), getBlock().getReplacedState(context.getLevel(), context.getClickedPos(), existingState, state.setValue(topShaft ? IAssemblageBlock.TOP_SHAFT_HALF : IAssemblageBlock.BOTTOM_SHAFT_HALF, true), context.getPlayer()), context.getClickedPos());
    };
    
};
