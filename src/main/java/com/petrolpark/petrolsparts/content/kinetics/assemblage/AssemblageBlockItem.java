package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import javax.annotation.Nullable;

import com.petrolpark.petrolsparts.PetrolsPartsBlocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AssemblageBlockItem extends ItemNameBlockItem {

    public AssemblageBlockItem(Item.Properties properties) {
        super(Blocks.AIR, properties);
    };

    @Override
    public SeparateShaftHalvesAssemblageBlock getBlock() {
        return PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE.get();
    };

    @Override
    public AssemblageBlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        if (!(context instanceof AssemblageBlockPlaceContext assemblageContext)) return null; // Cast should always succeed
        final AssemblagePart part = getTargetedPart(context);
        if (part != null && (context.getClickedFace().getAxis() == context.getLevel().getBlockState(context.getClickedPos()).getValue(IAssemblageBlock.AXIS) ? part.isOnEnd(context.getClickedFace()) : !part.isShaft())) {
            assemblageContext.dontReplaceClicked(); // Don't replace this Block, place in the next one
        };
        return assemblageContext.canPlace() ? assemblageContext : null;
    };

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        return super.place(new AssemblageBlockPlaceContext(context));
    };

    @Nullable
    public static final AssemblagePart getTargetedPart(BlockPlaceContext context) {
        if (context.replacingClickedOnBlock()) {
            final BlockState state = AssemblageBlock.getEquivalent(context.getLevel().getBlockState(context.getClickedPos()));
            if (state.getBlock() instanceof AssemblageBlock assemblage) {
                return assemblage.getTargetedPart(state, context.getClickedPos(), context.getPlayer());
            };
        };
        return null;
    };

    public static class AssemblageBlockPlaceContext extends BlockPlaceContext {

        public AssemblageBlockPlaceContext(UseOnContext context) {
            super(context);
            if (!replaceClicked) replaceClicked = AssemblageBlock.getEquivalent(getLevel().getBlockState(context.getHitResult().getBlockPos())).canBeReplaced(this);
        };

        public void dontReplaceClicked() {
            this.replaceClicked = false;
        };

        @Override
        public boolean canPlace() {
            return replacingClickedOnBlock() || AssemblageBlock.getEquivalent(getLevel().getBlockState(getClickedPos())).canBeReplaced(this);
        };

    };
    
};
