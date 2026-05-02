package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import javax.annotation.Nullable;

import com.petrolpark.petrolsparts.PetrolsPartsBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;

public abstract class AssemblageBlockItem extends ItemNameBlockItem {

    public AssemblageBlockItem(Item.Properties properties) {
        super(Blocks.AIR, properties);
    };

    @Override
    public SeparateShaftHalvesAssemblageBlock getBlock() {
        return PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE.get();
    };

    @Nullable
    public static final AssemblagePart getTargetedPart(BlockPlaceContext context) {
        if (context.replacingClickedOnBlock()) {
            final BlockState state = context.getLevel().getBlockState(context.getClickedPos());
            if (AssemblageBlock.getEquivalent(state).getBlock() instanceof AssemblageBlock assemblage) {
                return assemblage.getSelectedPart(state, context.getClickedPos(), context.getPlayer());
            };
        };
        return null;
    };

    public static final EnumProperty<AssemblageCog> getClosestTargetedCog(BlockPos pos, BlockState state, Vec3 location) {
        final double coord = location.get(state.getValue(IAssemblageBlock.AXIS)) - (double)pos.get(state.getValue(IAssemblageBlock.AXIS));
        if (coord < 5 / 16d) {
            return IAssemblageBlock.BOTTOM_COG;
        } else if (coord < 11 / 16d) {
            return IAssemblageBlock.MIDDLE_COG;
        } else {
            return IAssemblageBlock.TOP_COG;
        }
    };
    
};
