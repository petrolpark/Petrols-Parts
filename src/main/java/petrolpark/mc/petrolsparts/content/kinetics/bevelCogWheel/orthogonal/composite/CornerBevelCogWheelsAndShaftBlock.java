package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;

import com.simibubi.create.AllBlocks;
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
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.CornerBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.ThreeBevelCogWheelsBlock;

public class CornerBevelCogWheelsAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final EnumProperty<Orientation> ORIENTATION = Orientation.EDGE_ORIENTATION_PROPERTY;

    public CornerBevelCogWheelsAndShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        final Orientation orientation = state.getValue(ORIENTATION);
        return List.of(
            PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS.getDefaultState().setValue(CornerBevelCogWheelsBlock.ORIENTATION, orientation),
            AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, orientation.right.getAxis())
        );
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        if (!part.isCog) return null;
        final Orientation orientation = state.getValue(ORIENTATION);
        if (part == BevelCogWheelPart.COGS.get(orientation.top.getOpposite())) {
            return PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.getDefaultState()
                .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, orientation.front.getOpposite())
                .setValue(ThreeBevelCogWheelsBlock.OTHER_COGS_ON_FIRST_AXIS, orientation.top.getAxis() == Axis.X)
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else if (part == BevelCogWheelPart.COGS.get(orientation.front.getOpposite())) {
            return PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS.getDefaultState()
                .setValue(ThreeBevelCogWheelsBlock.EXCLUDED_FACE, orientation.top.getOpposite())
                .setValue(ThreeBevelCogWheelsBlock.OTHER_COGS_ON_FIRST_AXIS, orientation.front.getAxis() == Axis.Y)
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        } else return null;
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        final Orientation orientation = state.getValue(ORIENTATION);
        if (part.isCog) {
            //TODO
            return null;
        } else {
            return BlockHelper.copyAll(PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS.getDefaultState(), state);
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
