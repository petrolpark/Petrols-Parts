package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.function.Supplier;

import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.contraptions.StructureTransform;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.compat.create.core.world.block.MultiPartKineticBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.IOrthogonalBevelCogWheelBlock;

public abstract class SimpleBevelCogWheelBlock extends MultiPartKineticBlock<BevelCogWheelPart> implements IOrthogonalBevelCogWheelBlock {

    private final Supplier<BevelCogWheelSet> set;

    public SimpleBevelCogWheelBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(properties);
        this.set = set;
        registerDefaultState(defaultBlockState()
            .setValue(WATERLOGGED, false)
        );
    };

    public abstract Direction getPrimaryCogFace(BlockState state);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    };
    
    @Override
    public BevelCogWheelSet getSet() {
        return set.get();
    };

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        final IPlacementHelper helper = PlacementHelpers.get(getSet().singleAxisBlock().get().shaftPlacementHelperId);
		if (helper.matchesItem(stack))
			return helper.getOffset(player, level, state, pos, hitResult).placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    };

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        updateWater(level, state, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    };

    @Override
    protected FluidState getFluidState(BlockState state) {
        return fluidState(state);
    };

    @Override
    public Item asItem() {
        return getSet().item().get();
    };

    @Override
    public String getDescriptionId() {
        return getSet().translationKey();
    };

    public float getStressImpact(BlockState state) {
        float impact = 0f;
        for (BevelCogWheelPart part : getParts(state)) {
            impact += BlockStressValues.getImpact((switch (part) {
                case BevelCogWheelPart.Shaft shaft -> getSet().shaftBlock();
                case BevelCogWheelPart.Cog cog -> getSet().singleAxisBlock();
            }).get());
        };
        return impact;
    };

    public float getStressCapacity(BlockState state) {
        float impact = 0f;
        for (BevelCogWheelPart part : getParts(state)) {
            impact += BlockStressValues.getCapacity((switch (part) {
                case BevelCogWheelPart.Shaft shaft -> getSet().shaftBlock();
                case BevelCogWheelPart.Cog cog -> getSet().singleAxisBlock();
            }).get());
        };
        return impact;
    };

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, rotation, Mirror.NONE));
    };

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Axis.Y, Rotation.NONE, mirror));
    };
    
};
