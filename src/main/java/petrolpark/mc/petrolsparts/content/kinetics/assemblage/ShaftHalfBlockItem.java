package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.google.common.base.Predicates;
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class ShaftHalfBlockItem extends AssemblageBlockItem {

    protected final int placementHelperId;

    public ShaftHalfBlockItem(Supplier<AssemblageSet> set, Item.Properties properties) {
        super(set, properties);
        placementHelperId = PlacementHelpers.register(new ShaftHalfBlockItem.PlacementHelper());
    };

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        final IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final BlockState state = level.getBlockState(pos);
        final Player player = context.getPlayer();
        if (helper.matchesState(state) && !player.isShiftKeyDown()) {
            return helper.getOffset(player, level, state, pos, context.getHitResult()).placeInWorld(level, this, player, context.getHand(), context.getHitResult()).result();
        } else {
            return super.onItemUseFirst(stack, context);
        }
    };

    @Override
    public AssemblageBlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        final AssemblageBlockPlaceContext assemblageContext = super.updatePlacementContext(context);
        if (assemblageContext == null) return null;
        final BlockState replacingState = assemblageContext.getLevel().getBlockState(assemblageContext.getClickedPos());
        if (assemblageContext.replacingClickedOnBlock() && replacingState.getBlock() instanceof AssemblageBlock && replacingState.getValue(IAssemblageBlock.AXIS) != assemblageContext.getClickedFace().getAxis())
            assemblageContext.dontReplaceClicked(); // Shafts will never be placed on the side of another part within the same block (only front or back)
        return assemblageContext;
    };

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        final BlockState existingState = getSet().getEquivalent(context.getLevel().getBlockState(context.getClickedPos()));
        BlockState state = getBlock().defaultBlockState();
        boolean topShaft = context.getClickedFace().getAxisDirection() == AxisDirection.NEGATIVE;
        if (existingState.getBlock() instanceof AssemblageBlock) {
            final Axis axis = existingState.getValue(IAssemblageBlock.AXIS);
            final AssemblagePart part = getSet().getTargetedPart(context);
            state = state.setValue(IAssemblageBlock.AXIS, axis);
            topShaft ^= part != null && (part.isMiddleCog(getSet(), axis) || part.isShaft()); // If targeting the middle cog or a shaft half, place on the other side
        } else {
            return ProperWaterloggedBlock.withWater(context.getLevel(), state.setValue(IAssemblageBlock.AXIS, context.getClickedFace().getAxis()).setValue(topShaft ? IAssemblageBlock.TOP_SHAFT_HALF : IAssemblageBlock.BOTTOM_SHAFT_HALF, true), context.getClickedPos());
        };
        return ProperWaterloggedBlock.withWater(context.getLevel(), getBlock().getReplacedState(context.getLevel(), context.getClickedPos(), existingState, state.setValue(topShaft ? IAssemblageBlock.TOP_SHAFT_HALF : IAssemblageBlock.BOTTOM_SHAFT_HALF, true), context.getPlayer()), context.getClickedPos());
    };

    public class PlacementHelper extends AssemblageBlockItem.PlacementHelper {

        protected final Predicate<BlockState> statePredicate = Predicates.or(getSet().shaft()::has, getSet().separateShaftsAssemblage()::has, getSet().singleShaftAssemblage()::has);

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return s -> s.getItem().equals(ShaftHalfBlockItem.this);
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return statePredicate;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            int range = AllConfigs.server().equipment.placementAssistRange.get();
            final AttributeInstance reach = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
            if (reach != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier.id())) range += 4;

            for (Direction direction : IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.getLocation(), state.getValue(BlockStateProperties.AXIS))) {
                final boolean expandingPositiveDirection = direction.getAxisDirection() == AxisDirection.POSITIVE;
                BlockPos checkPos = pos;
                BlockState checkState = state;
                int count = 0;
                boolean bottom = expandingPositiveDirection;
                searchAlongPole: while (getStatePredicate().test(checkState) && checkState.getValue(BlockStateProperties.AXIS) == direction.getAxis()) {

                    if (checkState.getBlock() instanceof IAssemblageBlock assemblageBlock) { // Replace Block directly, don't move to next
                        final boolean hasFirstHalf = expandingPositiveDirection ? assemblageBlock.hasBottomShaft(checkState) : assemblageBlock.hasTopShaft(checkState);
                        final boolean hasSecondHalf = expandingPositiveDirection ? assemblageBlock.hasTopShaft(checkState) : assemblageBlock.hasBottomShaft(checkState);
                        if (hasFirstHalf) {
                            if (!hasSecondHalf) {
                                bottom = !bottom;
                                break;
                            };
                        } else {
                            break;
                        };
                    };

                    count++;
                    checkPos = checkPos.relative(direction);
                    checkState = world.getBlockState(checkPos);
                    if (count >= range) break searchAlongPole;
                };

                final BlockState stateToPlace = ShaftHalfBlockItem.this.getBlock().defaultBlockState()
                    .setValue(IAssemblageBlock.AXIS, direction.getAxis())
                    .setValue(bottom ? IAssemblageBlock.BOTTOM_SHAFT_HALF : IAssemblageBlock.TOP_SHAFT_HALF, true);

                if (getBlock().canBeReplaced(world, checkPos, checkState, stateToPlace, player)) return PlacementOffset.success(checkPos, $ -> stateToPlace).withGhostState(stateToPlace);
            };

            return PlacementOffset.fail();
        };
    };
    
};
