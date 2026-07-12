package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;

public class EncasedSingleDiagonalBevelCogWheelBlock extends KineticBlock implements ISingleDiagonalBevelCogWheelBlock, EncasedBlock {

    public static final NonNullFunction<BlockBehaviour.Properties, EncasedSingleDiagonalBevelCogWheelBlock> andesite() {
        return p -> new EncasedSingleDiagonalBevelCogWheelBlock(p, AllBlocks.ANDESITE_CASING::get, "andesite");
    };

    public static final NonNullFunction<BlockBehaviour.Properties, EncasedSingleDiagonalBevelCogWheelBlock> brass() {
        return p -> new EncasedSingleDiagonalBevelCogWheelBlock(p, AllBlocks.BRASS_CASING::get, "brass");
    };

    protected final Supplier<Block> casing;
    protected final String descriptionId;

    public EncasedSingleDiagonalBevelCogWheelBlock(BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName) {
        super(properties);
        this.casing = casing;
        this.descriptionId = Util.makeDescriptionId("block", PetrolsParts.asResource(casingName + "_encased_assemblage"));
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION, FIRST_AXIS_SHAFT, SECOND_AXIS_SHAFT));
    };

    @Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
		context.getLevel().setBlockAndUpdate(context.getClickedPos(), BlockHelper.copyAll(PetrolsPartsBlocks.SINGLE_DIAGONAL_BEVEL_COGWHEEL.getDefaultState(), state));
		return InteractionResult.SUCCESS;
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(getCasing());
    };

    @Override
    public String getDescriptionId() {
        return descriptionId;
    };

    @Override
    public Item asItem() {
        return PetrolsPartsItems.BEVEL_COGWHEEL.get();
    };

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return ISingleDiagonalBevelCogWheelBlock.super.rotateDiagonalBevelCogWheel(state, direction);
    };

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return ISingleDiagonalBevelCogWheelBlock.super.mirrorDiagonalBevelCogWheel(state, mirror);
    };
    
};
