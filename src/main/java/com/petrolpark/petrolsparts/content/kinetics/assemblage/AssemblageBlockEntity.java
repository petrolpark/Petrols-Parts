package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.petrolpark.compat.create.core.block.composite.CompositeKineticBlockEntity;
import com.petrolpark.petrolsparts.core.block.CogType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class AssemblageBlockEntity extends CompositeKineticBlockEntity {

    protected List<CompositeKineticBlockEntityPart> parts;

    public AssemblageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public List<CompositeKineticBlockEntityPart> getParts() {
        if (parts == null) recalculateParts();
        return parts == null ? Collections.emptyList() : parts;
    };

    protected void recalculateParts() {
        final BlockState state = getBlockState();
        if (!(state.getBlock() instanceof IAssemblageBlock block)) return;

        final AssemblageCog topCog = state.getValue(IAssemblageBlock.TOP_COG);
        final AssemblageCog middleCog = state.getValue(IAssemblageBlock.MIDDLE_COG);
        final AssemblageCog bottomCog = state.getValue(IAssemblageBlock.BOTTOM_COG);

        final Set<AssemblageBlockEntityPart> parts = new HashSet<>();

        final AssemblageBlockEntityPart topShaftPart = new AssemblageBlockEntityPart();
        if (block.hasTopShaft(state)) {
            topShaftPart.withTopShaft();
            parts.add(topShaftPart);
        };
        topShaftPart.trackedProperties.add(IAssemblageBlock.TOP_SHAFT_HALF);

        final AssemblageBlockEntityPart topCogPart = topCog.hasShaftConnection() ? topShaftPart.withTopShaft() : new AssemblageBlockEntityPart();
        topCogPart.topCogType = topCog.getCogType();
        if (!topCogPart.topCogType.isNone()) parts.add(topCogPart);
        topCogPart.trackedProperties.add(IAssemblageBlock.TOP_COG);

        final AssemblageBlockEntityPart bottomShaftPart = block.hasTopShaft(state) ? topShaftPart : new AssemblageBlockEntityPart();
        if (block.hasBottomShaft(state)) {
            bottomShaftPart.withBottomShaft();
            parts.add(bottomShaftPart);
        };
        bottomShaftPart.trackedProperties.add(IAssemblageBlock.BOTTOM_SHAFT_HALF);

        final AssemblageBlockEntityPart bottomCogPart = bottomCog.hasShaftConnection() ? bottomShaftPart.withBottomShaft() : new AssemblageBlockEntityPart();
        bottomCogPart.bottomCogType = bottomCog.getCogType();
        if (!bottomCogPart.bottomCogType.isNone()) parts.add(bottomCogPart);
        bottomCogPart.trackedProperties.add(IAssemblageBlock.BOTTOM_COG);

        final AssemblageBlockEntityPart middleCogPart = middleCog.hasShaftConnection()
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

    public class AssemblageBlockEntityPart extends CompositeKineticBlockEntityPart {

        protected final Set<Property<?>> trackedProperties = new HashSet<>();

        protected boolean topShaft = false;
        protected boolean bottomShaft = false;
        protected CogType topCogType = CogType.NONE;
        protected CogType middleCogType = CogType.NONE;
        protected CogType bottomCogType = CogType.NONE;

        public AssemblageBlockEntityPart() {
            super(null); //TODO
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

    };
    
};
