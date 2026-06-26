package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CornerShaftBlock extends AbstractCornerShaftBlock implements ProperWaterloggedBlock, EncasableBlock {

    public CornerShaftBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        super.createBlockStateDefinition(builder);
    };

    @Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
        return withWater(super.getStateForPlacement(context), context);
	};

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        updateWater(level, state, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    };

    @Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (player.isShiftKeyDown() || !player.mayBuild()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		ItemInteractionResult result = tryEncase(state, level, pos, stack, player, hand, hitResult);
		if (result.consumesAction()) return result;
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

    @Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction xDirection = null, yDirection = null, zDirection = null;
        for (Direction direction : getDirectionsConnectedByState(state)) {
            switch (direction.getAxis()) {
                case X:
                    xDirection = direction;
                    break;
                case Y:
                    yDirection = direction;
                    break;
                case Z:
                    zDirection = direction;
            };
        };
        return new AllShapes.Builder(Block.box(
            xDirection == Direction.WEST ? 0d : 5d,
            yDirection == Direction.DOWN ? 0d : 5d,
            zDirection == Direction.NORTH ? 0d : 5d,
            xDirection == Direction.EAST ? 16d : 11,
            yDirection == Direction.UP ? 16d : 11d,
            zDirection == Direction.SOUTH ? 16d : 11d
        )).build();
	};

    @Override
    protected FluidState getFluidState(BlockState state) {
        return fluidState(state);
    };

    @Override
    public Class<CornerShaftBlockEntity> getBlockEntityClass() {
        return CornerShaftBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends CornerShaftBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.CORNER_SHAFT.get();
    };
    
};
