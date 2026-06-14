package com.petrolpark.petrolsparts.content.kinetics.movement;

import java.util.List;

import com.petrolpark.compat.create.core.block.composite.CompositeKineticBlockEntity;
import com.petrolpark.core.world.block.DummyBlock;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.PetrolsPartsDataMapTypes;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.clock.CuckooClockBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.animation.LerpedFloat.Chaser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class MovementBlockEntity extends CompositeKineticBlockEntity {

    public final WindingPart windingPart = new WindingPart();
    public final GeneratingPart generatingPart = new GeneratingPart();

    protected final List<CompositeKineticBlockEntityPart> parts = List.of(generatingPart, windingPart);

    protected float rotationsCharge = 0;
    protected ItemStack weightStack = ItemStack.EMPTY;
    protected MovementWeightData weightData = null;

    protected LerpedFloat weightChainLength = LerpedFloat.linear().chase(0f, 0.01f, Chaser.LINEAR);

    public MovementBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        generatingPart.setBlockState(state);
        windingPart.setBlockState(state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    };

    public float getMaxRotationsCharge() {
        return 900f;
    };

    public float getBaseRotationSpeed() {
        return 16f;  
    };

    @SuppressWarnings("deprecation")
    public void setWeightStack(ItemStack newWeightStack) {
        weightStack = newWeightStack;
        weightData = newWeightStack.getItem().builtInRegistryHolder().getData(PetrolsPartsDataMapTypes.MOVEMENT_WEIGHT);
        if (weightData == null) rotationsCharge = 0f;
    };

    @Override
    public void tick() {
        final float chargeBefore = rotationsCharge;
        super.tick();
        weightChainLength.updateChaseTarget(Mth.clamp(rotationsCharge / getMaxRotationsCharge(), 0f, 1f));
        if (chargeBefore != rotationsCharge) sendData(); // In order to sync animation TODO check if this is necessary as its ticked on client anyway
    };

    public boolean isFullyCharged() {
        return rotationsCharge >= getMaxRotationsCharge();
    };

    @Override
    public List<CompositeKineticBlockEntityPart> getParts() {
        return parts;
    };

    @Override
    @SuppressWarnings("deprecation")
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        rotationsCharge = tag.getFloat("StoredRotations");
        if (tag.contains("Weight", Tag.TAG_COMPOUND)) weightStack = ItemStack.SINGLE_ITEM_CODEC.parse(NbtOps.INSTANCE, tag.getCompound("Weight")).getPartialOrThrow();
        else weightStack = ItemStack.EMPTY;
        weightData = weightStack.getItem().builtInRegistryHolder().getData(PetrolsPartsDataMapTypes.MOVEMENT_WEIGHT);
    };

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        tag.putFloat("StoredRotations", rotationsCharge);
        if (!weightStack.isEmpty()) tag.put("Weight", ItemStack.SINGLE_ITEM_CODEC.encodeStart(NbtOps.INSTANCE, weightStack).getOrThrow());
    };
    
    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().expandTowards(0, -1, 0);
    };

    public class WindingPart extends CompositeKineticBlockEntityPart {

        protected final DummyShaftEndBlock dummyBlock = new DummyShaftEndBlock();

        public WindingPart() {
            super(PetrolsPartsBlockEntityTypes.MOVEMENT_WINDING_PART.get());
        };

        @Override
        public float calculateStressApplied() {
            return weightData == null ? 0f : weightData.stressCapacity();
        };

        @Override
        public void tick() {
            super.tick();
            final float speed = Mth.abs(getSpeed());
            if (speed != 0f && weightData != null) {
                final boolean updateOutput = rotationsCharge <= 0f;
                rotationsCharge += speed / (20 * 60); // Convert RPM to rotations per tick
                if (isFullyCharged()) {
                    rotationsCharge = getMaxRotationsCharge();
                    //TODO recalculate applied stress
                };
                //if (updateOutput) generatingPart.updateGeneratedRotation(); //TODO this might cause flickering
            };
        };

        @Override
        public void setBlockState(BlockState blockState) {
            dummyBlock.face = blockState.getValue(MovementBlock.FACING).getOpposite();
        };

        @Override
        public BlockState getBlockState() {
            return dummyBlock.defaultBlockState(); // To trick RotationPropagator
        }; 

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false;
        };

        @Override
        public boolean isValidBlockState(BlockState p_353131_) {
            return true;
        };

        @Override
        public int getIndex() {
            return 1;
        };

    };

    public class GeneratingPart extends GeneratingCompositeKineticBlockEntityPart {

        protected final DummyShaftEndBlock dummyBlock = new DummyShaftEndBlock();

        public GeneratingPart() {
            super(PetrolsPartsBlockEntityTypes.MOVEMENT_GENERATING_PART.get());
        };

        @Override
        public void initialize() {
            super.initialize();
            if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed()) updateGeneratedRotation();
        };

        @Override
        public float getGeneratedSpeed() {
            return getBaseRotationSpeed();
        };

        @Override
        public float calculateAddedStressCapacity() {
            return 16f;
            //return lastCapacityProvided = rotationsCharge > 0f || weightData == null ? 0f : weightData.stressCapacity();
        };

        @Override
        public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
            if (diff.equals(Direction.UP.getNormal()) && target instanceof CuckooClockBlockEntity) return 1f;
            return 0f; 
        };

        @Override
        public void tick() {
            // if (rotationsCharge > 0f) {
            //     rotationsCharge -= Math.abs(getSpeed()) * 20 * 60; // Convert RPM to rotations per tick
            //     if (rotationsCharge < 0f) { // Depleted
            //         updateGeneratedRotation();
            //         rotationsCharge = 0f;
            //     };
            // };
            super.tick();
        };

        @Override
        public void setBlockState(BlockState blockState) {
            dummyBlock.face = blockState.getValue(MovementBlock.FACING);
        };

        @Override
        public BlockState getBlockState() {
            return dummyBlock.defaultBlockState(); // To trick RotationPropagator
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false;
        };

        @Override
        public boolean isValidBlockState(BlockState p_353131_) {
            return true;
        };

        @Override
        public int getIndex() {
            return 0;
        };

    };


    class DummyShaftEndBlock extends DummyBlock implements IRotate {

        protected Direction face = Direction.UP;

        public DummyShaftEndBlock() {
            super(BlockBehaviour.Properties.of());
        };

        @Override
        public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
            return face == this.face;
        };

        @Override
        public Axis getRotationAxis(BlockState state) {
            return getBlockState().getValue(MovementBlock.FACING).getAxis();
        };

    };
    
};
