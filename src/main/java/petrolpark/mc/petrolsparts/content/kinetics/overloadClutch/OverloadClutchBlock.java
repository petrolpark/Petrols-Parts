package petrolpark.mc.petrolsparts.content.kinetics.overloadClutch;

import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public class OverloadClutchBlock extends CompositeKineticBlock implements IBE<OverloadClutchBlockEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public OverloadClutchBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    };

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide()) {
            withBlockEntityDo(level, pos, be -> {
                final int signal = level.getBestNeighborSignal(pos);
                if (signal != be.redstonePower) {
                    be.redstonePower = signal;
                    be.update();
                };
            });
        };
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    };
    
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == getRotationAxis(state);
    };

    @Override
    public Class<OverloadClutchBlockEntity> getBlockEntityClass() {
        return OverloadClutchBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends OverloadClutchBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.OVERLOAD_CLUTCH.get();
    };
    
};
