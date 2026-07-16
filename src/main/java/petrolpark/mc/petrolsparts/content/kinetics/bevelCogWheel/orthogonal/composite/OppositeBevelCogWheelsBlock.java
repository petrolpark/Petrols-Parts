package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import static petrolpark.mc.petrolsparts.PetrolsPartsBlocks.SINGLE_AXIS_BEVEL_COGWHEEL;

import java.util.List;

import com.simibubi.create.content.contraptions.StructureTransform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SingleAxisBevelCogWheelBlock;

public class OppositeBevelCogWheelsBlock extends CompositeBevelCogWheelBlock {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    public OppositeBevelCogWheelsBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(AXIS, Axis.Y)
        );
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        final Axis axis = state.getValue(AXIS);
        return List.of(
            SINGLE_AXIS_BEVEL_COGWHEEL.getDefaultState().setValue(SingleAxisBevelCogWheelBlock.AXIS, axis).setValue(SingleAxisBevelCogWheelBlock.TYPE, SingleAxisBevelCogWheelBlock.Type.TOP),
            SINGLE_AXIS_BEVEL_COGWHEEL.getDefaultState().setValue(SingleAxisBevelCogWheelBlock.AXIS, axis).setValue(SingleAxisBevelCogWheelBlock.TYPE, SingleAxisBevelCogWheelBlock.Type.BOTTOM)
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        return SINGLE_AXIS_BEVEL_COGWHEEL.getDefaultState()
            .setValue(SingleAxisBevelCogWheelBlock.AXIS, state.getValue(AXIS))
            .setValue(SingleAxisBevelCogWheelBlock.TYPE, part.isTopCog() ? SingleAxisBevelCogWheelBlock.Type.BOTTOM : SingleAxisBevelCogWheelBlock.Type.TOP);
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        final Axis axis = state.getValue(AXIS);
        if (part.isCog) {
            //TODO u shapes
        } else { // Shafts
            if (part.axis == axis) {
                return SINGLE_AXIS_BEVEL_COGWHEEL.getDefaultState()
                    .setValue(SingleAxisBevelCogWheelBlock.AXIS, axis)
                    .setValue(SingleAxisBevelCogWheelBlock.TYPE, SingleAxisBevelCogWheelBlock.Type.BOTH);
            } else {
                // TODO perpendicular shafts
            };
        };
        return null;
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
