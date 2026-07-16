package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import java.util.Collection;
import java.util.Map;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;

public class FourBevelCogWheelsBlock extends SimpleBevelCogWheelBlock implements IBE<SimpleBevelCogWheelBlockEntity> {

    public static final EnumProperty<Axis> EXCLUDED_AXIS = BlockStateProperties.AXIS;

    public FourBevelCogWheelsBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(EXCLUDED_AXIS));
    };

    @Override
    public Collection<BevelCogWheelPart> getParts(BlockState state) {
        final Axis axis = state.getValue(EXCLUDED_AXIS);
        return BevelCogWheelPart.COGS.entrySet().stream().filter(entry -> entry.getKey().getAxis() != axis).map(Map.Entry::getValue).toList();
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        final Direction removed = Direction.get(part.isTopCog() ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, part.axis);
        return PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.getDefaultState()
            .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, removed)
            .setValue(ThreeBevelCogWheelsBlock.OTHER_COGS_ON_FIRST_AXIS, state.getValue(EXCLUDED_AXIS) == Axis.Z || (state.getValue(EXCLUDED_AXIS) == Axis.Y && removed.getAxis() == Axis.Z))
            .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        if (part == BevelCogWheelPart.SHAFTS.get(state.getValue(EXCLUDED_AXIS))) return BlockHelper.copyAll(PetrolsPartsBlocks.FOUR_BEVEL_COGWHEELS_AND_SHAFT.getDefaultState(), state);
        return null;
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return null;
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(EXCLUDED_AXIS); // Unused
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() != state.getValue(EXCLUDED_AXIS);
    };

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return false;
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return state.setValue(EXCLUDED_AXIS, transform.rotateAxis(state.getValue(EXCLUDED_AXIS)));
    };

    @Override
    public Class<SimpleBevelCogWheelBlockEntity> getBlockEntityClass() {
        return SimpleBevelCogWheelBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends SimpleBevelCogWheelBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.SIMPLE_BEVEL_COGWHEEL.get();
    };
    
};
