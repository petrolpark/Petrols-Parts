package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public non-sealed class SeparateShaftHalvesAssemblageBlock extends AssemblageBlock {

    public final int[] shaftAndCogWheelsPlacementHelperIds = new int[]{PlacementHelpers.register(new ShaftInAssemblagePlacementHelper()), PlacementHelpers.register(new CogWheelInAssemblagePlacementHelper())};

    public SeparateShaftHalvesAssemblageBlock(Supplier<AssemblageSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
        registerDefaultState(defaultBlockState()
            .setValue(TOP_SHAFT_HALF, false)
            .setValue(BOTTOM_SHAFT_HALF, false)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TOP_SHAFT_HALF, BOTTOM_SHAFT_HALF);
    };

    @Override
    public List<AssemblagePart> getParts(BlockState state) {
        final List<AssemblagePart> parts = super.getParts(state);
        final Axis axis = state.getValue(AXIS);
        if (state.getValue(TOP_SHAFT_HALF)) parts.add(getSet().shaftHalfParts().get(Direction.get(AxisDirection.POSITIVE, axis)));
        if (state.getValue(BOTTOM_SHAFT_HALF)) parts.add(getSet().shaftHalfParts().get(Direction.get(AxisDirection.NEGATIVE, axis)));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, AssemblagePart part) {
        state = super.withoutPart(state, part);
        if (state.getValue(TOP_COG).isNone() && state.getValue(MIDDLE_COG).isNone() && state.getValue(BOTTOM_COG).isNone() && !state.getValue(TOP_SHAFT_HALF) && !state.getValue(BOTTOM_SHAFT_HALF)) return Blocks.AIR.defaultBlockState();
        return state;
    };

    @Override
    public boolean hasTopShaft(BlockState state) {
        return state.getValue(TOP_SHAFT_HALF);
    };

    @Override
    public boolean hasBottomShaft(BlockState state) {
        return state.getValue(BOTTOM_SHAFT_HALF);
    };

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isShiftKeyDown() || !player.mayBuild()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // Encasing
        final ItemInteractionResult result = super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        if (result.consumesAction()) return result;

        // Shafts & Cogwheels
        for (int helperId : shaftAndCogWheelsPlacementHelperIds) {
            final IPlacementHelper helper = PlacementHelpers.get(helperId);
            if (helper.matchesItem(stack)) return helper.getOffset(player, level, state, pos, hitResult, stack).placeInWorld(level, (BlockItem)stack.getItem(), player, hand, hitResult);
        };

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    };

    public class ShaftInAssemblagePlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return getSet().shaftBlock()::isIn;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return state -> getSet().separateShaftsAssemblageBlock().has(state) && !state.getValue(IAssemblageBlock.TOP_SHAFT_HALF) && !state.getValue(IAssemblageBlock.BOTTOM_SHAFT_HALF);
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            if (state.getBlock() instanceof AssemblageBlock block) {
                final AssemblagePart part = block.getTargetedPart(state, pos, player);
                if (part != null && part.isOnEnd(ray.getDirection())) return PlacementOffset.fail(); // Don't place "through" face-aligned cogwheels
            };
            return PlacementOffset.success(pos, s -> s.setValue(BlockStateProperties.AXIS, state.getValue(IAssemblageBlock.AXIS)));
        };
        
    };

    public class CogWheelInAssemblagePlacementHelper extends SeparateShaftHalvesAssemblageBlock.ShaftInAssemblagePlacementHelper {

        private final Supplier<Predicate<ItemStack>> itemPredicate = Suppliers.memoize(() -> stack -> getSet().equivalentSmallCogBlock().map(entry -> entry.isIn(stack))
            .or(() -> getSet().equivalentLargeCogBlock().map(entry -> entry.isIn(stack))).orElse(false));
        
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return itemPredicate.get();
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return super.getStatePredicate().and(state -> state.getValue(IAssemblageBlock.MIDDLE_COG).isNone());
        };
    };
    
};
