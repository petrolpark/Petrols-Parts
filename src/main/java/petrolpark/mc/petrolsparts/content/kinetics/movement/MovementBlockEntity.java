package petrolpark.mc.petrolsparts.content.kinetics.movement;

import java.util.List;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.clock.CuckooClockBlockEntity;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchObservable;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;

import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.compat.create.core.world.block.entity.behaviour.FlagPoleBehaviour;
import petrolpark.mc.library.compat.pquality.OptionalQuality;
import petrolpark.mc.library.core.world.block.DummyBlock;
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsDataComponentTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsDataMapTypes;

public class MovementBlockEntity extends CompositeKineticBlockEntity implements IHaveHoveringInformation, IHaveGoggleInformation, ThresholdSwitchObservable {

    public final WindingPart windingPart = new WindingPart();
    public final GeneratingPart generatingPart = new GeneratingPart();

    protected final List<CompositeKineticBlockEntityPart> parts = List.of(generatingPart, windingPart);

    protected float rotationsCharge = 0;
    protected ItemStack weightStack = ItemStack.EMPTY;
    protected MovementWeightData weightData = null;

    protected FlagPoleBehaviour flagPole;

    public MovementBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        generatingPart.setBlockState(state);
        windingPart.setBlockState(state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(flagPole = new FlagPoleBehaviour(this));
    };

    public float getMaxRotationsCharge() {
        return OptionalQuality.multiply(flagPole.getFlagPole(), 900f);
    };

    public float getBaseRotationSpeed() {
        return 16f;  
    };

    @SuppressWarnings("deprecation")
    public void setWeightStack(ItemStack newWeightStack) {
        weightStack = newWeightStack;
        weightData = newWeightStack.getItem().builtInRegistryHolder().getData(PetrolsPartsDataMapTypes.MOVEMENT_WEIGHT);
        if (weightData == null) rotationsCharge = 0f;
        update();
        notifyUpdate();
    };

    public boolean isFullyCharged() {
        return rotationsCharge >= getMaxRotationsCharge();
    };

    public boolean shouldGenerate() {
        return (windingPart.getTheoreticalSpeed() != 0f || rotationsCharge > 0f) && weightData != null && getLevel().hasNeighborSignal(getBlockPos());
    };

    @Override
    public List<CompositeKineticBlockEntityPart> getParts() {
        return parts;
    };

    @Override
    public void tick() {
        final boolean generatingBefore = shouldGenerate();
        super.tick();
        if (generatingBefore != shouldGenerate()) update();
        setChanged(); // Update comparator
    };

    public void update() {
        generatingPart.updateGeneratedRotation();
        if (windingPart.hasNetwork()) windingPart.getOrCreateNetwork().updateStressFor(windingPart, windingPart.calculateStressApplied());
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

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        final MovementItemComponent component = componentInput.get(PetrolsPartsDataComponentTypes.MOVEMENT_DATA);
        if (component == null) return;
        setWeightStack(component.weightStack());
        rotationsCharge = component.storedRotations();
    };

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        if (weightData == null) return;
        components.set(PetrolsPartsDataComponentTypes.MOVEMENT_DATA, new MovementItemComponent(weightStack, rotationsCharge));
    };

    public class WindingPart extends CompositeKineticBlockEntityPart {

        protected final DummyShaftEndBlock dummyBlock = new DummyShaftEndBlock();

        public WindingPart() {
            super(PetrolsPartsBlockEntityTypes.MOVEMENT_WINDING_PART.get());
        };

        @Override
        public float calculateStressApplied() {
            if (isFullyCharged() && !shouldGenerate())
                return lastStressApplied = 0f;
            return lastStressApplied = weightData == null ? 0f : weightData.stressCapacity();
        };

        @Override
        public void tick() {
            super.tick();
            final float speed = Mth.abs(getSpeed());
            if (speed != 0f && weightData != null && !isFullyCharged()) {
                final boolean wasEmpty = rotationsCharge <= 0f;
                rotationsCharge += speed / (20 * 60); // Convert RPM to rotations per tick
                if (wasEmpty) generatingPart.updateGeneratedRotation();
                if (isFullyCharged()) {
                    rotationsCharge = getMaxRotationsCharge();
                    if (hasNetwork()) getOrCreateNetwork().updateStressFor(this, calculateStressApplied()); // Applied stress now 0
                };
            };
        };

        @Override
        public void setBlockState(BlockState blockState) {
            dummyBlock.face = blockState.getValue(MovementBlock.HORIZONTAL_FACING).getOpposite();
        };

        @Override
        public BlockState getBlockState() {
            return dummyBlock == null ? MovementBlockEntity.super.getBlockState() : dummyBlock.defaultBlockState(); // Can be null during initialization
        }; 

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return false;
        };

        @Override
        public int getIndex() {
            return 1;
        };

        @Override
        public void addStressImpactStats(List<Component> tooltip, float stressAtBase) {
            super.addStressImpactStats(tooltip, stressAtBase);
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
            return shouldGenerate() ? getBaseRotationSpeed() : 0f;
        };

        @Override
        public float calculateAddedStressCapacity() {
            return lastCapacityProvided = shouldGenerate() ? weightData.stressCapacity() : 0f;
        };

        @Override
        public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
            if (diff.equals(Direction.UP.getNormal()) && target instanceof CuckooClockBlockEntity) return 1f;
            return 0f; 
        };

        @Override
        public void tick() {
            final boolean wasFull = isFullyCharged();
            if (shouldGenerate()) {
                rotationsCharge -= Math.abs(getSpeed()) / (20 * 60); // Convert RPM to rotations per tick
                if (rotationsCharge < 0f) rotationsCharge = 0f;
                if (wasFull && windingPart.hasNetwork())
                    windingPart.getOrCreateNetwork().updateStress(); // Applied stress no longer 0
            };
            super.tick();
        };

        @Override
        public void setBlockState(BlockState blockState) {
            dummyBlock.face = blockState.getValue(MovementBlock.HORIZONTAL_FACING);
        };

        @Override
        public BlockState getBlockState() {
            return dummyBlock == null ? MovementBlockEntity.super.getBlockState() : dummyBlock.defaultBlockState(); // Can be null during initialization
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
            return getBlockState().getValue(MovementBlock.HORIZONTAL_FACING).getAxis();
        };

    };

    // Goggles

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (weightData == null) {
            PetrolsParts.langBuilder().translate("gui.goggles.movement.no_weight")
                .style(ChatFormatting.GOLD)
                .forGoggles(tooltip);
            for (Component line : TooltipHelper.cutTextComponent(PetrolsParts.translate("gui.goggles.movement.no_weight.info"), FontHelper.Palette.GRAY_AND_WHITE)) {
                PetrolsParts.langBuilder()
                    .add(line.copy())
                    .forGoggles(tooltip);
            };
            return true;
        };
        return false;
    };

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (weightData == null) return false;

        PetrolsParts.langBuilder().translate("gui.goggles.kinetic_battery_stats")
			.forGoggles(tooltip);

        PetrolsParts.langBuilder().translate("tooltip.movement.weight", "")
            .style(ChatFormatting.GRAY)
            .forGoggles(tooltip);
        PetrolsParts.langBuilder().add(weightStack.getHoverName())
            .style(ChatFormatting.AQUA)
            .forGoggles(tooltip, 1);

        PetrolsParts.langBuilder().translate("tooltip.stored_rotations", "")
            .style(ChatFormatting.GRAY)
            .forGoggles(tooltip);
        PetrolsParts.langBuilder().text(Lang.INT_DF.format(rotationsCharge)).style(ChatFormatting.AQUA)
            .add(PetrolsParts.langBuilder().text(" / ").style(ChatFormatting.GRAY))
            .add(PetrolsParts.langBuilder().text(Lang.INT_DF.format(getMaxRotationsCharge())).style(ChatFormatting.DARK_GRAY))
            .forGoggles(tooltip, 1);
            
        windingPart.addStressImpactStats(tooltip, windingPart.calculateStressApplied());
        generatingPart.addToGoggleTooltip(tooltip, isPlayerSneaking);

        return true;
    };

    // Threshold Switch

    @Override
    public int getMaxValue() {
        return (int)getMaxRotationsCharge();
    };

    @Override
    public int getMinValue() {
        return 0;
    };

    @Override
    public int getCurrentValue() {
        return (int)rotationsCharge;
    };

    @Override
    public MutableComponent format(int value) {
        return PetrolsParts.translate("gui.threshold_switch.movement_rotations", value);
    };
    
};
