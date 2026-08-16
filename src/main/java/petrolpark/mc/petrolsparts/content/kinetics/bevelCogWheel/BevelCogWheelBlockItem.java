package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.tterrag.registrate.util.RegistrateDistExecutor;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.createmod.catnip.ghostblock.GhostBlockParams;
import net.createmod.catnip.ghostblock.GhostBlocks;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.DiagonalBevelCogWheelGhostBlockRenderer;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.ISingleDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.IOrthogonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SingleAxisBevelCogWheelBlock;

public class BevelCogWheelBlockItem extends BlockItem {

    public final Supplier<BevelCogWheelSet> set;

    @OnlyIn(Dist.CLIENT)
    @Nullable
    protected DiagonalBevelCogWheelGhostBlockRenderer diagonalGhostBlockRenderer;

    protected final int[] placementHelperIds = new int[]{
        PlacementHelpers.register(new OrthogonalPlacementHelper()),
        PlacementHelpers.register(new DiagonalPlacementHelper()),
    };

    public BevelCogWheelBlockItem(Supplier<BevelCogWheelSet> set, Item.Properties properties) {
        super(Blocks.AIR, properties);
        this.set = set;
    };

    public BevelCogWheelSet getSet() {
        return set.get();
    };

    @Override
    public SingleAxisBevelCogWheelBlock getBlock() {
        return getSet().singleAxisBlock().get();
    };

    // @Override
    // public BevelCogWheelBlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
    //     if (!(context instanceof BevelCogWheelBlockPlaceContext bevelCogWheelContext)) return null; // Cast should always succeed
    //     final BevelCogWheelPart part = getSet().getTargetedPart(context);
    //     if (switch (part) {
    //         case BevelCogWheelPart.Cog cog -> {
    //             yield bevelCogWheelContext.getClickedFace() == cog.face;
    //         } case BevelCogWheelPart.Shaft shaft -> {
    //             yield bevelCogWheelContext.getClickedFace().getAxis() == shaft.axis;
    //         } case null -> {
    //             yield false;
    //         }
    //     }) bevelCogWheelContext.dontReplaceClicked();
    //     return bevelCogWheelContext.canPlace() ? bevelCogWheelContext : null;
    // };

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        return super.place(new BevelCogWheelBlockPlaceContext(context));
    };

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        final BlockState newState = getBlock().defaultBlockState()
            .setValue(SingleAxisBevelCogWheelBlock.AXIS, context.getClickedFace().getAxis())
            .setValue(SingleAxisBevelCogWheelBlock.TYPE, context.getClickedFace().getAxisDirection() == AxisDirection.POSITIVE ? SingleAxisBevelCogWheelBlock.Type.BOTTOM : SingleAxisBevelCogWheelBlock.Type.TOP);
        final BlockState existingState = context.getLevel().getBlockState(context.getClickedPos());

        if (existingState.canBeReplaced(context)) return newState;

        if (existingState.getBlock() instanceof IOrthogonalBevelCogWheelBlock bevelBlock && bevelBlock.getSet() == getSet()) {
           return bevelBlock.withPart(existingState, getSet().cogParts().get(context.getClickedFace().getOpposite()));
        } else if (getSet().shaftBlock().has(existingState)) {
            return getBlock().withPart(newState, getSet().shaftParts().get(existingState.getValue(ShaftBlock.AXIS)));
        };

        return null;
    };

    public class BevelCogWheelBlockPlaceContext extends BlockPlaceContext {

        public BevelCogWheelBlockPlaceContext(UseOnContext context) {
            super(context);
            // if (!replaceClicked) replaceClicked = getSet().isReplaceable(getLevel().getBlockState(context.getHitResult().getBlockPos()));
        };

        // public void dontReplaceClicked() {
        //     this.replaceClicked = false;
        // };

        @Override
        public boolean canPlace() {
            return replacingClickedOnBlock() || getSet().isReplaceable(getLevel().getBlockState(getClickedPos()));
        };

        public BevelCogWheelSet getSet() {
            return BevelCogWheelBlockItem.this.getSet();
        };

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
    public String getDescriptionId() {
        return getSet().translationKey();
    };

    public class OrthogonalPlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return getSet().item()::isIn;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return Predicate.<BlockState>not(BlockState::canBeReplaced).and(getSet()::isReplaceable);
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            // Don't place on ends of existing blocks
            if (getSet().shaftBlock().has(state) && ray.getDirection().getAxis() == state.getValue(ShaftBlock.AXIS)) return PlacementOffset.fail();
            if (state.getBlock() instanceof IOrthogonalBevelCogWheelBlock bevelBlock && switch (bevelBlock.getTargetedPart(state, pos, player)) {
                case null -> false;
                case BevelCogWheelPart.Shaft shaft -> shaft.axis == ray.getDirection().getAxis();
                case BevelCogWheelPart.Cog cog -> cog.face == ray.getDirection();
            }) return PlacementOffset.fail(); 
            // Place adjacent
            for (Direction direction : IPlacementHelper.orderedByDistance(pos, ray.getLocation())) {
                final BevelCogWheelPart.Cog part = getSet().cogParts().get(direction);
                if (!(state.getBlock() instanceof IOrthogonalBevelCogWheelBlock bevelBlock) || bevelBlock.withPart(state, part) != null) // Shafts are always replaceable
                    return PlacementOffset.success(pos, $ -> getBlock().get(direction)); // PlacementOffset mixin handles merging the two blocks
            };
            return PlacementOffset.fail();
        };

    };

    public class DiagonalPlacementHelper extends CogwheelBlockItem.DiagonalCogHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return getSet().item()::isIn;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return ICogWheel::isSmallCog;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            final Axis axis = state.getValue(CogWheelBlock.AXIS);
            if (hitOnShaft(state, ray)) return PlacementOffset.fail();
            final List<Direction> perpendicularDirections = IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.getLocation(), axis);
            return IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis).stream()
                .<PlacementOffset>mapMulti((direction, consumer) -> {
                    final BlockPos offsetPos = pos.relative(direction);
                    
                    final BlockState otherCogState = world.getBlockState(offsetPos.relative(perpendicularDirections.get(1)));
                    final List<Direction> secondaryDirections = (ICogWheel.isSmallCog(otherCogState) && otherCogState.getValue(CogWheelBlock.AXIS) == direction.getAxis()) ? Lists.reverse(perpendicularDirections) : perpendicularDirections;

                    for (Direction secondaryDirection : secondaryDirections) {
                        final BlockState bevelState = getSet().singleDiagonalBlock().getDefaultState().setValue(ISingleDiagonalBevelCogWheelBlock.ORIENTATION, Orientation.fromTopAndFront(direction.getOpposite(), secondaryDirection).asEdge());
                        if (!getSet().singleDiagonalBlock().get().canDiagonalBevelCogWheelSurvive(bevelState, world, offsetPos)) continue;
                        if (world.getBlockState(offsetPos).canBeReplaced() || getSet().singleDiagonalBlock().get().canBeReplaced(world, offsetPos, world.getBlockState(offsetPos), bevelState, player)) {
                            consumer.accept(PlacementOffset.success(offsetPos, $ -> bevelState));
                            return;
                        };
                    };
                    
                }).findFirst()
                .orElseGet(PlacementOffset::fail);
        };

        @Override
        public void renderAt(BlockPos pos, BlockState state, BlockHitResult ray, PlacementOffset offset) {
            if (!offset.hasGhostState() || diagonalGhostBlockRenderer == null) return;

            GhostBlocks.getInstance().showGhost(this, diagonalGhostBlockRenderer, GhostBlockParams.of(offset.getTransform().apply(offset.getGhostState())), 1)
                .at(offset.getBlockPos())
                .breathingAlpha();
        };
    };

    public static final <I extends BevelCogWheelBlockItem> NonNullConsumer<I> registerClientSet(NonNullSupplier<BevelCogWheelClientSet> clientSet) {
        return item -> RegistrateDistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> item.diagonalGhostBlockRenderer = new DiagonalBevelCogWheelGhostBlockRenderer(clientSet.get()));  
    };
    
};
