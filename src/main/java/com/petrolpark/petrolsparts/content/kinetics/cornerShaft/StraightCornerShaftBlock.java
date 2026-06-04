package com.petrolpark.petrolsparts.content.kinetics.cornerShaft;

import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

@EventBusSubscriber
public class StraightCornerShaftBlock extends ShaftBlock {

    public StraightCornerShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    public Item asItem() {
        return PetrolsPartsBlocks.CORNER_SHAFT.asItem();
    };

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return tryEncase(state, level, pos, stack, player, hand, hitResult);
    };

    @SubscribeEvent
    public static final void onBlockEntityTypeAddBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(AllBlockEntityTypes.BRACKETED_KINETIC.getKey(), PetrolsPartsBlocks.STRAIGHT_CORNER_SHAFT.get());
    };
    
};
