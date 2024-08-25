package com.petrolpark.petrolsparts.content.coaxial_gear;

import java.util.function.Predicate;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CoaxialGearBlockItem extends CogwheelBlockItem {

    public CoaxialGearBlockItem(CoaxialGearBlock block, Properties properties) {
        super(block, properties);
        // Placement Helpers are registered elsewhere to ensure they come before the default Shaft ones
    };

    @Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        if (ShaftBlock.isShaft(state)) {
            if (CoaxialGearBlock.tryMakeLongShaft(state, getBlock(), level, pos, context.getPlayer(), Direction.getFacingAxis(player, state.getValue(RotatedPillarKineticBlock.AXIS)))) {
                SoundType soundType = getBlock().defaultBlockState().getSoundType(level, pos, player);
		        level.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                if (!level.isClientSide() && !player.isCreative()) {
                    stack.shrink(1);
                };
                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                player.displayClientMessage(Component.translatable("petrolsparts.tooltip.coaxial_gear.shaft_too_short").withStyle(ChatFormatting.RED), true);
                return InteractionResult.SUCCESS;
            }
        };
		return super.onItemUseFirst(stack, context);
	};

    public static class GearOnShaftPlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return (stack) -> stack.getItem() instanceof BlockItem blockItem && CoaxialGearBlock.isCoaxialGear(blockItem.getBlock());
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return ShaftBlock::isShaft;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            if (ShaftBlock.isShaft(state)) {
                for (int i : Iterate.positiveAndNegative) {
                    if (ShaftBlock.isShaft(world.getBlockState(pos.offset(Direction.get(AxisDirection.POSITIVE, state.getValue(ShaftBlock.AXIS)).getNormal().multiply(i))))) return PlacementOffset.success(pos, s -> s.setValue(RotatedPillarKineticBlock.AXIS, state.getValue(RotatedPillarKineticBlock.AXIS)));
                };
            };
            return PlacementOffset.fail();
        };

    };

    public static class ShaftOnGearPlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.SHAFT::isIn;
        };

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return CoaxialGearBlock::isCoaxialGear;
        };

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            return PlacementOffset.success(pos, s -> s.setValue(RotatedPillarKineticBlock.AXIS, state.getValue(RotatedPillarKineticBlock.AXIS)));
        };

    };
    
};
