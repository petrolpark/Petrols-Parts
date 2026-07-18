package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single;

import java.util.function.Supplier;

import com.simibubi.create.content.kinetics.base.KineticBlock;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
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
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IEncasedBevelCogWheelBlock;

public class EncasedSingleDiagonalBevelCogWheelBlock extends KineticBlock implements ISingleDiagonalBevelCogWheelBlock, IEncasedBevelCogWheelBlock {

    private final Supplier<BevelCogWheelSet> set;
    protected final Supplier<Block> casing;
    protected final String descriptionId;

    public EncasedSingleDiagonalBevelCogWheelBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties, Supplier<Block> casing, String casingName) {
        super(properties);
        this.set = set;
        this.casing = casing;
        this.descriptionId = Util.makeDescriptionId("block", Lang.prependLocation(casingName + "_encased_", getSet().id()));
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION, FIRST_AXIS_SHAFT, SECOND_AXIS_SHAFT));
    };

    @Override
    public BevelCogWheelSet getSet() {
        return set.get();
    };

    @Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
		context.getLevel().setBlockAndUpdate(context.getClickedPos(), BlockHelper.copyAll(getSet().singleDiagonalBlock().getDefaultState(), state));
		return InteractionResult.SUCCESS;
	};

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return placeCogOrEncase(stack, state, level, pos, player, hand, hitResult);
    };

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canDiagonalBevelCogWheelSurvive(state, level, pos);
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
    
};
