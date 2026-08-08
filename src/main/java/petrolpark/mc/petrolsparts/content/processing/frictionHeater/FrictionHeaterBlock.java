package petrolpark.mc.petrolsparts.content.processing.frictionHeater;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.compat.create.core.world.block.composite.HorizontalAxisCompositeKineticBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.core.block.IFaceAlignedCogWheelBlock;

public class FrictionHeaterBlock extends HorizontalAxisCompositeKineticBlock implements IFaceAlignedCogWheelBlock, IBE<FrictionHeaterBlockEntity> {

    public static final EnumProperty<HeatLevel> HEAT_LEVEL = BlazeBurnerBlock.HEAT_LEVEL;

    public FrictionHeaterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(HEAT_LEVEL, HeatLevel.NONE)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(HEAT_LEVEL));
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == getRotationAxis(state);
    };

    @Override
    public Class<FrictionHeaterBlockEntity> getBlockEntityClass() {
        return FrictionHeaterBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends FrictionHeaterBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.FRICTION_HEATER.get();
    };
    
};
