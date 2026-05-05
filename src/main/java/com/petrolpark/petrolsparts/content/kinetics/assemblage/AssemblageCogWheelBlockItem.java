package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;

public class AssemblageCogWheelBlockItem extends AssemblageBlockItem {

    public final AssemblageCog cog;

    public AssemblageCogWheelBlockItem(AssemblageCog cog, Item.Properties properties) {
        super(properties);
        this.cog = cog;
    };

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = getBlock().defaultBlockState();
        final BlockState existingState = AssemblageBlock.getEquivalent(context.getLevel().getBlockState(context.getClickedPos()));
        if (context.replacingClickedOnBlock()) {
            state = state.setValue(IAssemblageBlock.AXIS, existingState.getValue(IAssemblageBlock.AXIS));
            final AssemblagePart part = getTargetedPart(context);
            if (part != null) {
                if (context.getClickedFace().getAxis() == existingState.getValue(IAssemblageBlock.AXIS)) {
                    if (part.isEndCog(context.getClickedFace().getOpposite()) || part.isShaft()) {
                        state = state.setValue(IAssemblageBlock.MIDDLE_COG, cog);
                    } else if (part.isMiddleCog(context.getClickedFace().getAxis())) {
                        state = state.setValue(context.getClickedFace().getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.TOP_COG : IAssemblageBlock.BOTTOM_COG, cog);
                    } else {
                        return null;
                    }
                } else {
                    final EnumProperty<AssemblageCog> cogProperty = getClosestTargetedCog(context.getClickedPos(), existingState, context.getClickLocation());
                    if (part.isShaft()) {
                        state = state.setValue(cogProperty, cog);
                    } else {
                        return null;
                    }
                };
            } else {
                return null;
            };
        } else {
            if (existingState.getBlock() instanceof AssemblageBlock) {
                state = state.setValue(IAssemblageBlock.AXIS, existingState.getValue(IAssemblageBlock.AXIS));
                if (context.getClickedFace().getAxis() == existingState.getValue(IAssemblageBlock.AXIS)) {
                    state = state.setValue(context.getClickedFace().getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.BOTTOM_COG : IAssemblageBlock.TOP_COG, cog);
                } else {
                    state = state.setValue(getClosestTargetedCog(context.getClickedPos(), existingState, context.getClickLocation()), cog);
                };
            } else {
                return state.setValue(IAssemblageBlock.AXIS, context.getClickedFace().getAxis()).setValue(context.getClickedFace().getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.BOTTOM_COG : IAssemblageBlock.TOP_COG, cog);
            };
        };
        return ProperWaterloggedBlock.withWater(context.getLevel(), getBlock().getReplacedState(context.getLevel(), context.getClickedPos(), existingState, state, context.getPlayer()), context.getClickedPos());
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
