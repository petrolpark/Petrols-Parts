package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.placement.PoleHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.Petrolpark;
import petrolpark.mc.library.core.world.block.multiPart.MultiPartBlock;
import petrolpark.mc.petrolsparts.util.ShaftHelper;

public class CornerShaftBlockItem extends BlockItem {

    protected final Supplier<CornerShaftSet> set;
    public final int placementHelperId;

    public CornerShaftBlockItem(CornerShaftBlock block, Item.Properties properties) {
        super(block, properties);
        this.set = block.set;
        placementHelperId = PlacementHelpers.register(new PlacementHelper());
    };
    
    public CornerShaftSet getSet() {
        return set.get();
    };

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final Player player = context.getPlayer();
        final BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        // Start connecting by using placement helper
        final IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        if (player != null && !player.isShiftKeyDown() && placementHelper.matchesState(state)) {
            final PlacementOffset offset = placementHelper.getOffset(player, context.getLevel(), state, context.getClickedPos(), context.getHitResult());
            if (offset.isSuccessful()) {
                if (context.getLevel().isClientSide()) CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> {
                    final Direction facing = offset.getTransform().apply(getBlock().defaultBlockState()).getValue(CornerShaftBlock.FACING);
                    AutoShaftRoutingClientHelper.tryPlace(getSet(), player, offset.getBlockPos().relative(facing.getOpposite()), facing);
                });
                return InteractionResult.FAIL;
            };
        };

        // Start connecting by actually clicking on a block
        final BlockPlaceContext blockPlaceContext = new BlockPlaceContext(context);
        final BlockState defaultPlacementState = getPlacementState(blockPlaceContext);

        if (!context.getPlayer().isShiftKeyDown() && defaultPlacementState != null && canPlace(blockPlaceContext, defaultPlacementState)) {
            if (context.getLevel().isClientSide()) {
                CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> AutoShaftRoutingClientHelper.tryPlace(getSet(), blockPlaceContext));
            };
            return InteractionResult.FAIL;
        };
        return super.useOn(context);
    };

    public class PlacementHelper extends PoleHelper<Axis> {

        public PlacementHelper() {
            super(ShaftHelper.SHAFT_PLACEMENT_HELPER_STATE_PREDICATE, ShaftHelper.SHAFT_PLACEMENT_HELPER_AXIS_FUNCTION, ShaftBlock.AXIS);
        };

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return s -> s.getItem() == CornerShaftBlockItem.this;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return ShaftHelper::isShaftLike;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            if (Petrolpark.runForDist(() -> AutoShaftRoutingClientHelper::isActive, () -> () -> false)) return PlacementOffset.fail();
            if (state.getBlock() instanceof MultiPartBlock<?> block && !ShaftHelper.isTargetingShaftPart(pos, block, state, player)) return PlacementOffset.fail();
            
            for (Direction dir : IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.getLocation(), axisFunction.apply(state))) {
                int range = AllConfigs.server().equipment.placementAssistRange.get();
                if (player != null) {
                    final AttributeInstance reach = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
                    if (reach != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier.id())) range += 4;
                }
                final int poles = attachedPoles(world, pos, dir);
                if (poles >= range) continue;

                final BlockPos newPos = pos.relative(dir, poles + 1);

                if (world.getBlockState(newPos).canBeReplaced())
                    return PlacementOffset.success(newPos, s -> {
                        if (s.hasProperty(property)) return s.setValue(property, state.getValue(property));
                        else if (s.hasProperty(BlockStateProperties.FACING)) return s.setValue(BlockStateProperties.FACING, dir);
                        else return s;
                    }).withGhostState(getSet().straightCornerShaftBlock().getDefaultState());
            };

            return PlacementOffset.fail();
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray, ItemStack heldItem) {
            return getOffset(player, world, state, pos, ray);
        };

    };
    
};
