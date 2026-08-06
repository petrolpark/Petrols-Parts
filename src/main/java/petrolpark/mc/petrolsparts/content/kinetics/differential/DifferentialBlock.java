package petrolpark.mc.petrolsparts.content.kinetics.differential;

import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public class DifferentialBlock extends CompositeKineticBlock implements ICogWheel, IBE<DifferentialBlockEntity> {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    public DifferentialBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS));
    };
    
    //TODO placement state

    @Override
    public boolean isLargeCog() {
        return true;
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == getRotationAxis(state);
    };

    @Override
    public Class<DifferentialBlockEntity> getBlockEntityClass() {
        return DifferentialBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends DifferentialBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.DIFFERENTIAL.get();
    };
    
};
