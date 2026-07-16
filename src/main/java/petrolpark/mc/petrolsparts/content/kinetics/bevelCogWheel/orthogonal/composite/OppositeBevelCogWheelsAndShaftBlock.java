package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;

public class OppositeBevelCogWheelsAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty SHAFT_ALONG_FIRST_AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;
    
    public OppositeBevelCogWheelsAndShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS, SHAFT_ALONG_FIRST_AXIS));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        final Axis axis = state.getValue(AXIS);
        return List.of(
            PetrolsPartsBlocks.SINGLE_AXIS_BEVEL_COGWHEEL.get().get(Direction.get(AxisDirection.POSITIVE, axis)),
            PetrolsPartsBlocks.SINGLE_AXIS_BEVEL_COGWHEEL.get().get(Direction.get(AxisDirection.NEGATIVE, axis)),
            AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, getShaftAxis(state))
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withoutPart'");
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        if (!part.isCog) return null;
        final Axis axis = state.getValue(AXIS);
        if (part.axis == axis || part.axis == getShaftAxis(state)) return null;
        return PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS_AND_SHAFT.getDefaultState()
            .setValue(ThreeBevelCogWheelsAndShaftBlock.EXCLUDED_FACE, Direction.get(part.isTopCog() ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, part.axis))
            .setValue(ThreeBevelCogWheelsAndShaftBlock.OTHER_COGS_ON_FIRST_AXIS, axis == Axis.X || (axis == Axis.Y && part.axis == Axis.X))
            .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return MathsHelper.getSecondaryAxis(state.getValue(AXIS), state.getValue(SHAFT_ALONG_FIRST_AXIS));
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS) || face.getAxis() == getShaftAxis(state);
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'transform'");
    };
};
