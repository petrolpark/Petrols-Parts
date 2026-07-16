package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.google.common.base.Predicates;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.IFaceAlignedCogWheelBlock;
import petrolpark.mc.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;

public class AssemblageCogWheelBlockItem extends AssemblageBlockItem {

    public final Supplier<AssemblageCog> cog;

    protected final int[] placementHelperIds;

    public AssemblageCogWheelBlockItem(AssemblageSet set, Supplier<AssemblageCog> cog, Item.Properties properties) {
        super(set, properties);
        this.cog = cog;
        placementHelperIds = new int[]{PlacementHelpers.register(getCog().isLarge() ? new LargePlacementHelper() : new SmallPlacementHelper()), PlacementHelpers.register(new DiagonalPlacementHelper())};
    };

    public AssemblageCog getCog() {
        return cog.get();
    };

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final BlockState state = level.getBlockState(pos);
        final Player player = context.getPlayer();
        if (!player.isShiftKeyDown()) {
            for (final int placementHelperId : placementHelperIds) {
                final IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
                if (helper.matchesState(state)) {
                    final InteractionResult result = helper.getOffset(player, level, state, pos, context.getHitResult()).placeInWorld(level, this, player, context.getHand(), context.getHitResult()).result();
                    if (result.consumesAction()) return result;
                };
            };
        };
        
        return super.onItemUseFirst(stack, context);
    };

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = getBlock().defaultBlockState();
        final BlockState existingState = set.getEquivalent(context.getLevel().getBlockState(context.getClickedPos()));
        if (context.replacingClickedOnBlock()) {
            state = state.setValue(IAssemblageBlock.AXIS, existingState.getValue(IAssemblageBlock.AXIS));
            final AssemblagePart part = set.getTargetedPart(context);
            if (part != null) {
                if (context.getClickedFace().getAxis() == existingState.getValue(IAssemblageBlock.AXIS)) {
                    if (part.isEndCog(set, context.getClickedFace().getOpposite()) || part.isShaft()) {
                        state = state.setValue(IAssemblageBlock.MIDDLE_COG, getCog());
                    } else if (part.isMiddleCog(set, context.getClickedFace().getAxis())) {
                        state = state.setValue(context.getClickedFace().getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.TOP_COG : IAssemblageBlock.BOTTOM_COG, getCog());
                    } else {
                        return null;
                    }
                } else {
                    final EnumProperty<AssemblageCog> cogProperty = getClosestTargetedCog(context.getClickedPos(), existingState, context.getClickLocation());
                    if (part.isShaft()) {
                        state = state.setValue(cogProperty, getCog());
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
                    state = state.setValue(context.getClickedFace().getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.BOTTOM_COG : IAssemblageBlock.TOP_COG, getCog());
                } else {
                    state = state.setValue(getClosestTargetedCog(context.getClickedPos(), existingState, context.getClickLocation()), getCog());
                };
            } else {
                state = state.setValue(IAssemblageBlock.AXIS, context.getClickedFace().getAxis()).setValue(context.getClickedFace().getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.BOTTOM_COG : IAssemblageBlock.TOP_COG, getCog());
                return state.canSurvive(context.getLevel(), context.getClickedPos()) ? ProperWaterloggedBlock.withWater(context.getLevel(), state, context.getClickedPos()) : null;
            };
        };
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? ProperWaterloggedBlock.withWater(context.getLevel(), getBlock().getReplacedState(context.getLevel(), context.getClickedPos(), existingState, state, context.getPlayer()), context.getClickedPos()) : null;
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

    public abstract class PlacementHelper implements IAssemblagePlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return stack -> stack.getItem() == AssemblageCogWheelBlockItem.this;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return Predicates.or(ICogWheel::isSmallCog, ICogWheel::isLargeCog, IFaceAlignedCogWheelBlock::canHaveFaceAlignedCogWheels);
        };

        public abstract PlacementOffset getOffsetForState(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray, Axis axis, CogType cogType, BlockState stateToPlace);

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            if (!(state.getBlock() instanceof IRotate rotate)) return PlacementOffset.fail();
            if (!(world.getBlockEntity(pos) instanceof final BlockEntity be && be != null)) return PlacementOffset.fail();
            final Axis axis = rotate.getRotationAxis(state);
            final BlockState defaultState = AssemblageCogWheelBlockItem.this.getBlock().defaultBlockState().setValue(IAssemblageBlock.AXIS, axis);

            final List<BlockState> attemptStates;

            if (state.getBlock() instanceof AssemblageBlock assemblageBlock) {
                final AssemblagePart part = assemblageBlock.getTargetedPart(state, pos, player);
                if (part != null && !part.isShaft() && !isTargetingCenter(pos, ray.getLocation(), axis)) attemptStates = Collections.singletonList(defaultState.setValue(part.isMiddleCog(set, axis)
                    ? IAssemblageBlock.MIDDLE_COG
                    : part.isEndCog(set, Direction.get(AxisDirection.POSITIVE, axis))
                        ? IAssemblageBlock.TOP_COG
                        : IAssemblageBlock.BOTTOM_COG, getCog()
                    )
                ); else return PlacementOffset.fail();
            } else {
                final double coord = ray.getLocation().get(axis) - (float)pos.get(axis);
                if (coord <= 5.5 / 16d) {
                    attemptStates = List.of(defaultState.setValue(IAssemblageBlock.BOTTOM_COG, getCog()), defaultState.setValue(IAssemblageBlock.MIDDLE_COG, getCog()), defaultState.setValue(IAssemblageBlock.TOP_COG, getCog()));
                } else if (coord <= 8 / 16d) {
                     attemptStates = List.of(defaultState.setValue(IAssemblageBlock.MIDDLE_COG, getCog()), defaultState.setValue(IAssemblageBlock.BOTTOM_COG, getCog()), defaultState.setValue(IAssemblageBlock.TOP_COG, getCog()));
                } else if (coord <= 10.5 / 16d) {
                    attemptStates = List.of(defaultState.setValue(IAssemblageBlock.MIDDLE_COG, getCog()), defaultState.setValue(IAssemblageBlock.TOP_COG, getCog()), defaultState.setValue(IAssemblageBlock.BOTTOM_COG, getCog()));
                } else {
                    attemptStates = List.of(defaultState.setValue(IAssemblageBlock.TOP_COG, getCog()), defaultState.setValue(IAssemblageBlock.MIDDLE_COG, getCog()), defaultState.setValue(IAssemblageBlock.BOTTOM_COG, getCog()));
                };
            }

            for (final BlockState attemptState : attemptStates) {
                final CogType cogType;
                if (!attemptState.getValue(IAssemblageBlock.TOP_COG).isNone()) {
                    cogType = IFaceAlignedCogWheelBlockEntity.getPossibleCogType(be, Direction.get(AxisDirection.POSITIVE, axis));
                } else if (!attemptState.getValue(IAssemblageBlock.MIDDLE_COG).isNone()) {
                    cogType = ICogWheel.isSmallCog(state) ? CogType.SMALL : ICogWheel.isLargeCog(state) ? CogType.LARGE : CogType.NONE;
                } else {
                    cogType = IFaceAlignedCogWheelBlockEntity.getPossibleCogType(be, Direction.get(AxisDirection.NEGATIVE, axis));
                };
                if (cogType.isNone()) continue;
                final PlacementOffset offset = getOffsetForState(player, world, state, pos, ray, axis, cogType, attemptState);
                if (offset.isSuccessful()) return offset.withGhostState(attemptState);
            };

            return PlacementOffset.fail();
        };

    };

    public class SmallPlacementHelper extends PlacementHelper {

        @Override
        public PlacementOffset getOffsetForState(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray, Axis axis, CogType cogType, BlockState stateToPlace) {
            if (!cogType.isSmall()) return PlacementOffset.fail();
            for (final Direction direction : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis)) {
                final BlockPos newPos = pos.relative(direction);

                if (!getBlock().canBeReplaced(world, newPos, world.getBlockState(newPos), stateToPlace, player)) continue;
                if (!stateToPlace.canSurvive(world, newPos)) continue;

                return PlacementOffset.success(newPos, $ -> stateToPlace);
            };

            return PlacementOffset.fail();
        };
    };

    public class LargePlacementHelper implements IAssemblagePlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return stack -> stack.getItem() == AssemblageCogWheelBlockItem.this;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return ICogWheel::isLargeCog;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            final Axis axis = ((IRotate) state.getBlock()).getRotationAxis(state);
            final Direction side = IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.getLocation(), axis).get(0);
            for (Direction dir : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis)) {
                final BlockPos newPos = pos.relative(dir).relative(side);
                final BlockState stateToPlace = getBlock().defaultBlockState().setValue(IAssemblageBlock.AXIS, dir.getAxis()).setValue(IAssemblageBlock.MIDDLE_COG, getCog());

                if (!CogWheelBlock.isValidCogwheelPosition(true, world, newPos, dir.getAxis())) continue;
                if (!getBlock().canBeReplaced(world, newPos, world.getBlockState(newPos), stateToPlace, player)) continue;
                return PlacementOffset.success(newPos, $ -> stateToPlace);
            };

            return PlacementOffset.fail();
        };

        
    };

    public class DiagonalPlacementHelper extends PlacementHelper {

        @Override
        public PlacementOffset getOffsetForState(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray, Axis axis, CogType cogType, BlockState stateToPlace) {
            if (cogType == getCog().getCogType()) return PlacementOffset.fail();

            final Direction closestDirection = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis).get(0);
			final List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis, d -> d.getAxis() != closestDirection.getAxis());

			for (final Direction direction : directions) {
				final BlockPos newPos = pos.relative(direction).relative(closestDirection);

				if (!getBlock().canBeReplaced(world, newPos, world.getBlockState(newPos), stateToPlace, player)) continue;
				if (!stateToPlace.canSurvive(world, newPos)) continue;

				return PlacementOffset.success(newPos, $ -> stateToPlace);
			};

			return PlacementOffset.fail();
        };

    };

    public static final boolean isTargetingCenter(BlockPos pos, Vec3 location, Axis axis) {
        for (Axis otherAxis : Iterate.axes) {
            if (otherAxis == axis) continue;
            final double coord = location.get(otherAxis) - (float)pos.get(otherAxis);
            if (coord < 5 / 16d || coord > 11 / 16d) return false;
        };
        return true;
    };
    
};
