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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.FourBevelCogWheelsBlock;

public class FourBevelCogWheelsAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final EnumProperty<Axis> SHAFT_AXIS = BlockStateProperties.AXIS;

    public FourBevelCogWheelsAndShaftBlock(Supplier<BevelCogWheelSet> set, BlockBehaviour.Properties properties) {
        super(set, properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(SHAFT_AXIS));
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        return List.of(
            getSet().fourBlock().getDefaultState().setValue(FourBevelCogWheelsBlock.EXCLUDED_AXIS, state.getValue(SHAFT_AXIS)),
            getSet().shaftBlock().getDefaultState().setValue(ShaftBlock.AXIS, state.getValue(SHAFT_AXIS))
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        return BlockHelper.copyAll(getSet().threeAndShaftBlock().getDefaultState(), getSet().fourBlock().get().withoutPart(state, part));
    };

    @Override
    public BlockState withPart(BlockState state, BevelCogWheelPart part) {
        return null;
    };

    @Override
    public Axis getShaftAxis(BlockState state) {
        return state.getValue(SHAFT_AXIS);
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return getShaftAxis(state); // Unused
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return true;
    };

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return state.setValue(SHAFT_AXIS, transform.rotateAxis(state.getValue(SHAFT_AXIS)));
    };
    
};
