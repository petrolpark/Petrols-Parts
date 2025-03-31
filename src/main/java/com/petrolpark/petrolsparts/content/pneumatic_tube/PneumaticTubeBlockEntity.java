package com.petrolpark.petrolsparts.content.pneumatic_tube;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.compat.create.core.tube.ITubeBlockEntity;
import com.petrolpark.compat.create.core.tube.TubeBehaviour;
import com.petrolpark.compat.create.core.tube.TubeSpline;
import com.petrolpark.core.item.QueueItemHandler;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.PetrolsPartsConfigs;
import com.petrolpark.petrolsparts.PetrolsPartsPackets;
import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementBehaviour;
import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementTrigger;
import com.petrolpark.util.BlockFace;
import com.petrolpark.util.ItemHelper;
import com.petrolpark.util.MathsHelper;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.deployer.DeployerFilterSlot;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.CapManipulationBehaviourBase;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
import com.simibubi.create.foundation.item.ItemHelper.ExtractionCountMode;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;

import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.animation.LerpedFloat.Chaser;
import net.createmod.catnip.lang.FontHelper.Palette;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

public class PneumaticTubeBlockEntity extends KineticBlockEntity implements ITubeBlockEntity {

    public static final int DISTANCE_PER_BLOCK = 320;

    public TubeBehaviour tube;

    protected Optional<Either<Input, Output>> handler = Optional.empty();

    protected FilteringBehaviour filtering;
    protected InvManipulationBehaviour targetInventory;
    protected VersionedInventoryTrackerBehaviour invVersionTracker;
    protected DirectBeltInputBehaviour beltInput;

    protected final QueueItemHandler itemBacklog = new QueueItemHandler();

    protected PetrolsPartsAdvancementBehaviour advancements;

    /**
     * Set to true to notify the client if the Tube has been disconnected, so the handler can be removed.
     */
    protected boolean removeHandlerClient = false;
    protected boolean updateFromSpeedClient = false;

    public PneumaticTubeBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        // Both ends
        behaviours.add(tube = new TubeBehaviour(this));
        behaviours.add(beltInput = new DirectBeltInputBehaviour(this)); //TODO
        behaviours.add(targetInventory = new InvManipulationBehaviour(this, CapManipulationBehaviourBase.InterfaceProvider.oppositeOfBlockFacing()));
        behaviours.add(advancements = new PetrolsPartsAdvancementBehaviour(this, PetrolsPartsAdvancementTrigger.PNEUMATIC_TUBE));

        // Relevant to input end only
        behaviours.add(invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
        filtering = new FilteringBehaviour(this, new PneumaticTubeValueBoxTransform())
		    .showCountWhen(this::supportsAmountOnFilter) 
		    .onlyActiveWhen(this::isInput)
		    .withCallback(this::onFilterChanged);
		behaviours.add(filtering);
    };

    public Optional<Either<Input, Output>> getHandler() {
        return handler;
    };

    public PneumaticTubeBlockEntity removeHandler() {
        handler = Optional.empty();
        removeHandlerClient = true;
        return this;
    };

    protected Optional<PneumaticTubeBlockEntity> getOther() {
        if (tube.getOtherEndPos() == null) return Optional.empty();
        return getLevel().getBlockEntity(tube.getOtherEndPos(), PetrolsPartsBlockEntityTypes.PNEUMATIC_TUBE.get());
    };

    protected Optional<Either<Input, Output>> getOtherHandler() {
        return getOther().flatMap(PneumaticTubeBlockEntity::getHandler);
    };

    public Optional<Input> asInput() {
        return getHandler().<Input>flatMap(PneumaticTubeBlockEntity::input);
    };

    public Optional<Output> asOutput() {
        return getHandler().<Output>flatMap(PneumaticTubeBlockEntity::output);
    };

    public boolean isInput() {
        return asInput().isPresent();
    };

    /**
     * Set this end as the Input (if it is not already). The other is {@link Input#getOrCreateOutput() lazily set as the Output}.
     */
    protected Optional<Input> setAsInput() {
        return asInput().or(() -> {
            handler = Optional.of(Either.left(new Input()));
            return handler.get().left();
        });
    };

    public boolean isOutput() {
        return asOutput().isPresent();
    };

    protected Optional<Input> getInput() {
        return asInput().or(() -> getOtherHandler().<Input>flatMap(PneumaticTubeBlockEntity::input));
    };

    protected Optional<Output> getOutput() {
        return asOutput().or(() -> getOtherHandler().<Output>flatMap(PneumaticTubeBlockEntity::output));
    };

    public QueueItemHandler getItemBacklog() {
        return itemBacklog;
    };

    protected float getCombinedAbsSpeed() {
        return Math.abs(getSpeed()) + getOther().map(PneumaticTubeBlockEntity::getSpeed).map(Math::abs).orElse(0f);
    };

    public void onFilterChanged(ItemStack filterStack) {
        resetInvVersionTracker();
        getOther().ifPresent(PneumaticTubeBlockEntity::resetInvVersionTracker);
    };

    public boolean supportsAmountOnFilter() {
        return ((PneumaticTubeBlock)getBlockState().getBlock()).filterable; //TODO check this works for Belts
    };

    public void resetInvVersionTracker() {
        invVersionTracker.reset();
    };

    protected int getItemTransportDistance() {
        return DISTANCE_PER_BLOCK * (int)(double)tube.getSplineOptional().map(TubeSpline::getLength).orElse(0d);
    };

    @Override
    public void tick() {
        super.tick();
        asInput().ifPresent(Input::tick);
        asOutput().ifPresent(Output::tick);
    };

    @Override
    public void afterTubeConnect() {
        setAsInput(); // By default the controller is the Input and the other is the Output
        notifyUpdate(); // Let the client know we are now an Input
        removeHandlerClient = false; // Just in case this is still somehow set to true
    };

    @Override
    public void beforeTubeDisconnect() {
        asInput().ifPresent(Input::dropItems);
        handler = Optional.empty();
        removeHandlerClient = true;
    };

    /**
     * Attempt to flip the input and output ends.
     * @param player
     */
    public InteractionResult flip(@Nullable Player player) {
        return getInput().map(input -> {
            if (!input.getStacksTransporting().isEmpty()) {
                if (player != null) player.displayClientMessage(Component.translatable("block.petrolsparts.pneumatic_tube.cant_flip").withStyle(ChatFormatting.RED), true);
                return InteractionResult.FAIL;
            };
            if (!getOutput().isPresent()) return InteractionResult.FAIL;
            getOutput().ifPresent(output -> {
                output.enclosing().setAsInput();
                output.enclosing().notifyUpdate();
            });
            input.enclosing().removeHandler().notifyUpdate(); // Wait to be lazily set as output
            return InteractionResult.SUCCESS;
        }).orElse(InteractionResult.PASS);
    };

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return getOutput()
            .map(Output::enclosing)
            .map(PneumaticTubeBlockEntity::getItemBacklog)
            .map(h -> {
                if (h.isEmpty()) return false;
                CreateLang.builder().add(Component.translatable("block.petrolsparts.pneumatic_tube.jammed")).style(ChatFormatting.GOLD).forGoggles(tooltip);
                TooltipHelper.cutTextComponent(Component.translatable("block.petrolsparts.pneumatic_tube.jammed.hint"), Palette.GRAY_AND_WHITE).stream()
                    .map(Component::copy)
                    .map(CreateLang.builder()::add)
                    .forEach(lb -> lb.forGoggles(tooltip));
                return true;
            }).orElse(false) | super.addToTooltip(tooltip, isPlayerSneaking);
    };

    public class Output {

        protected Boolean blocked = null;
        protected WeakReference<Entity> lastBlockingEntityRef = null;

        public PneumaticTubeBlockEntity enclosing() {
            return PneumaticTubeBlockEntity.this;
        };

        public Optional<Input> getInput() {
            return getOtherHandler().flatMap(PneumaticTubeBlockEntity::input);
        };

        public Direction getOutputFace() {
            return getBlockState().getValue(PneumaticTubeBlock.FACING).getOpposite();
        };

        public Vec3 getOutputLocation() {
            final Direction outputFace = getOutputFace();
            return BlockFace.of(getBlockPos(), outputFace).getCenter().relative(outputFace, 1 / 32d);
        };

        public void forgetBlocked() {
            blocked = null;
        };

        public boolean isBlocked() {
            if (blocked == null) {
                final BlockPos outputPos = getBlockPos().relative(getOutputFace());
                final BlockHitResult blockHit = level.clip(new ClipContext(getOutputLocation(), Vec3.atCenterOf(outputPos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
                blocked = blockHit.getType() == HitResult.Type.BLOCK && blockHit.getBlockPos().equals(outputPos);
            };
            return blocked;
        };

        public void tick() {
            ItemStack stack = itemBacklog.pollStack();
            if (!stack.isEmpty()) itemBacklog.add(output(stack, false)); // Cycle through the backlog
        };
        
        /**
         * Attempt to insert the Item Stack into the space in front of this output.
         * @param stack
         * @param simulate Whether to not actually store/shoot out the Item, but just try
         * @return The remaining Items after the Insertion is/would be complete
         * @see Output#outputOrBacklog(ItemStack)
         */
        protected ItemStack output(ItemStack stack, boolean simulate) {
            final Direction outputFace = getOutputFace();
            final BlockPos outputPos = getBlockPos().relative(outputFace);

            // First: check for Belts
            //TODO

            // Second: check for Inventories
            if (simulate) targetInventory.simulate();
            ItemStack remainder = targetInventory.insert(stack);
            if (!remainder.equals(stack, false)) {
                advancements.awardAdvancement(PetrolsPartsAdvancementTrigger.PNEUMATIC_TUBE);
                return remainder;
            };
            if (targetInventory.hasInventory()) return stack; // Don't try shooting out the item if the Inventory exists but is just full

            // Third: check to try shoot out Items
            final float speed = getCombinedAbsSpeed();
            if (isBlocked() || speed == 0f) return stack;
            boolean blockingEntityPresent = true;
            final AABB blockingArea = new AABB(outputPos);
            if (lastBlockingEntityRef == null) {
                blockingEntityPresent = false;
            } else {
                Entity lastBlockingEntity = lastBlockingEntityRef.get();
                if (lastBlockingEntity == null || !lastBlockingEntity.isAlive() || !lastBlockingEntity.getBoundingBox().intersects(blockingArea)) {
				    blockingEntityPresent = false;
				    lastBlockingEntity = null;
			    };
            };
            if (blockingEntityPresent) return stack;
            // Find a new Blocking Entity
            for (Entity entity : level.getEntities(null, blockingArea)) {
                if (entity instanceof ItemEntity || entity instanceof PackageEntity) {
                    lastBlockingEntityRef = new WeakReference<>(entity);
                    return stack;
                };
            };
            if (!simulate) {
                final Vec3 outputLoc = getOutputLocation();
                final ItemEntity itemEntity = new ItemEntity(level, outputLoc.x(), outputLoc.y(), outputLoc.z(), stack);
                itemEntity.setDefaultPickUpDelay();
                itemEntity.setDeltaMovement(new Vec3(outputFace.step()).scale(0.25f * Math.pow(speed, 0.25f)));
                level.addFreshEntity(itemEntity);
                lastBlockingEntityRef = new WeakReference<>(itemEntity);

                advancements.awardAdvancement(PetrolsPartsAdvancementTrigger.PNEUMATIC_TUBE);
            };
            return ItemStack.EMPTY;
        };

        /**
         * Attempt to insert the Item Stack into the space in front of this output.
         * If the Stack cannot be outputted and this is not simulated, stores the Stack in the {@link PneumaticTubeBlockEntity#itemBacklog backlog}.
         * @param stack
         */
        protected void outputOrBacklog(ItemStack stack) {
            ItemStack remainder = output(stack, false);
            if (!remainder.isEmpty()) itemBacklog.add(stack);
        };
    
        protected QueueItemHandler getItemBacklog() {
            return PneumaticTubeBlockEntity.this.getItemBacklog();
        };
    };
    
    public class Input implements INBTSerializable<CompoundTag> {

        public final Codec<StackTransporting> STACK_TRANSPORTING_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("item").forGetter(StackTransporting::getStack),
            Codec.INT.fieldOf("progress").forGetter(StackTransporting::getDistanceRemaining)
        ).apply(instance, StackTransporting::new));

        protected final Queue<StackTransporting> stacksTransporting;
        protected int distanceMovedPerTick = -1;
        protected int inputCooldown = 1;

        protected Input() {
            stacksTransporting = new LinkedList<>();
        };

        public PneumaticTubeBlockEntity enclosing() {
            return PneumaticTubeBlockEntity.this;
        };

        public Optional<Output> getOrCreateOutput() {
            return getOther().flatMap(other -> {
                if (!other.handler.isPresent()) other.handler = Optional.of(Either.right(other.new Output()));
                return other.asOutput();
            });
        };

        public Collection<StackTransporting> getStacksTransporting() {
            return stacksTransporting;
        };

        public void tick() {
            getOrCreateOutput();

            if (distanceMovedPerTick == -1) updateFromSpeed(); // Set speed if its not known yet (because we have just loaded in)

            if (getCombinedAbsSpeed() == 0f) return; // Don't do any more without power

            // Tick stacks currently being transported
            boolean changed = !stacksTransporting.isEmpty();
            Iterator<StackTransporting> iterator = stacksTransporting.iterator();
            while (iterator.hasNext()) {
                StackTransporting stackTransporting = iterator.next();
                stackTransporting.tick();
                if (stackTransporting.distanceRemaining <= 0) {
                    iterator.remove();
                    ItemStack stack = stackTransporting.getStack();
                    if (!stack.isEmpty()) getOrCreateOutput().ifPresentOrElse(
                        output -> output.outputOrBacklog(stack),
                        () -> itemBacklog.add(stack)
                    );
                };
            };

            if (getLevel().isClientSide()) return;

            // If there are Items backlogged in the Input, immediately send them to be backlogged in the Output
            ItemStack backloggedItem = itemBacklog.pollStack();
            while (backloggedItem != null && !backloggedItem.isEmpty()) {
                changed = true;
                transport(backloggedItem);
                resetInputCooldown();
                backloggedItem = itemBacklog.pollStack();
            };

            // Try transporting new Stacks
            if (inputCooldown > 0) inputCooldown--;
            if (inputCooldown <= 0) {
                if (input()) resetInputCooldown(); // Try inputting an Item and reset the cooldown if we could
            };

            if (changed) setChanged();
        };

        /**
         * Try and input an Item
         * @return Whether an Item was transported, meaning the delay to wait to try and transport again should be reset
         */
        public boolean input() {

            if (!itemBacklog.isEmpty()) return true; // No Item transport possible if this is backlogged

            if (!getOrCreateOutput()
                .map(Output::getItemBacklog)
                .map(QueueItemHandler::isEmpty)
                .orElse(false)
            ) return false; // No Item transport possible if the Output is missing or backlogged

            if (invVersionTracker.stillWaiting(targetInventory)) return false; // Don't transport if there is an attached Inventory but it still doesn't fit the Filtering requirements

            ItemStack toTryTransport = targetInventory.simulate().extract(getModeToExtract(), getAmountToExtract(), filtering::test);
            //TODO also try extract from Belt

            if (toTryTransport.isEmpty()) return false;
            final int remaining = getOrCreateOutput().map(output -> output.output(toTryTransport, true)).map(ItemStack::getCount).orElse(toTryTransport.getCount());
            if (toTryTransport.getCount() <= remaining) return false; // Don't transport if it won't fit in the output

            final ItemStack toTransport = targetInventory.extract(ExtractionCountMode.EXACTLY, toTryTransport.getCount() - remaining, filtering::test);
            //TODO also extract from belt
            transport(toTransport);
            AllSoundEvents.FWOOMP.playAt(level, getBlockPos(), 1f, 1f, true);
            return true;
        };

        protected void transport(ItemStack stack) {
            StackTransporting stackTransporting = new StackTransporting(stack, getItemTransportDistance());
            stacksTransporting.add(stackTransporting);
            stackTransporting.updateAnimation();
            if (getLevel() instanceof ServerLevel serverLevel) PetrolsPartsPackets.sendToAllClientsInDimension(new PneumaticTubeItemTransportPacket(getBlockPos(), stack), serverLevel);
        };

        /**
         * Animate this Pneumatic Tube as if it is transporting an Item, even if it is not.
         * @return Callback to remove the animated Stack
         */
        public Runnable transportAnimationOnly() {
            StackTransporting stackTransporting = new StackTransporting(ItemStack.EMPTY, getItemTransportDistance());
            stacksTransporting.add(stackTransporting);
            return () -> stacksTransporting.remove(stackTransporting);
        };

        public ExtractionCountMode getModeToExtract() {
            if (!supportsAmountOnFilter() || !filtering.isActive()) return ExtractionCountMode.UPTO;
            return targetInventory.getModeFromFilter();
        };

        public int getAmountToExtract() {
            if (!supportsAmountOnFilter()) return 64;
            if (!filtering.isActive()) return 1;
            return targetInventory.getAmountFromFilter();
        };

        public void dropItems() {
            TubeSpline spline = tube.getSplineOptional().orElse(null);
            if (spline == null) {
                stacksTransporting.stream().map(StackTransporting::getStack).forEach(s -> Block.popResource(getLevel(), getBlockPos(), s));
            } else {
                List<Vec3> points = spline.getPoints();
                if (!points.isEmpty()) stacksTransporting.forEach(stackTransporting -> {
                    ItemHelper.pop(getLevel(), points.get(
                        points.size() - 1 - (points.size() * stackTransporting.getDistanceRemaining() / getItemTransportDistance())
                    ), stackTransporting.getStack());
                });
            };
            stacksTransporting.clear();
        };

        public void updateFromSpeed() {
            distanceMovedPerTick = Math.max(1, (int)getCombinedAbsSpeed());
            stacksTransporting.forEach(StackTransporting::updateAnimation);
        };

        protected void resetInputCooldown() {
            inputCooldown = (int)(PetrolsPartsConfigs.server().pneumaticTubeSpacing.getF() * (float)DISTANCE_PER_BLOCK / (float)distanceMovedPerTick);
        };

        public class StackTransporting {

            protected final ItemStack stack;
            protected int distanceRemaining = 0;
            public final LerpedFloat animation = LerpedFloat.linear().startWithValue(0f).chase(1f, 0f, Chaser.LINEAR);
    
            public StackTransporting(ItemStack stack, int distanceRemaining) {
                this.stack = stack;
                this.distanceRemaining = distanceRemaining;
                if (getLevel() != null) updateAnimation(); // Level is null just after loading world for the first time
            };
    
            public ItemStack getStack() {
                return stack;
            };
    
            public int getDistanceRemaining() {
                return distanceRemaining;
            };
    
            public void updateAnimation() {
                animation.setValue(1f - ((float)distanceRemaining / (float)getItemTransportDistance()));
                if (getItemTransportDistance() == 0) animation.updateChaseSpeed(0f);
                animation.updateChaseSpeed((float)distanceMovedPerTick / (float)getItemTransportDistance());
            };
    
            public void tick() {
                distanceRemaining -= distanceMovedPerTick;
                animation.tickChaser();
            };
        };

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Cooldown", inputCooldown);
            tag.put("Items", NBTHelper.writeCompoundList(stacksTransporting, s -> (CompoundTag)STACK_TRANSPORTING_CODEC.encodeStart(NbtOps.INSTANCE, s).result().orElse(null)));
            return tag;
        };

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            distanceMovedPerTick = -1; // Reset this
            inputCooldown = nbt.getInt("Cooldown");
            stacksTransporting.clear();
            stacksTransporting.addAll(NBTHelper.readCompoundList(nbt.getList("Items", Tag.TAG_COMPOUND), t -> STACK_TRANSPORTING_CODEC.parse(NbtOps.INSTANCE, t).result().orElse(null)));
        };
    };

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        getInput().ifPresent(Input::updateFromSpeed);
        updateFromSpeedClient = true; // Notify client of speed change
        notifyUpdate();
    };

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        itemBacklog.deserializeNBT(compound.getList("Backlog", Tag.TAG_COMPOUND));
        if (compound.contains("Input", Tag.TAG_COMPOUND)) {
            setAsInput().ifPresent(input -> input.deserializeNBT(compound.getCompound("Input")));
        };

        if (compound.contains("RemoveHandler")) {
            handler = Optional.empty();
            removeHandlerClient = false;
        };
        if (compound.contains("UpdateFromSpeed")) {
            getInput().ifPresent(Input::updateFromSpeed);
            updateFromSpeedClient = false;
        };
    };

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Backlog", itemBacklog.serializeNBT());
        asInput().ifPresent(input -> compound.put("Input", input.serializeNBT()));

        if (clientPacket && removeHandlerClient) {
            compound.putBoolean("RemoveHandler", true);
            removeHandlerClient = false; // Handler removal has been dealt with
        };
        if (clientPacket && updateFromSpeedClient) {
            compound.putBoolean("UpdateFromSpeed", true);
            updateFromSpeedClient = false; // Speed update has been dealt with
        };
    };
    
    @Override
    public void invalidateTubeRenderBoundingBox() {
        invalidateRenderBoundingBox();
    };

    @Override
    protected AABB createRenderBoundingBox() {
        if (!tube.isController() || tube.getSpline() == null) return super.createRenderBoundingBox();
        return MathsHelper.expandToInclude(tube.getSpline().getOccupiedVolume(), getBlockPos());
    };

    public static Optional<Input> input(Either<Input, Output> handler) {
        return handler.left();
    };

    public static Optional<Output> output(Either<Input, Output> handler) {
        return handler.right();
    };

    protected class PneumaticTubeValueBoxTransform extends DeployerFilterSlot {

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            return super.getLocalOffset(level, pos, state)
                .subtract(Vec3.atLowerCornerOf(state.getValue(PneumaticTubeBlock.FACING).getNormal())
			        .scale(3 / 16f)
                );
        };

		@Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return state.getValue(PneumaticTubeBlock.FACING).getAxis() != direction.getAxis();
        };

	}; 
    
};
