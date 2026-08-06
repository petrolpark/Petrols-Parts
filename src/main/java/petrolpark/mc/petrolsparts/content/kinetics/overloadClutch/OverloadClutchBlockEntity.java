package petrolpark.mc.petrolsparts.content.kinetics.overloadClutch;

import java.util.List;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public class OverloadClutchBlockEntity extends CompositeKineticBlockEntity {

    protected ScrollValueBehaviour stressSetting;
    protected int redstonePower = 0;

    protected final OverloadClutchBlockEntity.GeneratingPart generatingPart;
    protected final OverloadClutchBlockEntity.ImpactPart impactPart;

    private final List<? extends CompositeKineticBlockEntityPart> parts;

    public OverloadClutchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        parts = List.of(
            generatingPart = new GeneratingPart(),
            impactPart = new ImpactPart()
        );
    }; 

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(stressSetting = new ScrollValueBehaviour(null, this, new OverloadClutchBlockEntity.ValueBox())
            .between(0, 64)
            .withFormatter(i -> Lang.TWO_DP_DF.format((float)i * 0.25f) + "x")
            .withCallback($ -> update())
        );
        stressSetting.value = 4;
    };

    @Override
    public List<? extends CompositeKineticBlockEntityPart> getParts() {
        return parts;
    };

    public void update() {
        impactPart.detachKinetics();
        impactPart.updateSpeed = true;
        generatingPart.updateGeneratedRotation();
    };

    public float getStressImpact() {
        return Math.max(redstonePower, 0.25f * stressSetting.value);
    };

    public class GeneratingPart extends GeneratingCompositeKineticBlockEntityPart {

        public GeneratingPart() {
            super(PetrolsPartsBlockEntityTypes.OVERLOAD_CLUTCH_GENERATING_PART.get());
        };

        @Override
        public float calculateAddedStressCapacity() {
            return lastCapacityProvided = impactPart.getSpeed() == 0f ? 0f : getStressImpact();
        };

        @Override
        public float getGeneratedSpeed() {
            return impactPart.getSpeed();
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false;
        };

        @Override
        public int getIndex() {
            return 0;
        };

    };

    public class ImpactPart extends CompositeKineticBlockEntityPart {

        public ImpactPart() {
            super(PetrolsPartsBlockEntityTypes.OVERLOAD_CLUTCH_IMPACT_PART.get());
        };

        @Override
        public void setSpeed(float speed) {
            super.setSpeed(speed);
            generatingPart.onSpeedChanged(generatingPart.getSpeed()); // Check for overstressing
        };

        @Override
        public float calculateStressApplied() {
            return lastStressApplied = getStressImpact();
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false;
        };

        @Override
        public int getIndex() {
            return 1;
        };

    };

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        redstonePower = tag.contains("RedstonePower", Tag.TAG_BYTE) ? tag.getByte("RedstonePower") : 0;
    };

    @Override
    protected void write(CompoundTag tag, Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        if (redstonePower > 0) tag.putByte("RedstonePower", (byte)redstonePower);
    };

    static class ValueBox extends ValueBoxTransform.Sided {

        static final Vec3 SOUTH_LOCATION = new Vec3(8d, 8d, 16.05d);

        @Override
        protected Vec3 getSouthLocation() {
            return SOUTH_LOCATION;
        };

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction.getAxis() != state.getValue(OverloadClutchBlock.FACING).getAxis();
        };
        
    };
    
};
