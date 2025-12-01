package com.petrolpark.petrolsparts.content.corner_shaft;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber
public class EncasedCornerShaftBlock extends AbstractCornerShaftBlock implements SpecialBlockItemRequirement, EncasedBlock {

    private final Supplier<Block> casing;

    public EncasedCornerShaftBlock(Properties properties, Supplier<Block> casing) {
        super(properties);
        this.casing = casing;
    };

    @Override
    public Block getCasing() {
        return casing.get();
    };

    @Override
    public ItemRequirement getRequiredItems(BlockState state, @Nullable BlockEntity blockEntity) {
        return ItemRequirement.of(PetrolsPartsBlocks.CORNER_SHAFT.getDefaultState(), blockEntity);
    };

    @Override
    public BlockEntityType<? extends CornerShaftBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.ENCASED_CORNER_SHAFT.get();
    };

    @Override
	public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray) {
		KineticBlockEntity.switchToBlockState(level, pos, defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(AXIS_ALONG_FIRST_COORDINATE, state.getValue(AXIS_ALONG_FIRST_COORDINATE)));
	};

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
		context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId(state));
		KineticBlockEntity.switchToBlockState(context.getLevel(), context.getClickedPos(), PetrolsPartsBlocks.CORNER_SHAFT.getDefaultState().setValue(FACING, state.getValue(FACING)).setValue(AXIS_ALONG_FIRST_COORDINATE, state.getValue(AXIS_ALONG_FIRST_COORDINATE)));
		return InteractionResult.SUCCESS;
    };

    @SubscribeEvent
    public static final void onRegister(RegisterEvent event) {
        if (event.getRegistry().key() == Registries.BLOCK) {
            EncasingRegistry.addVariant(PetrolsPartsBlocks.CORNER_SHAFT.get(), PetrolsPartsBlocks.ANDESITE_ENCASED_CORNER_SHAFT.get());
            EncasingRegistry.addVariant(PetrolsPartsBlocks.CORNER_SHAFT.get(), PetrolsPartsBlocks.BRASS_ENCASED_CORNER_SHAFT.get());
        };
    };
    
};
