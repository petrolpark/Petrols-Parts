package petrolpark.mc.petrolsparts.content.legacy;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

@Deprecated
public class LegacyCoaxialGearBlock extends CogWheelBlock {

    public static final BooleanProperty HAS_SHAFT = BooleanProperty.create("has_shaft");

    public LegacyCoaxialGearBlock(Properties properties, boolean large) {
        super(large, properties);
        registerDefaultState(defaultBlockState().setValue(HAS_SHAFT, false));
    };

    public static LegacyCoaxialGearBlock large(Properties properties) {
        return new LegacyCoaxialGearBlock(properties, true);
    };

    public static LegacyCoaxialGearBlock small(Properties properties) {
        return new LegacyCoaxialGearBlock(properties, false);
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(HAS_SHAFT);
        super.createBlockStateDefinition(builder);
    };

    @Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return false;
	};

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.LEGACY_COAXIAL_GEAR.get();
    };
};
