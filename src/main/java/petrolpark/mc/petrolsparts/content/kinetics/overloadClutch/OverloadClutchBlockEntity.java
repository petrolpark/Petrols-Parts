package petrolpark.mc.petrolsparts.content.kinetics.overloadClutch;

import java.util.List;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.core.world.block.DummyBlock;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;

public class OverloadClutchBlockEntity extends CompositeKineticBlockEntity implements IHaveHoveringInformation, IHaveGoggleInformation {

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
        behaviours.add(stressSetting =
            new ScrollValueBehaviour(PetrolsParts.translate("gui.overload_clutch.stress_impact"), this, new CenteredSideValueBoxTransform((s, f) -> f.getAxis() != s.getValue(OverloadClutchBlock.FACING).getAxis())) {
                @Override
                public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
                    final ValueSettingsBoard board = super.createBoard(player, hitResult);
                    return new ValueSettingsBoard(board.title(), board.maxValue(), 16, board.rows(), board.formatter());
                };
            }.between(0, 256)
            .withFormatter(i -> i + "x")
            .withCallback($ -> update())
        );
        stressSetting.value = 4;
    };

    @Override
    public List<? extends CompositeKineticBlockEntityPart> getParts() {
        return parts;
    };

    public void update() {
        impactPart.getOrCreateNetwork().updateStressFor(impactPart, impactPart.calculateStressApplied());
        generatingPart.updateGeneratedRotation();
    };

    public float getStressImpact() {
        if (redstonePower >= 15) return 0f;
        return stressSetting.value / 15 < 1
            ? ((float)stressSetting.value * (15f - redstonePower) / 15f)
            : (float)(stressSetting.value * (15 - redstonePower) / 15);
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
        public void onSpeedChanged(float previousSpeed) {
            super.onSpeedChanged(previousSpeed);
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

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        // double pipe so only one "overstressed" tooltip gets added
        return impactPart.addToTooltip(tooltip, isPlayerSneaking) || generatingPart.addToTooltip(tooltip, isPlayerSneaking);
    };

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CreateLang.translate("gui.goggles.kinetic_stats")
			.forGoggles(tooltip);

        PetrolsParts.langBuilder().translate("gui.goggles.overload_clutch")
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip);

		CreateLang.number(impactPart.calculateStressApplied() * Math.abs(impactPart.getTheoreticalSpeed()))
			.translate("generic.unit.stress")
			.style(ChatFormatting.AQUA)
			.space()
			.add(CreateLang.translate("gui.goggles.at_current_speed")
				.style(ChatFormatting.DARK_GRAY)
            ).forGoggles(tooltip, 1);

        return true;
    };
    
};
