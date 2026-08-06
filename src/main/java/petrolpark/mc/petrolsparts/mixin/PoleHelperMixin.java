package petrolpark.mc.petrolsparts.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.foundation.placement.PoleHelper;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.compat.create.core.world.block.IReplaceableBlock;

@Mixin(PoleHelper.class)
public abstract class PoleHelperMixin<T extends Comparable<T>> implements IPlacementHelper {
    
    @Shadow
    protected final Property<T> property = null;

    @Inject(
        method = "getOffset",
        at = @At(
            value = "INVOKE",
            target = "canBeReplaced"
        ),
        cancellable = true
    )
    public void petrolsParts$placeShaftsThroughBlocks(
        Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray,
        CallbackInfoReturnable<PlacementOffset> cir,
        List<Direction> directions, Direction dir, int range, int poles, BlockPos newPos, BlockState newState
    ) {
        if (newState instanceof IReplaceableBlock replaceableBlock && getItemPredicate().test(player.getItemInHand(InteractionHand.MAIN_HAND)) && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BlockItem blockItem) {
            final BlockState stateToPlace = blockItem.getBlock().defaultBlockState() // Best guess of what the item to place will be
                .setValue(property, state.getValue(property)); 
            if (replaceableBlock.canBeReplaced(world, newPos, newState, stateToPlace, player))
                cir.setReturnValue(PlacementOffset.success(newPos, bState -> bState.setValue(property, state.getValue(property)))); // Other mixin into PlacementOffset actually does the replacing
        };
    };
};
