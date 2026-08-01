package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import javax.annotation.Nullable;

import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelBlockItem;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IBevelCogWheelBlock;

public interface IDiagonalBevelCogWheelBlock extends IBevelCogWheelBlock {
    
    public static final int COG_ON_BEVEL_PLACEMENT_HELPER_ID = PlacementHelpers.register(new CogOnDiagonalPlacementHelper());
    
    /**
     * Used by {@link BevelCogWheelBlockItem.DiagonalPlacementHelper}
     */
    public @Nullable Axis getCogRotationAxisConnectedToFace(BlockState state, Direction face);

    default ItemInteractionResult placeCogOrEncase(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isShiftKeyDown() || !player.mayBuild()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // Encasing
        if (this instanceof EncasableBlock encasable) {
            final ItemInteractionResult result = encasable.tryEncase(state, level, pos, stack, player, hand, hitResult);
            if (result.consumesAction()) return result;
        };

        // Shafts & Cogwheels
        final IPlacementHelper helper = PlacementHelpers.get(COG_ON_BEVEL_PLACEMENT_HELPER_ID);
        if (helper.matchesItem(stack)) return helper.getOffset(player, level, state, pos, hitResult, stack).placeInWorld(level, (BlockItem)stack.getItem(), player, hand, hitResult);

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    };

    default boolean canDiagonalBevelCogWheelSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for (Direction face : Iterate.directions) {
            final Axis axis = getCogRotationAxisConnectedToFace(state, face);
            if (axis == null) continue;
            final BlockState adjacentState = level.getBlockState(pos.relative(face));
            if (ICogWheel.isLargeCog(adjacentState) && adjacentState.getValue(CogWheelBlock.AXIS) == axis) return false;
            //TODO check face-aligned dont clip
        };
        return false;
    };
};
