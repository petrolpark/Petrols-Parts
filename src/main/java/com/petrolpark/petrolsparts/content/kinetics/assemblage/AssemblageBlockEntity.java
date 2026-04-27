package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.petrolpark.compat.create.core.block.composite.CompositeKineticBlockEntity;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.core.block.CogType;
import com.petrolpark.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class AssemblageBlockEntity extends CompositeKineticBlockEntity {

    protected AssemblageBlockEntityPart topCogPart = null;
    protected AssemblageBlockEntityPart middleCogPart = null;
    protected AssemblageBlockEntityPart bottomCogPart = null;
    protected AssemblageBlockEntityPart topShaftPart = null;
    protected AssemblageBlockEntityPart bottomShaftPart = null;
    protected List<CompositeKineticBlockEntityPart> parts = null;

    public AssemblageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public List<CompositeKineticBlockEntityPart> getParts() {
        if (parts == null) calculateParts();
        return parts == null ? Collections.emptyList() : parts;
    };

    public void invalidateParts() {
        parts = null;
    };

    protected void calculateParts() {
        final BlockState state = getBlockState();
        if (!(state.getBlock() instanceof IAssemblageBlock block)) return;

        final AssemblageCog topCog = state.getValue(IAssemblageBlock.TOP_COG);
        final AssemblageCog middleCog = state.getValue(IAssemblageBlock.MIDDLE_COG);
        final AssemblageCog bottomCog = state.getValue(IAssemblageBlock.BOTTOM_COG);

        final Set<AssemblageBlockEntityPart> parts = new HashSet<>();

        topShaftPart = new AssemblageBlockEntityPart();
        if (block.hasTopShaft(state)) {
            topShaftPart.withTopShaft();
            parts.add(topShaftPart);
        };
        topShaftPart.trackedProperties.add(IAssemblageBlock.TOP_SHAFT_HALF);

        topCogPart = topCog.hasShaftConnection() ? topShaftPart.withTopShaft() : new AssemblageBlockEntityPart();
        topCogPart.topCogType = topCog.getCogType();
        if (!topCogPart.topCogType.isNone()) parts.add(topCogPart);
        topCogPart.trackedProperties.add(IAssemblageBlock.TOP_COG);

        bottomShaftPart = block.hasTopShaft(state) ? topShaftPart : new AssemblageBlockEntityPart();
        if (block.hasBottomShaft(state)) {
            bottomShaftPart.withBottomShaft();
            parts.add(bottomShaftPart);
        };
        bottomShaftPart.trackedProperties.add(IAssemblageBlock.BOTTOM_SHAFT_HALF);

        bottomCogPart = bottomCog.hasShaftConnection() ? bottomShaftPart.withBottomShaft() : new AssemblageBlockEntityPart();
        bottomCogPart.bottomCogType = bottomCog.getCogType();
        if (!bottomCogPart.bottomCogType.isNone()) parts.add(bottomCogPart);
        bottomCogPart.trackedProperties.add(IAssemblageBlock.BOTTOM_COG);

        middleCogPart = middleCog.hasShaftConnection()
            ? (block.hasTopShaft(state)
                ? topShaftPart
                : (block.hasBottomShaft(state)
                    ? bottomShaftPart
                    : new AssemblageBlockEntityPart()
                ) 
            ) : new AssemblageBlockEntityPart();
        middleCogPart.middleCogType = middleCog.getCogType();
        if (!middleCogPart.middleCogType.isNone()) parts.add(middleCogPart);
        middleCogPart.trackedProperties.add(IAssemblageBlock.MIDDLE_COG);

        this.parts = new ArrayList<>(parts);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    };

    @Override
    protected void read(CompoundTag tag, Provider registries, boolean clientPacket) {
        getParts(); // So we have the right list
        super.read(tag, registries, clientPacket);
    };

    public class AssemblageBlockEntityPart extends CompositeKineticBlockEntityPart implements IFaceAlignedCogWheelBlockEntity {

        protected final Set<Property<?>> trackedProperties = new HashSet<>();

        protected boolean topShaft = false;
        protected boolean bottomShaft = false;
        protected CogType topCogType = CogType.NONE;
        protected CogType middleCogType = CogType.NONE;
        protected CogType bottomCogType = CogType.NONE;

        protected final BlockState effectiveState = new DummyCogWheelBlock().defaultBlockState();

        public AssemblageBlockEntityPart() {
            super(PetrolsPartsBlockEntityTypes.ASSEMBLAGE_PART.get()); //TODO
        };

        public AssemblageBlockEntityPart withTopShaft() {
            topShaft = true;
            return this;
        };

        public AssemblageBlockEntityPart withBottomShaft() {
            bottomShaft = true;
            return this;
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            for (Property<?> property : trackedProperties) if (oldState.getValue(property) != state.getValue(property)) return false;
            return true;
        };

        @Override
        public boolean isValidBlockState(BlockState state) {
            return true;
        };

        /**
         * Trick {@link RotationPropagator} so we don't need to reimplement all the Cogwheel connection logic
         */
        @Override
        public BlockState getBlockState() {
            return effectiveState;
        };

        @Override
        public CogType getCogType(Direction face) {
            if (face.getAxis() == AssemblageBlockEntity.super.getBlockState().getValue(IAssemblageBlock.AXIS)) return face.getAxisDirection() == AxisDirection.POSITIVE ? topCogType : bottomCogType;
            return CogType.NONE;
        };

        @Override
        public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
            return IFaceAlignedCogWheelBlockEntity.propagateFaceAlignedCogwheels(this, target, diff);
        };

        public class DummyCogWheelBlock extends Block implements ICogWheel {

            public DummyCogWheelBlock() {
                super(BlockBehaviour.Properties.of());
            };

            @Override
            public boolean isSmallCog() {
                return middleCogType.isSmall();
            };
    
            @Override
            public boolean isLargeCog() {
                return middleCogType.isLarge();
            };

            @Override
            public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
                if (face.getAxis() != getRotationAxis(state)) return false;
                return face.getAxisDirection() == AxisDirection.POSITIVE ? topShaft : bottomShaft;
            };

            @Override
            public Axis getRotationAxis(BlockState state) {
                return AssemblageBlockEntity.super.getBlockState().getValue(IAssemblageBlock.AXIS);
            };

        };

    };
    
};
