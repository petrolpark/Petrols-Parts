package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import java.util.function.Predicate;

import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.compat.create.core.world.block.IReplaceableBlock;

public class CogOnDiagonalBevelPlacementHelper implements IPlacementHelper {

    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return ICogWheel::isSmallCogItem;
    };

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return state -> state.getBlock() instanceof IDiagonalBevelCogWheelBlock;
    };

    @Override
    @Deprecated
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
        return PlacementOffset.fail();
    };

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray, ItemStack stack) {
        return getOffset(player, world, state, pos, ray, ((BlockItem)stack.getItem()).getBlock().defaultBlockState());
    };

    public static final PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray, BlockState stateToPlace) {
        if (!(state.getBlock() instanceof IDiagonalBevelCogWheelBlock diagonalBevel)) return PlacementOffset.fail();
        return IPlacementHelper.orderedByDistance(pos, ray.getLocation()).stream()
            .<PlacementOffset>mapMulti((dir, consumer) -> {
                final Axis axis = diagonalBevel.getCogRotationAxisConnectedToFace(state, dir);
                if (axis == null) return;
                final BlockState placementState = stateToPlace.setValue(BlockStateProperties.AXIS, axis);
                if (IReplaceableBlock.canReplace(world, pos.relative(dir), world.getBlockState(pos.relative(dir)), placementState, player))
                    consumer.accept(PlacementOffset.success(pos.relative(dir), $ -> placementState).withGhostState(placementState));
            }).findFirst()
            .orElseGet(PlacementOffset::fail);
    };

};
