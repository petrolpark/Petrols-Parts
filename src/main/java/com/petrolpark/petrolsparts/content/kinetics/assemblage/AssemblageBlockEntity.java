package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.petrolpark.Petrolpark;
import com.petrolpark.compat.create.core.block.composite.CompositeKineticBlockEntity;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.core.block.CogType;
import com.petrolpark.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

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
        if (hasLevel() && !getLevel().isClientSide()) getParts().forEach(CompositeKineticBlockEntityPart::remove);
        parts = null;
    };

    protected void calculateParts() {
        final BlockState state = getBlockState();
        if (!(state.getBlock() instanceof IAssemblageBlock block)) return;

        final AssemblageCog topCog = state.getValue(IAssemblageBlock.TOP_COG);
        final AssemblageCog middleCog = state.getValue(IAssemblageBlock.MIDDLE_COG);
        final AssemblageCog bottomCog = state.getValue(IAssemblageBlock.BOTTOM_COG);

        topCogPart = middleCogPart = bottomCogPart = topShaftPart = bottomShaftPart = null;
        
        final Set<AssemblageBlockEntityPart> parts = new HashSet<>();

        topShaftPart = new AssemblageBlockEntityPart();
        if (block.hasTopShaft(state)) {
            topShaftPart.withTopShaft();
            parts.add(topShaftPart);
        };

        topCogPart = topCog.hasShaftConnection() ? topShaftPart.withTopShaft() : new AssemblageBlockEntityPart();
        topCogPart.topCogType = topCog.getCogType();
        if (!topCogPart.topCogType.isNone()) parts.add(topCogPart);

        bottomShaftPart = block.hasTopShaft(state) ? topShaftPart : new AssemblageBlockEntityPart();
        if (block.hasBottomShaft(state)) {
            bottomShaftPart.withBottomShaft();
            parts.add(bottomShaftPart);
        };

        bottomCogPart = bottomCog.hasShaftConnection() ? bottomShaftPart.withBottomShaft() : new AssemblageBlockEntityPart();
        bottomCogPart.bottomCogType = bottomCog.getCogType();
        if (!bottomCogPart.bottomCogType.isNone()) parts.add(bottomCogPart);

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

        this.parts = new ArrayList<>(parts);

        if (!getLevel().isClientSide()) {
            parts.forEach(part -> part.updateSpeed = true);
            sendData();
        };

    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    };

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        invalidateParts();
        super.read(tag, registries, clientPacket);
    };

    public class AssemblageBlockEntityPart extends CompositeKineticBlockEntityPart implements IFaceAlignedCogWheelBlockEntity {

        protected boolean topShaft = false;
        protected boolean bottomShaft = false;
        protected CogType topCogType = CogType.NONE;
        protected CogType middleCogType = CogType.NONE;
        protected CogType bottomCogType = CogType.NONE;

        protected final BlockState effectiveState = new DummyCogWheelBlock().defaultBlockState().setValue(CogWheelBlock.AXIS, AssemblageBlockEntity.this.getBlockState().getValue(IAssemblageBlock.AXIS));

        public AssemblageBlockEntityPart() {
            super(PetrolsPartsBlockEntityTypes.ASSEMBLAGE_PART.get());
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
            return false; // Always false as the order in getParts() can change
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

        @Override
        public void setSpeed(float speed) {
            Petrolpark.LOGGER.info("set speed to " + speed);
            super.setSpeed(speed);
        };

        // Unregistered - might be weird
        public class DummyCogWheelBlock extends Block implements ICogWheel {

            public DummyCogWheelBlock() {
                super(BlockBehaviour.Properties.of());
            };

            @Override
            protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
                builder.add(CogWheelBlock.AXIS); // Just because RotationPropagator accesses this sometimes
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
