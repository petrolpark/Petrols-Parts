package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite;

import java.util.List;
import java.util.stream.IntStream;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.IOrthogonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SimpleBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SimpleBevelCogWheelBlockEntity;

public class CompositeBevelCogWheelBlockEntity extends CompositeKineticBlockEntity implements IHaveHoveringInformation, IHaveGoggleInformation {

    protected List<CompositeBevelCogWheelBlockEntity.Part> parts;

    public CompositeBevelCogWheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setFromBlockState(state);
    };

    @Override
    public List<CompositeBevelCogWheelBlockEntity.Part> getParts() {
        return parts;
    };

    protected void setFromBlockState(BlockState state) {
        if (!(state.getBlock() instanceof CompositeBevelCogWheelBlock block)) throw new IllegalStateException("Must be a Composite Bevel Cogwheel block");
        final List<BlockState> states = block.getSimpleBevelCogWheelEquivalents(state);
        parts = IntStream.range(0, states.size()).mapToObj(i -> new CompositeBevelCogWheelBlockEntity.Part(i, states.get(i))).toList();
    };

    @Override
    public void setBlockState(BlockState blockState) {
        super.setBlockState(blockState);
        setFromBlockState(blockState);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {};

    public class Part extends CompositeKineticBlockEntityPart {

        protected final int index;
        protected final BlockState effectiveBlockState;

        protected Part(int index, BlockState effectiveBlockState) {
            super(PetrolsPartsBlockEntityTypes.COMPOSITE_BEVEL_COGWHEEL_PART.get());
            this.index = index;
            this.effectiveBlockState = effectiveBlockState;
        };

        @Override
        public float calculateStressApplied() {
            switch (effectiveBlockState.getBlock()) {
                case ShaftBlock shaftBlock: {
                    return lastStressApplied = (float)BlockStressValues.getImpact(shaftBlock);
                } case SimpleBevelCogWheelBlock bevelBlock: {
                    return lastStressApplied = bevelBlock.getStressImpact(effectiveBlockState);
                } default: {
                    return super.calculateStressApplied();
                }
            }
        };

        @Override
        public float calculateAddedStressCapacity() {
            switch (effectiveBlockState.getBlock()) {
                case ShaftBlock shaftBlock: {
                    return lastCapacityProvided = (float)BlockStressValues.getCapacity(shaftBlock);
                } case SimpleBevelCogWheelBlock bevelBlock: {
                    return lastCapacityProvided = bevelBlock.getStressCapacity(effectiveBlockState);
                } default: {
                    return super.calculateAddedStressCapacity();
                }
            }
        };

        @Override
        public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
            return SimpleBevelCogWheelBlockEntity.propagateRotationTo(this, target, stateFrom, stateTo, diff, connectedViaAxes);
        };

        @Override
        public boolean isValidBlockState(BlockState state) {
            return state.getBlock() instanceof IOrthogonalBevelCogWheelBlock || state.getBlock() instanceof ShaftBlock;
        };

        @Override
        public BlockState getBlockState() {
            return effectiveBlockState == null ? CompositeBevelCogWheelBlockEntity.super.getBlockState() : effectiveBlockState;
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false;
        };

        @Override
        public int getIndex() {
            return index;
        };

    };

    // @Override
    // public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
    //     if (!(getBlockState().getBlock() instanceof CompositeBevelCogWheelBlock block)) throw new IllegalStateException("Must be a Composite Bevel Cogwheel block");
    //     final BevelCogWheelPart part = block.getTargetedPart(getBlockState(), getBlockPos(), null);
    // };

    // @Override
    // public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
    //     // TODO Auto-generated method stub
    //     return IHaveGoggleInformation.super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    // };
    
};
