package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.petrolpark.util.BlockHelper;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public abstract class EncasedAssemblageBlock extends Block implements IBE<AssemblageBlockEntity>, IAssemblageBlock, EncasedBlock {

    protected final Supplier<Block> casing;

    public EncasedAssemblageBlock(BlockBehaviour.Properties properties, Supplier<Block> casing) {
        super(properties);
        this.casing = casing;
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS, TOP_COG, MIDDLE_COG, BOTTOM_COG);
    };

    @Override
	public void onPlace(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean movedByPiston) {
		IAssemblageBlock.super.onPlace(state, level, pos, oldState, movedByPiston);
	};

	@Override
	public void onRemove(@Nonnull BlockState pState, @Nonnull Level pLevel, @Nonnull BlockPos pPos, @Nonnull BlockState pNewState, boolean pIsMoving) {
		IBE.onRemove(pState, pLevel, pPos, pNewState);
	};

	@Override
	public void updateIndirectNeighbourShapes(@Nonnull BlockState stateIn, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, int flags, int count) {
		IAssemblageBlock.super.updateIndirectNeighbourShapes(stateIn, level, pos, flags, count);
	};

	@Override
	public void setPlacedBy(@Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
		IAssemblageBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
	};

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public Block getCasing() {
        return casing.get();
    };

    @Override
    public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray) {
        level.setBlock(pos, BlockHelper.copyAll(defaultBlockState(), state), Block.UPDATE_ALL);
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
