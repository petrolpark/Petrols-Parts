package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.ArrayList;
import java.util.List;

import com.petrolpark.compat.create.core.block.IReplaceableBlock;
import com.petrolpark.compat.create.core.block.composite.MultiPartCompositeKineticBlock;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.util.BlockHelper;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

public sealed abstract class AssemblageBlock extends MultiPartCompositeKineticBlock<AssemblagePart> implements IBE<AssemblageBlockEntity>, ProperWaterloggedBlock, IAssemblageBlock, EncasableBlock, IReplaceableBlock permits SeparateShaftHalvesAssemblageBlock, SingleShaftAssemblageBlock{

    public AssemblageBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(WATERLOGGED, false)
            .setValue(AXIS, Axis.Y)
            .setValue(TOP_COG, AssemblageCog.NONE)
            .setValue(MIDDLE_COG, AssemblageCog.NONE)
            .setValue(BOTTOM_COG, AssemblageCog.NONE)
        );
    };

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.getItemInHand().getItem() instanceof AssemblageBlockItem;
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return withWater(super.getStateForPlacement(context), context);
    };

    @Override
    public BlockState getReplacedState(Level level, BlockPos pos, BlockState existingState, BlockState newState, Player player) {
        existingState = getEquivalent(existingState);
        newState = getEquivalent(newState);
        if (
            !(existingState.getBlock() instanceof AssemblageBlock existingAssemblage) ||
            !(newState.getBlock() instanceof AssemblageBlock newAssemblage) ||
            existingState.getValue(AXIS) != newState.getValue(AXIS)
        ) return null;

        // Shafts
        if (existingAssemblage instanceof SingleShaftAssemblageBlock) {
            if (newAssemblage.hasTopShaft(newState) || newAssemblage.hasBottomShaft(newState)) return null;
            else newState = BlockHelper.copyAll(PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE.getDefaultState(), newState);
        } else if (newAssemblage instanceof SingleShaftAssemblageBlock) {
            if (existingAssemblage.hasTopShaft(existingState) || existingAssemblage.hasBottomShaft(existingState)) return null;
        } else {
            if (existingAssemblage.hasTopShaft(existingState) != newAssemblage.hasTopShaft(newState)) {
                if (newAssemblage.hasTopShaft(existingState)) return null;
                newState = newState.setValue(TOP_SHAFT_HALF, true);
            };
            if (existingAssemblage.hasBottomShaft(existingState) != newAssemblage.hasBottomShaft(newState)) {
                if (newAssemblage.hasBottomShaft(existingState)) return null;
                newState = newState.setValue(BOTTOM_SHAFT_HALF, true);
            };
        };

        // Cogs
        for (EnumProperty<AssemblageCog> property : COG_PROPERTIES) {
            if (newState.getValue(property).isNone()) newState = newState.setValue(property, existingState.getValue(property));
            else if (!existingState.getValue(property).isNone()) return null;
        };

        return newState;
    };

    public static final BlockState getEquivalent(BlockState state) {
        BlockState oldState = state;
        if (AllBlocks.SHAFT.has(oldState)) {
            state = PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE.getDefaultState();
        } else if (AllBlocks.COGWHEEL.has(oldState)) {
            state = PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE.getDefaultState().setValue(MIDDLE_COG, AssemblageCog.SMALL);
        } else if (AllBlocks.LARGE_COGWHEEL.has(oldState)) {
            state = PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE.getDefaultState().setValue(MIDDLE_COG, AssemblageCog.LARGE);
        } else {
            return state;
        }
        return state.setValue(AXIS, state.getValue(BlockStateProperties.AXIS));
    };

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        // Deferred update of usual KineticBlockEntity/CompositeKineticBlockEntity is no good
        withBlockEntityDo(level, pos, AssemblageBlockEntity::invalidateParts);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, AXIS, TOP_COG, MIDDLE_COG, BOTTOM_COG);
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
        state.getValue(TOP_COG).addTopPart(axis, parts::add);
        state.getValue(MIDDLE_COG).addMiddlePart(axis, parts::add);
        state.getValue(BOTTOM_COG).addBottomPart(axis, parts::add);
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, AssemblagePart part) {
        return part.remover.apply(state);
    };
    
    // TEMP
    public AssemblagePart getSelectedPart(BlockState state, BlockPos pos, Entity entity) {
        return clipperCache.get(state).clip(pos, entity);
    };

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isShiftKeyDown() || !player.mayBuild()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        return tryEncase(state, level, pos, stack, player, hand, hitResult);
    };

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        updateWater(level, state, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
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
    
};
