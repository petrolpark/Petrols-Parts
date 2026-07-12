package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem.DiagonalCogHelper;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.createmod.catnip.ghostblock.GhostBlockParams;
import net.createmod.catnip.ghostblock.GhostBlocks;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.ISingleDiagonalBevelCogWheelBlock;

public interface IDiagonalBevelCogWheelPlacementHelper extends IPlacementHelper {
    
    static final Predicate<ItemStack> BEVEL_ITEM_PREDICATE = PetrolsPartsItems.BEVEL_COGWHEEL::isIn;
    static final Predicate<BlockState> BEVEL_BLOCKSTATE_PREDICATE = state -> state.getBlock() instanceof IDiagonalBevelCogWheelBlock;
    static final Predicate<ItemStack> COG_ITEM_PREDICATE = ((Predicate<ItemStack>)(ICogWheel::isSmallCogItem)).and(ICogWheel::isDedicatedCogItem);
    static final Predicate<BlockState> COG_BLOCKSTATE_PREDICATE = ICogWheel::isSmallCog;

    @Override
    public default void renderAt(BlockPos pos, BlockState state, BlockHitResult ray, PlacementOffset offset) {
        if (!offset.hasGhostState()) return;

        GhostBlocks.getInstance().showGhost(this, DiagonalBevelCogWheelGhostBlockRenderer.INSTANCE, GhostBlockParams.of(offset.getTransform().apply(offset.getGhostState())), 1)
            .at(offset.getBlockPos())
            .breathingAlpha();
    };

    public static class BevelOnCog extends DiagonalCogHelper implements IDiagonalBevelCogWheelPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return BEVEL_ITEM_PREDICATE;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return COG_BLOCKSTATE_PREDICATE;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            final Axis axis = state.getValue(CogWheelBlock.AXIS);
            if (hitOnShaft(state, ray)) return PlacementOffset.fail();
            final List<Direction> perpendicularDirections = IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.getLocation(), axis);
            final Direction facing = perpendicularDirections.get(0);
            return IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis).stream()
                .<PlacementOffset>mapMulti((direction, consumer) -> {
                    final BlockPos offsetPos = pos.relative(direction);
                    Direction secondaryDirection = facing;
                    for (Direction perpendicularDirection : Lists.reverse(perpendicularDirections)) {
                        final BlockState otherCogState = world.getBlockState(offsetPos.relative(perpendicularDirection));
                        if (ICogWheel.isSmallCog(otherCogState) && otherCogState.getValue(CogWheelBlock.AXIS) == direction.getAxis()) secondaryDirection = perpendicularDirection; 
                    };
                    final BlockState bevelState = PetrolsPartsBlocks.SINGLE_DIAGONAL_BEVEL_COGWHEEL.getDefaultState().setValue(ISingleDiagonalBevelCogWheelBlock.ORIENTATION, Orientation.fromTopAndFront(direction.getOpposite(), secondaryDirection).asEdge());
                    if (world.getBlockState(offsetPos).canBeReplaced() || PetrolsPartsBlocks.SINGLE_DIAGONAL_BEVEL_COGWHEEL.get().canBeReplaced(world, offsetPos, world.getBlockState(offsetPos), bevelState, player))
                        consumer.accept(PlacementOffset.success(offsetPos, $ -> bevelState));
                }).findFirst()
                .orElseGet(PlacementOffset::fail);
        };

        
    };

    // public static class BevelOnBevel implements IDiagonalBevelCogWheelPlacementHelper {

    // };

    public static class CogOnBevel implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return COG_ITEM_PREDICATE;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return BEVEL_BLOCKSTATE_PREDICATE;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            if (!(state.getBlock() instanceof IDiagonalBevelCogWheelBlock diagonalBevel)) return PlacementOffset.fail();
            return IPlacementHelper.orderedByDistance(pos, ray.getLocation()).stream()
                .<PlacementOffset>mapMulti((dir, consumer) -> {
                    final Axis axis = diagonalBevel.getCogRotationAxisConnectedToFace(state, dir);
                    if (axis != null && world.getBlockState(pos.relative(dir)).canBeReplaced()) consumer.accept(PlacementOffset.success(pos.relative(dir), s -> s.setValue(CogWheelBlock.AXIS, axis)));
                }).findFirst()
                .orElseGet(PlacementOffset::fail);
        };

    };
};
