package petrolpark.mc.petrolsparts.content.kinetics.differential;

import java.util.List;
import java.util.Objects;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.compat.create.core.world.block.entity.IKineticBlockEntityDuck;
import petrolpark.mc.library.compat.create.core.world.block.entity.IOverridableKineticBlockEntity;
import petrolpark.mc.library.core.world.block.DummyBlock;
import petrolpark.mc.library.util.KineticsHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public class DifferentialBlockEntity extends CompositeKineticBlockEntity {

    protected final DifferentialBlockEntity.Part topCog, bottomCog, ringCog;
    protected final List<DifferentialBlockEntity.Part> parts;

    protected boolean resetting = true;

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
        if (resetting) {
            for (DifferentialBlockEntity.Part part : getParts()) {
                part.detachKinetics();
                part.updateSpeed = true; // Re-attach next tick
                if (part.hasSource() && Objects.equals(part.source, getBlockPos())) part.removeSource(); // Same-pos sources are only there so the speed doesn't get wiped, they are not true sources
            };
            if (topCog.hasSource() || bottomCog.hasSource() || ringCog.hasSource()) { // Anything powered
                if (ringCog.getSpeed() == (topCog.getSpeed() + bottomCog.getSpeed()) / 2f ) { // All matching speeds

                } else { // Not all matching speeds
                    if (topCog.hasSource() && bottomCog.hasSource() && ringCog.hasSource()) {
                        getLevel().destroyBlock(getBlockPos(), true); // All three powered with non-matching speeds -> impossible
                    } else {
                        if (ringCog.hasSource()) {
                            if (topCog.hasSource()) { // Ring & top powered
                                bottomCog.forceSpeed(2 * ringCog.getSpeed() - topCog.getSpeed());
                                setSource(bottomCog, ringCog);
                            } else if (bottomCog.hasSource()) { // Ring && bottom powered
                                topCog.forceSpeed(2 * ringCog.getSpeed() - bottomCog.getSpeed());
                                setSource(topCog, ringCog);
                            } else { // Ring only powered - rotate other two gears at same speed
                                topCog.forceSpeed(ringCog.getSpeed());
                                setSource(topCog, ringCog);
                                bottomCog.forceSpeed(ringCog.getSpeed());
                                setSource(bottomCog, ringCog);
                            };
                        } else if (topCog.hasSource()) {
                            if (bottomCog.hasSource()) { // Top & bottom powered
                                ringCog.forceSpeed((topCog.getSpeed() + bottomCog.getSpeed()) / 2f);
                                setSource(ringCog, topCog);
                            } else { // Top only powered - rotate other two gears at same speed
                                ringCog.forceSpeed(topCog.getSpeed());
                                setSource(ringCog, topCog);
                                bottomCog.forceSpeed(topCog.getSpeed()); // Should already be so
                                setSource(bottomCog, topCog);
                            };
                        } else { // Bottom only powered - rotate other two gears at same speed
                            ringCog.forceSpeed(bottomCog.getSpeed());
                            setSource(ringCog, bottomCog);
                            topCog.forceSpeed(bottomCog.getSpeed()); // Should already be so
                            setSource(topCog, bottomCog);
                        };
                    };
                };
            };
            resetting = false;
            notifyUpdate();
        };

        super.tick();
    };

    // Need to set a source so the imposed speed doesn't get wiped. Not a real source as having a source in the same blockpos is inadvisable
    protected static void setSource(DifferentialBlockEntity.Part base, DifferentialBlockEntity.Part toCopy) {
        ((IKineticBlockEntityDuck)base).setSourceIndex(toCopy.getIndex());
        base.setSource(toCopy.getBlockPos());
    };
    
    public class Part extends CompositeKineticBlockEntityPart implements IOverridableKineticBlockEntity {

        private final int index;
        protected final BlockState dummyBlockState = new DummyCogWheelBlock().defaultBlockState()
            .setValue(CogWheelBlock.AXIS, DifferentialBlockEntity.super.getBlockState().getValue(DifferentialBlock.AXIS));

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
            resetting = true;
            super.setSpeed(speed);
        };

        @Override
        public void setSource(BlockPos source) {
            if (resetting && hasSource()) { // Shouldn't change source while resetting
                getLevel().destroyBlock(getBlockPos(), true);
                return;
            }; 
            super.setSource(source);
        };

        @Override
        public void removeSource() {
            resetting = true;
            super.removeSource();
        };

        @Override
        public boolean isSourceAlwaysOverridable() {
            return Objects.equals(source, getBlockPos());
        };

        @Override
        public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
            KineticsHelper.addLargeCogwheelPropagationLocations(getBlockPos(), neighbours);
            return super.addPropagationLocations(block, state, neighbours);
        };

        @Override
        public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
            if (!resetting) return 0f; // Default behaviour if not mid-reset
            // getSpeed from this point on is the forced speed each part should have

            if (this != (connectedViaAxes
                ? Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ()).getAxisDirection() == AxisDirection.POSITIVE
                    ? topCog
                    : bottomCog
                : ringCog)
            ) return 0f;

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

            return ratio;
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
            protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
                super.createBlockStateDefinition(builder.add(CogWheelBlock.AXIS));
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

    // /**
    //  * If this {@link KineticBlockEntity} is or is ultimately powered by a part of a Differential,
    //  * then its speed is always overrideable
    //  * @param kbe
    //  */
    // public static final boolean shouldBeOverridden(KineticBlockEntity kbe) {
    //     final KineticBlockEntity original = kbe;
    //     while (kbe.hasSource()) {
    //         if (kbe instanceof DifferentialBlockEntity.Part && Objects.equals(kbe.source, kbe.getBlockPos())) return true;
    //         final BlockEntity source = kbe.getLevel().getBlockEntity(kbe.source);
    //         if (source == null) return false;
    //         if (source instanceof KineticBlockEntity sourceKbe && sourceKbe != original) {
    //             kbe = sourceKbe;
    //             continue;
    //         } else if (source instanceof CompositeKineticBlockEntity sourceComposite) {
    //             final int sourceIndex = ((IKineticBlockEntityDuck)kbe).getSourceIndex();
    //             if (sourceIndex >= 0 && sourceIndex <= sourceComposite.getParts().size()) {
    //                 kbe = sourceComposite.getParts().get(sourceIndex);
    //                 if (kbe == original) return false; // loop
    //                 continue;
    //             };
    //         };
    //         return false;
    //     };
    //     return false;
    // };
};
