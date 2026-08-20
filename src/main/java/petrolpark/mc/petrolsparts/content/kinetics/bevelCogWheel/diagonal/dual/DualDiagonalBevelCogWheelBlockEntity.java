package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual;

import java.util.List;
import java.util.stream.IntStream;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.IDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.IDiagonalBevelCogWheelBlockEntity;

public class DualDiagonalBevelCogWheelBlockEntity extends CompositeKineticBlockEntity {

    protected List<DualDiagonalBevelCogWheelBlockEntity.Part> parts;
    
    public DualDiagonalBevelCogWheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setPartsFromBlockState(state);
    };

    @Override
    public List<DualDiagonalBevelCogWheelBlockEntity.Part> getParts() {
        return parts;
    };

    @Override
    public void setBlockState(BlockState blockState) {
        if (parts == null || parts.get(0).getOrientation() != IDualDiagonalBevelCogWheelBlock.getCogOrientations(blockState)[0]) setPartsFromBlockState(blockState);
        super.setBlockState(blockState);
    };

    protected void setPartsFromBlockState(BlockState state) {
        final Orientation[] orientations = IDualDiagonalBevelCogWheelBlock.getCogOrientations(state);
        parts = IntStream.of(0, 1).mapToObj(i -> new Part(i, orientations[i])).toList();
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {};

    public class Part extends CompositeKineticBlockEntityPart implements IDiagonalBevelCogWheelBlockEntity {

        protected final int index;
        protected final Orientation orientation;

        protected Part(int index, Orientation orientation) {
            super(PetrolsPartsBlockEntityTypes.DUAL_DIAGONAL_BEVEL_COGWHEEL_PART.get());
            this.index = index;
            this.orientation = orientation;
        };

        @Override
        public Orientation getOrientation() {
            return orientation;
        };

        @Override
        protected Block getStressConfigKey() {
            return ((IDiagonalBevelCogWheelBlock)getBlockState().getBlock()).getSet().singleAxisBlock().get();
        };

        @Override
        public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
            return propagateRotationToCogWheel(target, stateTo, diff);
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
};
