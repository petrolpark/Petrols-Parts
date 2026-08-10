package petrolpark.mc.petrolsparts.content.processing.frictionHeater;

import java.util.List;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.IRotate.StressImpact;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;

import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import petrolpark.mc.library.compat.create.core.world.block.composite.CompositeKineticBlockEntity;
import petrolpark.mc.library.compat.create.core.world.block.composite.ICompositeKineticBlock;
import petrolpark.mc.library.compat.create.core.world.block.entity.behaviour.FlagPoleBehaviour;
import petrolpark.mc.library.compat.pquality.OptionalQuality;
import petrolpark.mc.library.core.world.block.DummyBlock;
import petrolpark.mc.library.util.Lang;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsConfigs;
import petrolpark.mc.petrolsparts.core.block.CogType;
import petrolpark.mc.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;

public class FrictionHeaterBlockEntity extends CompositeKineticBlockEntity implements IHaveHoveringInformation, IHaveGoggleInformation {

    public final FrictionHeaterBlockEntity.Part topPart, bottomPart;
    protected final List<FrictionHeaterBlockEntity.Part> parts;

    protected FlagPoleBehaviour flagPole;

    protected boolean updateHeatNextTick = true;

    public FrictionHeaterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        parts = List.of(
            topPart = new FrictionHeaterBlockEntity.Part(true),
            bottomPart = new FrictionHeaterBlockEntity.Part(false)
        );
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(flagPole = new FlagPoleBehaviour(this));
    };

    @Override
    public List<FrictionHeaterBlockEntity.Part> getParts() {
        return parts;
    };

    public void updateHeat() {
        final float diff = Math.abs(topPart.getSpeed() - bottomPart.getSpeed());
        if (diff >= getSuperHeatedThreshold()) {
            ICompositeKineticBlock.switchToBlockState(getLevel(), getBlockPos(), getBlockState().setValue(FrictionHeaterBlock.HEAT_LEVEL, HeatLevel.SEETHING));
        } else if (diff >= getHeatedThreshold()) {
            ICompositeKineticBlock.switchToBlockState(getLevel(), getBlockPos(), getBlockState().setValue(FrictionHeaterBlock.HEAT_LEVEL, HeatLevel.KINDLED));
        } else {
            ICompositeKineticBlock.switchToBlockState(getLevel(), getBlockPos(), getBlockState().setValue(FrictionHeaterBlock.HEAT_LEVEL, HeatLevel.NONE));
        };
    };

    public float getHeatedThreshold() {
        return OptionalQuality.reduce(flagPole.getFlagPole(), PetrolsPartsConfigs.server().heatedMinSpeedDifference.get());
    };

    public float getSuperHeatedThreshold() {
        return OptionalQuality.reduce(flagPole.getFlagPole(), PetrolsPartsConfigs.server().superHeatedMinSpeedDifference.get());
    };

    public void spawnParticles() {
		if (level == null) return;

		final RandomSource r = level.getRandom();
        if (r.nextInt(4) != 0) return;

        if (topPart.getSpeed() == bottomPart.getSpeed()) return;

		final Vec3 c = VecHelper.getCenterOf(getBlockPos());
		final Vec3 smokePos = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .125f)
			.multiply(1, 0, 1));

		final boolean empty = level.getBlockState(getBlockPos().above())
			.getCollisionShape(level, getBlockPos().above())
			.isEmpty();

		if (empty || r.nextInt(8) == 0)
		    level.addParticle(ParticleTypes.LARGE_SMOKE, smokePos.x(), smokePos.y(), smokePos.z(), 0, 0, 0);

        final HeatLevel heatLevel = getBlockState().getValue(FrictionHeaterBlock.HEAT_LEVEL);

        if (heatLevel == HeatLevel.NONE) return;

		final double yMotion = empty ? .0625f : r.nextDouble() * .0125f;
		final Vec3 flamePos = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
			.multiply(1, .25f, 1)
			.normalize()
			.scale((empty ? .25f : .5) + r.nextDouble() * .125f))
			.add(0, .5, 0);

		if (heatLevel.isAtLeast(HeatLevel.SEETHING)) {
			level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, flamePos.x(), flamePos.y(), flamePos.z(), 0, yMotion, 0);
		} else if (heatLevel.isAtLeast(HeatLevel.FADING)) {
			level.addParticle(ParticleTypes.FLAME, flamePos.x(), flamePos.y(), flamePos.z(), 0, yMotion, 0);
		};
		return;
	};

    @Override
    public void tick() {
        super.tick();
        if (updateHeatNextTick) updateHeat();
        if (getLevel().isClientSide()) spawnParticles();
    };

    public class Part extends CompositeKineticBlockEntityPart implements IFaceAlignedCogWheelBlockEntity {

        public final boolean top;
        private final BlockState effectiveState = new DummyHalfBlock().defaultBlockState();

        Part(boolean top) {
            super(PetrolsPartsBlockEntityTypes.FRICTION_HEATER_PART.get());
            this.top = top;
        };

        @Override
        public CogType getCogType(Direction face) {
            return face.getAxis() == FrictionHeaterBlockEntity.super.getBlockState().getValue(FrictionHeaterBlock.HORIZONTAL_AXIS)
                && (face.getAxisDirection() == AxisDirection.POSITIVE == top)
                ? CogType.SMALL
                : CogType.NONE;
        };
        
        @Override
        protected Block getStressConfigKey() {
            return FrictionHeaterBlockEntity.super.getBlockState().getBlock();
        };

        @Override
        public void onSpeedChanged(float previousSpeed) {
            super.onSpeedChanged(previousSpeed);
            updateHeatNextTick = true;
        };

        @Override
        public BlockState getBlockState() {
            return effectiveState == null ? FrictionHeaterBlockEntity.super.getBlockState() : effectiveState; // Effective state can be null during initialization
        };

        @Override
        public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState state) {
            return oldState.getValue(FrictionHeaterBlock.HORIZONTAL_AXIS) == state.getValue(FrictionHeaterBlock.HORIZONTAL_AXIS);
        };

        @Override
        public int getIndex() {
            return top ? 0 : 1;
        };

        @Override
        public void addStressImpactStats(List<Component> tooltip, float stressAtBase) {
            super.addStressImpactStats(tooltip, stressAtBase);
        };

        class DummyHalfBlock extends DummyBlock implements IRotate {

            DummyHalfBlock() {
                super(BlockBehaviour.Properties.of());
            };

            @Override
            public Axis getRotationAxis(BlockState state) {
                return FrictionHeaterBlockEntity.super.getBlockState().getValue(FrictionHeaterBlock.HORIZONTAL_AXIS);
            };

            @Override
            public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
                return face.getAxis() == getRotationAxis(state) && (face.getAxisDirection() == AxisDirection.POSITIVE == top);
            };

        };
    };

    @Override
    protected void read(CompoundTag tag, Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        if (tag.contains("UpdateHeat", Tag.TAG_BYTE)) updateHeatNextTick = true;
    };

    @Override
    protected void write(CompoundTag tag, Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        if (updateHeatNextTick) tag.putBoolean("UpdateHeat", true);
    };

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        
        final float speedDiff = Math.abs(topPart.getSpeed() - bottomPart.getSpeed());

        PetrolsParts.langBuilder().translate("gui.goggles.friction_heater.status",
            switch (getBlockState().getValue(FrictionHeaterBlock.HEAT_LEVEL)) {
                case SEETHING -> superHeatedComponent();
                case KINDLED -> heatedComponent();
                default -> unheatedComponent();
            })
            .forGoggles(tooltip);

        PetrolsParts.langBuilder()
            .translate("gui.goggles.friction_heater.speed_difference")
            .style(ChatFormatting.GRAY)
            .forGoggles(tooltip);
        final LangBuilder speedDifferenceBuilder = CreateLang.number(speedDiff)
            .space()
            .add(CreateLang.translate("generic.unit.rpm"))
            .style(ChatFormatting.GREEN);
        if (speedDiff < getSuperHeatedThreshold()) {
            final boolean unheated = speedDiff < getHeatedThreshold();
            speedDifferenceBuilder.space().add(PetrolsParts.langBuilder()
                .translate("gui.goggles.friction_heater.next_level",
                    unheated
                        ? heatedComponent()
                        : superHeatedComponent(),
                    CreateLang.number(unheated ? getHeatedThreshold() : getSuperHeatedThreshold())
                        .space()
                        .add(CreateLang.translate("generic.unit.rpm"))
                        .style(ChatFormatting.RED)
                ).style(ChatFormatting.DARK_GRAY)
            );
        };
        speedDifferenceBuilder.forGoggles(tooltip, 1);

        tooltip.add(Component.empty());

        CreateLang.translate("gui.goggles.kinetic_stats")
			.forGoggles(tooltip);

        final Axis axis = getBlockState().getValue(FrictionHeaterBlock.HORIZONTAL_AXIS);
        for (FrictionHeaterBlockEntity.Part part : getParts()) {
            PetrolsParts.langBuilder().translate("gui.goggles.friction_heater.part_kinetic_stats", Lang.direction(Direction.get(part.top ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, axis)))
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
            if (StressImpact.isEnabled()) {
                CreateLang.number(part.calculateStressApplied() * Math.abs(part.getTheoreticalSpeed()))
                    .add(CreateLang.translate("generic.unit.stress"))
                    .style(ChatFormatting.AQUA)
                    .space()
                    .add(CreateLang.translate("gui.goggles.at_current_speed").style(ChatFormatting.DARK_GRAY)
                        .space()
                        .text("(")
                        .add(CreateLang.number(part.getSpeed())
                            .space()
                            .add(CreateLang.translate("generic.unit.rpm"))
                            .style(ChatFormatting.GREEN)
                        ).text(")")
                    ).forGoggles(tooltip, 1);
            } else {
                CreateLang.number(part.getSpeed())
                    .space()
                    .add(CreateLang.translate("generic.unit.rpm"))
                    .style(ChatFormatting.GREEN)
                    .forGoggles(tooltip, 1);
            };
        };
        
        return true;
    };

    public static final Component unheatedComponent() {
        return PetrolsParts.translate("gui.goggles.friction_heater.not_heating").withColor(HeatCondition.NONE.getColor());
    };

    public static final Component heatedComponent() {
        return CreateLang.translateDirect(HeatCondition.HEATED.getTranslationKey()).withColor(HeatCondition.HEATED.getColor());
    };

    public static final Component superHeatedComponent() {
        return CreateLang.translateDirect(HeatCondition.SUPERHEATED.getTranslationKey()).withColor(HeatCondition.SUPERHEATED.getColor());
    };
    
};
