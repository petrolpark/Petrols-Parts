package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.IReplaceableBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IBevelCogWheelBlock;

@ParametersAreNonnullByDefault
public interface IOrthogonalBevelCogWheelBlock extends IBevelCogWheelBlock, IReplaceableBlock, ProperWaterloggedBlock {

    public BevelCogWheelSet getSet();

    public Collection<BevelCogWheelPart> getParts(BlockState state);

    public BevelCogWheelPart getTargetedPart(BlockState state, BlockPos pos, Entity entity);
  
    @Nullable
    public BlockState withPart(BlockState state, BevelCogWheelPart part);

    /**
     * If this Bevel Cogwheel block has a Shaft, return the axis about which it rotates.
     * Otherwise, return {@code null}
     * @param state
     */
    @Nullable
    public Axis getShaftAxis(BlockState state);

    @Override
    public default BlockState getReplacedState(Level level, BlockPos pos, BlockState existingState, BlockState newState, Player player) {
        IOrthogonalBevelCogWheelBlock block;
        BlockState state;
        final Collection<BevelCogWheelPart> additionalParts;
        if (existingState.getBlock() instanceof IOrthogonalBevelCogWheelBlock bcwb) {
            block = bcwb;
            state = existingState;
            if (newState.getBlock() instanceof IOrthogonalBevelCogWheelBlock otherBcwb) {
                additionalParts = otherBcwb.getParts(newState);
            } else if (getSet().shaftBlock().has(newState)) {
                additionalParts = Collections.singletonList(getSet().shaftParts().get(newState.getValue(ShaftBlock.AXIS)));
            } else {
                return null;
            };
        } else if (newState.getBlock() instanceof IOrthogonalBevelCogWheelBlock bcwb) {
            if (!getSet().shaftBlock().has(existingState)) return null;
            block = bcwb;
            state = newState;
            additionalParts = Collections.singletonList(getSet().shaftParts().get(existingState.getValue(ShaftBlock.AXIS)));
        } else return null;

        // Try add all da new parts
        for (BevelCogWheelPart part : additionalParts) {
            state = block.withPart(state, part);
            if (state == null) return null;
            block = (IOrthogonalBevelCogWheelBlock)state.getBlock();
        };

        if (existingState.getValue(WATERLOGGED) || newState.getValue(WATERLOGGED)) state = state.setValue(WATERLOGGED, true);

        return state;
    };
};
