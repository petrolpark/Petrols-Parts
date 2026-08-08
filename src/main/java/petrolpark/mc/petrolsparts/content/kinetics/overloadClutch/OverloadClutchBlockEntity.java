package petrolpark.mc.petrolsparts.content.kinetics.overloadClutch;

import java.util.List;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.core.world.block.DummyBlock;
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.petrolsparts.PetrolsParts;
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
        behaviours.add(stressSetting = new ScrollValueBehaviour(PetrolsParts.translate("gui.overload_clutch.stress_impact"), this, new CenteredSideValueBoxTransform((s, f) -> f.getAxis() != s.getValue(OverloadClutchBlock.FACING).getAxis()))
            .between(0, 64)
            .withFormatter(i -> Lang.TWO_DP_DF.format((float)i * 0.25f))
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
        if (redstonePower >= 15) return 0f;
        return 0.25f * stressSetting.value * (15f - redstonePower) / 15f;
    };

    public Direction getFacing() {
        return getBlockState().getValue(OverloadClutchBlock.FACING);
    };

    public class GeneratingPart extends GeneratingCompositeKineticBlockEntityPart {

        final BlockState effectiveState = new GeneratingPart.DummyShaftBlock().defaultBlockState();

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
        public BlockState getBlockState() {
            return effectiveState == null ? OverloadClutchBlockEntity.super.getBlockState() : effectiveState;
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false;
        };

        @Override
        public int getIndex() {
            return 0;
        };

        class DummyShaftBlock extends DummyBlock implements IRotate {

            protected DummyShaftBlock() {
                super(BlockBehaviour.Properties.of());
            };

            @Override
            public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
                return face == getFacing();
            };

            @Override
            public Axis getRotationAxis(BlockState state) {
                return getFacing().getAxis();
            };

        };

    };

    public class ImpactPart extends CompositeKineticBlockEntityPart {

        final BlockState effectiveState = new ImpactPart.DummyShaftBlock().defaultBlockState();

        public ImpactPart() {
            super(PetrolsPartsBlockEntityTypes.OVERLOAD_CLUTCH_IMPACT_PART.get());
        };

        @Override
        public void setSpeed(float speed) {
            super.setSpeed(speed);
            generatingPart.updateGeneratedRotation();
        };

        @Override
        public float calculateStressApplied() {
            return lastStressApplied = getStressImpact();
        };

        @Override
        public BlockState getBlockState() {
            return effectiveState == null ? OverloadClutchBlockEntity.super.getBlockState() : effectiveState;
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false;
        };

        @Override
        public int getIndex() {
            return 1;
        };

        class DummyShaftBlock extends DummyBlock implements IRotate {

            protected DummyShaftBlock() {
                super(BlockBehaviour.Properties.of());
            };

            @Override
            public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
                return face == getFacing().getOpposite();
            };

            @Override
            public Axis getRotationAxis(BlockState state) {
                return getFacing().getAxis();
            };

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
    
};
