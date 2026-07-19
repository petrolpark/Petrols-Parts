package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.content.contraptions.StructureTransform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SingleAxisBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.ThreeBevelCogWheelsBlock;

public class OppositeBevelCogWheelsBlock extends CompositeBevelCogWheelBlock {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    public OppositeBevelCogWheelsBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
        registerDefaultState(defaultBlockState()
            .setValue(AXIS, Axis.Y)
        );
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        final Axis axis = state.getValue(AXIS);
        return List.of(
            getSet().singleAxisBlock().getDefaultState().setValue(SingleAxisBevelCogWheelBlock.AXIS, axis).setValue(SingleAxisBevelCogWheelBlock.TYPE, SingleAxisBevelCogWheelBlock.Type.TOP),
            getSet().singleAxisBlock().getDefaultState().setValue(SingleAxisBevelCogWheelBlock.AXIS, axis).setValue(SingleAxisBevelCogWheelBlock.TYPE, SingleAxisBevelCogWheelBlock.Type.BOTTOM)
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        if (!(part instanceof BevelCogWheelPart.Cog cog)) throw new IllegalArgumentException("No shaft to remove");
        return getSet().singleAxisBlock().getDefaultState()
            .setValue(SingleAxisBevelCogWheelBlock.AXIS, state.getValue(AXIS))
            .setValue(SingleAxisBevelCogWheelBlock.TYPE, cog.isTop() ? SingleAxisBevelCogWheelBlock.Type.BOTTOM : SingleAxisBevelCogWheelBlock.Type.TOP)
            .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        final Axis axis = state.getValue(AXIS);

        return switch (part) {
            case BevelCogWheelPart.Cog cog -> {
                if (cog.face.getAxis() == axis) yield null;
                yield getSet().threeBlock().getDefaultState()
                    .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, cog.face.getOpposite())
                    .setValue(ThreeBevelCogWheelsAndShaftBlock.OTHER_COGS_ON_FIRST_AXIS, MathsHelper.isSecondaryAxis(cog.face.getAxis(), axis))
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
            } case BevelCogWheelPart.Shaft shaft -> {
                if (shaft.axis == axis) {
                    yield getSet().singleAxisBlock().getDefaultState()
                        .setValue(SingleAxisBevelCogWheelBlock.AXIS, axis)
                        .setValue(SingleAxisBevelCogWheelBlock.TYPE, SingleAxisBevelCogWheelBlock.Type.BOTH)
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                } else {
                    yield getSet().oppositesAndShaftBlock().getDefaultState()
                        .setValue(OppositeBevelCogWheelsAndShaftBlock.AXIS, axis)
                        .setValue(OppositeBevelCogWheelsAndShaftBlock.SHAFT_ALONG_FIRST_AXIS, MathsHelper.isSecondaryAxis(axis, shaft.axis))
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                }
            }
        };
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return null;
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS);
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return state.setValue(AXIS, transform.rotateAxis(state.getValue(AXIS)));
    };
    
};
