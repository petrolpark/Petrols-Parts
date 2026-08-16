package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.library.compat.create.core.world.block.IReplaceableBlock;
import petrolpark.mc.library.compat.create.core.world.block.composite.MultiPartCompositeKineticBlock;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;

public sealed abstract class AssemblageBlock extends MultiPartCompositeKineticBlock<AssemblagePart> implements IBE<AssemblageBlockEntity>, ProperWaterloggedBlock, IAssemblageBlock, EncasableBlock, IReplaceableBlock permits SeparateShaftHalvesAssemblageBlock, SingleShaftAssemblageBlock {

    public static final <B extends AssemblageBlock> NonNullFunction<BlockBehaviour.Properties, B> create(Supplier<AssemblageSet> set, AssemblageBlock.Factory<B> factory) {
        return p -> factory.create(set, p);  
    };

    private final Supplier<AssemblageSet> set;

    public AssemblageBlock(Supplier<AssemblageSet> set, BlockBehaviour.Properties properties) {
        super(properties);
        this.set = set;
        registerDefaultState(defaultBlockState()
            .setValue(WATERLOGGED, false)
            .setValue(AXIS, Axis.Y)
            .setValue(TOP_COG, AssemblageCog.NONE)
            .setValue(MIDDLE_COG, AssemblageCog.NONE)
            .setValue(BOTTOM_COG, AssemblageCog.NONE)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED, AXIS, TOP_COG, MIDDLE_COG, BOTTOM_COG));
    };

    @Override
    public AssemblageSet getSet() {
        return set.get();
    };

    @Override
    protected FluidState getFluidState(BlockState state) {
        return fluidState(state);
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public List<AssemblagePart> getParts(BlockState state) {
        final List<AssemblagePart> parts = new ArrayList<>(5);
        final Axis axis = state.getValue(AXIS);
        state.getValue(TOP_COG).addTopPart(getSet(), axis, parts::add);
        state.getValue(MIDDLE_COG).addMiddlePart(getSet(), axis, parts::add);
        state.getValue(BOTTOM_COG).addBottomPart(getSet(), axis, parts::add);
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, AssemblagePart part) {
        return part.remover.apply(state);
    };

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.getItemInHand().getItem() instanceof AssemblageBlockItem item && item.getSet() == getSet();
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return withWater(super.getStateForPlacement(context), context);
    };

    @Override
    public BlockState getReplacedState(Level level, BlockPos pos, BlockState existingState, BlockState newState, Player player) {
        if (BlockEntityBehaviour.get(level, pos, BracketedBlockEntityBehaviour.TYPE) instanceof BracketedBlockEntityBehaviour behaviour && behaviour != null && behaviour.isBracketPresent()) return null;
        if (existingState.canBeReplaced()) return newState;
        if (newState.canBeReplaced()) return existingState;
        existingState = getSet().getEquivalent(existingState);
        newState = getSet().getEquivalent(newState);
        if (
            !(existingState.getBlock() instanceof AssemblageBlock existingAssemblage) ||
            !(newState.getBlock() instanceof AssemblageBlock newAssemblage) ||
            existingState.getValue(AXIS) != newState.getValue(AXIS) ||
            existingAssemblage.set != set || newAssemblage.set != set
        ) return null;

        // Shafts
        if (existingAssemblage instanceof SingleShaftAssemblageBlock) {
            if (newAssemblage.hasTopShaft(newState) || newAssemblage.hasBottomShaft(newState)) return null;
            else newState = BlockHelper.copyAll(PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE.getDefaultState(), newState);
        } else if (newAssemblage instanceof SingleShaftAssemblageBlock) {
            if (existingAssemblage.hasTopShaft(existingState) || existingAssemblage.hasBottomShaft(existingState)) return null;
        } else {
            if (existingAssemblage.hasTopShaft(existingState) != newAssemblage.hasTopShaft(newState)) {
                newState = newState.setValue(TOP_SHAFT_HALF, true);
            } else if (newAssemblage.hasTopShaft(newState)) {
                return null;
            };
            if (existingAssemblage.hasBottomShaft(existingState) != newAssemblage.hasBottomShaft(newState)) {
                newState = newState.setValue(BOTTOM_SHAFT_HALF, true);
            } else if (newAssemblage.hasBottomShaft(newState)) {
                return null;
            };
        };

        // Cogs
        for (EnumProperty<AssemblageCog> property : COG_PROPERTIES) {
            if (newState.getValue(property).isNone()) newState = newState.setValue(property, existingState.getValue(property));
            else if (!existingState.getValue(property).isNone()) return null;
        };

        return newState;
    };

    @Override
    public boolean canBeReplaced(Level level, BlockPos pos, BlockState existingState, BlockState newState, Player player) {
        if (BlockEntityBehaviour.get(level, pos,  BracketedBlockEntityBehaviour.TYPE) instanceof BracketedBlockEntityBehaviour behaviour && behaviour != null && behaviour.isBracketPresent()) return false;
        existingState = getSet().getEquivalent(existingState);
        newState = getSet().getEquivalent(newState);
        if (existingState.canBeReplaced() || newState.canBeReplaced()) return true;
        return newState.getBlock() instanceof AssemblageBlock newAssemblage && existingState.getBlock() instanceof AssemblageBlock existingAssemblage
            && newState.getValue(AXIS) == existingState.getValue(AXIS)
            && !(newAssemblage.hasTopShaft(newState) && existingAssemblage.hasTopShaft(existingState))
            && !(newAssemblage.hasBottomShaft(newState) && existingAssemblage.hasBottomShaft(existingState))
            && (newState.getValue(TOP_COG).isNone() || existingState.getValue(TOP_COG).isNone())
            && (newState.getValue(MIDDLE_COG).isNone() || existingState.getValue(MIDDLE_COG).isNone())
            && (newState.getValue(BOTTOM_COG).isNone() || existingState.getValue(BOTTOM_COG).isNone());
    };

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return IAssemblageBlock.super.canDiagonalBevelCogWheelSurvive(state, level, pos);
    };

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        // Deferred update of usual KineticBlockEntity/CompositeKineticBlockEntity is no good
        withBlockEntityDo(level, pos, AssemblageBlockEntity::invalidateParts);
    };

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isShiftKeyDown() || !player.mayBuild()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        final ItemInteractionResult result = tryEncase(state, level, pos, stack, player, hand, hitResult);
        if (result.consumesAction()) return result;

        final IPlacementHelper helper = PlacementHelpers.get(ShaftBlock.placementHelperId);
        if (helper.matchesItem(stack)) return helper.getOffset(player, level, state, pos, hitResult).placeInWorld(level, (BlockItem)stack.getItem(), player, hand, hitResult);
        
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    };

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        updateWater(level, state, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    };

    @Override
    public String getDescriptionId() {
        return getSet().descriptionId();
    };

    @Override
    @OnlyIn(Dist.CLIENT)
    public AssemblageBlockEntityPart getTargetedKineticPart(AssemblageBlockEntity be, Player player) {
        final AssemblagePart part = getTargetedPart(be.getBlockState(), be.getBlockPos(), player);
        return part == null ? null : part.getKineticPart(be);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS) && (face.getAxisDirection() == AxisDirection.POSITIVE ? hasTopShaft(state) || state.getValue(TOP_COG).hasShaftConnection() : hasBottomShaft(state) || state.getValue(BOTTOM_COG).hasShaftConnection());
    };
    
    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return IAssemblageBlock.rotate(state, Axis.Y, rotation);
    };

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return IAssemblageBlock.mirror(state, mirror);
    };

    @Override
    public Class<AssemblageBlockEntity> getBlockEntityClass() {
        return AssemblageBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends AssemblageBlockEntity> getBlockEntityType() {
        return getSet().blockEntity().get();
    };

    @FunctionalInterface
    public interface Factory<B extends AssemblageBlock> {

        public B create(Supplier<AssemblageSet> set, BlockBehaviour.Properties properties);
    };
    
};
