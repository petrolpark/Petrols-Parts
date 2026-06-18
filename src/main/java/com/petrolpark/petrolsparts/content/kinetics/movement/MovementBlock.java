package com.petrolpark.petrolsparts.content.kinetics.movement;

import com.petrolpark.compat.create.core.block.composite.WaterloggedHorizontalCompositeKineticBlock;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.PetrolsPartsDataMapTypes;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MovementBlock extends WaterloggedHorizontalCompositeKineticBlock implements IBE<MovementBlockEntity> {

    public static final VoxelShape SHAPE = Block.box(1d, 0d, 1d, 15d, 16d, 15d);

    public MovementBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    @SuppressWarnings("deprecation")
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return onBlockEntityUseItemOn(level, pos, be -> {
            if (!be.weightStack.isEmpty() || stack.getItem().builtInRegistryHolder().getData(PetrolsPartsDataMapTypes.MOVEMENT_WEIGHT) == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            be.setWeightStack(stack.copyWithCount(1));
            stack.shrink(1);
            level.playSound(player, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
            return ItemInteractionResult.SUCCESS;
        });
    };

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return onBlockEntityUse(level, pos, be -> {
            if (be.weightStack.isEmpty() || !player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) return InteractionResult.PASS;
            player.getInventory().placeItemBackInInventory(be.weightStack);
            be.setWeightStack(ItemStack.EMPTY);
            level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        });
    };

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == getRotationAxis(state);
    };

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        withBlockEntityDo(level, pos, be -> be.generatingPart.updateGeneratedRotation());
    };

    @Override
    protected boolean isSignalSource(BlockState state) { // So Redstone Wire always connects
        return true;
    };

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    };

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return getBlockEntityOptional(level, pos).map(be -> be.rotationsCharge / be.getMaxRotationsCharge())
            .map(ComparatorUtil::fractionToRedstoneLevel)
            .orElse(0);
    };

    @Override
    public Class<MovementBlockEntity> getBlockEntityClass() {
        return MovementBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends MovementBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.MOVEMENT.get();
    };
    
};
