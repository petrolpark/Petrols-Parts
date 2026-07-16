package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.FourBevelCogWheelsBlock;

public class FourBevelCogWheelsAndShaftBlock extends CompositeBevelCogWheelBlock {

    public static final EnumProperty<Axis> SHAFT_AXIS = BlockStateProperties.AXIS;

    public FourBevelCogWheelsAndShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    protected List<BlockState> getSimpleBevelCogWheelEquivalents(BlockState state) {
        return List.of(
            PetrolsPartsBlocks.FOUR_BEVEL_COGWHEELS.getDefaultState().setValue(FourBevelCogWheelsBlock.EXCLUDED_AXIS, state.getValue(SHAFT_AXIS)),
            AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, state.getValue(SHAFT_AXIS))
        );
    };

    @Override
    public BlockState withoutPart(BlockState state, BevelCogWheelPart part) {
        return BlockHelper.copyAll(PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS_AND_SHAFT.getDefaultState(), PetrolsPartsBlocks.FOUR_BEVEL_COGWHEELS.get().withoutPart(state, part));
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
        return getShaftAxis(state);
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
