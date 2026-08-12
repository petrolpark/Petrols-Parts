package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleBevelCogWheelBlockEntity extends SingleAxisBevelCogWheelBlockEntity {

    public SimpleBevelCogWheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        return propagateRotationTo(this, target, stateFrom, stateTo, diff, connectedViaAxes);
    };

    /**
     * Mirrors vanilla Create's {@code RotationPropagator#getAxisModifier} handling for e.g. Gearboxes: since this
     * returns a non-zero value, it entirely replaces (rather than supplementing) the generic axis-connection ratio,
     * so it must account for {@code to}'s own internal structure as well as {@code from}'s - not just {@code from}'s,
     * otherwise a connection between two Bevel Cogwheel blocks would only ever be correct from one side.
     */
    public static final float propagateRotationTo(KineticBlockEntity from, KineticBlockEntity to, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes) {
        if (!connectedViaAxes) return 0f;
        final Direction face = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) return 0f; // Should never fail
        final float modifierFrom = getRotationRatio(from, stateFrom, face);
        final float modifierTo = getRotationRatio(to, stateTo, face.getOpposite());
        return modifierTo == 0f ? modifierFrom : modifierFrom / modifierTo;
    };

    /**
     * The ratio between the rotation speed at the given {@code face} of {@code from} and the rotation speed at the
     * face of {@code from} pointing towards its own {@link KineticBlockEntity#source}, if any (otherwise {@code 1f}).
     * Used both to propagate rotation to neighbours and by {@link SimpleBevelCogWheelRenderer} to determine the
     * visual rotation of each cog face, so the two must stay in sync.
     */
    public static float getRotationRatio(KineticBlockEntity from, BlockState stateFrom, Direction face) {
        if (from.getSpeed() == 0f || !from.hasSource()) return 1f;
        final BlockPos sourceDiff = from.source.subtract(from.getBlockPos());
        final Direction sourceFace = Direction.fromDelta(sourceDiff.getX(), sourceDiff.getY(), sourceDiff.getZ());
        if (sourceFace == null) return 1f;
        if (!(stateFrom.getBlock() instanceof SimpleBevelCogWheelBlock bevelBlock)) return 1f; // e.g. a plain Shaft: always coaxial with its own source, never inverts

        final Direction effectiveSource = shaftStubEquivalent(bevelBlock, stateFrom, sourceFace);
        final Direction effectiveFace = shaftStubEquivalent(bevelBlock, stateFrom, face);

        if (effectiveSource.getAxis() != effectiveFace.getAxis())
            // Perpendicular cogs meshing at 90 degrees: invert iff they point into 'matching' octants
            return effectiveSource.getAxisDirection() == effectiveFace.getAxisDirection() ? -1f : 1f;
        // Same axis: either the same rigid Shaft (no invert), or two Cogs meshing only via a shared perpendicular
        // partner (a differential - the two ends always turn opposite ways, like Create's Gearbox/DirectionalShaftHalvesBlockEntity)
        return effectiveSource == effectiveFace ? 1f : -1f;
    };

    /**
     * If {@code face} is the bare Shaft stub opposite this block's driven Cog (i.e. has no Cog of its own, only a
     * plain Shaft connection), returns the face of the Cog it is rigidly keyed to instead. Otherwise, returns
     * {@code face} unchanged.
     */
    private static Direction shaftStubEquivalent(SimpleBevelCogWheelBlock block, BlockState state, Direction face) {
        final Axis shaftAxis = block.getShaftAxis(state);
        if (face.getAxis() == shaftAxis && face != block.getPrimaryCogFace(state)) return face.getOpposite();
        return face;
    };

};
