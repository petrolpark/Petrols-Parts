package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.simibubi.create.content.decoration.encasing.EncasableBlock;

import net.createmod.catnip.ghostblock.GhostBlockParams;
import net.createmod.catnip.ghostblock.GhostBlocks;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.compat.create.core.world.block.IReplaceableBlock;
import petrolpark.mc.library.compat.create.core.world.block.multiPart.WaterloggedMultiPartKineticBlock;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.IAssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.DiagonalBevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual.IDualDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.CornerBevelCogWheelsBlock;

public class SingleDiagonalBevelCogWheelBlock extends WaterloggedMultiPartKineticBlock<DiagonalBevelCogWheelPart> implements ISingleDiagonalBevelCogWheelBlock, IReplaceableBlock, EncasableBlock {

    private final Supplier<BevelCogWheelSet> set;

    public final int shaftHalfPlacementHelperId;

    public SingleDiagonalBevelCogWheelBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(properties);
        this.set = set;
        shaftHalfPlacementHelperId = PlacementHelpers.register(new ShaftHalfPlacementHelper());
        registerDefaultState(defaultBlockState()
            .setValue(ORIENTATION, Orientation.UP_SOUTH)
            .setValue(SHAFT, CornerBevelCogWheelsBlock.ShaftType.NONE)
        );
    };

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION, SHAFT));
    };

    @Override
    public BevelCogWheelSet getSet() {
        return set.get();
    };

    @Override
    public boolean canSurviveWithout(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid, DiagonalBevelCogWheelPart part) {
        return part instanceof DiagonalBevelCogWheelPart.ShaftHalf;
    };

    @Override
    public Collection<DiagonalBevelCogWheelPart> getParts(BlockState state) {
        final Orientation orientation = state.getValue(ORIENTATION);
        final CornerBevelCogWheelsBlock.ShaftType shaft = state.getValue(SHAFT);
        final List<DiagonalBevelCogWheelPart> parts = new ArrayList<>(2);
        parts.add(getSet().diagonalCogParts().get(orientation));
        if (shaft != CornerBevelCogWheelsBlock.ShaftType.NONE) parts.add(getSet().shaftHalfParts().get((shaft == CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS ? orientation.top : orientation.front).getOpposite()));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, DiagonalBevelCogWheelPart part) {
        return part instanceof DiagonalBevelCogWheelPart.ShaftHalf ? state.setValue(SHAFT, CornerBevelCogWheelsBlock.ShaftType.NONE) : state;
    };

    @Override
    public BlockState getReplacedState(Level level, BlockPos pos, BlockState existingState, BlockState newState, Player player) {
        if (existingState.getBlock() instanceof SingleDiagonalBevelCogWheelBlock singleBlock1 && singleBlock1.getSet() == getSet()) {
            // Place Assemblage Shaft Half on this
            final BlockState stateWithShaftHalf = getReplacedWithAssemblageShaftHalf(existingState, newState);
            if (stateWithShaftHalf != null) return stateWithShaftHalf;
            // Place two diagonal Bevel Cogwheels together
            if (newState.getBlock() instanceof SingleDiagonalBevelCogWheelBlock singleBlock2 && singleBlock2.getSet() == getSet()) {
                final Orientation existingOrientation = existingState.getValue(ORIENTATION);
                final Orientation newOrientation = newState.getValue(ORIENTATION);
                if (existingOrientation.top == newOrientation.top.getOpposite() && existingOrientation.front == newOrientation.front.getOpposite()) {
                    final List<Axis> axes = Stream.of(Axis.values()).collect(Collectors.toCollection(ArrayList::new));
                    axes.remove(existingOrientation.top.getAxis());
                    axes.remove(existingOrientation.front.getAxis());
                    return getSet().dualDiagonalBlock().getDefaultState()
                        .setValue(IDualDiagonalBevelCogWheelBlock.EXCLUDED_AXIS, axes.get(0))
                        .setValue(IDualDiagonalBevelCogWheelBlock.FACE_PARITY, existingOrientation.top.getAxisDirection() == existingOrientation.front.getAxisDirection())
                        .setValue(WATERLOGGED, existingState.getValue(WATERLOGGED));
                };
            };
        } else if (newState.getBlock() instanceof SingleDiagonalBevelCogWheelBlock) {
            // Place this on existing Assemblage Shaft Half
            return getReplacedWithAssemblageShaftHalf(newState, existingState);
        };
        return null;
    };

    public static final BlockState getReplacedWithAssemblageShaftHalf(BlockState bevelCogWheelState, BlockState potentialAssemblageState) {
        final CornerBevelCogWheelsBlock.ShaftType shaft = bevelCogWheelState.getValue(SHAFT);
        if (shaft != CornerBevelCogWheelsBlock.ShaftType.NONE) return null;
        final Orientation orientation = bevelCogWheelState.getValue(ORIENTATION);
        if (!(potentialAssemblageState.getBlock() instanceof AssemblageBlock assemblage)) return null;
        final Axis axis = potentialAssemblageState.getValue(IAssemblageBlock.AXIS);
        if (axis == orientation.right.getAxis()) return null;
        if (!potentialAssemblageState.getValue(IAssemblageBlock.TOP_COG).isNone() || !potentialAssemblageState.getValue(IAssemblageBlock.MIDDLE_COG).isNone() || !potentialAssemblageState.getValue(IAssemblageBlock.BOTTOM_COG).isNone()) return null;
        if (assemblage.hasTopShaft(potentialAssemblageState) == assemblage.hasBottomShaft(potentialAssemblageState)) return null;
        bevelCogWheelState.setValue(WATERLOGGED, bevelCogWheelState.getValue(WATERLOGGED) || potentialAssemblageState.getValue(WATERLOGGED));
        final Direction face = Direction.get(assemblage.hasTopShaft(potentialAssemblageState) ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, axis);
        if (face == orientation.top.getOpposite()) return bevelCogWheelState.setValue(SHAFT, CornerBevelCogWheelsBlock.ShaftType.FIRST_AXIS);
        if (face == orientation.front.getOpposite()) return bevelCogWheelState.setValue(SHAFT, CornerBevelCogWheelsBlock.ShaftType.SECOND_AXIS);
        return null;
    };

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        final IPlacementHelper helper = PlacementHelpers.get(shaftHalfPlacementHelperId);
        if (helper.matchesItem(stack)) {
            final ItemInteractionResult result = helper.getOffset(player, level, state, pos, hitResult, stack).placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);
            if (result.consumesAction()) return result;
        };
        
        return placeCogOrEncase(stack, state, level, pos, player, hand, hitResult);
    };

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return ISingleDiagonalBevelCogWheelBlock.super.canDiagonalBevelCogWheelSurvive(state, level, pos);
    };

    @Override
    protected FluidState getFluidState(BlockState state) {
        return fluidState(state);
    };

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        updateWater(level, state, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    };

    @Override
    public String getDescriptionId() {
        return getSet().translationKey();
    };

    @Override
    public Item asItem() {
        return getSet().item().get();
    };

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return ISingleDiagonalBevelCogWheelBlock.super.rotateDiagonalBevelCogWheel(state, direction);
    };

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return ISingleDiagonalBevelCogWheelBlock.super.mirrorDiagonalBevelCogWheel(state, mirror);
    };

    public class ShaftHalfPlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return getSet().shaftHalfItem()::isIn;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.getBlock() == SingleDiagonalBevelCogWheelBlock.this && s.getValue(SHAFT) == CornerBevelCogWheelsBlock.ShaftType.NONE;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            final Orientation orientation = state.getValue(ORIENTATION);
            final Direction dir = IPlacementHelper.orderedByDistance(pos, ray.getLocation(), List.of(orientation.top.getOpposite(), orientation.front.getOpposite())).getFirst();
            return PlacementOffset.success(pos, s -> s.setValue(IAssemblageBlock.AXIS, dir.getAxis())
                .setValue(dir.getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.TOP_SHAFT_HALF : IAssemblageBlock.BOTTOM_SHAFT_HALF, true)
            );
        };

        @Override
        public void renderAt(BlockPos pos, BlockState state, BlockHitResult ray, PlacementOffset offset) {
            if (!offset.hasGhostState() || getSet().shaftHalfItem().get().getGhostBlockRenderer() == null) return;

            GhostBlocks.getInstance().showGhost(this, getSet().shaftHalfItem().get().getGhostBlockRenderer(), GhostBlockParams.of(offset.getTransform().apply(offset.getGhostState())), 1)
                .at(offset.getBlockPos())
                .breathingAlpha();
        };

    };
    
};
