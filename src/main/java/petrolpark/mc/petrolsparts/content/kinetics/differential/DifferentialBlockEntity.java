package petrolpark.mc.petrolsparts.content.kinetics.differential;

import java.util.List;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.core.world.block.DummyBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public class DifferentialBlockEntity extends CompositeKineticBlockEntity {

    protected final DifferentialBlockEntity.Part topCog, bottomCog, ringCog;
    protected final List<DifferentialBlockEntity.Part> parts;

    protected boolean resetting = false;

    public DifferentialBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        parts = List.of(
            topCog = new DifferentialBlockEntity.Part(0),
            bottomCog = new DifferentialBlockEntity.Part(1),
            ringCog = new DifferentialBlockEntity.Part(2)
        );
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {};

    @Override
    public List<DifferentialBlockEntity.Part> getParts() {
        return parts;
    };

    @Override
    public void tick() {
        super.tick();
    };
    
    public class Part extends CompositeKineticBlockEntityPart {

        private final int index;
        protected final BlockState dummyBlockState = new DummyCogWheelBlock().defaultBlockState();

        public Part(int index) {
            super(PetrolsPartsBlockEntityTypes.DIFFERENTIAL_PART.get());
            this.index = index;
        };

        @Override
        public BlockState getBlockState() {
            return dummyBlockState == null ? DifferentialBlockEntity.super.getBlockState() : dummyBlockState; // dummyBlockState is null during initialization
        };

        protected void forceSpeed(float speed) {
            super.setSpeed(speed);
        };

        @Override
        public void setSpeed(float speed) {
            if (resetting && speed != this.speed) getLevel().destroyBlock(getBlockPos(), true); // Feedback loop -> destroy
            resetting = true;

            super.setSpeed(speed);

            // Set speeds of other components
            if ((!topCog.hasSource() && !bottomCog.hasSource() && !ringCog.hasSource()) || // Nothing powered (should be impossible at this point)
                (ringCog.getSpeed() == (topCog.getSpeed() + bottomCog.getSpeed()) / 2) // All matching speeds
            ) {
                resetting = false;
                return;
            }; 
            if (topCog.hasSource() && bottomCog.hasSource() && ringCog.hasSource()) {
                resetting = false;
                getLevel().destroyBlock(getBlockPos(), true); // All three powered with non-matching speeds -> impossible
                return;
            };
            if (ringCog.hasSource()) {
                if (topCog.hasSource()) { // Ring & top powered
                    bottomCog.forceSpeed(2 * ringCog.getSpeed() - topCog.getSpeed());
                } else if (bottomCog.hasSource()) { // Ring && bottom powered
                    topCog.forceSpeed(2 * ringCog.getSpeed() - bottomCog.getSpeed());
                } else { // Ring only powered - rotate other two gears at same speed
                    topCog.forceSpeed(ringCog.getSpeed());
                    bottomCog.forceSpeed(ringCog.getSpeed());
                };
            } else if (topCog.hasSource()) {
                if (bottomCog.hasSource()) { // Top & bottom powered
                    ringCog.forceSpeed((topCog.getSpeed() + bottomCog.getSpeed()) / 2f);
                } else { // Top only powered - behaves as if bottomCog is fixed at 0 RPM
                    ringCog.forceSpeed(topCog.getSpeed() / 2f);
                    bottomCog.forceSpeed(0f); // Should already be so 
                };
            } else { // Bottom only powered - behaves as if topCog is fixed at 0 RPM
                ringCog.forceSpeed(bottomCog.getSpeed() / 2f);
                topCog.forceSpeed(0f); // Should already be so
            };

            attachKinetics(); // Refresh kinetics: each part now propagates to each other part
            resetting = false;
        };

        @Override
        public void setSource(BlockPos source) {
            if (resetting) {
                getLevel().destroyBlock(getBlockPos(), true);
                return;
            }; // Shouldn't change source while resetting
            super.setSource(source);
        };

        @Override
        public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
            if (!resetting) return 0f; // Default behaviour if not mid-reset
            // getSpeed from this point on is the forced speed each part should have

            final float ratio;
            if (connectedViaAxes) {
                ratio = 1f;
            } else if (ICogWheel.isSmallCog(stateTo)) {
                ratio = -0.5f;
            } else if (ICogWheel.isLargeCog(stateTo)) { // Copied from RotationPropagator
                final Axis fromAxis = DifferentialBlockEntity.super.getBlockState().getValue(DifferentialBlock.AXIS);
                final Axis toAxis = stateTo.getValue(CogWheelBlock.AXIS);
                ratio = fromAxis.choose(diff.getX(), diff.getY(), diff.getZ()) > 0 ^ toAxis.choose(diff.getX(), diff.getY(), diff.getZ()) > 0 ? -1f : 1f;
            } else {
                return 0f; // Should be unreachable
            };

            final DifferentialBlockEntity.Part connectingPart = (connectedViaAxes
                ? Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ()).getAxisDirection() == AxisDirection.POSITIVE
                    ? topCog
                    : bottomCog
                : ringCog);

            if (this == connectingPart) return ratio;
            if (getSpeed() == 0f) return 0f; // Don't propagate at all

            return ratio * connectingPart.getSpeed() / getSpeed();
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false;
        };

        @Override
        public int getIndex() {
            return index;
        };

        class DummyCogWheelBlock extends DummyBlock implements ICogWheel {

            public DummyCogWheelBlock() {
                super(BlockBehaviour.Properties.of());
            };

            @Override
            public boolean isSmallCog() {
                return false;
            };

            @Override
            public boolean isLargeCog() {
                return DifferentialBlockEntity.Part.this == ringCog;
            };

            @Override
            public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
                if (face.getAxis() != getRotationAxis(state)) return false;
                // if (resetting) return true;
                if (DifferentialBlockEntity.Part.this == ringCog) return false;
                return DifferentialBlockEntity.Part.this == (face.getAxisDirection() == AxisDirection.POSITIVE ? topCog : bottomCog);
            };

            @Override
            public Axis getRotationAxis(BlockState state) {
                return DifferentialBlockEntity.super.getBlockState().getValue(DifferentialBlock.AXIS);
            };

        };
    };
};
