package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.compat.create.core.world.item.ItemStressValues;
import petrolpark.mc.library.core.world.block.DummyBlock;
import petrolpark.mc.library.util.KineticsHelper;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;

public class AssemblageBlockEntity extends CompositeKineticBlockEntity implements IHaveHoveringInformation, IHaveGoggleInformation {

    protected AssemblageBlockEntityPart topCogPart = null;
    protected AssemblageBlockEntityPart middleCogPart = null;
    protected AssemblageBlockEntityPart bottomCogPart = null;
    protected AssemblageBlockEntityPart shaftPart = null;
    protected List<AssemblageBlockEntityPart> parts = null;

    public AssemblageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public List<AssemblageBlockEntityPart> getParts() {
        if (parts == null) calculateParts();
        return parts == null ? Collections.emptyList() : parts;
    };

    public void invalidateParts() {
        if (hasLevel() && !getLevel().isClientSide()) getParts().forEach(AssemblageBlockEntityPart::remove);
        parts = null;
    };

    protected void calculateParts() {
        final BlockState state = getBlockState();
        if (!(state.getBlock() instanceof IAssemblageBlock block)) return;

        final AssemblageCog topCog = state.getValue(IAssemblageBlock.TOP_COG);
        final AssemblageCog middleCog = state.getValue(IAssemblageBlock.MIDDLE_COG);
        final AssemblageCog bottomCog = state.getValue(IAssemblageBlock.BOTTOM_COG);

        topCogPart = middleCogPart = bottomCogPart = shaftPart = null;
        
        final Set<AssemblageBlockEntityPart> parts = new LinkedHashSet<>(); // Deterministic ordering required

        final float shaftHalfStressImpact = (float)BlockStressValues.getImpact(block.getSet().shaftBlock().get()) / 2f;
        final float shaftHalfStressCapacity = (float)BlockStressValues.getCapacity(block.getSet().shaftBlock().get()) / 2f;

        shaftPart = new AssemblageBlockEntityPart();
        if (block.hasTopShaft(state)) {
            parts.add(shaftPart
                .withTopShaftConnection()
                .withStress(shaftHalfStressImpact, shaftHalfStressCapacity)
            );
        };
        if (block.hasBottomShaft(state)) {
            parts.add(shaftPart
                .withBottomShaftConnection()
                .withStress(shaftHalfStressImpact, shaftHalfStressCapacity)
            );
        };

        topCogPart = topCog.hasShaftConnection() && block.hasTopShaft(state) ? shaftPart : new AssemblageBlockEntityPart();
        if (topCog.hasShaftConnection()) topCogPart.withTopShaftConnection();
        topCogPart.topCogType = topCog.getCogType();
        if (!topCogPart.topCogType.isNone()) parts.add(topCogPart.withStressOfCog(block.getSet(), topCog));

        bottomCogPart = bottomCog.hasShaftConnection() && block.hasBottomShaft(state) ? shaftPart : new AssemblageBlockEntityPart();
        if (bottomCog.hasShaftConnection()) bottomCogPart.withBottomShaftConnection();
        bottomCogPart.bottomCogType = bottomCog.getCogType();
        if (!bottomCogPart.bottomCogType.isNone()) parts.add(bottomCogPart.withStressOfCog(block.getSet(), bottomCog));

        middleCogPart = middleCog.hasShaftConnection() && (block.hasTopShaft(state) || block.hasBottomShaft(state)) ? shaftPart : new AssemblageBlockEntityPart();
        middleCogPart.middleCogType = middleCog.getCogType();
        if (!middleCogPart.middleCogType.isNone()) parts.add(middleCogPart.withStressOfCog(block.getSet(), middleCog));

        this.parts = new ArrayList<>(parts);

        if (hasLevel() && !getLevel().isClientSide()) {
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        final AssemblageBlockEntityPart part = ((IAssemblageBlock)(getBlockState().getBlock())).getTargetedKineticPart(this, Minecraft.getInstance().player);
        if (part != null) return part.addToTooltip(tooltip, isPlayerSneaking);
        return false;
    };

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        final AssemblageBlockEntityPart part = ((IAssemblageBlock)(getBlockState().getBlock())).getTargetedKineticPart(this, Minecraft.getInstance().player);
        if (part != null) return part.addToGoggleTooltip(tooltip, isPlayerSneaking);
        return false;
    };

    public class AssemblageBlockEntityPart extends CompositeKineticBlockEntityPart implements IFaceAlignedCogWheelBlockEntity {

        protected boolean hasTopShaftConnection = false;
        protected boolean hasBottomShaftConnection = false;
        protected CogType topCogType = CogType.NONE;
        protected CogType middleCogType = CogType.NONE;
        protected CogType bottomCogType = CogType.NONE;

        protected final BlockState effectiveState = new DummyCogWheelBlock().defaultBlockState().setValue(CogWheelBlock.AXIS, AssemblageBlockEntity.this.getBlockState().getValue(IAssemblageBlock.AXIS));

        protected AssemblageBlockEntityPart() {
            super(PetrolsPartsBlockEntityTypes.ASSEMBLAGE_PART.get());
            lastStressApplied = 0f;
            lastCapacityProvided = 0f;
        };

        // BUILDING

        protected AssemblageBlockEntityPart withTopShaftConnection() {
            hasTopShaftConnection = true;
            return this;
        };

        protected AssemblageBlockEntityPart withBottomShaftConnection() {
            hasBottomShaftConnection = true;
            return this;
        };

        protected AssemblageBlockEntityPart withStressOfCog(AssemblageSet set, AssemblageCog cogType) {
            if (cogType.isNone()) return this;
            final Item key = (switch (cogType) {
                case LARGE -> set.largeCogItem();
                case SMALL_COAXIAL -> set.coaxialCogItem();
                case LARGE_COAXIAL -> set.largeCoaxialCogItem();
                default -> set.smallCogItem();
            }).get();
            return withStress((float)ItemStressValues.getImpact(key), (float)ItemStressValues.getCapacity(key));
        };

        protected AssemblageBlockEntityPart withStress(float impact, float capacity) {
            lastStressApplied += impact;
            lastCapacityProvided += capacity;
            return this;
        };

        //

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false; // Always false as the order in getParts() can change
        };

        @Override
        public int getIndex() {
            return getParts().indexOf(this);
        };

        /**
         * Trick {@link RotationPropagator} so we don't need to reimplement all the Cogwheel connection logic
         */
        @Override
        public BlockState getBlockState() {
            return effectiveState == null ? AssemblageBlockEntity.super.getBlockState() : effectiveState; // Will be null during initialization
        };

        @Override
        public CogType getCogType(Direction face) {
            if (face.getAxis() == AssemblageBlockEntity.super.getBlockState().getValue(IAssemblageBlock.AXIS)) return face.getAxisDirection() == AxisDirection.POSITIVE ? topCogType : bottomCogType;
            return CogType.NONE;
        };

        @Override
        public float calculateStressApplied() {
            return lastStressApplied;
        };

        @Override
        public float calculateAddedStressCapacity() {
            return lastCapacityProvided;
        };

        @Override
        protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
            return !topCogType.isNone() || !middleCogType.isNone() || !bottomCogType.isNone();
        };

        @Override
        public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
            return IFaceAlignedCogWheelBlockEntity.propagateFaceAlignedCogwheels(this, target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
        };

        @Override
        public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
            super.addPropagationLocations(block, state, neighbours);
            KineticsHelper.addLargeCogwheelPropagationLocations(getBlockPos(), neighbours);
            return neighbours;
        };

        public class DummyCogWheelBlock extends DummyBlock implements ICogWheel {

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
                return face.getAxisDirection() == AxisDirection.POSITIVE ? hasTopShaftConnection : hasBottomShaftConnection;
            };

            @Override
            public Axis getRotationAxis(BlockState state) {
                return AssemblageBlockEntity.super.getBlockState().getValue(IAssemblageBlock.AXIS);
            };

        };

    };
    
};
