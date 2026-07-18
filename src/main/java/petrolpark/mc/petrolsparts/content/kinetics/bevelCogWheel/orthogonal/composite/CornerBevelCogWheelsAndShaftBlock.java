package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.library.util.MathsHelper;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.CornerBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.ThreeBevelCogWheelsBlock;

public class CornerBevelCogWheelsAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final EnumProperty<Orientation> ORIENTATION = Orientation.EDGE_ORIENTATION_PROPERTY;

    public CornerBevelCogWheelsAndShaftBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        final Orientation orientation = state.getValue(ORIENTATION);
        return List.of(
            getSet().cornerBlock().getDefaultState().setValue(CornerBevelCogWheelsBlock.ORIENTATION, orientation),
            getSet().shaftBlock().getDefaultState().setValue(ShaftBlock.AXIS, orientation.right.getAxis())
        );
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        if (!(part instanceof BevelCogWheelPart.Cog)) return null;
        final Orientation orientation = state.getValue(ORIENTATION);
        if (part == getSet().cogParts().get(orientation.top.getOpposite())) {
            return getSet().threeBlock().getDefaultState()
                .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, orientation.front.getOpposite())
                .setValue(ThreeBevelCogWheelsBlock.OTHER_COGS_ON_FIRST_AXIS, orientation.top.getAxis() == Axis.X)
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else if (part == getSet().cogParts().get(orientation.front.getOpposite())) {
            return getSet().threeBlock().getDefaultState()
                .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, orientation.top.getOpposite())
                .setValue(ThreeBevelCogWheelsBlock.OTHER_COGS_ON_FIRST_AXIS, orientation.front.getAxis() == Axis.Y)
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else return null;
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        final Orientation orientation = state.getValue(ORIENTATION);
        if (part instanceof BevelCogWheelPart.Cog cog) {
            final Direction remainingFace = orientation.top == cog.face ? orientation.front : orientation.top;
            return getSet().singleAndShaftBlock().getDefaultState()
                .setValue(BevelCogWheelAndShaftBlock.FACING, remainingFace)
                .setValue(BevelCogWheelAndShaftBlock.SHAFT_ON_FIRST_AXIS, MathsHelper.isSecondaryAxis(remainingFace.getAxis(), getShaftAxis(state)))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else {
            return BlockHelper.copyAll(getSet().cornerBlock().getDefaultState(), state);
        }
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return state.getValue(ORIENTATION).right.getAxis();
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return getShaftAxis(state);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        final Orientation orientation = state.getValue(ORIENTATION);
        return face == orientation.top || face == orientation.front || face.getAxis() == orientation.right.getAxis();
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return state.setValue(ORIENTATION, state.getValue(ORIENTATION).mirror(transform.mirror).rotate(transform.rotationAxis, transform.rotation));
    };
    
};
