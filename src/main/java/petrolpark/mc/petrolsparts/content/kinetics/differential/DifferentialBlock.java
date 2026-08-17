package petrolpark.mc.petrolsparts.content.kinetics.differential;

import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.composite.WaterloggedRotatedPillarCompositeKineticBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public class DifferentialBlock extends WaterloggedRotatedPillarCompositeKineticBlock implements ICogWheel, IBE<DifferentialBlockEntity> {

    public DifferentialBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    public boolean isLargeCog() {
        return true;
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == getRotationAxis(state);
    };

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return CogWheelBlock.isValidCogwheelPosition(true, level, pos, state.getValue(AXIS));
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
