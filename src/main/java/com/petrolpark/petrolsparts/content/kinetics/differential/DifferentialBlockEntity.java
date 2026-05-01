package com.petrolpark.petrolsparts.content.kinetics.differential;

import java.util.List;
import java.util.Objects;

import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementBehaviour;
import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementTriggers;
import com.petrolpark.petrolsparts.core.block.DirectionalRotatedPillarKineticBlock;
import com.petrolpark.util.KineticsHelper;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public class DifferentialBlockEntity extends GeneratingKineticBlockEntity {

    public PetrolsPartsAdvancementBehaviour advancementBehaviour;

    private DifferentialSink inputSink;
    private DifferentialSink controlSink;
    private Long lastInputNetwork = null;
    private Long lastControlNetwork = null;

    public DifferentialBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        advancementBehaviour = new PetrolsPartsAdvancementBehaviour(this, PetrolsPartsAdvancementTriggers.DIFFERENTIAL);
        behaviours.add(advancementBehaviour);
    };

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            inputSink = new DifferentialSink(this);
            controlSink = new DifferentialSink(this);
        }
    };

    @Override
    public float calculateAddedStressCapacity() {
        if (!hasLevel() || speed == 0f) return 0f;

        Direction face = DirectionalRotatedPillarKineticBlock.getDirection(getBlockState());
        KineticBlockEntity kbeA = getAdjacentKBE(getBlockPos().relative(face), face.getOpposite());
        KineticBlockEntity kbeB = getAdjacentKBE(getBlockPos().relative(face.getOpposite()), face);
        float absA = kbeA != null ? Math.abs(getPropagatedSpeed(kbeA, face.getOpposite())) : 0f;
        float absB = kbeB != null ? Math.abs(getPropagatedSpeed(kbeB, face)) : 0f;

        float capacityA = absA > 0f ? getNetworkCapacity(inputSink, lastInputNetwork) : 0f;
        float capacityB = absB > 0f
                ? (Objects.equals(lastInputNetwork, lastControlNetwork) ? 0f : getNetworkCapacity(controlSink, lastControlNetwork))
                : 0f;
        float coefficient = (capacityA + capacityB) / Math.abs(speed);
        this.lastCapacityProvided = coefficient;
        return coefficient;
    };

    /**
     * Returns the current capacity (in SU) of the KineticNetwork identified by {@code networkId}.
     * Reads directly from the network object (no per-tick caching lag) via {@link KineticNetworkAccessor}.
     */
    private float getNetworkCapacity(DifferentialSink sink, Long networkId) {
        if (sink == null || networkId == null) return 0f;
        Long previous = sink.network;
        sink.network = networkId;
        KineticNetwork net = sink.getOrCreateNetwork();
        sink.network = previous;
        if (net == null) return 0f;
        return ((com.petrolpark.petrolsparts.mixin.accessor.KineticNetworkAccessor) net).getCurrentCapacity();
    };

    /**
     * Returns the current stress (in SU) of the KineticNetwork identified by {@code networkId}.
     */
    private float getNetworkStress(DifferentialSink sink, Long networkId) {
        if (sink == null || networkId == null) return 0f;
        Long previous = sink.network;
        sink.network = networkId;
        KineticNetwork net = sink.getOrCreateNetwork();
        sink.network = previous;
        if (net == null) return 0f;
        return ((com.petrolpark.petrolsparts.mixin.accessor.KineticNetworkAccessor) net).getCurrentStress();
    };

    @Override
    public float getGeneratedSpeed() {
        if (!hasLevel()) return 0f;
        Direction face = DirectionalRotatedPillarKineticBlock.getDirection(getBlockState());
        BlockPos sideAPos = getBlockPos().relative(face);
        BlockPos sideBPos = getBlockPos().relative(face.getOpposite());
        float speedA = 0f, speedB = 0f;
        BlockEntity beA = level.getBlockEntity(sideAPos);
        BlockEntity beB = level.getBlockEntity(sideBPos);
        if (propagatesToMe(sideAPos, face.getOpposite()) && beA instanceof KineticBlockEntity kbeA)
            speedA = getPropagatedSpeed(kbeA, face.getOpposite());
        if (propagatesToMe(sideBPos, face) && beB instanceof KineticBlockEntity kbeB)
            speedB = getPropagatedSpeed(kbeB, face);
        return (speedA + speedB) / 2f;
    };

    @Override
    @SuppressWarnings("null")
    public void tick() {
        super.tick();
        if (level.isClientSide || inputSink == null) return;

        Direction face = DirectionalRotatedPillarKineticBlock.getDirection(getBlockState());
        BlockPos sideAPos = getBlockPos().relative(face);
        BlockPos sideBPos = getBlockPos().relative(face.getOpposite());
        KineticBlockEntity kbeA = getAdjacentKBE(sideAPos, face.getOpposite());
        KineticBlockEntity kbeB = getAdjacentKBE(sideBPos, face);

        float newSpeed = getGeneratedSpeed();
        if (newSpeed != speed) updateGeneratedRotation();
        Long netA = kbeA != null ? kbeA.network : null;
        Long netB = kbeB != null ? kbeB.network : null;

        if (!Objects.equals(netA, lastInputNetwork)) {
            removeSink(inputSink, lastInputNetwork);
            injectSink(inputSink, netA);
            lastInputNetwork = netA;
        }
        if (!Objects.equals(netB, lastControlNetwork)) {
            removeSink(controlSink, lastControlNetwork);
            injectSink(controlSink, netB);
            lastControlNetwork = netB;
        }

        // Proportional split with capacity-aware redistribution:
        // Each side's requested share is proportional to its speed contribution.
        // If one network is at capacity, clamp it to its available headroom and
        // push the remainder to the other side. If neither network can absorb the full
        // demand, both become overstressed
        float absA = kbeA != null ? Math.abs(getPropagatedSpeed(kbeA, face.getOpposite())) : 0f;
        float absB = kbeB != null ? Math.abs(getPropagatedSpeed(kbeB, face)) : 0f;
        float totalAbs = absA + absB;
        float totalDemand = this.stress;

        float requestedA = totalAbs > 0f ? totalDemand * (absA / totalAbs) : totalDemand * 0.5f;
        float requestedB = totalAbs > 0f ? totalDemand * (absB / totalAbs) : totalDemand * 0.5f;

        // Available headroom on each input network.
        float capacityA = absA > 0f ? getNetworkCapacity(inputSink, lastInputNetwork) : 0f;
        float stressA   = absA > 0f ? getNetworkStress(inputSink, lastInputNetwork)   : 0f;
        float capacityB = absB > 0f ? getNetworkCapacity(controlSink, lastControlNetwork) : 0f;
        float stressB   = absB > 0f ? getNetworkStress(controlSink, lastControlNetwork)   : 0f;
        float sinkOwnDrawA = inputSink  != null ? inputSink.getStressImpact()  : 0f;
        float sinkOwnDrawB = controlSink != null ? controlSink.getStressImpact() : 0f;
        float headroomA = absA > 0f ? Math.max(0f, capacityA - (stressA - sinkOwnDrawA)) : 0f;
        float headroomB = absB > 0f ? Math.max(0f, capacityB - (stressB - sinkOwnDrawB)) : 0f;

        float allocA = distributeStress(requestedA, requestedB, headroomA, headroomB)[0];
        float allocB = distributeStress(requestedA, requestedB, headroomA, headroomB)[1];

        updateSinkStress(inputSink,   lastInputNetwork,   allocA);
        updateSinkStress(controlSink, lastControlNetwork, allocB);
    };

    @Nullable
    private KineticBlockEntity getAdjacentKBE(BlockPos pos, Direction directionToMe) {
        if (!propagatesToMe(pos, directionToMe)) return null;
        BlockEntity be = level.getBlockEntity(pos);
        return be instanceof KineticBlockEntity kbe ? kbe : null;
    };

    private void injectSink(DifferentialSink sink, Long networkId) {
        if (networkId == null) return;
        sink.network = networkId;
        KineticNetwork net = sink.getOrCreateNetwork();
        if (net == null) { sink.network = null; return; }
        boolean alreadyPresent = net.members.containsKey(sink);
        if (!alreadyPresent) net.members.put(sink, 0f);
        net.updateStress();
    };

    private void removeSink(DifferentialSink sink, Long networkId) {
        if (sink == null || networkId == null) return;
        sink.network = networkId;
        KineticNetwork net = sink.getOrCreateNetwork();
        if (net != null) {
            net.members.remove(sink);
            net.sources.remove(sink);
            net.updateStress();
        }
        sink.network = null;
    };

    private void updateSinkStress(DifferentialSink sink, Long networkId, float absoluteSU) {
        if (networkId == null || sink.network == null) return;
        sink.setStressImpact(absoluteSU);
        KineticNetwork net = sink.getOrCreateNetwork();
        // updateStressFor: members.put(sink, absoluteSU) + updateStress()
        // KineticNetworkMixin bypasses identity-check eviction for DifferentialSink
        if (net != null) net.updateStressFor(sink, absoluteSU);
    };

    private float[] distributeStress(float requestedA, float requestedB, float headroomA, float headroomB) {
        float allocA = requestedA;
        float allocB = requestedB;

        if (allocA > headroomA) {
            float spill = allocA - headroomA;
            allocA = headroomA;
            allocB += spill;
        }

        if (allocB > headroomB) {
            float spill = allocB - headroomB;
            allocB = headroomB;
            allocA += spill;

            if (allocA > headroomA && allocA - headroomA < 1f) {
                allocA = headroomA;
            }
        }

        return new float[]{allocA, allocB};
    };

    @Override
    public void remove() {
        if (!level.isClientSide) {
            removeSink(inputSink, lastInputNetwork);
            removeSink(controlSink, lastControlNetwork);
        }
        super.remove();
    };

    @Override
    public void onChunkUnloaded() {
        if (!level.isClientSide) {
            removeSink(inputSink, lastInputNetwork);
            removeSink(controlSink, lastControlNetwork);
        }
        super.onChunkUnloaded();
    };

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if (speed == 0f || advancementBehaviour == null || advancementBehaviour.getPlayer() == null) return;
        Direction face = DirectionalRotatedPillarKineticBlock.getDirection(getBlockState());
        boolean sideAActive = getAdjacentKBE(getBlockPos().relative(face), face.getOpposite()) != null;
        boolean sideBActive = getAdjacentKBE(getBlockPos().relative(face.getOpposite()), face) != null;
        if (sideAActive && sideBActive) advancementBehaviour.awardAdvancement(PetrolsPartsAdvancementTriggers.DIFFERENTIAL);
    };

    @SuppressWarnings("null")
    public boolean propagatesToMe(BlockPos pos, Direction directionToMe) {
        if (!hasLevel()) return false;
        BlockState state = getLevel().getBlockState(pos);
        return state.getBlock() instanceof KineticBlock kineticBlock && kineticBlock.hasShaftTowards(getLevel(), pos, state, directionToMe);
    };

    public float getPropagatedSpeed(KineticBlockEntity from, Direction directionToMe) {
        if (from instanceof DifferentialBlockEntity) return 0f;

        return from.getSpeed() * com.petrolpark.petrolsparts.mixin.accessor.RotationPropagatorAccessor.invokeGetAxisModifier(from, directionToMe);
    };

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        super.addPropagationLocations(block, state, neighbours);
        KineticsHelper.addLargeCogwheelPropagationLocations(worldPosition, neighbours);
        return neighbours;
    };

    @Override
    protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
        return true;
    };

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(1);
    };

};
