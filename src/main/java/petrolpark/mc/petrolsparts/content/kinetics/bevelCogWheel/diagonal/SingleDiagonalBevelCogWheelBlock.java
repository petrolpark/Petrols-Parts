package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import petrolpark.mc.library.compat.create.core.world.block.MultiPartKineticBlock;
import petrolpark.mc.library.util.Orientation;

public class SingleDiagonalBevelCogWheelBlock extends MultiPartKineticBlock<DiagonalBevelCogWheelPart> implements ISingleDiagonalBevelCogWheelBlock {

    public SingleDiagonalBevelCogWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(FIRST_AXIS_SHAFT, false)
            .setValue(SECOND_AXIS_SHAFT, false)
        );
    };

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ORIENTATION, FIRST_AXIS_SHAFT, SECOND_AXIS_SHAFT));
    };

    @Override
    public boolean canSurviveWithout(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid, DiagonalBevelCogWheelPart part) {
        return part instanceof DiagonalBevelCogWheelPart.ShaftHalf;
    };

    @Override
    public Collection<DiagonalBevelCogWheelPart> getParts(BlockState state) {
        final Orientation orientation = state.getValue(ORIENTATION);
        final List<DiagonalBevelCogWheelPart> parts = new ArrayList<>(3);
        parts.add(DiagonalBevelCogWheelPart.COGS.get(orientation));
        if (state.getValue(FIRST_AXIS_SHAFT)) parts.add(DiagonalBevelCogWheelPart.SHAFT_HALVES.get(orientation.top.getOpposite()));
        if (state.getValue(SECOND_AXIS_SHAFT)) parts.add(DiagonalBevelCogWheelPart.SHAFT_HALVES.get(orientation.front.getOpposite()));
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, DiagonalBevelCogWheelPart part) {
        if (!(part instanceof DiagonalBevelCogWheelPart.ShaftHalf shaft)) return state;
        return state.setValue(state.getValue(ORIENTATION).top == shaft.face.getOpposite() ? FIRST_AXIS_SHAFT : SECOND_AXIS_SHAFT, false);
    };

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return false;
    };

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return ISingleDiagonalBevelCogWheelBlock.super.rotateDiagonalBevelCogWheel(state, direction);
    };

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return ISingleDiagonalBevelCogWheelBlock.super.mirrorDiagonalBevelCogWheel(state, mirror);
    };
    
};
